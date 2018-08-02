/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.una.pol.vone.aco.problema;

import java.util.ArrayList;
import java.util.List;
import py.una.pol.vone.aco.entidades.fisicas.NodosFisicosCandidatos;
import py.una.pol.vone.aco.entidades.virtuales.VirtualNodeStatus;
import py.una.pol.vone.aco.utils.OrdenamientoNodosFisicos;
import py.una.pol.vone.aco.utils.OrdenamientoNodosVirtuales;
import py.una.pol.vone.aco.utils.rsa.Rmsa;
import static py.una.pol.vone.aco.utils.rsa.Rmsa.calcularPeso;
import py.una.pol.vone.aco.utils.rsa.SolucionAco;
import py.una.pol.vone.aco.utils.rsa.kshortespaths.Graph;
import py.una.pol.vone.aco.utils.rsa.kshortespaths.ksp.Yen;
import py.una.pol.vone.aco.utils.rsa.kshortespaths.utilrsa.Path;
import static py.una.pol.vone.simulator.algorithms.Voraz.asignarFSEnlacesFisicos;
import static py.una.pol.vone.simulator.algorithms.Voraz.estaLibre;
import py.una.pol.vone.simulator.model.SustrateEdge;
import py.una.pol.vone.simulator.model.SustrateNetwork;
import py.una.pol.vone.simulator.model.SustrateNode;
import py.una.pol.vone.simulator.model.VirtualEdge;
import py.una.pol.vone.simulator.model.VirtualNetwork;
import py.una.pol.vone.simulator.model.VirtualNode;
import thiagodnf.jacof.aco.MaxMinAntSystem;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.problem.Problem;
import thiagodnf.jacof.util.ExecutionStats;

/**
 *
 * @author Diana Ferreira
 */
public class VoneAcoPrincipal {

    public static int ejecutarAco(SustrateNetwork redFisica, VirtualNetwork redVirtual) throws Exception {
        List<Integer> listaPosicionInicial = new ArrayList<>();
//        System.out.println("**********");
        //System.out.println("****" + redVirtual.getNombre() + "*******");
//        System.out.println("**********");

        List<VirtualNodeStatus> nodosOrdenados = OrdenamientoNodosVirtuales.ordenarNodos(redVirtual);

        List<NodosFisicosCandidatos> probList = new ArrayList<>();

        nodosOrdenados.forEach((virtualNode) -> {
            probList.addAll(OrdenamientoNodosFisicos.cpuNodoFisico(redFisica, virtualNode.getNodoVirtual(), virtualNode.getOrden(), listaPosicionInicial));
        });

        List<Integer> nodosMap = new ArrayList<>();
        probList.stream().filter((p) -> (!nodosMap.contains(p.getNodoVirtual().getIdentificador()))).forEachOrdered((p) -> {
            nodosMap.add(p.getNodoVirtual().getIdentificador());
        });
        if (nodosMap.size() < redVirtual.getNroNodos()) {
            return 1;
        }

        List<NodosFisicosCandidatos> probAuxList = new ArrayList<>();
        for (int n = 1; n < redVirtual.getNroNodos(); n++) {
            int cont = 0;
            for (int pr = 0; pr < probList.size(); pr++) {

                if (probList.get(pr).getNodoVirtual().getIdentificador() == n) {
                    cont++;
                    probAuxList.add(probList.get(pr));

                }

            }
            if (cont > 1) {
                probAuxList.clear();
            }
        }

        boolean esMapeable = true;

        for (int pl = 0; pl < probAuxList.size(); pl++) {
            for (int pa = pl + 1; pa < probAuxList.size(); pa++) {
                if (probAuxList.get(pl).getNodoFisico().getIdentificador() == probAuxList.get(pa).getNodoFisico().getIdentificador()) {
                    esMapeable = false;
                }

            }
        }
        //  //System.out.println(probList);
        if (esMapeable) {
            Problem problem = new VoneProblemCosto(probList, redFisica, redVirtual);

            MaxMinAntSystem aco = new MaxMinAntSystem(problem);
            aco.setListaPosicionInicial(listaPosicionInicial);
            aco.setNumberOfAnts(50);
            aco.setNumberOfIterations(1);
            aco.setAlpha(1.0);
            aco.setBeta(2.0);
            aco.setRho(0.1);
            aco.setStagnation(10);

            ExecutionStats es = ExecutionStats.execute(aco, problem);

            es.printStats();
            Ant globalBest = aco.getGlobalBest();
            int[] solution = globalBest.getSolution();
            SolucionAco solucionAco = new SolucionAco();
            if (solution.length == redVirtual.getNroNodos()) {
                ubicarEnlaces(solution, redFisica, redVirtual, solucionAco, probList);
            } else {
                return 1;
            }
        }
        return 0;
    }

