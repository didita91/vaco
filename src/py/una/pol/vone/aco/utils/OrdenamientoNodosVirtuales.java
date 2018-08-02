/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.una.pol.vone.aco.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import py.una.pol.vone.aco.entidades.virtuales.VirtualNodeStatus;
import py.una.pol.vone.simulator.model.VirtualEdge;
import py.una.pol.vone.simulator.model.VirtualNetwork;
import py.una.pol.vone.simulator.model.VirtualNode;

/**
 * Clase que se encarga de ordenar los nodos físicos y virtuales.
 *
 * @author <a href="mailto: didif.91@gmail.com">Diana Ferreira</a>
 */
public class OrdenamientoNodosVirtuales {

    public static List<VirtualNodeStatus> ordenarNodos(VirtualNetwork redVirtual) {
        List<VirtualNodeStatus> estadosNodoVirtual = new ArrayList<>();
        Comparator<VirtualNodeStatus> comparator = new Comparator<VirtualNodeStatus>() {

            @Override
            public int compare(VirtualNodeStatus t, VirtualNodeStatus t1) {
                int resultado = new Integer(t.getNodoVirtual().getCapacidadCPU()).compareTo(t1.getNodoVirtual().getCapacidadCPU());
                if (resultado == 0) {
                    if (t.getNodoPadre() != null && t1.getNodoPadre() != null) {
                        resultado = t1.getNodoPadre().getOrden().compareTo(t.getNodoPadre().getOrden());
                        if (resultado == 0) {
                            if (t1.getNodoVirtual().getIdentificador() == t.getNodoVirtual().getIdentificador()) {
                                resultado = 0;
                            } else if (t1.getNodoVirtual().getIdentificador() < t.getNodoVirtual().getIdentificador()) {
                                resultado = -1;
                            } else {
                                resultado = 1;
                            }
                        }
                    } else if (t.getNodoPadre() == null && t1.getNodoPadre() != null) {
                        resultado = t1.getNodoPadre().getOrden().compareTo(t.getOrden());
                        if (resultado == 0) {
                            if (t1.getNodoVirtual().getIdentificador() == t.getNodoVirtual().getIdentificador()) {
                                resultado = 0;
                            } else if (t1.getNodoVirtual().getIdentificador() < t.getNodoVirtual().getIdentificador()) {
                                resultado = -1;
                            } else {
                                resultado = 1;
                            }
                        }
                    } else if (t1.getNodoPadre() == null && t.getNodoPadre() != null) {
                        resultado = t1.getOrden().compareTo(t.getNodoPadre().getOrden());
                        if (resultado == 0) {
                            if (t1.getNodoVirtual().getIdentificador() == t.getNodoVirtual().getIdentificador()) {
                                resultado = 0;
                            } else if (t1.getNodoVirtual().getIdentificador() < t.getNodoVirtual().getIdentificador()) {
                                resultado = -1;
                            } else {
                                resultado = 1;
                            }
                        }
                    }
                }

                return resultado;
            }

        };
        TreeSet<VirtualNodeStatus> colaNodos = new TreeSet<VirtualNodeStatus>(comparator);

        if (redVirtual != null) {
            buscarNodoRaiz(redVirtual, estadosNodoVirtual);
            colaNodos.add(estadosNodoVirtual.get(0));

            ordenamientoNodosVirtuales(estadosNodoVirtual, colaNodos, 1);

            for (VirtualNodeStatus virtualNodeStatus : estadosNodoVirtual) {
                //System.out.println("orden: " + virtualNodeStatus.getOrden() + " nodo: " + virtualNodeStatus.getNodoVirtual().getNombre());
            }
        } else {
            System.err.print("Se requiere  Red Virtual");
        }

        return estadosNodoVirtual;

    }

    private static void buscarNodoRaiz(VirtualNetwork redVirtual, List<VirtualNodeStatus> estadosNodoVirtual) {
        VirtualNode nodoMaximoCPU = new VirtualNode();
        if (redVirtual.getNodosVirtuales() != null && !redVirtual.getNodosVirtuales().isEmpty()) {
            nodoMaximoCPU = redVirtual.getNodosVirtuales().get(0);
        }
        for (int i = 1; i < redVirtual.getNodosVirtuales().size(); i++) {
            if (redVirtual.getNodosVirtuales().get(i).getCapacidadCPU() > nodoMaximoCPU.getCapacidadCPU()) {
                nodoMaximoCPU = redVirtual.getNodosVirtuales().get(i);
            }
        }
        VirtualNodeStatus estados = new VirtualNodeStatus();
        estados.setNodoVirtual(nodoMaximoCPU);
        estados.setMarcado(true);
        estados.setOrden(1);
        estadosNodoVirtual.add(estados);

    }

    private static void ordenamientoNodosVirtuales(List<VirtualNodeStatus> nodeVirtual, TreeSet<VirtualNodeStatus> colaNodos, int numeroOrden) {
        VirtualNodeStatus arbol = colaNodos.last();
        //System.out.println("  Nodo Padre:" + arbol.getNodoVirtual().getNombre());

        for (VirtualEdge adyacente : arbol.getNodoVirtual().getAdyacentes()) {
            boolean bandera = false;
            VirtualNodeStatus estados = new VirtualNodeStatus();
            for (VirtualNodeStatus vn : nodeVirtual) {
                if ((arbol.getNodoVirtual().getIdentificador() == (adyacente.getNodoDos().getIdentificador()))) {
                    bandera = true;
                    break;
                }
            }
            if (bandera) {
                estados.setNodoVirtual(adyacente.getNodoUno());
            } else {
                estados.setNodoVirtual(adyacente.getNodoDos());
            }
            estados.setNodoPadre(arbol);
            estados.setRecorrido(true);
            int posicion = verificarNode(estados, nodeVirtual);
            if (posicion != -1) {
                if (!nodeVirtual.get(posicion).isMarcado()) {
                    colaNodos.add(estados);
                }
            } else {
                if (!verificarCola(estados, colaNodos)) {
                    colaNodos.add(estados);
                    //System.out.println("  Esto es el nodo:" + estados.getNodoVirtual().getNombre());
                   // colaNodos.forEach(n -> System.out.println("Esto es: " + n.getNodoVirtual().getNombre()));

                }
            }

        }
        colaNodos.remove(arbol);
        if (!colaNodos.isEmpty()) {

            colaNodos.last().setMarcado(true);
            colaNodos.last().setOrden(numeroOrden + 1);
            nodeVirtual.add(colaNodos.last());
            ordenamientoNodosVirtuales(nodeVirtual, colaNodos, numeroOrden + 1);
        }

    }

    private static int verificarNode(VirtualNodeStatus estados, List<VirtualNodeStatus> listaNodesEstados) {
        for (int i = 0; i < listaNodesEstados.size(); i++) {
            if (listaNodesEstados.get(i).getNodoVirtual().getIdentificador() == (estados.getNodoVirtual().getIdentificador())) {
                return i;
            }
        }
        return -1;

    }

    private static boolean verificarCola(VirtualNodeStatus cola, TreeSet<VirtualNodeStatus> colaNodos) {
        for (VirtualNodeStatus colaNodo : colaNodos) {
            if (colaNodo.getNodoVirtual().getIdentificador() == cola.getNodoVirtual().getIdentificador()) {
                return true;
            }
        }
        return false;
    }

}
