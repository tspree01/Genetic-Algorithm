import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeMap;

class Game {
	private static int numberOfTourements = 100;
	private static double mutationRate = 0.1;
	private static double adverageDeviation = 1.0;
	private static double winnerSurviveRate = 0.6;
	static int winner = 0;


	static double[] evolveWeights() throws Exception {
		// Create a random initial population
		Random r = new Random();
		Matrix population = new Matrix(100, 291);
		ArrayList<Individual> individuals = new ArrayList<>();
		long before = 0;
		long after = 0;

		for (int i = 0; i < 100; i++) {
			double[] chromosome = population.row(i);
			Individual individual = new Individual(population.row(i),0);

			for (int j = 0; j < chromosome.length; j++) {
				chromosome[j] = 0.03 * r.nextGaussian();
			}
			for (int j = 0; j < individual.chromosome.length; j++) {
				individual.chromosome[j] = 0.03 * r.nextGaussian();
			}
			individuals.add(individual);

		}
		for (int i = 0; i < 500; i++) {
		//calculate the fitness of each chromosome in the population
			for (int j = 0; j < numberOfTourements; j++) {
				before = System.currentTimeMillis();
				winner = Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent(individuals.get(j).chromosome));
				after = System.currentTimeMillis();
				// If the Reflex agent wins then calculate the fitness of neural agent but if the neural agent wins it get a bonus for winning

				if (winner == 1) {
					individuals.get(j).fitness = (int)(after - before);
				}
				else if (winner == - 1) {
					individuals.get(j).fitness = (int)(100 + 100/(after - before));
				}

			}
		}
		//Each element in m_data is a row or a chromosome

		//call setWeights when you do the tournament
		// Evolve the population

		//       Please write some code to evolve this population.

		//       (For tournament selection, you will need to call Controller.doBattleNoGui(agent1, agent2).)
		// Return an arbitrary member from the population
		return population.row(0);
	}

	static void mutate(Matrix population) {


	}


	public static void main(String[] args) throws Exception {
		double[] w = evolveWeights();
		Controller.doBattle(new ReflexAgent(), new NeuralAgent(w));

	}

}

class FitnessComparator implements Comparator<Integer> {

	public int compare(Integer o1, Integer o2) {
		if (o1 > o2) { return -1; }
		else if ( o1 < o2) { return 1; }
		return 0;
	}

}

