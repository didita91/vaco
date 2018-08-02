package py.una.pol.vone.aco.problema;

import edu.ufl.cise.bsmock.graph.ksp.Yen;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import py.una.pol.vone.aco.entidades.fisicas.NodosFisicosCandidatos;
import py.una.pol.vone.simulator.model.Camino;
import py.una.pol.vone.simulator.model.SustrateEdge;
import py.una.pol.vone.simulator.model.SustrateNetwork;
import py.una.pol.vone.simulator.model.VirtualEdge;
import py.una.pol.vone.simulator.model.VirtualNetwork;
import edu.ufl.cise.bsmock.graph.*;
import edu.ufl.cise.bsmock.graph.util.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import py.una.pol.vone.aco.utils.rsa.Rmsa;
import py.una.pol.vone.aco.utils.rsa.SolucionAco;
import py.una.pol.vone.simulator.model.SustrateNode;
import py.una.pol.vone.simulator.model.VirtualNode;

import thiagodnf.jacof.problem.Problem;

/**
 *
 * @author Diana Ferreira
 */
public class VoneProblemBalFragm extends Problem {

    public double Q = 1.0;

    protected SustrateNetwork redFisica;
    /**
     * opciones de probabilidades
     */
    protected List<NodosFisicosCandidatos> listaNodos;
    protected int numeroDeNodos;
    /**
     * informacion heuristica
     */
    protected double cnn;
    protected VirtualNetwork redVirtual;
    protected double[][] distance;
    protected int tourLength;
    protected List<Integer> tour;

    public VoneProblemBalFragm(List<NodosFisicosCandidatos> lista, SustrateNetwork redFisic, VirtualNetwork redVirt) throws IOException {
        listaNodos = lista;
        redFisica = redFisic;
        redVirtual = redVirt;
        numeroDeNodos = listaNodos.size();
        //el minimo numero de caminos posibles.
        cnn = redVirt.getNroNodos() - 1;
        tourLength = 0;

        distance = new double[redFisica.getNroNodos()][redFisica.getNroNodos()];
    }

