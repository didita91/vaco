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
public class VoneProblemVersionInicial extends Problem {

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

    public VoneProblemVersionInicial(List<NodosFisicosCandidatos> lista, SustrateNetwork redFisic, VirtualNetwork redVirt) throws IOException {
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
        double result = 999999999.0;

        double cpuNorm = obtenerCPU(j);
        double distancia = 0.0;

        int nodoOrigen = obtenerNodoVirtual(j);
        int nodoDestino;
        // List<Integer> tourActual = this.tour;
        if (tourActual != null && !tourActual.isEmpty()) {
            for (int o = 0; o < tourActual.size(); o++) {
                if (o < tourActual.size()) {
                    ////System.out.println("TOUR INICIAL size: " + tour.size() + " k:" + k + " valoer: " + tour.get(k));

                    nodoDestino = obtenerNodoVirtual(tourActual.get(o));
                    for (VirtualEdge ev : redVirtual.getEnlacesVirtuales()) {
                        if ((ev.getNodoUno().getIdentificador() == nodoOrigen && ev.getNodoDos().getIdentificador() == nodoDestino)
                                || (ev.getNodoUno().getIdentificador() == nodoDestino && ev.getNodoDos().getIdentificador() == nodoOrigen)) {
                            // //System.out.println("TOUR size: " + tour.size() + " k:" + k + " valoer: " + tour.get(k));

                            distancia = distancia + obtenerDistancia(j, tourActual.get(o));

                        }
                    }
                }

            }
        } else {
            distancia = obtenerDistancia(i, j);

            tourLength = 1;
        }
        if (distancia != 0) {
            result = (1.0 / distancia) * cpuNorm;
        }
        return result;

    }

    @Override
    public boolean better(double s1, double best) {
        return s1 < best;
    }

    @Override
    public final double evaluate(int[] solution) {

        double totalDistance = 0;

        for (int h = 1; h < solution.length; h++) {

            int i = solution[h - 1];
            int j = solution[h];

            totalDistance += obtenerDistancia(i, j);
        }

        return totalDistance;
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
        return VoneProblemVersionInicial.class.getSimpleName();
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

    private double obtenerDistancia(int i, int j) {
        int origen = obtenerNodoFisico(i);
        int destino = obtenerNodoFisico(j);
        return distance[origen][destino];
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

    private double obtenerDistanciaSolucion(int[] solucion) throws CloneNotSupportedException {

        double distancia = 0;

        for (VirtualEdge ve : redVirtual.getEnlacesVirtuales()) {

            int nodoOrigenV = ve.getNodoUno().getIdentificador();
            int nodoDestinoV = ve.getNodoDos().getIdentificador();
            for (Integer s1 : solucion) {

                int nodoOrigenS = obtenerNodoVirtual(s1);
                int nodoOrigenFisico = obtenerNodoFisico(s1);

                for (Integer s2 : solucion) {

                    int nodoDestinoS = obtenerNodoVirtual(s2);
                    if (nodoDestinoS != nodoOrigenS) {
                        if ((nodoOrigenV == nodoOrigenS && nodoDestinoV == nodoDestinoS)) {
                            int nodoDestinoFisico = obtenerNodoFisico(s2);
                            double costo;
                            if (distance[nodoOrigenFisico][nodoDestinoFisico] == 0) {
                                costo = obtenerDistancia(s1, s2);
                            } else {
                                costo = distance[nodoOrigenFisico][nodoDestinoFisico];

                            }
                            distancia = distancia + costo;
                        }
                    }

                }
            }

        }
        return distancia;

    }

    private double obtenerBalanceNodos(int[] solution) throws CloneNotSupportedException {
        double diferencia = 0;
        SustrateNetwork redFisicaAux = new SustrateNetwork();
        redFisicaAux = (SustrateNetwork) redFisica.clone();
        for (int sl = 0; sl < solution.length; sl++) {
            int nodoFisicoId = VoneAcoPrincipal.obtenerNodoFisicoId(solution[sl], listaNodos);
            VirtualNode nodoVirtual = VoneAcoPrincipal.obtenerNodoVirtual(solution[sl], listaNodos);
            VoneAcoPrincipal.actualizarNodoFisico(nodoFisicoId, nodoVirtual.getCapacidadCPU(), redFisicaAux);
        }
        int maxCpu = 0;
        int minCpu = 1000;
        for (SustrateNode nf : redFisicaAux.getNodosFisicos()) {
            if (nf.capacidadActual() > maxCpu) {
                maxCpu = nf.capacidadActual();
            }
            if (nf.capacidadActual() < minCpu) {
                minCpu = nf.capacidadActual();
            }
        }
        diferencia = maxCpu - minCpu;
        double distancia = obtenerDistanciaSolucion(solution);
        double fitness = diferencia + 0.5 * distancia;
        return fitness;
    }

    private double obtenerFragmentacion(int[] solution) throws CloneNotSupportedException {
        SustrateNetwork redFisicaAux = (SustrateNetwork) redFisica.clone();
        VirtualNetwork redVirtualAux = new VirtualNetwork();
        redVirtualAux = redVirtual;
        SolucionAco solucionAco = new SolucionAco();

        redFisicaAux = VoneAcoPrincipal.ubicarEnlacesFrag(solution, redFisicaAux, redVirtualAux, solucionAco, listaNodos);
        double fitness = Long.MAX_VALUE;
        int maxIndex = 0;
        if (redFisicaAux != null) {
            for (SustrateEdge ef : redFisicaAux.getEnlacesFisicos()) {
                for (int i = 0; i < ef.getFrequencySlot().length; i++) {
                    if (ef.getFrequencySlot()[i]) {
                        if (i > maxIndex) {
                            maxIndex = i;
                        }
                    }
                }
            }
            double indexFrag;
            indexFrag = maxIndex / redFisicaAux.getNroEnlaces();
            double distancia = obtenerDistanciaSolucion(solution);
            fitness = indexFrag + 0.5 * distancia;
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

    private double obtenerDistanciaRSA(int origenFisico, int destinoFisico, int origenvirtual, int destinovirtual) throws CloneNotSupportedException {
        SustrateNetwork redFisicaAUX = (SustrateNetwork) redFisica.clone();
        SolucionAco solAco = new SolucionAco();
        for (VirtualEdge ev : redVirtual.getEnlacesVirtuales()) {
            if (ev.getNodoUno().getIdentificador() == origenvirtual && ev.getNodoDos().getIdentificador() == destinovirtual) {
                Rmsa.realizarRmsa(redFisicaAUX, Integer.toString(origenFisico), Integer.toString(destinoFisico),
                        1, ev.getCantidadFS(), solAco, ev,false);
                break;
            }
            if (ev.getNodoUno().getIdentificador() == destinovirtual && ev.getNodoDos().getIdentificador() == origenvirtual) {
                Rmsa.realizarRmsa(redFisicaAUX, Integer.toString(destinoFisico), Integer.toString(origenFisico),
                        1, ev.getCantidadFS(), solAco, ev,false);
                break;
            }
        }
        if (redFisicaAUX != null) {

            return (solAco.getList() == null || solAco.getList().isEmpty()) ? 999999999 : solAco.getList().get(0).getTotalCost();
        }
        return 999999999;
    }

}
