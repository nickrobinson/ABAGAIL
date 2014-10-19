package func.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Scanner;

import shared.ConvergenceTrainer;
import shared.DataSet;
import shared.FixedIterationTrainer;
import shared.Instance;
import shared.SumOfSquaresError;
import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import func.nn.backprop.BatchBackPropagationTrainer;
import func.nn.backprop.RPROPUpdateRule;

/**
 * A simple classification test
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class NNClassificationTest {
	
	private static DecimalFormat df = new DecimalFormat("0.000");

	private static Instance[] initializeInstances() {

        double[][][] attributes = new double[1599][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("/Users/nrobinson/Development/ABAGAIL/data/winequality-red.csv")));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[7]; // 7 attributes
                attributes[i][1] = new double[1];

                for(int j = 0; j < 7; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next());

                attributes[i][1][0] = Double.parseDouble(scan.next());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[attributes.length];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            instances[i].setLabel(new Instance((int) attributes[i][1][0]));
        }

        return instances;
    }
	
    /**
     * Tests out the perceptron with the classic xor test
     * @param args ignored
     */
    public static void main(String[] args) {
    	Integer numIters = Integer.parseInt(args[0]);
    	
        BackPropagationNetworkFactory factory = 
            new BackPropagationNetworkFactory();
        
        
        Instance[] patterns = initializeInstances();

        BackPropagationNetwork network = factory.createClassificationNetwork(
           new int[] { 7, 5, 1 });
        DataSet set = new DataSet(patterns);
        FixedIterationTrainer trainer = new FixedIterationTrainer(
               new BatchBackPropagationTrainer(set, network,
                   new SumOfSquaresError(), new RPROPUpdateRule()), numIters);
        trainer.train();
        System.out.println("Convergence in " 
            + numIters.toString() + " iterations");
        
        double predicted, actual;
        double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
        
        for(int j = 0; j < patterns.length; j++) {
        	network.setInputValues(patterns[j].getData());
        	network.run();

            predicted = Double.parseDouble(patterns[j].getLabel().toString());
            actual = Double.parseDouble(network.getOutputValues().toString());

            double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
        }
        
        String results = "";
        results +=  "\nResults for " + "Neural Net" + ": \nCorrectly classified " + correct + " instances." +
                "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: ";
        
        System.out.println(results);
        
        for (int i = 0; i < patterns.length; i++) {
            network.setInputValues(patterns[i].getData());
            network.run();
            System.out.println("~~");
            System.out.println(patterns[i].getLabel());
            System.out.println(network.getOutputValues());
        }
    }
}
