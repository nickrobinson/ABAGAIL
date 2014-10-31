package opt.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * A test of the knap sack problem
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class KnapsackTest {
    /** Random number generator */
    private static final Random random = new Random();
    /** The number of items */
    private static int NUM_ITEMS = 40;
    /** The number of copies each */
    private static final int COPIES_EACH = 4;
    /** The maximum weight for a single element */
    private static final double MAX_WEIGHT = 50;
    /** The maximum volume for a single element */
    private static final double MAX_VOLUME = 50;
    /** The volume of the knapsack */
    private static final double KNAPSACK_VOLUME = 
         MAX_VOLUME * NUM_ITEMS * COPIES_EACH * .4;
    private static String filename = "logs/knapsack.csv";
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
    	if(args.length < 4)
        {
            System.err.println("TravelingSalesmanTest <range> <gaIterations> <saIterations> <mimicIterations>\n");
            System.exit(1);
        }
    	
    	int numItems = Integer.parseInt(args[0]);
    	int gaIters = Integer.parseInt(args[1]);
    	int saIters = Integer.parseInt(args[2]);
    	int mimicIters = Integer.parseInt(args[3]);
    	
    	NUM_ITEMS = numItems;
    	
        int[] copies = new int[NUM_ITEMS];
        Arrays.fill(copies, COPIES_EACH);
        double[] weights = new double[NUM_ITEMS];
        double[] volumes = new double[NUM_ITEMS];
        for (int i = 0; i < NUM_ITEMS; i++) {
            weights[i] = random.nextDouble() * MAX_WEIGHT;
            volumes[i] = random.nextDouble() * MAX_VOLUME;
        }
         int[] ranges = new int[NUM_ITEMS];
        Arrays.fill(ranges, COPIES_EACH + 1);
        EvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, KNAPSACK_VOLUME, copies);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new UniformCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        double saStart = System.nanoTime(), saEnd, saTime;
        SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
        FixedIterationTrainer fit = new FixedIterationTrainer(sa, saIters); //200000
        fit.train();
        saEnd = System.nanoTime();
        saTime = saEnd - saStart;
        saTime /= Math.pow(10,9);
        System.out.println("SA: " + ef.value(sa.getOptimal()) + "," + saTime);
        // Write output to CSV file
        String saResults = NUM_ITEMS + "," + "SA," + saIters + "," + ef.value(sa.getOptimal()) + "," + saTime;
        try (Writer writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.append("\n" + saResults);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        double gaStart = System.nanoTime(), gaEnd, gaTime;
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 25, gap);
        fit = new FixedIterationTrainer(ga, gaIters); //1000
        fit.train();
        gaEnd = System.nanoTime();
        gaTime = gaEnd - gaStart;
        gaTime /= Math.pow(10,9);
        System.out.println("GA: " + ef.value(ga.getOptimal()) + "," + gaTime);
        // Write output to CSV file
        String gaResults = NUM_ITEMS + "," + "GA," + gaIters + "," + ef.value(ga.getOptimal()) + "," + gaTime;
        try (Writer writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.append("\n" + gaResults);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        double mimicStart = System.nanoTime(), mimicEnd, mimicTime;
        MIMIC mimic = new MIMIC(200, 100, pop);
        fit = new FixedIterationTrainer(mimic, mimicIters); //1000
        fit.train();
        mimicEnd = System.nanoTime();
        mimicTime = mimicEnd - mimicStart;
        mimicTime /= Math.pow(10,9);
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()) + "," + mimicTime);
        // Write output to CSV file
        String mimicResults = NUM_ITEMS + "," + "MIMIC," + mimicIters + "," + ef.value(mimic.getOptimal()) + "," + mimicTime;
        try (Writer writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.append("\n" + mimicResults);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