    public Path calcularCamino(SustrateNetwork sustrateNetwork, String nodoUno, String nodoDos) throws Exception {

        Graph graph = new Graph();
        sustrateNetwork
                .getEnlacesFisicos().stream().map((sustrateEdge) -> {
                    if (sustrateEdge.getNodoUno() == null
                            || sustrateEdge.getNodoDos() == null) {
                        //  //System.out.println("SustrateEdge nulo");
                    }
                    return sustrateEdge;
                }).map((sustrateEdge) -> {
            graph.addEdge(String.valueOf(sustrateEdge.getNodoUno()
                    .getIdentificador()), String.valueOf(sustrateEdge
                            .getNodoDos().getIdentificador()),
                    calcularPeso(sustrateEdge));
            return sustrateEdge;
        }).forEachOrdered((sustrateEdge) -> {
            // se carga desde el nodo inverso por ser un grafo bidireccional
            graph.addEdge(String.valueOf(sustrateEdge.getNodoDos()
                    .getIdentificador()), String.valueOf(sustrateEdge
                            .getNodoUno().getIdentificador()),
                    calcularPeso(sustrateEdge));
        });
        List<Path> ksp = null;
        Yen yenAlgorithm = new Yen();
        ksp = yenAlgorithm.ksp(graph, nodoUno, nodoDos, 1);
        if (!ksp.isEmpty()) {
            return ksp.get(0);
        } else {
            return null;
        }
    }

    private static int obtenerNodoVirtualId(int posicion, List<NodosFisicosCandidatos> listaNodos) {
        return listaNodos.get(posicion).getNodoVirtual().getIdentificador();

    }

    public static int obtenerNodoFisicoId(int posicion, List<NodosFisicosCandidatos> listaNodos) {
        return listaNodos.get(posicion).getNodoFisico().getIdentificador();
    }

    private static SustrateNode obtenerNodoFisico(int posicion, List<NodosFisicosCandidatos> listaNodos) {
        return listaNodos.get(posicion).getNodoFisico();
    }

    public static VirtualNode obtenerNodoVirtual(int posicion, List<NodosFisicosCandidatos> listaNodos) {
        return listaNodos.get(posicion).getNodoVirtual();

    }

    private static void actualizarNodoVirtual(int nodoVirtualId, VirtualNetwork redVirtual, SustrateNode nodoFisico) {
        for (VirtualNode nv : redVirtual.getNodosVirtuales()) {
            if (nv.getIdentificador() == nodoVirtualId) {
                nv.setNodoFisico(nodoFisico);
                nv.setMapeado(true);
                break;
            }
        }
    }

    public static void actualizarNodoFisico(int nodoFisicoId, int capacidadCpu, SustrateNetwork redFisica) {
        redFisica.getNodosFisicos().stream().filter((nf) -> (nf.getIdentificador() == nodoFisicoId)).forEachOrdered((nf) -> {
            nf.asignarRecursoCPU(capacidadCpu);
        });
    }

    private static ArrayList<SustrateEdge>
            pasarEnlacesFisicos(List<Path> camino,
                    ArrayList<SustrateEdge> enlacesFisicos, int caminoElegido) {
        ArrayList<SustrateEdge> enlaces = new ArrayList<>();
        camino.get(caminoElegido).getEdges().forEach((enlaceDelCamino) -> {
            int idNodoUno = Integer.parseInt(enlaceDelCamino.getFromNode());
            int idNodoDos = Integer.parseInt(enlaceDelCamino.getToNode());
            for (SustrateEdge enlaceFisico : enlacesFisicos) {
                if (enlaceFisico.getNodoUno().getIdentificador() == idNodoUno) {
                    if (enlaceFisico.getNodoDos().getIdentificador()
                            == idNodoDos) {
                        enlaces.add(enlaceFisico);
                        break;
                    }
                } else if (enlaceFisico.getNodoDos().getIdentificador()
                        == idNodoUno) {
                    if (enlaceFisico.getNodoUno().getIdentificador()
                            == idNodoDos) {
                        enlaces.add(enlaceFisico);
                        break;
                    }
                }
            }
        });
        return enlaces;
    }

