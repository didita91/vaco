/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.una.pol.vone.aco.utils;

import java.util.ArrayList;
import py.una.pol.vone.simulator.model.SustrateEdge;
import py.una.pol.vone.simulator.model.SustrateNetwork;
import py.una.pol.vone.simulator.model.SustrateNode;


/**
 *
 * @author Diana Ferreira
 */
public class NetworkGenerator {

    private SustrateNetwork redFisica;

    public static SustrateNetwork getRedFisica() {
        SustrateNetwork red = new SustrateNetwork();
        red.setCantidadFS(12);
//1
        ArrayList<SustrateEdge> enlacesFisicos = new ArrayList<>();
        SustrateEdge enlace = new SustrateEdge();
        enlace.setCantidadFS(2);
        enlace.setIdentificador(0);
        SustrateNode nodoDos = new SustrateNode();
        nodoDos.setNombre("2");
        nodoDos.setIdentificador(2);

        enlace.setNodoDos(nodoDos);
        SustrateNode nodoUno = new SustrateNode();
        nodoUno.setNombre("1");
        nodoUno.setIdentificador(1);

        enlace.setNodoUno(nodoUno);
        enlacesFisicos.add(enlace);
///2
        enlace = new SustrateEdge();
        enlace.setIdentificador(1);

        enlace.setCantidadFS(2);
        nodoDos = new SustrateNode();
        nodoDos.setNombre("6");
        nodoDos.setIdentificador(6);

        enlace.setNodoDos(nodoDos);
        nodoUno = new SustrateNode();
        nodoUno.setIdentificador(5);

        nodoUno.setNombre("5");
        enlace.setNodoUno(nodoUno);
        enlacesFisicos.add(enlace);
///3
        enlace = new SustrateEdge();

        enlace.setCantidadFS(2);
        nodoDos = new SustrateNode();
        nodoDos.setNombre("3");
        nodoDos.setIdentificador(3);
        enlace.setNodoDos(nodoDos);
        nodoUno = new SustrateNode();
        nodoUno.setNombre("2");
        nodoUno.setIdentificador(2);
        enlace.setNodoUno(nodoUno);
        enlacesFisicos.add(enlace);
//4
        enlace = new SustrateEdge();
        enlace.setCantidadFS(2);
        nodoDos = new SustrateNode();
        nodoDos.setNombre("5");
        nodoDos.setIdentificador(5);

        enlace.setNodoDos(nodoDos);
        nodoUno = new SustrateNode();
        nodoUno.setNombre("4");
        nodoUno.setIdentificador(4);

        enlace.setNodoUno(nodoUno);
        enlacesFisicos.add(enlace);
//5
        enlace = new SustrateEdge();
        enlace.setCantidadFS(2);
        nodoDos = new SustrateNode();
        nodoDos.setNombre("4");
        nodoDos.setIdentificador(4);
        enlace.setNodoDos(nodoDos);
        nodoUno = new SustrateNode();
        nodoUno.setNombre("1");
        nodoUno.setIdentificador(1);

        enlace.setNodoUno(nodoUno);
        enlacesFisicos.add(enlace);
//6
        enlace = new SustrateEdge();
        enlace.setCantidadFS(2);
        nodoDos = new SustrateNode();
        nodoDos.setIdentificador(6);

        nodoDos.setNombre("6");
        enlace.setNodoDos(nodoDos);
        nodoUno = new SustrateNode();
        nodoUno.setIdentificador(3);

        nodoUno.setNombre("3");
        enlace.setNodoUno(nodoUno);
        enlacesFisicos.add(enlace);

        red.setEnlacesFisicos(enlacesFisicos);

        ArrayList<SustrateNode> nodosFisicos = new ArrayList<>();
        SustrateNode nodo = new SustrateNode();
        nodo.setNombre("1");
        nodosFisicos.add(nodo);
        nodo = new SustrateNode();
        nodo.setNombre("2");
        nodosFisicos.add(nodo);
        nodo = new SustrateNode();
        nodo.setNombre("3");
        nodosFisicos.add(nodo);
        nodo = new SustrateNode();
        nodo.setNombre("4");
        nodosFisicos.add(nodo);
        nodo = new SustrateNode();
        nodo.setNombre("5");
        nodosFisicos.add(nodo);
        nodo = new SustrateNode();
        nodo.setNombre("6");
        nodosFisicos.add(nodo);
        //nodos fisicos
        red.setNodosFisicos(nodosFisicos);
        red.setEnlacesFisicos(enlacesFisicos);
        return red;
    }
}
