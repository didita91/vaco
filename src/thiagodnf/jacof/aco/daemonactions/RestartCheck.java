package thiagodnf.jacof.aco.daemonactions;

//import static com.google.common.base.Preconditions.checkArgument;
//import org.apache.log4j.Logger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.Ant;

/**
 * Pheromone trail reinitialization is typically triggered when the algorithm
 * approaches the stagnation behavior (as measured by some statistics on the
 * pheromone trails) or if for a given number of algorithm iterations no
 * improved tour is found. This class uses the number of iterations
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public class RestartCheck extends AbstractDaemonActions {

    protected int stagnation;

    protected int stagnationCounter = 0;

    protected Ant bestAnt;

    /**
     * The class logger
     */
    //static final Logger LOGGER = Logger.getLogger(RestartCheck.class);
    public RestartCheck(ACO aco, int stagnation) {
        super(aco);

        //checkArgument(stagnation >= 1, "The stagnation should be greater or equal than 0");
        if (stagnation < 1) {
            System.err.println("The stagnation should be greater or equal than 0");
        }
        this.stagnation = stagnation;
    }

    public RestartCheck(ACO aco) {
        this(aco, 20);
    }

    @Override
    public void doAction() {
//        Date fecha = Calendar.getInstance().getTime();
//        String nombreArchivo = "vone_aco_results_dinamico_";
//        nombreArchivo = nombreArchivo.
//                concat(new SimpleDateFormat("yyyyMMdd").format(fecha)).
//                concat(".txt");
//
//        File directorio = new File(nombreArchivo);
//        BufferedWriter writer;
//
//        try {
//            writer = new BufferedWriter(new FileWriter(directorio, true));
//            writer.write("Verificando si la matriz de feromonas puede ser reiniciada.");
//            writer.newLine();
//            writer.flush();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        //LOGGER.debug("Verifing if the pheromone matrix should be restarted");
        //System.out.println("Verifing if the pheromone matrix should be restarted");
        if (bestAnt == null) {
            bestAnt = aco.getGlobalBest().clone();
        }

        if (bestAnt.getTourLength() == aco.getGlobalBest().getTourLength()) {
            stagnationCounter++;
        } else {
            bestAnt = aco.getGlobalBest().clone();
            stagnationCounter = 0;
        }

        if (stagnationCounter == stagnation) {

            //LOGGER.debug("The stagnation was reached. The pheromone matrix will be restarted");
            //System.out.println("The stagnation was reached. The pheromone matrix will be restarted");

            try {
                aco.getGraph().initialize(aco.getGraph().getTMax());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            stagnationCounter = 0;
        }
    }

    @Override
    public String toString() {
        return RestartCheck.class.getSimpleName() + " after " + stagnation + " iterations";
    }
}
