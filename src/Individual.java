public class Individual {
	double[] chromosome;
	long fitness = 0;
	boolean win = false;
	boolean tie = false;
	double sumOfWeights = 0.0;

	public Individual(double[] chromosome, long fitness) {
		this.chromosome = chromosome;
		this.fitness = fitness;
	}
}
