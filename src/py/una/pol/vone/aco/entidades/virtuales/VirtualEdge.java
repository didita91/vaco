package py.una.pol.vone.aco.entidades.virtuales;

import java.util.ArrayList;

import py.una.pol.vone.aco.entidades.fisicas.SustrateEdge;

/**
 * Modelo que representa al enlace virtual entre dos nodos
 *
 * @author <a href="mailto: liliaguero1208@gmail.com">Liliana Agüero</a>
 * 
 */

public class VirtualEdge implements Cloneable{
	 private int identificador;
	    private String nombre;
	    private int cantidadFS;
	    private boolean mapeado;
	    private VirtualNode nodoUno;
	    private VirtualNode nodoDos;
	    private ArrayList<SustrateEdge> enlaceFisico = new ArrayList<>();
	    private int posicionFisica;

	    public VirtualEdge() {
	        super();
	        this.mapeado = false;
	    }

	    public VirtualEdge(int identificador, String nombre, int cantidadFS) {
	        super();
	        this.identificador = identificador;
	        this.nombre = nombre;
	        this.cantidadFS = cantidadFS;
	        this.mapeado = false;
	    }

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

	    public int getCantidadFS() {
	        return cantidadFS;
	    }

	    public void setCantidadFS(int cantidadFS) {
	        this.cantidadFS = cantidadFS;
	    }

	    public boolean isMapeado() {
	        return mapeado;
	    }

	    public void setMapeado(boolean mapeado) {
	        this.mapeado = mapeado;
	    }

	    public VirtualNode getNodoUno() {
	        return nodoUno;
	    }

	    public void setNodoUno(VirtualNode nodoUno) {
	        this.nodoUno = nodoUno;
	    }

	    public VirtualNode getNodoDos() {
	        return nodoDos;
	    }

	    public void setNodoDos(VirtualNode nodoDos) {
	        this.nodoDos = nodoDos;
	    }

	    public ArrayList<SustrateEdge> getEnlaceFisico() {
	        return enlaceFisico;
	    }

	    public void setEnlaceFisico(ArrayList<SustrateEdge> enlaceFisico) {
	        this.enlaceFisico = enlaceFisico;
	    }

	    public int getPosicionFisica() {
	        return posicionFisica;
	    }

	    public void setPosicionFisica(int posicionFisica) {
	        this.posicionFisica = posicionFisica;
	    }

}
