package py.una.pol.vone.aco.entidades.fisicas;

import py.una.pol.vone.simulator.model.SustrateNode;
import py.una.pol.vone.simulator.model.VirtualNode;

/**
 * Clase que contiene las probabiidades de nodos fisicos
 *
 * @author <a href="mailto: liliaguero1208@gmail.com">Liliana Agüero</a>
 *
 */
public class NodosFisicosCandidatos {

    SustrateNode nodoFisico;
    // double probabilidadNodoFisico;
    Integer distancia;
    VirtualNode nodoVirtual;
//    double cpuNormalizado;
    int orden;
    boolean mapeado;

    public boolean isMapeado() {
        return mapeado;
    }

    public void setMapeado(boolean mapeado) {
        this.mapeado = mapeado;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public VirtualNode getNodoVirtual() {
        return nodoVirtual;
    }

    public void setNodoVirtual(VirtualNode nodoVirtual) {
        this.nodoVirtual = nodoVirtual;
    }

    public Integer getDistancia() {
        return distancia;
    }

    public void setDistancia(Integer distancia) {
        this.distancia = distancia;
    }

    public SustrateNode getNodoFisico() {
        return nodoFisico;
    }

    public void setNodoFisico(SustrateNode nodoFisico) {
        this.nodoFisico = nodoFisico;
    }

//    public double getProbabilidadNodoFisico() {
//        return probabilidadNodoFisico;
//    }
//
//    public void setProbabilidadNodoFisico(double probabilidadNodoFisico) {
//        this.probabilidadNodoFisico = probabilidadNodoFisico;
//    }
}
