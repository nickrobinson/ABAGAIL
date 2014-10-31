package opt.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

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
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class FourPeaksTest {
    /** The n value */
    private static int N = 200;
    /** The t value */
    private static final int T = N / 5;
    
    private static String filename = "logs/four_peaks.csv";
    
    public static void main(String[] args) {
    	
    	if(args.length < 4)
        {
            System.err.println("FourPeaksTest <range> <gaIterations> <saIterations> <mimicIterations>\n");
            System.exit(1);
        }
    	
    	int numItems = Integer.parseInt(args[0]);
    	int gaIters = Integer.parseInt(args[1]);
    	int saIters = Integer.parseInt(args[2]);
    	int mimicIters = Integer.parseInt(args[3]);
    	
    	N = numItems;
    	
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        EvaluationFunction ef = new FourPeaksEvaluationFunction(T);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new SingleCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        int iter = 1; 
        FixedIterationTrainer fit;
        
        if(gaIters > 0)
        {
        	double saStart = System.nanoTime(), saEnd, saTime;
            SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
            fit = new FixedIterationTrainer(sa, gaIters);
            fit.train();
            saEnd = System.nanoTime();
            saTime = saEnd - saStart;
            saTime /= Math.pow(10,9);
            System.out.println("SA: " + ef.value(sa.getOptimal()) + "," + saTime);
            // Write output to CSV file
            String saResults = N + "," + "SA," + gaIters + "," + ef.value(sa.getOptimal()) + "," + saTime;
            try (Writer writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.append("\n" + saResults);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if(saIters > 0)
        {
	        double gaStart = System.nanoTime(), gaEnd, gaTime;
	        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
	        fit = new FixedIterationTrainer(ga, saIters);
	        fit.train();
	        gaEnd = System.nanoTime();
	        gaTime = gaEnd - gaStart;
	        gaTime /= Math.pow(10,9);
	        System.out.println("GA: " + ef.value(ga.getOptimal()) + "," + gaTime);
	        // Write output to CSV file
	        String gaResults = N + "," + "GA," + saIters + "," + ef.value(ga.getOptimal()) + "," + gaTime;
	        try (Writer writer = new BufferedWriter(new FileWriter(filename, true))) {
	            writer.append("\n" + gaResults);
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
        }
        
        if(mimicIters > 0)
        {
        	double mimicStart = System.nanoTime(), mimicEnd, mimicTime;
            MIMIC mimic = new MIMIC(200, 20, pop);
            fit = new FixedIterationTrainer(mimic, mimicIters);
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
}