    @Override
    public double getNij(int i, int j, List<Integer> tourActual) {

        int nodoOrigen = obtenerNodoVirtual(i);
        int nodoDestino = obtenerNodoVirtual(j);
        int nodoOrigenF = obtenerNodoFisico(i);
        int nodoDestinoF = obtenerNodoFisico(j);
        // List<Integer> tourActual = this.tour;

        double visi = 9999;
        try {
            visi = 1 / obtenerDistanciaRSA(nodoOrigenF, nodoDestinoF, nodoOrigen, nodoDestino, true);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(VoneProblemBalFragm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return visi;

    }

    @Override
    public boolean better(double s1, double best) {
        return s1 < best;
    }

    @Override
    public final double evaluate(int[] solution) {
        double totalMaxMin = 0;

        try {

            totalMaxMin = obtenerFragmentacion(solution);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(VoneProblemBalFragm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalMaxMin;

    }

    @Override
    public int getNumberOfNodes() {
        return numeroDeNodos;
    }

    @Override
    public double getCnn() {
        return cnn;
    }

    @Override
    public double getDeltaTau(double tourLength, int i, int j) {
        return Q / tourLength;
    }

    @Override
    public String toString() {
        return VoneProblemBalFragm.class.getSimpleName();
    }

    @Override
    public List<Integer> initNodesToVisit(int startingNode) {
        List<Integer> nodesToVisit = new ArrayList<>();
        int orden = obtenerOrden(startingNode);
        orden++;
        for (int i = 0; i < getNumberOfNodes(); i++) {
            if (i != startingNode) {
                if (!nodesToVisit.isEmpty()) {
                    if (obtenerOrden(i) == orden) {
                        if (obtenerNodoVirtual(i) != obtenerNodoVirtual(startingNode)
                                && !verificarFisico(nodesToVisit, i) && obtenerNodoFisico(startingNode) != obtenerNodoFisico(i)) {
                            nodesToVisit.add(i);
                        }
                    }
                } else {
                    if (obtenerOrden(i) == orden) {
                        if (obtenerNodoVirtual(i) != obtenerNodoVirtual(startingNode)
                                && obtenerNodoFisico(startingNode) != obtenerNodoFisico(i)) {

                            nodesToVisit.add(i);
                        }
                    }
                }
            }
        }

        return nodesToVisit;
    }

    private boolean verificar(List<Integer> nodosAvisitar, int nodo) {
        int nodovirtual = obtenerNodoVirtual(nodo);
        for (Integer i : nodosAvisitar) {
            if (obtenerNodoVirtual(i) == nodovirtual) {
                return true;
            }
        }
        return false;
    }

    private boolean verificarFisico(List<Integer> nodosAvisitar, int nodo) {
        int nodofisico = obtenerNodoFisico(nodo);
        for (Integer i : nodosAvisitar) {
            if (obtenerNodoFisico(i) == nodofisico) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Integer> updateNodesToVisit(List<Integer> tourCurrent, List<Integer> nodesToVisit) {
        List<Integer> nodesToRemove = new ArrayList<Integer>();
//        for (Integer nodo : nodesToVisit) {
//
//            for (Integer t : tourCurrent) {
//                if ((obtenerNodoVirtual(nodo)) == obtenerNodoVirtual(t)
//                        || obtenerNodoFisico(t) == obtenerNodoFisico(nodo)) {
//                    nodesToRemove.add(nodo);
//
//                }
//            }
//        }
        for (Integer inte : nodesToVisit) {
            nodesToRemove.add(inte);
        }
        for (Integer i : nodesToRemove) {
            nodesToVisit.remove(i);
        }
//        if ( //                                obtenerNodoVirtual(i) != obtenerNodoVirtual(startingNode) && !verificar(nodesToVisit, i)
//                //                                &&
//                !verificarFisico(nodesToVisit, i) && obtenerNodoFisico(startingNode) != obtenerNodoFisico(i)) {
//            nodesToVisit.add(i);
//        }
        tour = tourCurrent;

        int orden = obtenerOrden(tourCurrent.get(tourCurrent.size() - 1)) + 1;
        if (orden <= redVirtual.getNroNodos()) {
            for (int i = 0; i < getNumberOfNodes(); i++) {
                if (!nodesToVisit.isEmpty()) {
                    if (obtenerOrden(i) == orden) {
                        if (!nodesToVisit.contains(i) && !verificarFisico(nodesToVisit, i)
                                && !verificar(tourCurrent, i) && !verificarFisico(tourCurrent, i)) {
                            nodesToVisit.add(i);
                        }
                    }
                } else {
                    if (obtenerOrden(i) == orden) {
                        if (!verificar(tourCurrent, i) && !verificarFisico(tourCurrent, i)) {

                            nodesToVisit.add(i);
                        }
                    }
                }
            }
        }
        return nodesToVisit;
    }

    public int obtenerNodoVirtual(int posicion) {
        return listaNodos.get(posicion).getNodoVirtual().getIdentificador();

    }

    private int obtenerNodoFisico(int posicion) {
        return listaNodos.get(posicion).getNodoFisico().getIdentificador();
    }

    private double obtenerCPU(int posicion) {
        return listaNodos.get(posicion).getNodoFisico().capacidadActual();
    }

    private double obtenerDistancia(int i, int j, boolean buscarMaxindex) throws CloneNotSupportedException {
        int origen = obtenerNodoFisico(i);
        int destino = obtenerNodoFisico(j);
        int origenv = obtenerNodoVirtual(i);
        int destinov = obtenerNodoVirtual(j);
        double dist = 9999;
        double fs = 0;
        if (distance[origen][destino] == 0.0) {
            dist = obtenerDistanciaRSA(origen, destino, origenv, destinov, buscarMaxindex);
            fs = obtenerFS(origenv, destinov);
            distance[origen][destino] = dist * fs;
            distance[destino][origen] = dist * fs;
            return distance[origen][destino];

        } else {
            return distance[origen][destino];

        }

    }

    public int getTourLength() {
        return tourLength;
    }

    public void setTourLength(int tourLength) {
        this.tourLength = tourLength;
    }

    private int obtenerOrden(int posicion) {
        return listaNodos.get(posicion).getOrden();
    }

    private double obtenerFragmentacion(int[] solution) throws CloneNotSupportedException {
        SustrateNetwork redFisicaAux = (SustrateNetwork) redFisica.clone();
        VirtualNetwork redVirtualAux = new VirtualNetwork();
        redVirtualAux = redVirtual;
        SolucionAco solucionAco = new SolucionAco();

        redFisicaAux = VoneAntBalanceoFragmentacion.ubicarEnlacesFrag(solution, redFisicaAux, redVirtualAux, solucionAco, listaNodos);
        double fitness = Long.MAX_VALUE;
        int maxIndex = 0;
        int cont = 0;
        int index = 0;
        if (redFisicaAux != null) {
            for (SustrateEdge ef : redFisicaAux.getEnlacesFisicos()) {
                for (int i = 0; i < ef.getFrequencySlot().length; i++) {
                    if (ef.getFrequencySlot()[i]) {
                        index = i;
                    }

                }
                if (index > maxIndex) {
                    maxIndex = index;
                }
                maxIndex = maxIndex + index;
                index = 0;
            }

            double indexFrag;
            indexFrag = (double) maxIndex / redFisicaAux.getNroEnlaces();
            //double distancia = obtenerDistanciaSolucion(solution);
            //  fitness = indexFrag + 0.5 * distancia;
            fitness = indexFrag;
            return fitness;
        }
        return fitness;
    }

    private int obtenerFS(int nodoOrigen, int nodoDestino) {

        int fs = 0;
        for (VirtualEdge ef : redVirtual.getEnlacesVirtuales()) {
            if (ef.getNodoUno().getIdentificador() == nodoOrigen && ef.getNodoDos().getIdentificador() == nodoDestino) {
                fs = ef.getCantidadFS();
                break;
            }
            if (ef.getNodoUno().getIdentificador() == nodoDestino && ef.getNodoDos().getIdentificador() == nodoOrigen) {
                fs = ef.getCantidadFS();
                break;
            }
        }
        return fs;
    }

    private double obtenerDistanciaRSA(int origenFisico, int destinoFisico, int origenvirtual, int destinovirtual, boolean buscarMaxIndex) throws CloneNotSupportedException {
        SustrateNetwork redFisicaAUX = (SustrateNetwork) redFisica.clone();
        VirtualNetwork redVirtualAux = new VirtualNetwork();
        redVirtualAux = redVirtual;
        SolucionAco solAco = new SolucionAco();

        for (VirtualEdge ev : redVirtualAux.getEnlacesVirtuales()) {

            if (ev.getNodoUno().getIdentificador() == origenvirtual && ev.getNodoDos().getIdentificador() == destinovirtual) {
                Rmsa.realizarRmsa(redFisicaAUX, Integer.toString(origenFisico), Integer.toString(destinoFisico),
                        1, ev.getCantidadFS(), solAco, ev, true);
                break;
            }
            if (ev.getNodoUno().getIdentificador() == destinovirtual && ev.getNodoDos().getIdentificador() == origenvirtual) {
                Rmsa.realizarRmsa(redFisicaAUX, Integer.toString(destinoFisico), Integer.toString(origenFisico),
                        1, ev.getCantidadFS(), solAco, ev, true);
                break;
            }

        }
        int sum = 0;
        int index = 0;
        double prom = 9999;

        if (redFisicaAUX != null) {
            if (buscarMaxIndex) {
                if (solAco.getList() != null) {
                    for (py.una.pol.vone.aco.utils.rsa.kshortespaths.utilrsa.Path path : solAco.getList()) {
                        for (py.una.pol.vone.aco.utils.rsa.kshortespaths.Edge edge : path.getEdges()) {
                            for (int i = 0; i < edge.getFrequencySlot().length; i++) {
                                if (edge.getFrequencySlot()[i]) {
                                    index = i;
                                }

                            }
                            sum = sum + index;
                            index = 0;

                        }
                        if (path.getEdges() != null && !path.getEdges().isEmpty()) {
                            prom = (double) sum / path.getEdges().size();
                        }
                    }
                }
                return prom;
            } else {
                return (solAco.getList() == null || solAco.getList().isEmpty()) ? 9999 : solAco.getList().get(0).getTotalCost();

            }
        }
        return 9999;
    }

}
