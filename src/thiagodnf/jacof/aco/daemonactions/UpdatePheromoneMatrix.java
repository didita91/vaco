package thiagodnf.jacof.aco.daemonactions;

//import org.apache.log4j.Logger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import thiagodnf.jacof.aco.ACO;

/**
 * Update the pheromone value regarding the tMin and tMax values
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public class UpdatePheromoneMatrix extends AbstractDaemonActions {

    /**
     * The class logger
     */
    //static final Logger LOGGER = Logger.getLogger(UpdatePheromoneMatrix.class);
    public UpdatePheromoneMatrix(ACO aco) {
        super(aco);
    }

    @Override
    public void doAction() {

        //LOGGER.debug("Updating the pheromone matrix values");
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
//            writer.write("Actualizando los valores de la matriz de feromonas.");
//            writer.newLine();
//            writer.flush();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

        //System.out.println("Updating the pheromone matrix values");
        for (int i = 0; i < aco.getProblem().getNumberOfNodes(); i++) {

            for (int j = i; j < aco.getProblem().getNumberOfNodes(); j++) {

                if (i != j) {
                    aco.getGraph().setTau(i, j, Math.min(aco.getGraph().getTau(i, j), aco.getGraph().getTMax()));
                    aco.getGraph().setTau(i, j, Math.max(aco.getGraph().getTau(i, j), aco.getGraph().getTMin()));
                    aco.getGraph().setTau(j, i, aco.getGraph().getTau(i, j));
                }
            }
        }
    }

    @Override
    public String toString() {
        return UpdatePheromoneMatrix.class.getSimpleName();
    }
}