    public static int ubicarEnlaces(int[] solution, SustrateNetwork redFisica, VirtualNetwork redVirtual, SolucionAco solucionAco, List<NodosFisicosCandidatos> probList) {

        for (VirtualEdge ev1 : redVirtual.getEnlacesVirtuales()) {
            for (int si = 0; si < solution.length; si++) {

                for (int sj = si + 1; sj < solution.length; sj++) {
                    if (si != sj) {
                        int origenVirtual = probList.get(solution[si]).getNodoVirtual().getIdentificador();
                        int destinoVirtual = probList.get(solution[sj]).getNodoVirtual().getIdentificador();
                        if ((ev1.getNodoUno().getIdentificador() == origenVirtual && ev1.getNodoDos().getIdentificador() == destinoVirtual)) {
                            int origenFisico = probList.get(solution[si]).getNodoFisico().getIdentificador();
                            int destinoFisico = probList.get(solution[sj]).getNodoFisico().getIdentificador();
                            //    //System.out.println("A Test: " + origenVirtual + " " + destinoVirtual);
                            Rmsa.realizarRmsa(redFisica, Integer.toString(origenFisico), Integer.toString(destinoFisico),
                                    1, ev1.getCantidadFS(), solucionAco, ev1, false);
                            if (redFisica != null) {
                                ev1.setMapeado(true);
                            } else {
                                return 2;
                            }
                        } else if (ev1.getNodoUno().getIdentificador() == destinoVirtual && ev1.getNodoDos().getIdentificador() == origenVirtual) {

                            int origenFisico = probList.get(solution[sj]).getNodoFisico().getIdentificador();
                            int destinoFisico = probList.get(solution[si]).getNodoFisico().getIdentificador();
                            //  //System.out.println("B Test: " + origenVirtual + " " + destinoVirtual);

                            Rmsa.realizarRmsa(redFisica, Integer.toString(origenFisico), Integer.toString(destinoFisico),
                                    1, ev1.getCantidadFS(), solucionAco, ev1, false);
                            if (redFisica != null) {
                                ev1.setMapeado(true);
                            } else {
                                return 2;
                            }
                        }
                    }
                }
            }
        }
        int camino = 0;
        int cont = 0;
        for (VirtualEdge enlaceVirtual : redVirtual.getEnlacesVirtuales()) {

            ArrayList<SustrateEdge> enlacesFisicosARelacionar = null;
            if (redFisica != null) {
                if (camino < solucionAco.getList().size()) {
                    enlacesFisicosARelacionar = pasarEnlacesFisicos(solucionAco.getList(), redFisica.getEnlacesFisicos(), camino);
                }
            }
            if (enlacesFisicosARelacionar != null) {
                for (int fi = 0; fi <= redFisica.getCantidadFS()
                        - enlaceVirtual.getCantidadFS(); fi++) {
                    if (estaLibre(enlacesFisicosARelacionar, fi,
                            fi + enlaceVirtual.getCantidadFS() - 1)) {
                        asignarFSEnlacesFisicos(enlacesFisicosARelacionar, fi,
                                fi + enlaceVirtual.getCantidadFS() - 1);
                        enlaceVirtual.setEnlaceFisico(
                                enlacesFisicosARelacionar);
                        enlaceVirtual.setPosicionFisica(fi);
                        enlaceVirtual.setMapeado(true);
                        cont++;
                        break;
                    }
                }
                camino++;
            }
        }
        if (cont < redVirtual.getNroEnlaces()) {
            return 2;
        }
        for (int sl = 0; sl < solution.length; sl++) {
            int nodoFisicoId = obtenerNodoFisicoId(solution[sl], probList);
            VirtualNode nodoVirtual = obtenerNodoVirtual(solution[sl], probList);
            actualizarNodoFisico(nodoFisicoId, nodoVirtual.getCapacidadCPU(), redFisica);
        }

        for (int sm = 0; sm < solution.length; sm++) {
            int nodoVirtualId = obtenerNodoVirtualId(solution[sm], probList);
            actualizarNodoVirtual(nodoVirtualId, redVirtual, obtenerNodoFisico(solution[sm], probList));
            redVirtual.setMapeado(true);

        }
        return 0;

    }

