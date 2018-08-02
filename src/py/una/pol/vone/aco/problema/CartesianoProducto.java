package py.una.pol.vone.aco.problema;

import java.util.ArrayList;
import java.util.List;
import py.una.pol.vone.aco.entidades.fisicas.NodosFisicosCandidatos;

public class CartesianoProducto {

    public static List<List<NodosFisicosCandidatos>> cartesianProduct(List<List<NodosFisicosCandidatos>> lists) {
        List<List<NodosFisicosCandidatos>> resultLists = new ArrayList<>();
        if (lists.isEmpty()) {
            resultLists.add(new ArrayList<>());
            return resultLists;
        } else {
            List<NodosFisicosCandidatos> firstList = lists.get(0);
            List<List<NodosFisicosCandidatos>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
            firstList.forEach((condition) -> {
                remainingLists.stream().map((remainingList) -> {
                    ArrayList<NodosFisicosCandidatos> resultList = new ArrayList<>();
                    resultList.add(condition);
                    resultList.addAll(remainingList);
                    return resultList;
                }).forEachOrdered((resultList) -> {
                    resultLists.add(resultList);
                });
            });
        }
        return resultLists;
    }
}
