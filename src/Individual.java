public class Individual {
	double[] chromosome;
	long fitness = 0;
	boolean win = false;
	boolean tie = false;

	public Individual(double[] chromosome, long fitness, boolean win) {
		this.chromosome = chromosome;
		this.fitness = fitness;
		this.win = win;
	}
}
