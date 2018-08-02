package py.una.pol.vone.aco.utils.rsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import py.una.pol.vone.aco.utils.rsa.kshortespaths.Edge;
import py.una.pol.vone.aco.utils.rsa.kshortespaths.Graph;
import py.una.pol.vone.aco.utils.rsa.kshortespaths.ksp.Yen;
import py.una.pol.vone.aco.utils.rsa.kshortespaths.utilrsa.Path;
import py.una.pol.vone.simulator.model.SustrateEdge;
import py.una.pol.vone.simulator.model.SustrateNetwork;
import py.una.pol.vone.simulator.model.VirtualEdge;

public class Rmsa {

    private static Double evaluatefuntion1 = 0.0;

    /**
     *
     *
     * @param sustrateNetwork red fisica
     * @param sourceNode nodo origen
     * @param targetNode nodo destino
     * @param k cantidad de shortest path
     * @param slotRequerido cantidad de slot requerido
     * @param solucionAco
     * @param virtualEdge enlace virtual que hay entre ambos nodos source y
     * target
     * @return la red fisica reciba, en caso de que no encuentre un camino
     * existente retorna null
     */
    @SuppressWarnings("rawtypes")
    public static SustrateNetwork realizarRmsa(SustrateNetwork sustrateNetwork,
            String sourceNode, String targetNode, Integer k,
            Integer slotRequerido, SolucionAco solucionAco,
            VirtualEdge virtualEdge, boolean setearPosFisica) {

        try {
            // validaciones de argumentos
            if (sustrateNetwork == null || sourceNode == null
                    || targetNode == null || k == null || slotRequerido == null) {
                //System.out.println("Argumentos recibidos nulos");
            } else if (sustrateNetwork.getCantidadFS() == 0
                    || slotRequerido == 0
                    || sustrateNetwork.getEnlacesFisicos() == null
                    || sustrateNetwork.getEnlacesFisicos().isEmpty()) {
                //System.out.println("Elementos de sustrateNetwork son nulos");
            }

            // //System.out.println(sustrateNetwork);
            Graph graph = new Graph();
            for (SustrateEdge sustrateEdge : sustrateNetwork
                    .getEnlacesFisicos()) {
                if (sustrateEdge.getNodoUno() == null
                        || sustrateEdge.getNodoDos() == null) {
                    //System.out.println("SustrateEdge nulo");
                }
                graph.addEdge(String.valueOf(sustrateEdge.getNodoUno()
                        .getIdentificador()), String.valueOf(sustrateEdge
                                .getNodoDos().getIdentificador()),
                        calcularPeso(sustrateEdge));
                // se carga desde el nodo inverso por ser un grafo bidireccional
                graph.addEdge(String.valueOf(sustrateEdge.getNodoDos()
                        .getIdentificador()), String.valueOf(sustrateEdge
                                .getNodoUno().getIdentificador()),
                        calcularPeso(sustrateEdge));
            }

            List<Path> ksp;
            Yen yenAlgorithm = new Yen();
            ksp = yenAlgorithm.ksp(graph, sourceNode, targetNode, k);
            // //System.out.println("Este es mi path list: " + ksp);
            int n = 0;
            // matriz filas y columnas
            Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
            if (ksp == null || ksp.isEmpty()) {
                // //System.out.println("retorne null en ksp null o vacio");
                return null;
                // throw new ValidationsExceptions("No se encontro camino");
            }
            for (Path p : ksp) {
                // //System.out.println(++n + ") " + p.getNodes());
                ++n;
                boolean[][] matriz = new boolean[sustrateNetwork
                        .getCantidadFS()][p.getEdges().size()];
                Iterator<Edge> iter = p.getEdges().iterator();
                int columna = 0;
                while (iter.hasNext()) {
                    Edge edge = iter.next();
                    edge.setCantidadFS(sustrateNetwork.getCantidadFS());
                    if (CutRmsa.obtenerEdge(sustrateNetwork,
                            Integer.valueOf(edge.getFromNode()),
                            Integer.valueOf(edge.getToNode())) != null) {
                        edge.setFrequencySlot(CutRmsa.obtenerEdge(
                                sustrateNetwork,
                                Integer.valueOf(edge.getFromNode()),
                                Integer.valueOf(edge.getToNode()))
                                .getFrequencySlot());
                        for (int i = 0; i < sustrateNetwork.getCantidadFS(); i++) {
                            matriz[i][columna] = edge.getFrequencySlot()[i];
                        }

                    }
                    columna++;
                }
                p.setCaminos(matriz);
                List<Integer> indiceCortes = CutRmsa.indicesDisponiblesPath(
                        p.getCaminos(), slotRequerido);
                List<Integer> valoresCortes = CutRmsa.calcularValorCorte(
                        matriz, indiceCortes);
                /*
				 * los valores del map es una lista que contiene pos 0 = indice
				 * del corte pos 1 = valor del corte pos 2 = sumatoria de
				 * desalianeacion pos 3 = costo del path pos 4 = size del path
				 * pos 5 = elemento del ksp
                 */
                for (int i = 0; i < indiceCortes.size(); i++) {
                    List<Integer> forMap = new ArrayList<>();
                    forMap.add(indiceCortes.get(i));
                    forMap.add(valoresCortes.get(i));
                    forMap.add(AlignmentRmsa.numeroDesaliancion(
                            sustrateNetwork, p, indiceCortes.get(i)));
                    forMap.add(CutRmsa.calcularCosto(p));
                    forMap.add(p.size());
                    forMap.add(n - 1);
                    map.put("Path" + n + "." + i, forMap);
                }
            }
            Set<?> set = map.entrySet();
            Iterator<?> iterator = set.iterator();
            Double minValue = Double.MAX_VALUE;
            Double valorActual = 0.0;
            String seleccionado = "";
            while (iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                // //System.out.print("key is: "+ mentry.getKey() +
                // " & Value is: ");
                // //System.out.println(mentry.getValue());
                valorActual = (double) (((map.get(mentry.getKey()).get(4) * slotRequerido)
                        + map.get(mentry.getKey()).get(1) + map.get(
                        mentry.getKey()).get(2)) / (double) map.get(
                        mentry.getKey()).get(3));
                // //System.out.println(valorActual);
                if (minValue > valorActual) {
                    seleccionado = (String) mentry.getKey();
                    minValue = valorActual;
                }
            }
            Integer identificadorEnlace;
            if (("".equals(seleccionado))) {
                // //System.out.println("retorne null en que no encuentra valor en el map");
                return null;
            } else {
                List<Integer> listaSeleccionados = map.get(seleccionado);
                // //System.out.println(ksp.get(listaSeleccionados.get(5)));
                for (int i = 0; i < ksp.get(listaSeleccionados.get(5))
                        .getEdges().size(); i++) {
                    identificadorEnlace = obtenerEnlaceFisico(sustrateNetwork,
                            ksp.get(listaSeleccionados.get(5)).getEdges()
                                    .get(i));
                    if (!identificadorEnlace.equals(-1)) {
                        for (SustrateEdge sustrateEdge : sustrateNetwork
                                .getEnlacesFisicos()) {
                            if (identificadorEnlace.equals(sustrateEdge
                                    .getIdentificador())) {
                                // //System.out.println("Longitud enlaces fisicos en el edge virtual "
                                // + virtualEdge.
                                // getEnlaceFisico().size());
                                if (setearPosFisica) {
                                    for (int j = 0; j < slotRequerido; j++) {
                                        sustrateEdge.getFrequencySlot()[Integer
                                                .valueOf(listaSeleccionados.get(0))
                                                + j] = true;
                                    }
                                    virtualEdge
                                            .setPosicionFisica(listaSeleccionados
                                                    .get(0));
                                    virtualEdge.getEnlaceFisico().add(sustrateEdge);
                                }
                                break;

                            }
                        }
                    }
                }
                List<Path> listPath = solucionAco.getList();
                listPath.add(ksp.get(listaSeleccionados.get(5)));
                solucionAco.setList(listPath);
                solucionAco.getVirtualEdge().add(virtualEdge);
                Rmsa.evaluatefuntion1 = evaluatefuntion1
                        + (double) (ksp.get(listaSeleccionados.get(5)).size() * slotRequerido);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sustrateNetwork;

    }

    public static Integer obtenerEnlaceFisico(SustrateNetwork network, Edge edge) {
        for (SustrateEdge sustrateEdge : network.getEnlacesFisicos()) {
            if ((Integer.valueOf(edge.getFromNode()) == sustrateEdge
                    .getNodoUno().getIdentificador() && Integer.valueOf(edge
                            .getToNode()) == sustrateEdge.getNodoDos()
                            .getIdentificador())
                    || (Integer.valueOf(edge.getFromNode()) == sustrateEdge
                    .getNodoDos().getIdentificador() && Integer
                            .valueOf(edge.getToNode()) == sustrateEdge
                    .getNodoUno().getIdentificador())) {
                return sustrateEdge.getIdentificador();
            }
        }
        return -1;
    }

    public static Double calcularPeso(SustrateEdge sustrateEdge) {
        Double peso = 1.0;
//        for (int cont = 0; cont < sustrateEdge.getCantidadFS(); cont++) {
//            if (sustrateEdge.getFrequencySlot()[cont]) {
//                peso++;
//            }
//        }
        // //System.out.println("Identificador sustrateEdge: " +
        // sustrateEdge.getNombre() + " y su peso: " + peso);
        return peso;
    }

    public Double getEvaluatefuntion1() {
        return evaluatefuntion1;
    }

    public void setEvaluatefuntion1(Double evaluatefuntion1) {
        this.evaluatefuntion1 = evaluatefuntion1;
    }

}
