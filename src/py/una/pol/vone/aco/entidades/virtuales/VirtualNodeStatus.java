package py.una.pol.vone.aco.entidades.virtuales;

/**
 * Clase que almacena los estados de los nodos
 *
 * @author <a href="mailto: liliaguero1208@gmail.com">Liliana Agüero</a>
 *
 */ 
import py.una.pol.vone.simulator.model.VirtualNode;
public class VirtualNodeStatus  {

    private VirtualNode nodoVirtual;
    private boolean marcado;
    private Integer orden = 0;
    private boolean recorrido;
    private VirtualNodeStatus nodoPadre;

    public VirtualNodeStatus() {
        this.orden = 0;
    }

    public VirtualNodeStatus getNodoPadre() {
        return nodoPadre;
    }

    public void setNodoPadre(VirtualNodeStatus nodoPadre) {
        this.nodoPadre = nodoPadre;
    }

    public boolean isRecorrido() {
        return recorrido;
    }

    public void setRecorrido(boolean recorrido) {
        this.recorrido = recorrido;
    }

    public VirtualNode getNodoVirtual() {
        return nodoVirtual;
    }

    public void setNodoVirtual(VirtualNode nodoVirtual) {
        this.nodoVirtual = nodoVirtual;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

  


}
