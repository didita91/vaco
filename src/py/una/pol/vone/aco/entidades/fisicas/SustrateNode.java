/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.una.pol.vone.aco.entidades.fisicas;

import java.util.ArrayList;

/**
 * Modelo que representa el nodo físico.
 *
 * @author <a href="mailto: didif.91@gmail.com">Diana Ferreira</a>
 */
public class SustrateNode implements Cloneable  {

    private int identificador;
    private String nombre;
    private int capacidadCPU;
    private ArrayList<Integer> CPU = new ArrayList<>();
    private ArrayList<SustrateEdge> adyacentes = new ArrayList<>();

    /*Constructores*/
    public SustrateNode() {
        super();
    }

    public SustrateNode(int id, String nombre, int capacidadCPU) {
        super();
        this.identificador = id;
        this.nombre = nombre;
        this.capacidadCPU = capacidadCPU;
    }

    /*Setters y Getters*/
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidadCPU() {
        return capacidadCPU;
    }

    public void setCapacidadCPU(int capacidadCPU) {
        this.capacidadCPU = capacidadCPU;
    }

    public ArrayList<SustrateEdge> getAdyacentes() {
        return adyacentes;
    }

    public void setAdyacentes(ArrayList<SustrateEdge> adyacentes) {
        this.adyacentes = adyacentes;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public ArrayList<Integer> getCPU() {
        return CPU;
    }

    public void setCPU(ArrayList<Integer> CPU) {
        this.CPU = CPU;
    }

    /**
     * Metodo para agregar un enlace en el cual el nodo esta relacionado como
     * nodoUno o nodoDos.
     *
     * @param enlace objeto que tiene la informacion del enlace a relacionar.
     */
    public void agregarEnlaceAdyacente(SustrateEdge enlace) {
        this.adyacentes.add(enlace);
    }

    /**
     * Metodo que retorna la capacidad actual del nodo.
     *
     * @return capacidad disponible de CPU.
     */
    public int capacidadActual() {
        return this.capacidadCPU - this.CPU.size();
    }

    /**
     * Metodo que retorna si el nodo cuenta con capacidad suficiente para
     * albergar al nodo virtual que quiere ser seteado.
     *
     * @param capacidadNodoVirtual capacidad de CPU del nodo que quiere ser
     * seteado alli.
     * @return true si es posible setear.
     */
    public boolean tieneCapacidad(int capacidadNodoVirtual) {
        return capacidadNodoVirtual <= this.capacidadActual();
    }

    /**
     * Metodo para asignar capacidad de CPU en el nodo fisico (se asume que ya
     * se ha ejecutado la verificacion del nodo).
     *
     * @param capacidad capacidad a ser asignada.
     */
    public void asignarRecursoCPU(int capacidad) {
        for (int i = 0; i < capacidad; i++) {
            this.CPU.add(i);
        }
    }
    
    /**
     * Metodo para asignar capacidad de CPU en el nodo fisico (se asume que ya
     * se ha ejecutado la verificacion del nodo).
     *
     * @param capacidad capacidad a ser desasignada.
     */
    public void desAsignarRecursoCPU(int capacidad) {
        for (int i = 0; i < capacidad; i++) {
            this.CPU.remove(0);
        }
    }
    
    /**
     * Metodo para clonar un nodo sustrato.
     *
     * @return objeto con la copia del nodo sustrato.
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return (SustrateNode)super.clone();
    }
}
