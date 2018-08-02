/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.una.pol.vone.aco.problema;

import java.util.ArrayList;
import java.util.List;
import py.una.pol.vone.aco.entidades.fisicas.NodosFisicosCandidatos;
import py.una.pol.vone.simulator.model.VirtualNode;

/**
 *
 * @author Diana Ferreira
 */
public class NodosVirtualProbabilidad {

    private List<NodosFisicosCandidatos> listaNodosCandidatos;
    private VirtualNode nodoVirtual;

    public NodosVirtualProbabilidad() {
        this.listaNodosCandidatos = new ArrayList<>();
    }

    public List<NodosFisicosCandidatos> getListaNodosCandidatos() {
        return listaNodosCandidatos;
    }

    public void setListaNodosCandidatos(List<NodosFisicosCandidatos> listaNodosCandidatos) {
        this.listaNodosCandidatos = listaNodosCandidatos;
    }

    public VirtualNode getNodoVirtual() {
        return nodoVirtual;
    }

    public void setNodoVirtual(VirtualNode nodoVirtual) {
        this.nodoVirtual = nodoVirtual;
    }

}
