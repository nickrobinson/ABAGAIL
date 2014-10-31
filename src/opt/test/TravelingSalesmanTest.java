package opt.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.SwapNeighbor;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.SwapMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class TravelingSalesmanTest {
    /** The n value */
    private static int N = 50;
    
    private static String filename = "logs/traveling_salesman.csv";
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
    	
    	N = numItems;
    	
        Random random = new Random();
        // create the random points
        double[][] points = new double[N][2];
        for (int i = 0; i < points.length; i++) {
            points[i][0] = random.nextDouble();
            points[i][1] = random.nextDouble();   
        }
        // for rhc, sa, and ga we use a permutation based encoding
        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
        Distribution odd = new DiscretePermutationDistribution(N);
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new TravelingSalesmanCrossOver(ef);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
       
        
        double saStart = System.nanoTime(), saEnd, saTime;
        SimulatedAnnealing sa = new SimulatedAnnealing(1E12, .95, hcp);
        FixedIterationTrainer fit = new FixedIterationTrainer(sa, saIters); //200000
        fit.train();
        saEnd = System.nanoTime();
        saTime = saEnd - saStart;
        saTime /= Math.pow(10,9);
        System.out.println("SA: " + ef.value(sa.getOptimal()) + "," + saTime);
        // Write output to CSV file
        String saResults = N + "," + "SA," + saIters + "," + ef.value(sa.getOptimal()) + "," + saTime;
        try (Writer writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.append("\n" + saResults);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        double gaStart = System.nanoTime(), gaEnd, gaTime;
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 20, gap);
        fit = new FixedIterationTrainer(ga, gaIters); //1000
        fit.train();
        gaEnd = System.nanoTime();
        gaTime = gaEnd - gaStart;
        gaTime /= Math.pow(10,9);
        System.out.println("GA: " + ef.value(ga.getOptimal()) + "," + gaTime);
        // Write output to CSV file
        String gaResults = N + "," + "GA," + gaIters + "," + ef.value(ga.getOptimal()) + "," + gaTime;
        try (Writer writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.append("\n" + gaResults);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        // for mimic we use a sort encoding
        ef = new TravelingSalesmanSortEvaluationFunction(points);
        int[] ranges = new int[N];
        Arrays.fill(ranges, N);
        odd = new  DiscreteUniformDistribution(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        double mimicStart = System.nanoTime(), mimicEnd, mimicTime;
        MIMIC mimic = new MIMIC(200, 100, pop);
        fit = new FixedIterationTrainer(mimic, mimicIters); //1000
        fit.train();
        mimicEnd = System.nanoTime();
        mimicTime = mimicEnd - mimicStart;
        mimicTime /= Math.pow(10,9);
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()) + "," + mimicTime);
        // Write output to CSV file
        String mimicResults = N + "," + "MIMIC," + mimicIters + "," + ef.value(mimic.getOptimal()) + "," + mimicTime;
        try (Writer writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.append("\n" + mimicResults);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
