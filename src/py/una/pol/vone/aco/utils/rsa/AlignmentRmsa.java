package py.una.pol.vone.aco.utils.rsa;

import java.util.ArrayList;
import java.util.List;


import py.una.pol.vone.aco.utils.rsa.kshortespaths.Edge;
import py.una.pol.vone.aco.utils.rsa.kshortespaths.utilrsa.Path;
import py.una.pol.vone.simulator.model.SustrateEdge;
import py.una.pol.vone.simulator.model.SustrateNetwork;
import py.una.pol.vone.simulator.model.SustrateNode;

public class AlignmentRmsa {
	/**
     * Obtiene la sumatoria de desalineacion del path con los vecinos de cada nodo
     * @param network red fisica
     * @param path camino a obtener el valor de la desalineacion
     * @param cut identificador del corte a obtener la desalineacion
     * @return Integer en valor de la sumatoria, null caso contrario
     */
	public static Integer numeroDesaliancion(SustrateNetwork network, Path path, Integer cut){
		////System.out.println("NODOS DEL PATH " + path.getNodes());
		////System.out.println("EDGES " + path.getEdges());
		List<SustrateNode> nodosFisicos = network.getNodosFisicos();
		List<String> nodosPath = path.getNodes();
		List<Edge> edges = path.getEdges();
		Integer identificadorNodo = -1;
		Integer sumatoriaDesaliniacion = 0;
		List<Integer> excluir = new ArrayList<Integer>();
		for (int i = 0; i < nodosPath.size(); i++) {
			identificadorNodo = -1;
			if( i == 0){
				////System.out.println("Nodo extremo primero " + nodosPath.get(i));
				identificadorNodo = identificarNodo(nodosFisicos, Integer.valueOf(edges.get(0).getFromNode()));
				if(identificadorNodo != -1){
					excluir.clear();
					excluir.add(Integer.valueOf(edges.get(0).getToNode()));
					excluir.add(-1);
					sumatoriaDesaliniacion = sumatoriaDesaliniacion + sumatoria(nodosFisicos.get(identificadorNodo).getAdyacentes(), 
							edges.get(0), excluir, cut, identificadorNodo.toString());
					
				}
			} else if(i == nodosPath.size() - 1){
				////System.out.println("Nodos extremo ultimo " + nodosPath.get(i));
				identificadorNodo = identificarNodo(nodosFisicos, Integer.valueOf(edges.get(edges.size() -1).getToNode()));
				if(identificadorNodo != -1){
					excluir.clear();
					excluir.add(Integer.valueOf(edges.get(edges.size() - 1).getFromNode()));
					excluir.add(-1);
					sumatoriaDesaliniacion = sumatoriaDesaliniacion + sumatoria(nodosFisicos.get(identificadorNodo).getAdyacentes(), 
							edges.get(edges.size() -1),
							excluir, cut, identificadorNodo.toString());
				}
			} else {
				////System.out.println("Otros nodos internos " + nodosPath.get(i));
				excluir.clear();
				excluir.add(Integer.valueOf(nodosPath.get(i - 1)));
				excluir.add(Integer.valueOf(nodosPath.get(i + 1)));
				for (Edge edge : edges) {
					if(edge.getFromNode() == nodosPath.get(i)){
						////System.out.println("FROM NODE: " + edge.getFromNode() + " TO NODE: " + edge.getToNode());
						identificadorNodo = identificarNodo(nodosFisicos, Integer.valueOf(nodosPath.get(i)));
						if(identificadorNodo != -1){
							sumatoriaDesaliniacion = sumatoriaDesaliniacion + sumatoria(nodosFisicos.get(identificadorNodo).getAdyacentes(), 
									edge,
									excluir, cut, identificadorNodo.toString());
							
						}
					} else if(edge.getToNode() == nodosPath.get(i)){
						////System.out.println("TO NODE: " + edge.getToNode() + " FROM TO: " + edge.getFromNode());
						identificadorNodo = identificarNodo(nodosFisicos, Integer.valueOf(nodosPath.get(i)));
						if(identificadorNodo != -1){
							sumatoriaDesaliniacion = sumatoriaDesaliniacion + sumatoria(nodosFisicos.get(identificadorNodo).getAdyacentes(), 
									edge,
									excluir, cut, identificadorNodo.toString());
							
						}
					}
				}

			}
		}
		return sumatoriaDesaliniacion;
	}

	/**
     * Realiza la sumatoria del un enlace especifico
     * @param sustrateEdges enlaces de la red fisica
     * @param edge enlace del path
     * @param excluir excluye un edge que pertenece al path de la lista de adyacentes
     * @param cut indice del frecuency slot a sumar
     * @param nodoActual nodo de donde obtener los vecinos
     * @return Integer valor de la sumatoria de los vecinos de un egde
     */
	private static Integer sumatoria(List<SustrateEdge> sustrateEdges, Edge edge, List<Integer> excluir, Integer cut, String nodoActual){
		Integer cont = 0;
		//String nodoActual = Integer.valueOf(edge.getToNode()) == excluir ? edge.getFromNode():edge.getToNode();
		for (SustrateEdge sustrateEdge : sustrateEdges) {
			
			if(sustrateEdge.getNodoUno().getIdentificador() !=  Integer.valueOf(nodoActual)){
				if(sustrateEdge.getNodoUno().getIdentificador() != excluir.get(0) && 
						sustrateEdge.getNodoUno().getIdentificador() != excluir.get(1)){
					if(sustrateEdge.getFrequencySlot()[cut] == edge.getFrequencySlot()[cut]){
						cont++;
					}
				}
			} else if(sustrateEdge.getNodoDos().getIdentificador() !=  Integer.valueOf(nodoActual)){
				if(sustrateEdge.getNodoDos().getIdentificador() != excluir.get(0) && 
						sustrateEdge.getNodoDos().getIdentificador() != excluir.get(1)){
					if(sustrateEdge.getFrequencySlot()[cut] == edge.getFrequencySlot()[cut]){
						cont++;
					}
				}
			}
			
		}
		return cont;
	}
	
	/**
     * Obtiene el indice del identificador del sustrate node
     * @param sustrateEdges enlaces de la red fisica
     * @param edge enlace del path
     * @return Integer valor de la sumatoria de los vecinos de un egde
     */
	private static Integer identificarNodo(List<SustrateNode> nodosFisicos, int id){
		for (int i = 0; i < nodosFisicos.size(); i++) {
			if(nodosFisicos.get(i).getIdentificador() == id){
				return i;
			}
		}
		return -1;
	}


}
