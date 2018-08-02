package py.una.pol.vone.aco.utils.rsa.kshortespaths.ksp;

import java.util.List;

import py.una.pol.vone.aco.utils.rsa.kshortespaths.Graph;
import py.una.pol.vone.aco.utils.rsa.kshortespaths.utilrsa.Path;

public interface KSPAlgorithm {
	public boolean isLoopless();

    public List<Path> ksp(Graph graph, String sourceLabel, String targetLabel, int K);

}
