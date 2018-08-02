/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.una.pol.vone.aco.entidades.fisicas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Modelo que representa la red física.
 *
 * @author <a href="mailto: didif.91@gmail.com">Diana Ferreira</a>
 */
public class SustrateNetwork implements Cloneable  {
    private String nombre;
    private int minCPU;
    private int maxCPU;
    private int cantidadFS;
    private Date fecha;
    private ArrayList<SustrateNode> nodosFisicos;
    private ArrayList<SustrateEdge> enlacesFisicos;
    
     /*Constructores*/
    public SustrateNetwork() {
        super();
    }

    public SustrateNetwork(String nombre) {
        super();
        this.nombre = nombre;
    }

    /*Setters y Getters*/
    public int getTotalCPU() {
        int totalCPU = 0;
        for (SustrateNode nodo : this.nodosFisicos) {
            totalCPU += nodo.getCapacidadCPU();
        }
        return totalCPU;
    }

    public ArrayList<SustrateNode> getNodosFisicos() {
        return nodosFisicos;
    }

    public void setNodosFisicos(ArrayList<SustrateNode> nodosFisicos) {
        this.nodosFisicos = nodosFisicos;
    }

    public void agregarNodoFisico(SustrateNode nodo) {
        this.nodosFisicos.add(nodo);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNroNodos() {
        return this.nodosFisicos.size();
    }

    public int getNroEnlaces() {
        return this.enlacesFisicos.size();
    }

    public ArrayList<SustrateEdge> getEnlacesFisicos() {
        return enlacesFisicos;
    }

    public void setEnlacesFisicos(ArrayList<SustrateEdge> enlacesFisicos) {
        this.enlacesFisicos = enlacesFisicos;
    }

    public int getMinCPU() {
        return minCPU;
    }

    public void setMinCPU(int minCPU) {
        this.minCPU = minCPU;
    }

    public int getMaxCPU() {
        return maxCPU;
    }

    public void setMaxCPU(int maxCPU) {
        this.maxCPU = maxCPU;
    }

    public int getCantidadFS() {
        return cantidadFS;
    }

    public void setCantidadFS(int cantidadFS) {
        this.cantidadFS = cantidadFS;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Metodo para clonar una red sustrato.
     *
     * @return objeto con la copia de la red fisica que llama a este metodo.
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        SustrateNetwork copia = (SustrateNetwork) super.clone();
        ArrayList<SustrateNode> nodosFisicosCopiados = new ArrayList<>();
        for (SustrateNode nodoFisicoOriginal : this.getNodosFisicos()) {
            SustrateNode nodoCopiado = (SustrateNode) nodoFisicoOriginal.
                    clone();
            nodoCopiado.setAdyacentes(new ArrayList<>());
            nodoCopiado.setCPU((ArrayList<Integer>) nodoFisicoOriginal.getCPU().
                    clone());
            nodosFisicosCopiados.add(nodoCopiado);
        }
        copia.setNodosFisicos(nodosFisicosCopiados);
        ArrayList<SustrateEdge> enlacesFisicosCopiados = new ArrayList<>();
        this.enlacesFisicos.forEach((enlaceFisicoOriginal) -> {
            SustrateEdge enlaceNuevo = new SustrateEdge();
            enlaceNuevo.setIdentificador(enlaceFisicoOriginal.
                    getIdentificador());
            enlaceNuevo.setDistancia(enlaceFisicoOriginal.getDistancia());
            enlaceNuevo.setNombre(enlaceFisicoOriginal.getNombre());
            enlaceNuevo.setCantidadFS(enlaceFisicoOriginal.getCantidadFS());
            enlaceNuevo.setFrequencySlot(Arrays.copyOf(enlaceFisicoOriginal.
                    getFrequencySlot(), enlaceFisicoOriginal.getCantidadFS()));
            int idNodoUno = enlaceFisicoOriginal.getNodoUno().
                    getIdentificador();
            int idNodoDos = enlaceFisicoOriginal.getNodoDos().
                    getIdentificador();
            SustrateNode nodoUno = null, nodoDos = null;
            boolean nodoUnoEncontrado = false, nodoDosEncontrado = false;
            for (SustrateNode nodo : copia.getNodosFisicos()) {
                if (nodo.getIdentificador() == idNodoUno) {
                    nodoUno = nodo;
                    nodoUnoEncontrado = true;
                } else if (nodo.getIdentificador() == idNodoDos) {
                    nodoDos = nodo;
                    nodoDosEncontrado = true;
                }
                if (nodoUnoEncontrado && nodoDosEncontrado) {
                    break;
                }
            }
            enlaceNuevo.setNodoUno(nodoUno);
            enlaceNuevo.setNodoDos(nodoDos);
            nodoUno.agregarEnlaceAdyacente(enlaceNuevo);
            nodoDos.agregarEnlaceAdyacente(enlaceNuevo);
            enlacesFisicosCopiados.add(enlaceNuevo);
        });
        copia.setEnlacesFisicos(enlacesFisicosCopiados);
        return copia;
    }

    /**
     * Re-implementacion del toString.
     *
     * @return cadena de la red fisica.
     */
    @Override
    public String toString() {
        String cadena = new String();
        cadena = cadena.concat("Nombre de la red: " + this.nombre + ".\n");
        cadena = cadena.concat("Nodos que pertenecen a esta red: \nNombre Nodo"
                + "\t|\tCapacidad Disponible\t|\tCapacidad Total\n");
        for (SustrateNode nodoFisico : this.nodosFisicos) {
            cadena = cadena.concat(nodoFisico.getNombre() + "\t\t|\t\t"
                    + nodoFisico.capacidadActual() + "\t\t|\t\t"
                    + nodoFisico.getCapacidadCPU() + "\n");
        }
        cadena = cadena.concat("Cantidad de FS por enlace físico: "
                + this.getCantidadFS());
        cadena = cadena.concat("\nEnlaces que pertenecen a esta red: \nNombre "
                + "Enlace\t|\tNodo Uno\t|\tNodo Dos\t|\tCantidad FS Disponible"
                + "\t|\tUtilizados (0 representa libre, 1 ocupado)\n");
        for (SustrateEdge enlaceFisico : this.enlacesFisicos) {
            cadena = cadena.concat(enlaceFisico.getNombre() + "\t|\t"
                    + enlaceFisico.getNodoUno().getNombre() + "\t\t|\t"
                    + enlaceFisico.getNodoDos().getNombre() + "\t\t|\t\t"
                    + enlaceFisico.cantidadFSDisponibles() + "\t\t|\t[");
            for (int i = 0; i < enlaceFisico.getCantidadFS(); i++) {
                if (!enlaceFisico.devolverEstadoFS(i)) {
                    cadena = cadena.concat("0");
                } else {
                    cadena = cadena.concat("1");
                }
                if (i != enlaceFisico.getCantidadFS() - 1) {
                    cadena = cadena.concat(" | ");
                } else {
                    cadena = cadena.concat(" ]\n");
                }
            }
        }
        return cadena;
    }
}
