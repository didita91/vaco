package py.una.pol.vone.aco.utils.rsa;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.ValidationException;


import py.una.pol.vone.aco.utils.rsa.kshortespaths.utilrsa.Path;
import py.una.pol.vone.simulator.model.SustrateEdge;
import py.una.pol.vone.simulator.model.SustrateNetwork;

public class CutRmsa {
	/**
     * Obtiene el enlace o edge de la red fisica de a partir de dos nodos fisicos vecinos
     * @param sustrateNetwork red fisica
     * @param from id del nodo fisico origen
     * @param to id del nodo fisico destino
     * @return SustrateEdge si existe, null caso contrario
     */
	public static SustrateEdge obtenerEdge(SustrateNetwork sustrateNetwork, Integer from, Integer to){
		  for (SustrateEdge edge : sustrateNetwork.getEnlacesFisicos()) {
			  if((edge.getNodoUno().getIdentificador() == from 
					  && edge.getNodoDos().getIdentificador() == to) 
					  || (edge.getNodoDos().getIdentificador() == from 
					  && edge.getNodoUno().getIdentificador() == to)){
				  //edge.getFrequencySlot();
				  return edge;
			  }
		  }
		  return null;
	  }
	/*Calcula si el requerimiento de slots tiene lugar para alojarse en el 
	 * enlace fisico edge
	 */
	/*public static boolean tieneSlotDisponible(Edge edge, int slotRequerido){
		boolean band = false;
		//para identificar la maxima iteracion y no salir fuera de rango
		int recorrible = edge.getCantidadFS() - slotRequerido;
		if(edge.getCantidadFS() < slotRequerido ){
			return false;
		} 
		for (int i = 0; i <= recorrible; i++) {
			band = false;
			for (int j = i; j <= i + slotRequerido - 1; j++) {
				if(edge.getFrequencySlot()[j] == true){
					band = true;
				}
			}
			if(!band){
				return true;
			}
		}
		return false;
	}*/
	
	/*Retorna los indices que pueden aceptar los requerimientos del 
	 * enlace fisico edge
	 */
	/*public static List<Integer> indicesDisponibles(Edge edge, int slotRequerido){
		List<Integer> indices = new ArrayList<Integer>();
		boolean band = false;
		//para identificar la maxima iteracion y no salir fuera de rango
		int recorrible = edge.getCantidadFS() - slotRequerido;
		if(edge.getCantidadFS() < slotRequerido ){
			return null;
		} 
		for (int i = 0; i <= recorrible; i++) {
			band = false;
			for (int j = i; j <= i + slotRequerido - 1; j++) {
				if(edge.getFrequencySlot()[j] == true){
					band = true;
				}
			}
			if(!band){
				indices.add(i);
			}
		}
		return indices;
	}*/
	
	/**
     * Metodo que calcula si hay espacio fisico disponible para lo requerido
     * @param matrizPath[][] matriz que representa el conjunto de todos los edges a traves del path
     * @param slotRequerido cantidad de slot que requiere el request
     * @return List<Integer> indices de los espacios disponibles, null en caso de que no se pueda mapear
     * lo requerido
	 * @throws ValidationException 
     */
	public static List<Integer> indicesDisponiblesPath(boolean matrizPath[][], int slotRequerido)
				{
		List<Integer> indices = new ArrayList<Integer>();
			List<Integer> aEliminar = new ArrayList<>();
			boolean band = false;
			int recorrible = matrizPath.length - slotRequerido;
			if(matrizPath.length < slotRequerido){
				return indices;
				//throw new ValidationException("La cantidad de Slot requerido es mayor al disponible");
			}
			for (int i = 0; i <= recorrible; i++) {
				band = false;
				for (int j = i; j <= i + slotRequerido - 1; j++) {
					for (int k = 0; k < matrizPath[i].length; k++) {
						if(matrizPath[j][k] == true){
							band = true;
						}
					}
				}
				if(!band){
					indices.add(i);
				}
			}
			aEliminar = eliminarContiguos(indices, slotRequerido);
			for (int i = 0; i < aEliminar.size(); i++) {
				if(indices.contains(aEliminar.get(i))){
					indices.remove(indices.indexOf(aEliminar.get(i)));
				}
			}
			if(indices.size() == 0){
				return indices;
				//throw new ValidationException("Sin indices disponibles para el corte");
			}
		
		return indices;
	}
	
	/**
     * Metodo encargado de eliminar los indices que estan dentro de una misma seccion de
     * slots disponibles, por ejemplo se tiene slots disponibles de 1, 2, 3, 7, 8 y 9
	 * entonces como el (1, 2 y 3) y el (7, 8 y 9) estan agrupados en la misma seccion 
	 * del espectro se elimina el (2, 3) y el (8, 9)
     * @param lista lista de indices disponibles para mapear la solicitud
     * @param slotRequerido cantidad de slot que requiere el request
     * @return List<Integer> indices sin los elementos contiguos superiores
     * @throws ValidationException
     */
	public static List<Integer> eliminarContiguos(List<Integer> lista, int slotRequerido) 
		{
		if(lista.size() == 0){
			return lista;
			//throw new ValidationException("No se encontraron elementos para el corte");
		}
		List<Integer> aEliminar = new ArrayList<>();
		for (int i = 0; i < lista.size() - 1; i++) {
			for (int j = i + 1; j < lista.size(); j++) {
				if(lista.get(j) <=  lista.get(i) + slotRequerido){
					if(!aEliminar.contains(lista.get(j))){
						aEliminar.add(lista.get(j));
					}
				}
			}
		}
		////System.out.println("Indices a eliminar " +aEliminar);
		return aEliminar;
	}
	
	/*De acuerdo a los indices obtenidos calcula en valor del corte, el se suma uno al corte al
	 * tener un slot disponible
	 * 
	 */
	/**
     * Metodo encargado de obtener el valor del corte, suma uno al romper una continuidad, por ejemplo si 
     * el elemento anterior es false suma uno
     * @param path matriz[][] que representa el camino completo con los slots disponibles
     * @param indiceCortes representa los indices de los paths que debe calcular el valor de corte
     * @return List<Integer> valor del corte por indice
     * @throws ValidationException
     */
	public static List<Integer> calcularValorCorte(boolean[][] path, List<Integer> indiceCortes)
					{
		List<Integer> valoresCortesPorIndice = new ArrayList<>();
		int cont = 0;
		for (int i = 0; i < indiceCortes.size(); i++) {
			cont = 0;
			if(indiceCortes.get(i) == 0){
				valoresCortesPorIndice.add(0);
			} else {
				for (int j = 0; j < path[0].length; j++) {
					if(!path[indiceCortes.get(i) - 1][j]){
						cont++;
					}
				}
				valoresCortesPorIndice.add(cont);
			}
		}
		if(valoresCortesPorIndice.size() == 0){
			return valoresCortesPorIndice;
			//throw new ValidationException("No se el valor del corte");
		}
		return valoresCortesPorIndice;
	}
	
	/**
     * Metodo que calcula el número de ranuras espectrales disponibles en la trayectoria actual
     * @param path trayectoria actual
     * @return Integer slots disponibles
     */
	public static Integer calcularCosto(Path path){
		Integer cont = 0;
		for (int i = 0; i < path.getCaminos().length; i++) {
			for (int j = 0; j < path.getCaminos()[i].length; j++) {
				if(!path.getCaminos()[i][j]){
					cont++;
				}
			}
		}
		return cont;
	}


}
