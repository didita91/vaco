package thiagodnf.jacof.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

//import org.apache.log4j.Logger;
import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.problem.Problem;

/**
 * The algorithm's executor class
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public class ExecutionStats {

    public double executionTime;

    public ACO aco;

    public Problem problem;

    public int[] bestSolution;
    public int hormiga;

    /**
     * The class logger
     *
     * @param aco
     * @param problem
     * @return
     * @throws java.io.IOException
     */
///	static final Logger LOGGER = Logger.getLogger(ExecutionStats.class);
    public static ExecutionStats execute(ACO aco, Problem problem) throws IOException {
        ExecutionStats ets = new ExecutionStats();

        ets.aco = aco;
        ets.problem = problem;

        long initTime = System.currentTimeMillis();
        ets.bestSolution = aco.solve();
        ets.setHormiga(aco.getHormiga());
        ets.executionTime = System.currentTimeMillis() - initTime;

        return ets;
    }

    public void printStats() throws IOException {

    }

    public void printDotFormat() {
    }

    public int getHormiga() {
        return hormiga;
    }

    public void setHormiga(int hormiga) {
        this.hormiga = hormiga;
    }

}
