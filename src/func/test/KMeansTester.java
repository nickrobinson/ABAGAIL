package func.test;

import dist.Distribution;
import dist.MultivariateGaussian;
import func.KMeansClusterer;
import shared.DataSet;
import shared.Instance;
import util.linalg.DenseVector;
import util.linalg.RectangularMatrix;

/**
 * Testing
 * @author Nick Robinson nrobinson38@mail.gatech.edu
 * @version 1.0
 */
public class KMeansTester {
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) throws Exception {

        if(args.length < 1)
        {
            System.err.println("KMeansTester <k> \n");
            System.exit(1);
        }
        
        int k = Integer.parseInt(args[0]);

        Instance[] instances = new Instance[100];
        MultivariateGaussian mga = new MultivariateGaussian(new DenseVector(new double[] {10, 20, 30}), RectangularMatrix.eye(3).times(.5)); 
        MultivariateGaussian mgb = new MultivariateGaussian(new DenseVector(new double[] {-2, -3, -1}), RectangularMatrix.eye(3).times(.4)); 
        for (int i = 0; i < instances.length; i++) {
            if (Distribution.random.nextBoolean()) {
                instances[i] = mga.sample(null);   
            } else {
                instances[i] = mgb.sample(null);
            }
        }
        DataSet set = new DataSet(instances);
        KMeansClusterer km = new KMeansClusterer(k);
        km.estimate(set);
        System.out.println(km);
    }

    private static Instance[] initializeInstances() {

        double[][][] attributes = new double[1599][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("data/winequality-red.csv")));

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
}
