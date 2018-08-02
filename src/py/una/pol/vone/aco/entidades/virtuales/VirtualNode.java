package py.una.pol.vone.aco.entidades.virtuales;

import java.util.ArrayList;
import py.una.pol.vone.simulator.model.SustrateNode;


/**
 * Modelo que representa el nodo virtual
 *
 * @author <a href="mailto: liliaguero1208@gmail.com">Liliana Agüero</a>
 *
 */
public class VirtualNode implements Cloneable {

    private String nombre;
    private int identificador;
    private int capacidadCPU;
    private boolean mapeado;
    private ArrayList<VirtualEdge> adyacentes = new ArrayList<>();
    private SustrateNode nodoFisico;
    private int posicionFisica;


    /*Constructores*/
    public VirtualNode() {
        super();
        this.mapeado = false;
    }

    public VirtualNode(String nombre, int identificador, int capacidadCPU) {
        super();
        this.nombre = nombre;
        this.identificador = identificador;
        this.capacidadCPU = capacidadCPU;
        this.mapeado = false;
    }

    /*Setters y Getters*/
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public int getCapacidadCPU() {
        return capacidadCPU;
    }

    public void setCapacidadCPU(int capacidadCPU) {
        this.capacidadCPU = capacidadCPU;
    }

    public boolean isMapeado() {
        return mapeado;
    }

    public void setMapeado(boolean mapeado) {
        this.mapeado = mapeado;
    }

    public ArrayList<VirtualEdge> getAdyacentes() {
        return adyacentes;
    }

    public void setAdyacentes(ArrayList<VirtualEdge> adyacentes) {
        this.adyacentes = adyacentes;
    }

    public SustrateNode getNodoFisico() {
        return nodoFisico;
    }

    public void setNodoFisico(SustrateNode nodoFisico) {
        this.nodoFisico = nodoFisico;
    }

    public int getPosicionFisica() {
        return posicionFisica;
    }

    public void setPosicionFisica(int posicionFisica) {
        this.posicionFisica = posicionFisica;
    }

    /**
     * Metodo para agregar un enlace a un nodo en el cual el mismo esta como
     * nodoUno o nodoDos
     *
     * @param enlace objeto que tiene la informacion del enlace a relacionar.
     */
    public void agregarEnlaceVirtual(VirtualEdge enlace) {
        this.adyacentes.add(enlace);
    }

}
