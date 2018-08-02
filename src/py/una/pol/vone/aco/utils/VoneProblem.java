//package py.una.pol.vone.aco.utils;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import py.una.pol.vone.aco.entidades.fisicas.NodosFisicosCandidatos;
//
//import static py.una.pol.vone.aco.utils.rsa.Rmsa.calcularPeso;
//import py.una.pol.vone.aco.utils.rsa.kshortespaths.Graph;
//import py.una.pol.vone.aco.utils.rsa.kshortespaths.ksp.Yen;
//import py.una.pol.vone.aco.utils.rsa.kshortespaths.utilrsa.Path;
//import py.una.pol.vone.simulator.model.SustrateEdge;
//import py.una.pol.vone.simulator.model.SustrateNetwork;
//import py.una.pol.vone.simulator.model.SustrateNode;
//import py.una.pol.vone.simulator.model.VirtualEdge;
//import py.una.pol.vone.simulator.model.VirtualNetwork;
//import py.una.pol.vone.simulator.model.VirtualNode;
//import thiagodnf.jacof.problem.Problem;
//
//public class VoneProblem extends Problem {
//
//    protected List<VirtualNode> listaNodosVirtuales;
//    protected List<VirtualEdge> listaEnlacesVirtuales;
//
//    protected SustrateNetwork redFisica;
//    protected double cnn;
//    /**
//     * esta matriz debera ser cargado cuando hallo la distancia de entre los
//     * nodos
//     */
//    protected double[][] distance;
//    // aqui se almacenaria  las probabilidades de los nodos 
//    protected double[] probabilidad;
//
//    public VoneProblem() {
//        super();
//    }
//
//    public VoneProblem(SustrateNetwork redFisica, VirtualNetwork redVirtual) throws IOException {
//
//        this.listaNodosVirtuales = redVirtual.getNodosVirtuales();
//        this.listaEnlacesVirtuales = redVirtual.getEnlacesVirtuales();
//
////        calcularCamino(SustrateNetwork sustrateNetwork
////        , String nodoUno, String nodoDos
//        // );
////        InstanceReader reader = new InstanceReader(new File(filename));
////
////        this.numberOfItems = reader.readIntegerValue();
////        this.weights = reader.readDoubleArray();
////        this.profits = reader.readDoubleArray();
////        this.capacity = reader.readDoubleValue();
////
////        this.profitR = Arrays.stream(profits).reduce(Double::sum).getAsDouble();
////        this.weightR = Arrays.stream(weights).reduce(Double::sum).getAsDouble();
//    }
//
//    @Override
//    public double evaluate(int[] solution) {
//
//        double totalDistance = 0;
//
//        for (int h = 1; h < solution.length; h++) {
//
//            int i = solution[h - 1];
//            int j = solution[h];
//
//            totalDistance += distance[i][j];
//        }
//
//        return totalDistance;
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
//        distance[Integer.parseInt(nodoUno)][Integer.parseInt(nodoDos)] = ksp.get(0).getTotalCost();
//
//        if (!ksp.isEmpty()) {
//            return ksp.get(0);
//        } else {
//            return null;
//        }
//
//    }
//
//    /**
//     *
//     * @param redFisica
//     * @param nodoVirtual
//     * @return lista de probabilidades, donde cada posicion del array va a a
//     * representar el identificador del nodo
//     */
//    private int obtenerCpu(int i)
//    {
//    return 
//    }
////    public List<NodosFisicosCandidatos> obtenerNodosCandidatosPorProbabilidad(SustrateNetwork redFisica, VirtualNode nodoVirtual) {
////        OrdenamientoNodosFisicos ordNodosFisicos = new OrdenamientoNodosFisicos();
////        List<NodosFisicosCandidatos> probNodosFisicos = ordNodosFisicos.probabilidadNodosFisicos(redFisica, nodoVirtual);
////        //verificar qeu probNodosFisicos sea distinto de null
////        for (NodosFisicosCandidatos probNodosFisico : probNodosFisicos) {
////            probabilidad[probNodosFisico.getNodoFisico().getIdentificador()] = probNodosFisico.getProbabilidadNodoFisico();
////
////        }
////
////        return probNodosFisicos;
////    }
//
//    /**
//     * Heuristica
//     *
//     * @param i
//     * @param j
//     * @return
//     */
//    @Override
//    public double getNij(int i, int j) {
//        return (1.0 / distance[i][j]) * probabilidad[j];
//
//    }
//
//    /**
//     *
//     * @param tourLength
//     * @param i
//     * @param j
//     * @return
//     */
//    @Override
//    public double getDeltaTau(double tourLength, int i, int j) {
//        return 1.0 / tourLength;
//    }
//
//    @Override
//    public double getCnn() {
//        // suma la distancia de la solucion
//        return cnn;
//    }
//    //cnn=evaluate(solve(this)); this-> es el problema instanciado evalua la distancia de la solucion
//
//    @Override
//    public boolean better(double s1, double best) {
//        return s1 > best;
//    }
//
//    // public double capacity(int[] solution) {
//    //
//    // Preconditions.checkNotNull(solution, "The solution cannot be null");
//    //
//    // double sum = 0.0;
//    //
//    // for (int i = 0; i < solution.length; i++) {
//    // sum += this.weights[solution[i]];
//    // }
//    //
//    // return sum;
//    // }
//    //
//    @Override
//    public int getNumberOfNodes() {
//        return this.redFisica.getNroNodos();
//    }
//
//    // adaptar
//    @Override
//    public List<Integer> initNodesToVisit(int startingNode) {
//        VirtualNode nodoVirtual = new VirtualNode();
//
//        for (VirtualNode nodo : listaNodosVirtuales) {
//            if (nodo.getIdentificador() == startingNode) {
//                nodoVirtual = nodo;
//            }
//        }
//        List<NodosFisicosCandidatos> listaProbabilidad = null;
//        listaProbabilidad = obtenerNodosCandidatosPorProbabilidad(redFisica, nodoVirtual);
//        List<Integer> nodesToVisit = new ArrayList<>();
//
//        for (NodosFisicosCandidatos nodos : listaProbabilidad) {
//            nodesToVisit.add(nodos.getNodoFisico().getIdentificador());
//        }
//        return nodesToVisit;
//    }
//
//    @Override
//    public List<Integer> updateNodesToVisit(List<Integer> tour,
//            List<Integer> nodesToVisit) {
//
//        List<Integer> nodesToRemove = new ArrayList<Integer>();
//        for (Integer nodo : nodesToVisit) {
//            for (SustrateNode nodoFisico : redFisica.getNodosFisicos()) {
//                if (nodoFisico.getIdentificador() == nodo) {
//                    if (nodoFisico.getCapacidadCPU() < 0) {
//                        nodesToRemove.add(nodo);
//                    }
//                }
//            }
//        }
//
////
////        double totalCost = 0.0;
////
////        for (Integer i : tour) {
////           // totalCost += weights[i];
////        }
////
////        for (Integer i : nodesToVisit) {
////            
////        }
////
//        for (Integer i : nodesToRemove) {
//            nodesToVisit.remove(i);
//        }
////        
//        return nodesToVisit;
//    }
//
//    @Override
//    public String toString() {
//        return VoneProblem.class.getSimpleName();
//    }
//
//    public List<VirtualNode> getListaNodosVirtuales() {
//        return listaNodosVirtuales;
//    }
//
//    public void setListaNodosVirtuales(List<VirtualNode> listaNodosVirtuales) {
//        this.listaNodosVirtuales = listaNodosVirtuales;
//    }
//
//    public List<VirtualEdge> getListaEnlacesVirtuales() {
//        return listaEnlacesVirtuales;
//    }
//
//    public void setListaEnlacesVirtuales(List<VirtualEdge> listaEnlacesVirtuales) {
//        this.listaEnlacesVirtuales = listaEnlacesVirtuales;
//    }
//
//    public SustrateNetwork getRedFisica() {
//        return redFisica;
//    }
//
//    public void setRedFisica(SustrateNetwork redFisica) {
//        this.redFisica = redFisica;
//    }
//
//    public void setCnn(double cnn) {
//        this.cnn = cnn;
//    }
//
//}
