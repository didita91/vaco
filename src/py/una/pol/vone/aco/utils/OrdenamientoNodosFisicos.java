package py.una.pol.vone.aco.utils;

import java.util.ArrayList;
import java.util.List;

import py.una.pol.vone.aco.entidades.fisicas.NodosFisicosCandidatos;
import py.una.pol.vone.aco.entidades.virtuales.VirtualNodeStatus;
import py.una.pol.vone.simulator.model.SustrateNetwork;
import py.una.pol.vone.simulator.model.SustrateNode;
import py.una.pol.vone.simulator.model.VirtualNode;

/**
 * Probabilidades de los nodos fisicos para ordenamiento
 *
 * @author <a href="mailto: liliaguero1208@gmail.com">Liliana Agüero</a>
 *
 */
public class OrdenamientoNodosFisicos {

    //se va a obtener la lista (identificadores) de los posibles nodoos a ser mapeados en la red fisica
    public static List<NodosFisicosCandidatos> cpuNodoFisico(SustrateNetwork redFisica, VirtualNode nodoVirtual, int orden, List<Integer> listaPosicionInicial) {

        List<SustrateNode> nodoFisicoList = new ArrayList<>();

        int capacidadCpuNodoVirtual = nodoVirtual.getCapacidadCPU();
        for (int i = 0; i < redFisica.getNodosFisicos().size(); i++) {
            SustrateNode nodoFisico = redFisica.getNodosFisicos().get(i);

            if (nodoFisico.tieneCapacidad(capacidadCpuNodoVirtual)) {

                nodoFisicoList.add(redFisica.getNodosFisicos().get(i));
            }
        }
        List<NodosFisicosCandidatos> probabilidadNodoFisico = calcularNodosCandidatos(nodoFisicoList, nodoVirtual, orden, listaPosicionInicial);
        return probabilidadNodoFisico;
    }

    private static List<NodosFisicosCandidatos> calcularNodosCandidatos(List<SustrateNode> nodoFisicoCandidato, VirtualNode nodoVirtual, int orden, List<Integer> listaPosicionInicial) {
        List<NodosFisicosCandidatos> candidatos = new ArrayList<>();
        for (int i = 0; i < nodoFisicoCandidato.size(); i++) {
            if (orden == 1) {
                listaPosicionInicial.add(i);
            }
            NodosFisicosCandidatos cand = new NodosFisicosCandidatos();
            cand.setNodoFisico(nodoFisicoCandidato.get(i));
            cand.setNodoVirtual(nodoVirtual);
            cand.setOrden(orden);
            cand.setMapeado(false);
            // cand.setCpuNormalizado((double) nodoFisicoCandidato.get(i).capacidadActual() / maximoCpu);
            candidatos.add(cand);

            // result[i]=nodoFisico.get(i).getIdentificador();
        }
        ordenacionBurbuja(listaPosicionInicial,nodoFisicoCandidato);
        return candidatos;
    }

    public static void ordenacionBurbuja(List<Integer> lista, List<SustrateNode> nodoCandidato) {
        int N = lista.size();
        for (int i = N - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                SustrateNode nodo1 = nodoCandidato.get(lista.get(j));
                SustrateNode nodo2 = nodoCandidato.get(lista.get(j + 1));

                if (nodo1.capacidadActual() < nodo2.capacidadActual()) {
                    int temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
//                    vec[j] = vec[j + 1];
                    lista.set(j + 1, temp);
//                    vec[j + 1] = temp;
                }
            }
        }
    }
}
