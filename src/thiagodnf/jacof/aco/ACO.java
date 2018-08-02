package thiagodnf.jacof.aco;

//import static com.google.common.base.Preconditions.checkNotNull;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.apache.log4j.Logger;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.exploration.AbstractAntExploration;
import thiagodnf.jacof.aco.ant.initialization.AbstractAntInitialization;
import thiagodnf.jacof.aco.daemonactions.AbstractDaemonActions;
import thiagodnf.jacof.aco.graph.AntGraph;
import thiagodnf.jacof.aco.graph.initialization.AbstractGraphInitialization;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.AbstractDeposit;
import thiagodnf.jacof.aco.rule.globalupdate.evaporation.AbstractEvaporation;
import thiagodnf.jacof.aco.rule.localupdate.AbstractLocalUpdateRule;
import thiagodnf.jacof.problem.Problem;

/**
 * This is the base class. This one has the main components of all ACO's
 * implementations. So, All ACO's implementations should be extended from this
 * class.
 * <p>
 * In this framework, all ants build their solutions by using java's threads
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public abstract class ACO implements Observer {

    /**
     * Importance of the pheromones values
     */
    protected double alpha;

    /**
     * Importance of the heuristic information
     */
    protected double beta;

    /**
     * The number of ants
     */
    protected int numberOfAnts;

    /**
     * The number of iterations
     */
    protected int numberOfIterations;

    /**
     * Ants *
     */
    protected Ant[] ants;

    /**
     * The graph
     */
    protected AntGraph graph;

    /**
     * The current iteration
     */
    protected int it = 0;

    /**
     * Total of ants that finished your tour
     */
    protected int finishedAnts = 0;

    /**
     * Best Ant in tour
     */
    protected Ant globalBest;

    /**
     * Best Current Ant in tour
     */
    protected Ant currentBest;

    /**
     * The addressed problem
     */
    protected Problem problem;

    /**
     * The graph initialization
     */
    protected AbstractGraphInitialization graphInitialization;

    /**
     * The ant initialization
     */
    protected AbstractAntInitialization antInitialization;

    /**
     * The ant exploration
     */
    protected AbstractAntExploration antExploration;

    /**
     * The ant local update rule
     */
    protected AbstractLocalUpdateRule antLocalUpdate;

    /**
     * The daemon actions
     */
    protected List<AbstractDaemonActions> daemonActions = new ArrayList<>();

    /**
     * The pheromone evaporation's rules
     */
    protected List<AbstractEvaporation> evaporations = new ArrayList<>();

    /**
     * The pheromone deposit's rules
     */
    protected List<AbstractDeposit> deposits = new ArrayList<>();

    /**
     * The class logger
     */
    //static final Logger LOGGER = Logger.getLogger(ACO.class);
    /**
     * The evaporation rate
     */
    protected double rho;

    protected List<Integer> listaPosicionInicial;

    protected int hormiga;

    public int getHormiga() {
        return hormiga;
    }

    public void setHormiga(int hormiga) {
        this.hormiga = hormiga;
    }

    public List<Integer> getListaPosicionInicial() {
        return listaPosicionInicial;
    }

    public void setListaPosicionInicial(List<Integer> listaPosicionInicial) {
        this.listaPosicionInicial = listaPosicionInicial;
    }

    /**
     * Constructor
     *
     * @param problem The addressed problem
     */
    public ACO(Problem problem) {

        //checkNotNull(problem, "The problem cannot be null");
        this.problem = problem;
        this.graph = new AntGraph(problem);
    }

    /**
     * Solve the addressed problem
     *
     * @return the best solution found by the ants
     * @throws java.io.IOException
     */
    public int[] solve() throws IOException {

        build();

        printParameters();

        initializePheromones();
        initializeAnts();

        while (!terminationCondition()) {
            constructAntsSolutions();
            updatePheromones();
            daemonActions(); // optional
        }

      
        return globalBest.getSolution();

    }

    /**
     * Initialize the pheromone values. This method creates a graph and
     * initialize it.
     */
    protected void initializePheromones() throws IOException {

  
        this.graph.initialize(graphInitialization);
    }

    /**
     * Initialize the ants. This method creates an array of ants and positions
     * them in one of the graph's vertex
     */
    protected void initializeAnts() throws IOException {


        this.ants = new Ant[numberOfAnts];

        for (int k = 0; k < numberOfAnts; k++) {

            //se modifica se envia la lista de posiciones
            ants[k] = new Ant(this, k);
            ants[k].setAntInitialization(getAntInitialization());
            ants[k].addObserver(this);
        }
    }

    /**
     * Verify if the search has finished. To reach this, the number of
     * iterations is verified.
     *
     * @return true if the search has finished. Otherwise, false
     */
    protected boolean terminationCondition() {
        return ++it > numberOfIterations;
    }

    /**
     * Update the pheromone values in the graph
     */
    protected void updatePheromones() throws IOException {

        for (int i = 0; i < problem.getNumberOfNodes(); i++) {

            for (int j = i; j < problem.getNumberOfNodes(); j++) {

                if (i != j) {
                    // Do Evaporation
                    for (AbstractEvaporation evaporation : evaporations) {
                        graph.setTau(i, j, evaporation.getTheNewValue(i, j));
                        graph.setTau(j, i, graph.getTau(i, j));
                    }
                    // Do Deposit
                    for (AbstractDeposit deposit : deposits) {
                        graph.setTau(i, j, deposit.getTheNewValue(i, j));
                        graph.setTau(j, i, graph.getTau(i, j));
                    }
                }
            }
        }
    }

    /**
     * Construct the ant's solutions
     */
    private synchronized void constructAntsSolutions() throws IOException {
        currentBest = null;

        //Construct the ant solutions by using threads
        for (int k = 0; k < numberOfAnts; k++) {
            Thread t = new Thread(ants[k], "Ant " + ants[k].getId());
            // //System.out.println("ants size: "+ants.length+" k:"+k+" valoer: "+ants[k].getId());

            t.start();

        }

        //Wait all ants finish your tour
        try {
            wait();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Perform the daemon actions
     */
    public void daemonActions() {

        if (daemonActions.isEmpty()) {
            //System.out.println("There are no daemon actions for this algorithm");
//            LOGGER.debug("There are no daemon actions for this algorithm");
        } else {
            //System.out.println("Ejecutando daemon actions");
//            LOGGER.debug("Executing daemon actions");
        }

        for (AbstractDaemonActions daemonAction : daemonActions) {
            daemonAction.doAction();
        }
    }

    /**
     * When an ant has finished its search process, this method is called to
     * update the current and global best solutions.
     *
     * @param obj
     */
    @Override
    public synchronized void update(Observable obj, Object arg) {

        Ant ant = (Ant) obj;

        // Calculate the fitness function for the found solution
        ant.setTourLength(problem.evaluate(ant.getSolution()));
        // Update the current best solution
        if (currentBest == null || problem.better(ant.getTourLength(), currentBest.getTourLength())) {

            currentBest = ant.clone();
        }   // Update the global best solution
        if (globalBest == null || problem.better(ant.getTourLength(), globalBest.getTourLength())) {
            globalBest = ant.clone();

        }

        if (++finishedAnts == numberOfAnts) {
            // Restart the counter to build the solutions again
            finishedAnts = 0;

            notify();
        }

    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public AntGraph getGraph() {
        return graph;
    }

    public void setGraph(AntGraph graph) {
        this.graph = graph;
    }

    public int getNumberOfAnts() {
        return numberOfAnts;
    }

    public void setNumberOfAnts(int numberOfAnts) {
        this.numberOfAnts = numberOfAnts;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Ant[] getAnts() {
        return ants;
    }

    public void setAnts(Ant[] ants) {
        this.ants = ants;
    }

    public Ant getGlobalBest() {
        return globalBest;
    }

    public void setGlobalBest(Ant globalBest) {
        this.globalBest = globalBest;
    }

    public Ant getCurrentBest() {
        return currentBest;
    }

    public void setCurrentBest(Ant currentBest) {
        this.currentBest = currentBest;
    }

    public AbstractAntInitialization getAntInitialization() {
        return antInitialization;
    }

    public void setAntInitialization(AbstractAntInitialization antInitialization) {
        this.antInitialization = antInitialization;
    }

    public void setGraphInitialization(AbstractGraphInitialization graphInitialization) {
        this.graphInitialization = graphInitialization;
    }

    public AbstractGraphInitialization getGraphInitialization() {
        return graphInitialization;
    }

    public AbstractAntExploration getAntExploration() {
        return antExploration;
    }

    public void setAntExploration(AbstractAntExploration antExploration) {
        this.antExploration = antExploration;
    }

    public AbstractLocalUpdateRule getAntLocalUpdate() {
        return antLocalUpdate;
    }

    public void setAntLocalUpdate(AbstractLocalUpdateRule antLocalUpdate) {
        this.antLocalUpdate = antLocalUpdate;
    }

    public List<AbstractDeposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<AbstractDeposit> deposits) {
        this.deposits = deposits;
    }

    public List<AbstractEvaporation> getEvaporations() {
        return evaporations;
    }

    public void setEvaporations(List<AbstractEvaporation> evaporations) {
        this.evaporations = evaporations;
    }

    public List<AbstractDaemonActions> getDaemonActions() {
        return daemonActions;
    }

    public void setDaemonActions(List<AbstractDaemonActions> daemonActions) {
        this.daemonActions = daemonActions;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    /**
     * Print the parameters
     */
    protected void printParameters() throws IOException {
////        Date fecha = Calendar.getInstance().getTime();
////        String nombreArchivo = "vone_aco_results_dinamico_";
////        nombreArchivo = nombreArchivo.
////                concat(new SimpleDateFormat("yyyyMMdd").format(fecha)).
////                concat(".txt");
////
////        File directorio = new File(nombreArchivo);
////        BufferedWriter writer
////                = new BufferedWriter(new FileWriter(directorio, true));
////
////        writer.write("=================== Parametros ===================");
////        writer.newLine();
////        writer.write("Derivacion: " + this.toString());
////        writer.newLine();
////
////        writer.write("Problema: " + this.problem);
////        writer.newLine();
////
////        writer.write("Numero de Hormigas: " + this.numberOfAnts);
////        writer.newLine();
////
////        writer.write("Numero de Iteraciones: " + this.numberOfIterations);
////        writer.newLine();
////
////        writer.write("Alpha: " + this.alpha);
////        writer.newLine();
////
////        writer.write("Beta: " + this.beta);
////        writer.newLine();
////
////        writer.write("Inicializacion Grafo: " + this.graphInitialization);
////        writer.newLine();
////
////        writer.write("Inicializacion Hormiga: " + this.antInitialization);
////        writer.newLine();
////
////        writer.write("Ant Exploration: " + this.antExploration);
////        writer.newLine();
////
////        writer.write("Ant Local Update Rule: " + this.antLocalUpdate);
////        writer.newLine();
////
////        writer.write("Evaporations: " + this.evaporations);
////        writer.newLine();
////
////        writer.write("Deposits: " + this.deposits);
////        writer.newLine();
////
////        writer.write("Daemon Actions: " + this.daemonActions);
////        writer.newLine();
////
////        writer.write("==================================================");
////        writer.newLine();
////
////        writer.flush();

        //System.out.println("=================== Parametros ===================");
        //System.out.println("Derivacion: " + this.toString());
        //System.out.println("Problema: " + this.problem);
        //System.out.println("Numero de Hormigas: " + this.numberOfAnts);
        //System.out.println("Numero de Iteraciones: " + this.numberOfIterations);
        //System.out.println("Alpha: " + this.alpha);
        //System.out.println("Beta: " + this.beta);
        //System.out.println("Inicializacion Grafo: " + this.graphInitialization);
        //System.out.println("Inicializacion Hormiga: " + this.antInitialization);
        //System.out.println("Ant Exploration: " + this.antExploration);
        //System.out.println("Ant Local Update Rule: " + this.antLocalUpdate);
        //System.out.println("Evaporations: " + this.evaporations);
        //System.out.println("Deposits: " + this.deposits);
        //System.out.println("Daemon Actions: " + this.daemonActions);
        //System.out.println("==================================================");
    }

    /**
     * Build an ant's implementation
     */
    public abstract void build();

    /**
     * Returns a string representation of the object.
     */
    public abstract String toString();
}
