/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.una.pol.vone.aco.utils.rsa;

import java.util.ArrayList;
import java.util.List;

import py.una.pol.vone.aco.utils.rsa.kshortespaths.utilrsa.Path;
import py.una.pol.vone.simulator.model.VirtualEdge;

/**
 *
 * @author Diana Ferreira
 */
public class SolucionAco {

    private List<Path> list;
    private List<VirtualEdge> virtualEdge;
    private double probabilidad;

    public double getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(double probabilidad) {
        this.probabilidad = probabilidad;
    }

    public SolucionAco() {
        this.list = new ArrayList<>();
        this.virtualEdge = new ArrayList<>();
    }

    public List<Path> getList() {
        return list;
    }

    public void setList(List<Path> list) {
        this.list = list;
    }

    public List<VirtualEdge> getVirtualEdge() {
        return virtualEdge;
    }

    public void setVirtualEdge(List<VirtualEdge> virtualEdge) {
        this.virtualEdge = virtualEdge;
    }

}
