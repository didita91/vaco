/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.una.pol.vone.aco.utils;

import java.util.Random;

import py.una.pol.vone.aco.utils.rsa.Rmsa;
import py.una.pol.vone.aco.utils.rsa.SolucionAco;
import py.una.pol.vone.simulator.model.SustrateEdge;
import py.una.pol.vone.simulator.model.SustrateNetwork;
import py.una.pol.vone.simulator.model.VirtualEdge;
import py.una.pol.vone.simulator.model.VirtualNode;

/**
 *
 * @author Diana Ferreira
 */
public class TestRsa {

    @SuppressWarnings("rawtypes")
    public static void main(String args[]) {
        Integer cantidadFS = 12;
        Integer identificador = 0;
        Random random = new Random();
        SustrateNetwork sustrateNetwork = new SustrateNetwork();
        sustrateNetwork = NetworkGenerator.getRedFisica();
        for (SustrateEdge sustrateEdge : sustrateNetwork.getEnlacesFisicos()) {
            sustrateEdge.setCantidadFS(cantidadFS);
            sustrateEdge.setFrequencySlot(new boolean[cantidadFS]);
            sustrateEdge.setIdentificador(identificador);
            /*Si se descomenta el for se genera de manera randomica 
			  * los slocks ocupados, y habria que comentar 
			  * el switch
             */
 /*for (int i = 0; i < cantidadFS; i++) {
				  sustrateEdge.getFrequencySlot()[i] = randomno.nextBoolean();
			  }*/
            switch (identificador) {
                case 0:
                    sustrateEdge.getFrequencySlot()[0] = true;
                    sustrateEdge.getFrequencySlot()[1] = true;
                    sustrateEdge.getFrequencySlot()[2] = false;
                    sustrateEdge.getFrequencySlot()[3] = false;
                    sustrateEdge.getFrequencySlot()[4] = true;
                    sustrateEdge.getFrequencySlot()[5] = true;
                    sustrateEdge.getFrequencySlot()[6] = false;
                    sustrateEdge.getFrequencySlot()[7] = false;
                    sustrateEdge.getFrequencySlot()[8] = true;
                    sustrateEdge.getFrequencySlot()[9] = true;
                    sustrateEdge.getFrequencySlot()[10] = false;
                    sustrateEdge.getFrequencySlot()[11] = false;
                    break;
                case 1:
                    sustrateEdge.getFrequencySlot()[0] = true;
                    sustrateEdge.getFrequencySlot()[1] = false;
                    sustrateEdge.getFrequencySlot()[2] = false;
                    sustrateEdge.getFrequencySlot()[3] = false;
                    sustrateEdge.getFrequencySlot()[4] = true;
                    sustrateEdge.getFrequencySlot()[5] = true;
                    sustrateEdge.getFrequencySlot()[6] = true;
                    sustrateEdge.getFrequencySlot()[7] = false;
                    sustrateEdge.getFrequencySlot()[8] = false;
                    sustrateEdge.getFrequencySlot()[9] = false;
                    sustrateEdge.getFrequencySlot()[10] = false;
                    sustrateEdge.getFrequencySlot()[11] = false;
                    break;
                case 2:
                    sustrateEdge.getFrequencySlot()[0] = true;
                    sustrateEdge.getFrequencySlot()[1] = true;
                    sustrateEdge.getFrequencySlot()[2] = false;
                    sustrateEdge.getFrequencySlot()[3] = false;
                    sustrateEdge.getFrequencySlot()[4] = true;
                    sustrateEdge.getFrequencySlot()[5] = true;
                    sustrateEdge.getFrequencySlot()[6] = false;
                    sustrateEdge.getFrequencySlot()[7] = false;
                    sustrateEdge.getFrequencySlot()[8] = false;
                    sustrateEdge.getFrequencySlot()[9] = false;
                    sustrateEdge.getFrequencySlot()[10] = false;
                    sustrateEdge.getFrequencySlot()[11] = false;
                    break;
                case 3:
                    sustrateEdge.getFrequencySlot()[0] = false;
                    sustrateEdge.getFrequencySlot()[1] = true;
                    sustrateEdge.getFrequencySlot()[2] = false;
                    sustrateEdge.getFrequencySlot()[3] = false;
                    sustrateEdge.getFrequencySlot()[4] = true;
                    sustrateEdge.getFrequencySlot()[5] = false;
                    sustrateEdge.getFrequencySlot()[6] = false;
                    sustrateEdge.getFrequencySlot()[7] = false;
                    sustrateEdge.getFrequencySlot()[8] = true;
                    sustrateEdge.getFrequencySlot()[9] = true;
                    sustrateEdge.getFrequencySlot()[10] = true;
                    sustrateEdge.getFrequencySlot()[11] = true;
                    break;
                case 4:
                    sustrateEdge.getFrequencySlot()[0] = false;
                    sustrateEdge.getFrequencySlot()[1] = false;
                    sustrateEdge.getFrequencySlot()[2] = false;
                    sustrateEdge.getFrequencySlot()[3] = false;
                    sustrateEdge.getFrequencySlot()[4] = true;
                    sustrateEdge.getFrequencySlot()[5] = true;
                    sustrateEdge.getFrequencySlot()[6] = true;
                    sustrateEdge.getFrequencySlot()[7] = false;
                    sustrateEdge.getFrequencySlot()[8] = false;
                    sustrateEdge.getFrequencySlot()[9] = false;
                    sustrateEdge.getFrequencySlot()[10] = false;
                    sustrateEdge.getFrequencySlot()[11] = false;
                    break;
                case 5:
                    sustrateEdge.getFrequencySlot()[0] = true;
                    sustrateEdge.getFrequencySlot()[1] = false;
                    sustrateEdge.getFrequencySlot()[2] = false;
                    sustrateEdge.getFrequencySlot()[3] = false;
                    sustrateEdge.getFrequencySlot()[4] = false;
                    sustrateEdge.getFrequencySlot()[5] = false;
                    sustrateEdge.getFrequencySlot()[6] = false;
                    sustrateEdge.getFrequencySlot()[7] = false;
                    sustrateEdge.getFrequencySlot()[8] = false;
                    sustrateEdge.getFrequencySlot()[9] = false;
                    sustrateEdge.getFrequencySlot()[10] = false;
                    sustrateEdge.getFrequencySlot()[11] = false;
                    break;
                case 6:
                    sustrateEdge.getFrequencySlot()[0] = true;
                    sustrateEdge.getFrequencySlot()[1] = false;
                    sustrateEdge.getFrequencySlot()[2] = false;
                    sustrateEdge.getFrequencySlot()[3] = false;
                    sustrateEdge.getFrequencySlot()[4] = true;
                    sustrateEdge.getFrequencySlot()[5] = false;
                    sustrateEdge.getFrequencySlot()[6] = false;
                    sustrateEdge.getFrequencySlot()[7] = false;
                    sustrateEdge.getFrequencySlot()[8] = false;
                    sustrateEdge.getFrequencySlot()[9] = false;
                    sustrateEdge.getFrequencySlot()[10] = false;
                    sustrateEdge.getFrequencySlot()[11] = false;
                    break;
                case 7:
                    sustrateEdge.getFrequencySlot()[0] = true;
                    sustrateEdge.getFrequencySlot()[1] = true;
                    sustrateEdge.getFrequencySlot()[2] = false;
                    sustrateEdge.getFrequencySlot()[3] = false;
                    sustrateEdge.getFrequencySlot()[4] = false;
                    sustrateEdge.getFrequencySlot()[5] = true;
                    sustrateEdge.getFrequencySlot()[6] = true;
                    sustrateEdge.getFrequencySlot()[7] = true;
                    sustrateEdge.getFrequencySlot()[8] = false;
                    sustrateEdge.getFrequencySlot()[9] = false;
                    sustrateEdge.getFrequencySlot()[10] = false;
                    sustrateEdge.getFrequencySlot()[11] = false;
                    break;
            }

            ////System.out.println(sustrateEdge.getNombre());
            identificador++;
        }
        Rmsa rmsa = new Rmsa();
        SolucionAco solucionAco = new SolucionAco();
//nodo A
        VirtualEdge enlaceVirtual = new VirtualEdge();
        VirtualNode nodoVirtual1 = new VirtualNode();
        nodoVirtual1.setNombre("1");
        nodoVirtual1.setCapacidadCPU(2);

        VirtualNode nodoVirtual2 = new VirtualNode();
        nodoVirtual2.setNombre("3");
        nodoVirtual2.setCapacidadCPU(2);

        enlaceVirtual.setNodoUno(nodoVirtual1);
        enlaceVirtual.setNodoDos(nodoVirtual2);
        rmsa.realizarRmsa(sustrateNetwork, "1", "3", 5, 2, solucionAco, enlaceVirtual,false);
    }
}