    public static SustrateNetwork ubicarEnlacesFrag(int[] solution, SustrateNetwork redFisica, VirtualNetwork redVirtual, SolucionAco solucionAco, List<NodosFisicosCandidatos> probList) {

        for (VirtualEdge ev1 : redVirtual.getEnlacesVirtuales()) {
            for (int si = 0; si < solution.length; si++) {

                for (int sj = si + 1; sj < solution.length; sj++) {
                    if (si != sj) {
                        int origenVirtual = probList.get(solution[si]).getNodoVirtual().getIdentificador();
                        int destinoVirtual = probList.get(solution[sj]).getNodoVirtual().getIdentificador();
                        if ((ev1.getNodoUno().getIdentificador() == origenVirtual && ev1.getNodoDos().getIdentificador() == destinoVirtual)) {
                            int origenFisico = probList.get(solution[si]).getNodoFisico().getIdentificador();
                            int destinoFisico = probList.get(solution[sj]).getNodoFisico().getIdentificador();
                            //    //System.out.println("A Test: " + origenVirtual + " " + destinoVirtual);
                            Rmsa.realizarRmsa(redFisica, Integer.toString(origenFisico), Integer.toString(destinoFisico),
                                    1, ev1.getCantidadFS(), solucionAco, ev1, false);
                            if (redFisica != null) {
                                ev1.setMapeado(true);
                            } else {
                                break;
                            }
                        } else if (ev1.getNodoUno().getIdentificador() == destinoVirtual && ev1.getNodoDos().getIdentificador() == origenVirtual) {

                            int origenFisico = probList.get(solution[sj]).getNodoFisico().getIdentificador();
                            int destinoFisico = probList.get(solution[si]).getNodoFisico().getIdentificador();
                            //  //System.out.println("B Test: " + origenVirtual + " " + destinoVirtual);

                            Rmsa.realizarRmsa(redFisica, Integer.toString(origenFisico), Integer.toString(destinoFisico),
                                    1, ev1.getCantidadFS(), solucionAco, ev1, false);
                            if (redFisica != null) {
                                ev1.setMapeado(true);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        int camino = 0;
        int cont = 0;
        for (VirtualEdge enlaceVirtual : redVirtual.getEnlacesVirtuales()) {

            ArrayList<SustrateEdge> enlacesFisicosARelacionar = null;
            if (redFisica != null) {
                if (camino < solucionAco.getList().size()) {
                    enlacesFisicosARelacionar = pasarEnlacesFisicos(solucionAco.getList(), redFisica.getEnlacesFisicos(), camino);
                }
            }
            if (enlacesFisicosARelacionar != null) {
                for (int fi = 0; fi <= redFisica.getCantidadFS()
                        - enlaceVirtual.getCantidadFS(); fi++) {
                    if (estaLibre(enlacesFisicosARelacionar, fi,
                            fi + enlaceVirtual.getCantidadFS() - 1)) {
                        asignarFSEnlacesFisicos(enlacesFisicosARelacionar, fi,
                                fi + enlaceVirtual.getCantidadFS() - 1);
                        enlaceVirtual.setEnlaceFisico(
                                enlacesFisicosARelacionar);
                        enlaceVirtual.setPosicionFisica(fi);
                        enlaceVirtual.setMapeado(true);
                        cont++;
                        break;
                    }
                }
                camino++;
            }
        }
        if (cont < redVirtual.getNroEnlaces()) {

        }
        for (int sl = 0; sl < solution.length; sl++) {
            int nodoFisicoId = obtenerNodoFisicoId(solution[sl], probList);
            VirtualNode nodoVirtual = obtenerNodoVirtual(solution[sl], probList);
            actualizarNodoFisico(nodoFisicoId, nodoVirtual.getCapacidadCPU(), redFisica);
        }

        for (int sm = 0; sm < solution.length; sm++) {
            int nodoVirtualId = obtenerNodoVirtualId(solution[sm], probList);
            actualizarNodoVirtual(nodoVirtualId, redVirtual, obtenerNodoFisico(solution[sm], probList));
            redVirtual.setMapeado(true);

        }
        return redFisica;

    }
}
