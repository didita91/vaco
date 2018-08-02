///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package py.una.pol.vone.aco.problema;
//
//import java.util.ArrayList;
//import java.util.List;
//import py.una.pol.vone.aco.entidades.fisicas.ProbabilidadSustrateNode;
//import py.una.pol.vone.aco.entidades.virtuales.VirtualNodeStatus;
//import py.una.pol.vone.aco.utils.OrdenamientoNodosFisicos;
//import py.una.pol.vone.aco.utils.OrdenamientoNodosVirtuales;
//import py.una.pol.vone.aco.utils.rsa.Rmsa;
//import static py.una.pol.vone.aco.utils.rsa.Rmsa.calcularPeso;
//import py.una.pol.vone.aco.utils.rsa.SolucionAco;
//import py.una.pol.vone.aco.utils.rsa.kshortespaths.Graph;
//import py.una.pol.vone.aco.utils.rsa.kshortespaths.ksp.Yen;
//import py.una.pol.vone.aco.utils.rsa.kshortespaths.utilrsa.Path;
//import static py.una.pol.vone.simulator.algorithms.Voraz.asignarFSEnlacesFisicos;
//import static py.una.pol.vone.simulator.algorithms.Voraz.estaLibre;
//import py.una.pol.vone.simulator.model.SustrateEdge;
//import py.una.pol.vone.simulator.model.SustrateNetwork;
//import py.una.pol.vone.simulator.model.SustrateNode;
//import py.una.pol.vone.simulator.model.VirtualEdge;
//import py.una.pol.vone.simulator.model.VirtualNetwork;
//import py.una.pol.vone.simulator.model.VirtualNode;
//import thiagodnf.jacof.aco.MaxMinAntSystem;
//import thiagodnf.jacof.aco.ant.Ant;
//import thiagodnf.jacof.problem.Problem;
//import thiagodnf.jacof.util.ExecutionStats;
//
///**
// *
// * @author Diana Ferreira
// */
//public class PruebaAco {
//
//    public static int aco(SustrateNetwork redFisica, VirtualNetwork redVirtual) throws Exception {
//        OrdenamientoNodosVirtuales ordenados = new OrdenamientoNodosVirtuales();
//        List<VirtualNodeStatus> nodosOrdenados = ordenados.ordenarNodosVirtuales(redVirtual);
//        List<NodosVirtualProbabilidad> nodoVirtualFisico = new ArrayList<NodosVirtualProbabilidad>();
//        OrdenamientoNodosFisicos ordeFisicos = new OrdenamientoNodosFisicos();
//        List<List<ProbabilidadSustrateNode>> listaSinComb = new ArrayList<>();
//        List<List<ProbabilidadSustrateNode>> listaComb = new ArrayList<>();
//
//        List<ProbabilidadSustrateNode> probList = new ArrayList<>();
//
//        for (VirtualNodeStatus virtualNode : nodosOrdenados) {
//
//            probList.addAll(ordeFisicos.probabilidadNodosFisicos(redFisica, virtualNode.getNodoVirtual()));
//
//        }
//        List<Integer> nodosMap = new ArrayList<>();
//        for (ProbabilidadSustrateNode p : probList) {
//            if (!nodosMap.contains(p.getNodoVirtual().getIdentificador())) {
//                nodosMap.add(p.getNodoVirtual().getIdentificador());
//            }
//        }
//        if (nodosMap.size() < redVirtual.getNroNodos()) {
//            return 1;
//        }
//        List<ProbabilidadSustrateNode> probAuxList = new ArrayList<>();
//        ProbabilidadSustrateNode aux = null;
//        for (int j = 1; j < redVirtual.getNroNodos(); j++) {
//            int cont = 0;
//            for (int i = 0; i < probList.size(); i++) {
//
//                if (probList.get(i).getNodoVirtual().getIdentificador() == j) {
//                    cont++;
//                    probAuxList.add(probList.get(i));
//
//                }
//
//            }
//            if (cont > 1) {
//                probAuxList.clear();
//            }
//        }
//        boolean esMapeable = true;
//
//        for (int i = 0; i < probAuxList.size(); i++) {
//            for (int j = i + 1; j < probAuxList.size(); j++) {
//                if (probAuxList.get(i).getNodoFisico().getIdentificador() == probAuxList.get(j).getNodoFisico().getIdentificador()) {
//                    esMapeable = false;
//                }
//
//            }
//        }
//        //System.out.println(probList);
//        if (esMapeable) {
//            Problem problem = new VoneProblem(probList, redFisica, redVirtual);
//
//            MaxMinAntSystem aco = new MaxMinAntSystem(problem);
//
//            aco.setNumberOfAnts(10);
//            aco.setNumberOfIterations(10);
//            aco.setAlpha(1.0);
//            aco.setBeta(2.0);
//            aco.setRho(0.1);
//            aco.setStagnation(1000);
//
//            ExecutionStats es = ExecutionStats.execute(aco, problem);
//
//            es.printStats();
//            Ant globalBest = aco.getGlobalBest();
//            int[] solution = globalBest.getSolution();
//            Rmsa rmsa = new Rmsa();
//            SolucionAco solucionAco = new SolucionAco();
//            if (solution.length == redVirtual.getNroNodos()) {
//                //FALTA ACUTALIZAR LOS NODOOOOOS!!! URGENTE MANANA 
//                for (VirtualEdge ev1 : redVirtual.getEnlacesVirtuales()) {
//                    for (int i = 0; i < solution.length; i++) {
//
//                        for (int j = i + 1; j < solution.length; j++) {
//                            if (i != j) {
//                                int origenVirtual = probList.get(solution[i]).getNodoVirtual().getIdentificador();
//                                int destinoVirtual = probList.get(solution[j]).getNodoVirtual().getIdentificador();
//                                if ((ev1.getNodoUno().getIdentificador() == origenVirtual && ev1.getNodoDos().getIdentificador() == destinoVirtual)) {
////                                    || (ev1.getNodoUno().getIdentificador() == destinoVirtual && ev1.getNodoDos().getIdentificador() == origenVirtual)) {
//                                    int origenFisico = probList.get(solution[i]).getNodoFisico().getIdentificador();
//                                    int destinoFisico = probList.get(solution[j]).getNodoFisico().getIdentificador();
//                                    //System.out.println("A Test: " + origenVirtual + " " + destinoVirtual);
//                                    rmsa.realizarRmsa(redFisica, Integer.toString(origenFisico), Integer.toString(destinoFisico),
//                                            1, ev1.getCantidadFS(), solucionAco, ev1);
//                                    if (redFisica != null) {
//                                        ev1.setMapeado(true);
//                                    }
//                                } else if (ev1.getNodoUno().getIdentificador() == destinoVirtual && ev1.getNodoDos().getIdentificador() == origenVirtual) {
//
//                                    int origenFisico = probList.get(solution[j]).getNodoFisico().getIdentificador();
//                                    int destinoFisico = probList.get(solution[i]).getNodoFisico().getIdentificador();
//                                    //System.out.println("B Test: " + origenVirtual + " " + destinoVirtual);
//
//                                    rmsa.realizarRmsa(redFisica, Integer.toString(origenFisico), Integer.toString(destinoFisico),
//                                            1, ev1.getCantidadFS(), solucionAco, ev1);
//                                    if (redFisica != null) {
//                                        ev1.setMapeado(true);
//                                    } else {
//                                        return 2;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                int camino = 0;
//                for (VirtualEdge enlaceVirtual : redVirtual.getEnlacesVirtuales()) {
//
//                    ArrayList<SustrateEdge> enlacesFisicosARelacionar = null;
//                    if (redFisica != null) {
//                        enlacesFisicosARelacionar = pasarEnlacesFisicos(solucionAco.getList(), redFisica.getEnlacesFisicos(), camino);
//                    }
//                    for (int i = 0; i <= redFisica.getCantidadFS()
//                            - enlaceVirtual.getCantidadFS(); i++) {
//                        if (estaLibre(enlacesFisicosARelacionar, i,
//                                i + enlaceVirtual.getCantidadFS() - 1)) {
//                            asignarFSEnlacesFisicos(enlacesFisicosARelacionar, i,
//                                    i + enlaceVirtual.getCantidadFS() - 1);
//                            enlaceVirtual.setEnlaceFisico(
//                                    enlacesFisicosARelacionar);
//                            enlaceVirtual.setPosicionFisica(i);
//                            enlaceVirtual.setMapeado(true);
//                            break;
//                        }
//                    }
//                    camino++;
//                }
//                for (int i = 0; i < solution.length; i++) {
//                    int nodoFisicoId = obtenerNodoFisicoId(solution[i], probList);
//                    VirtualNode nodoVirtual = obtenerNodoVirtual(solution[i], probList);
//                    actualizarNodoFisico(nodoFisicoId, nodoVirtual.getCapacidadCPU(), redFisica);
//                }
//
//                for (int i = 0; i < solution.length; i++) {
//                    int nodoVirtualId = obtenerNodoVirtualId(solution[i], probList);
//                    actualizarNodoVirtual(nodoVirtualId, redVirtual, obtenerNodoFisico(solution[i], probList));
//                    redVirtual.setMapeado(true);
//
//                }
//            } else {
//                return 1;
//            }
//        }
//        return 0;
//    }
//
//    public Path calcularCamino(SustrateNetwork sustrateNetwork, String nodoUno, String nodoDos) throws Exception {
//
//        Graph graph = new Graph();
//        for (SustrateEdge sustrateEdge : sustrateNetwork
//                .getEnlacesFisicos()) {
//            if (sustrateEdge.getNodoUno() == null
//                    || sustrateEdge.getNodoDos() == null) {
//                //System.out.println("SustrateEdge nulo");
//            }
//            graph.addEdge(String.valueOf(sustrateEdge.getNodoUno()
//                    .getIdentificador()), String.valueOf(sustrateEdge
//                            .getNodoDos().getIdentificador()),
//                    calcularPeso(sustrateEdge));
//            // se carga desde el nodo inverso por ser un grafo bidireccional
//            graph.addEdge(String.valueOf(sustrateEdge.getNodoDos()
//                    .getIdentificador()), String.valueOf(sustrateEdge
//                            .getNodoUno().getIdentificador()),
//                    calcularPeso(sustrateEdge));
//        }
//        List<Path> ksp = new ArrayList<>();
//        Yen yenAlgorithm = new Yen();
//        ksp = yenAlgorithm.ksp(graph, nodoUno, nodoDos, 1);
//        if (!ksp.isEmpty()) {
//            return ksp.get(0);
//        } else {
//            return null;
//        }
//    }
//
//    private static int obtenerNodoVirtualId(int posicion, List<ProbabilidadSustrateNode> listaNodos) {
//        return listaNodos.get(posicion).getNodoVirtual().getIdentificador();
//
//    }
//
//    private static int obtenerNodoFisicoId(int posicion, List<ProbabilidadSustrateNode> listaNodos) {
//        return listaNodos.get(posicion).getNodoFisico().getIdentificador();
//    }
//
//    private static SustrateNode obtenerNodoFisico(int posicion, List<ProbabilidadSustrateNode> listaNodos) {
//        return listaNodos.get(posicion).getNodoFisico();
//    }
//
//    private static VirtualNode obtenerNodoVirtual(int posicion, List<ProbabilidadSustrateNode> listaNodos) {
//        return listaNodos.get(posicion).getNodoVirtual();
//
//    }
//
//    private static void actualizarNodoVirtual(int nodoVirtualId, VirtualNetwork redVirtual, SustrateNode nodoFisico) {
//        for (VirtualNode nv : redVirtual.getNodosVirtuales()) {
//            if (nv.getIdentificador() == nodoVirtualId) {
//                nv.setNodoFisico(nodoFisico);
//                nv.setMapeado(true);
//                break;
//            }
//        }
//    }
//
//    private static void actualizarNodoFisico(int nodoFisicoId, int capacidadCpu, SustrateNetwork redFisica) {
//        for (SustrateNode nf : redFisica.getNodosFisicos()) {
//            if (nf.getIdentificador() == nodoFisicoId) {
//
//                nf.asignarRecursoCPU(capacidadCpu);
//
//            }
//        }
//    }
//
//    public static ArrayList<SustrateEdge>
//            pasarEnlacesFisicos(List<Path> camino,
//                    ArrayList<SustrateEdge> enlacesFisicos, int caminoElegido) {
//        ArrayList<SustrateEdge> enlaces = new ArrayList<>();
//        camino.get(caminoElegido).getEdges().forEach((enlaceDelCamino) -> {
//            int idNodoUno = Integer.parseInt(enlaceDelCamino.getFromNode());
//            int idNodoDos = Integer.parseInt(enlaceDelCamino.getToNode());
//            for (SustrateEdge enlaceFisico : enlacesFisicos) {
//                if (enlaceFisico.getNodoUno().getIdentificador() == idNodoUno) {
//                    if (enlaceFisico.getNodoDos().getIdentificador()
//                            == idNodoDos) {
//                        enlaces.add(enlaceFisico);
//                        break;
//                    }
//                } else if (enlaceFisico.getNodoDos().getIdentificador()
//                        == idNodoUno) {
//                    if (enlaceFisico.getNodoUno().getIdentificador()
//                            == idNodoDos) {
//                        enlaces.add(enlaceFisico);
//                        break;
//                    }
//                }
//            }
//        });
//        return enlaces;
//    }
//
//}
