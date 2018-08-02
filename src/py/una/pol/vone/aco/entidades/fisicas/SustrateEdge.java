/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.una.pol.vone.aco.entidades.fisicas;

/**
 * Modelo que representa el enlace físico.
 *
 * @author <a href="mailto: didif.91@gmail.com">Diana Ferreira</a>
 */
public class SustrateEdge implements Cloneable {

    private int identificador;
    private String nombre;
    private double distancia;
    private int cantidadFS;
    private boolean[] frequencySlot;
    private SustrateNode nodoUno;
    private SustrateNode nodoDos;
    /*Constructores*/
    public SustrateEdge() {
        super();
    }

    public SustrateEdge(int identificador, String nombre, float distancia,
            int cantidadFS) {
        super();
        this.identificador = identificador;
        this.nombre = nombre;
        this.distancia = distancia;
        this.cantidadFS = cantidadFS;
        this.frequencySlot = new boolean[cantidadFS];
        for (int cont = 0; cont < cantidadFS; cont++) {
            this.frequencySlot[cont] = false;
        }
    }

    public SustrateEdge(double distancia, int cantidadFS) {
        super();
        this.distancia = distancia;
        this.cantidadFS = cantidadFS;
        this.frequencySlot = new boolean[cantidadFS];
        for (int cont = 0; cont < cantidadFS; cont++) {
            this.frequencySlot[cont] = false;
        }
    }

    /*Setters y Getters*/
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public int getCantidadFS() {
        return cantidadFS;
    }

    public void setCantidadFS(int cantidadFS) {
        this.cantidadFS = cantidadFS;
        this.frequencySlot = new boolean[cantidadFS];
        for (int cont = 0; cont < cantidadFS; cont++) {
            this.frequencySlot[cont] = false;
        }
    }

    public boolean[] getFrequencySlot() {
        return frequencySlot;
    }

    public void setFrequencySlot(boolean[] frequencySlot) {
        this.frequencySlot = frequencySlot;
    }

    public SustrateNode getNodoUno() {
        return nodoUno;
    }

    public void setNodoUno(SustrateNode nodoUno) {
        this.nodoUno = nodoUno;
    }

    public SustrateNode getNodoDos() {
        return nodoDos;
    }

    public void setNodoDos(SustrateNode nodoDos) {
        this.nodoDos = nodoDos;
    }

    /**
     * Metodo para asignar un estado a la posicion del FS que se pasa como
     * parametro.
     *
     * @param estado estado a setear en la posicion mencionada.
     * @param posicion posicion del array (o espectro) a ser seteada.
     */
    public void asignarEstadoFS(boolean estado, int posicion) {
        this.frequencySlot[posicion] = estado;
    }

    /**
     * Metodo que retorna el estado del FS seleccionado.
     *
     * @param posicion posicion del FS que se quiere saber su estado.
     * @return estado del FS en la posicion mencionada.
     */
    public boolean devolverEstadoFS(int posicion) {
        return this.frequencySlot[posicion];
    }

    /**
     * Metodo que devuelve la capacidad de FS disponible en el enlace fisico
     * (sin tener en cuenta en que posicion es la que esta o no disponible, solo
     * si esta disponible).
     *
     * @return cantidad de FS disponibles (no tiene en cuenta si estan o no
     * contiguos los mismos, solo lanza el total disponible).
     */
    public int cantidadFSDisponibles() {
        int cantidadDisponible = 0;
        for (int i = 0; i < this.cantidadFS; i++) {
            if (!(this.frequencySlot[i])) {
                cantidadDisponible++;
            }
        }
        return cantidadDisponible;
    }

    /**
     * Metodo que clona un enlace sustrato.
     *
     * @return objeto con la copia del enlace sustrato.
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        SustrateEdge copia = (SustrateEdge) super.clone();
        return copia;
    }
}
