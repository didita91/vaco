///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package py.una.pol.vone.aco.problema;
//
//import java.util.ArrayList;
//
//
//import py.una.pol.vone.aco.utils.OrdenamientoNodosVirtuales;
//import py.una.pol.vone.simulator.model.VirtualEdge;
//import py.una.pol.vone.simulator.model.VirtualNetwork;
//import py.una.pol.vone.simulator.model.VirtualNode;
//
///**
// *
// * @author dianaferreira
// */
//public class VONEACO {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        VirtualNetwork redVirtual = new VirtualNetwork();
//        redVirtual.setNombre("RedVirtualPrueba");
//
//        ArrayList<VirtualNode> nodoPrueba = new ArrayList<VirtualNode>();
//        ArrayList<VirtualEdge> enlacePrueba = new ArrayList<VirtualEdge>();
//        VirtualNode nodoVirtual = new VirtualNode();
//
//        nodoVirtual = new VirtualNode();
//        nodoVirtual.setNombre("Nodo A");
//        nodoVirtual.setCapacidadCPU(3);
//        nodoPrueba.add(nodoVirtual);
//
//        nodoVirtual = new VirtualNode();
//        nodoVirtual.setNombre("Nodo B");
//        nodoVirtual.setCapacidadCPU(2);
//        nodoPrueba.add(nodoVirtual);
//
//        nodoVirtual = new VirtualNode();
//        nodoVirtual.setNombre("Nodo C");
//        nodoVirtual.setCapacidadCPU(1);
//        nodoPrueba.add(nodoVirtual);
//        
//        nodoVirtual = new VirtualNode();
//        nodoVirtual.setNombre("Nodo D");
//        nodoVirtual.setCapacidadCPU(5);
//        nodoPrueba.add(nodoVirtual);
//        
//        nodoVirtual = new VirtualNode();
//        nodoVirtual.setNombre("Nodo E");
//        nodoVirtual.setCapacidadCPU(4);
//        nodoPrueba.add(nodoVirtual);
//
//        //nodo A
//        VirtualEdge enlaceVirtual = new VirtualEdge();
//        enlaceVirtual.setNodoUno(nodoPrueba.get(0));
//        enlaceVirtual.setNodoDos(nodoPrueba.get(1));
//        enlacePrueba.add(enlaceVirtual);
//
//        enlaceVirtual = new VirtualEdge();
//        enlaceVirtual.setNodoUno(nodoPrueba.get(0));
//        enlaceVirtual.setNodoDos(nodoPrueba.get(2));
//        enlacePrueba.add(enlaceVirtual);
//        nodoPrueba.get(0).setAdyacentes(enlacePrueba);
//
//        //nodo B
//        enlacePrueba = new ArrayList<>();
//        enlaceVirtual = new VirtualEdge();
//        enlaceVirtual.setNodoUno(nodoPrueba.get(1));
//        enlaceVirtual.setNodoDos(nodoPrueba.get(0));
//        enlacePrueba.add(enlaceVirtual);
//        
//        enlaceVirtual = new VirtualEdge();
//        enlaceVirtual.setNodoUno(nodoPrueba.get(1));
//        enlaceVirtual.setNodoDos(nodoPrueba.get(2));
//        enlacePrueba.add(enlaceVirtual);
//        
//        enlaceVirtual = new VirtualEdge();
//        enlaceVirtual.setNodoUno(nodoPrueba.get(1));
//        enlaceVirtual.setNodoDos(nodoPrueba.get(3));
//        enlacePrueba.add(enlaceVirtual);
// 
//        nodoPrueba.get(1).setAdyacentes(enlacePrueba);
//        
//        //nodo C
//        enlacePrueba = new ArrayList<>();
//        enlaceVirtual = new VirtualEdge();
//        enlaceVirtual.setNodoUno(nodoPrueba.get(2));
//        enlaceVirtual.setNodoDos(nodoPrueba.get(0));
//        enlacePrueba.add(enlaceVirtual);
//        
//       enlaceVirtual = new VirtualEdge();
//        enlaceVirtual.setNodoUno(nodoPrueba.get(2));
//        enlaceVirtual.setNodoDos(nodoPrueba.get(1));
//        enlacePrueba.add(enlaceVirtual);
//        
//        enlaceVirtual = new VirtualEdge();
//        enlaceVirtual.setNodoUno(nodoPrueba.get(2));
//        enlaceVirtual.setNodoDos(nodoPrueba.get(4));
//        enlacePrueba.add(enlaceVirtual);
// 
//        nodoPrueba.get(2).setAdyacentes(enlacePrueba);
//        
//        
//        //nodo D
//        enlaceVirtual = new VirtualEdge();
//        enlacePrueba = new ArrayList<>();
//        enlaceVirtual.setNodoUno(nodoPrueba.get(3));
//        enlaceVirtual.setNodoDos(nodoPrueba.get(1));
//        enlacePrueba.add(enlaceVirtual);
//
//        nodoPrueba.get(3).setAdyacentes(enlacePrueba);
//        
//        //nodo E
//        enlaceVirtual = new VirtualEdge();
//        enlacePrueba = new ArrayList<>();
//        enlaceVirtual.setNodoUno(nodoPrueba.get(4));
//        enlaceVirtual.setNodoDos(nodoPrueba.get(2));
//        enlacePrueba.add(enlaceVirtual);
//
//        nodoPrueba.get(4).setAdyacentes(enlacePrueba);
//        
//
//        redVirtual.setNodosVirtuales(nodoPrueba);
//        redVirtual.setEnlacesVirtuales(enlacePrueba);
//
//        OrdenamientoNodosVirtuales ordenamiento = new OrdenamientoNodosVirtuales();
//        ordenamiento.ordenarNodosVirtuales(redVirtual);
//
//    }
//}
