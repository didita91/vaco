package py.una.pol.vone.aco.entidades.virtuales;

import java.util.ArrayList;

import py.una.pol.vone.aco.entidades.fisicas.SustrateEdge;

/**
 * Modelo que representa al VNR
 *
 * @author <a href="mailto: liliaguero1208@gmail.com">Liliana Agüero</a>
 *
 */
public class VirtualNetwork implements Cloneable {

    private int identificador;
    private String nombre;
    private int tiempoInicial;
    private int tiempoIngresado;
    private int tiempoFinal;
    private boolean mapeado;
    private ArrayList<VirtualNode> nodosVirtuales = new ArrayList<>();
    private ArrayList<VirtualEdge> enlacesVirtuales = new ArrayList<>();

    /*Constructores*/
    public VirtualNetwork() {
        super();
        this.mapeado = false;
    }

    public VirtualNetwork(int identificador, String nombre) {
        super();
        this.identificador = identificador;
        this.nombre = nombre;
        this.mapeado = false;
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

    public int getNroNodos() {
        return this.nodosVirtuales.size();
    }

    public int getNroEnlaces() {
        return this.enlacesVirtuales.size();
    }

    public int getTiempoInicial() {
        return tiempoInicial;
    }

    public void setTiempoInicial(int tiempoInicial) {
        this.tiempoInicial = tiempoInicial;
    }

    public int getTiempoFinal() {
        return tiempoFinal;
    }

    public void setTiempoFinal(int tiempoFinal) {
        this.tiempoFinal = tiempoFinal;
    }

    public int getTiempoIngresado() {
        return tiempoIngresado;
    }

    public void setTiempoIngresado(int tiempoIngresado) {
        this.tiempoIngresado = tiempoIngresado;
    }

    /**
     * Metodo que retorna el total de CPU que ocupara la red virtual en la red
     * fisica.
     *
     * @return capacidad de CPU total de la red virtual.
     */
    public int getTotalCPU() {
        int totalCPU = 0;
        for (VirtualNode nodo : this.nodosVirtuales) {
            totalCPU += nodo.getCapacidadCPU();
        }
        return totalCPU;
    }

    public boolean isMapeado() {
        return mapeado;
    }

    public void setMapeado(boolean mapeado) {
        this.mapeado = mapeado;
    }

    public ArrayList<VirtualNode> getNodosVirtuales() {
        return nodosVirtuales;
    }

    public void setNodosVirtuales(ArrayList<VirtualNode> nodosVirtuales) {
        this.nodosVirtuales = nodosVirtuales;
    }

    public ArrayList<VirtualEdge> getEnlacesVirtuales() {
        return enlacesVirtuales;
    }

    public void setEnlacesVirtuales(ArrayList<VirtualEdge> enlacesVirtuales) {
        this.enlacesVirtuales = enlacesVirtuales;
    }

    /**
     * Re-implementacion del metodo toString.
     *
     * @return cadena de la red virtual.
     */
    @Override
    public String toString() {
        String cadena = new String();
        cadena = cadena.concat("Nombre de la red: " + this.nombre + ".\n");
        cadena = cadena.concat("Nodos que pertenecen a esta red: \nNombre Nodo"
                + "\t|\tCapacidad CPU necesarios\t|\tNodo Fisico Relacionado\n");
        String enCasoDeNoMapeado;
        for (VirtualNode nodoVirtual : this.nodosVirtuales) {
            if (this.mapeado) {
                enCasoDeNoMapeado = String.valueOf(nodoVirtual.getNodoFisico().
                        getIdentificador());
            } else {
                enCasoDeNoMapeado = "No Mapeado";
            }
            cadena = cadena.concat(nodoVirtual.getNombre() + "\t|\t\t"
                    + nodoVirtual.getCapacidadCPU() + "\t\t\t|\t\t"
                    + enCasoDeNoMapeado + "\n");
        }
        cadena = cadena.concat("Enlaces que pertenecen a esta red:\n");
        cadena = cadena.concat("Nombre "
                + "Enlace\t|\tNodo Uno\t|\tNodo Dos\t|\tCantidad FS "
                + "Necesarios\t|\tPosicion Inicial de FS Fisico"
                + "\t|\tEnlaces Fisicos relacionados\n");
        for (VirtualEdge enlaceVirtual : this.enlacesVirtuales) {
            cadena = cadena.concat(enlaceVirtual.getNombre() + "\t|\t"
                    + enlaceVirtual.getNodoUno().getNombre() + "\t|\t"
                    + enlaceVirtual.getNodoDos().getNombre() + "\t|\t\t"
                    + enlaceVirtual.getCantidadFS() + "\t\t|\t\t\t");
            if (this.mapeado) {
                cadena = cadena.concat(enlaceVirtual.getPosicionFisica()
                        + "\t\t|\t\t");
                for (int i = 0; i < enlaceVirtual.getEnlaceFisico().size();
                        i++) {
                    SustrateEdge enlaceFisicoRelacionado = enlaceVirtual.
                            getEnlaceFisico().get(i);
                    cadena = cadena.concat(enlaceFisicoRelacionado.getNombre());
                    if (i != enlaceVirtual.getEnlaceFisico().size() - 1) {
                        cadena = cadena.concat("->");
                    } else {
                        cadena = cadena.concat("\n");
                    }
                }
            } else {
                cadena = cadena.concat("No Mapeado");
                cadena = cadena.concat("\n");
            }
        }
        return cadena;
    }
}
