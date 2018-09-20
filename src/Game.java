import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

class Game {
	private static int numberOfTourements = 100;
	private static double mutationRate = 0.1;
	private static double adverageDeviation = 1.0;
	private static double winnerSurviveRate = 0.6;
	private static double crossoverRate = 0.8;


	static double[] evolveWeights() throws Exception {
		// Create a random initial population
		Random r = new Random();
		Matrix p = new Matrix(100, 291);
		ArrayList<Individual> population = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			double[] chromosome = p.row(i);
			Individual individual = new Individual(p.row(i), 0, false);

			for (int j = 0; j < chromosome.length; j++) {
				chromosome[j] = 0.03 * r.nextGaussian();
			}
			for (int j = 0; j < individual.chromosome.length; j++) {
				individual.chromosome[j] = 0.03 * r.nextGaussian();
			}
			population.add(individual);

		}
		//for (int i = 0; i < 500; i++) {
		//calculate the fitness of each chromosome in the population
		calculateFitness(population);

		naturalSelection(population);
		//}
		//Each element in m_data is a row or a chromosome

		//call setWeights when you do the tournament
		// Evolve the population

		//       Please write some code to evolve this population.

		//       (For tournament selection, you will need to call Controller.doBattleNoGui(agent1, agent2).)
		// Return an arbitrary member from the population
		return p.row(0);
	}

	static void calculateFitness(ArrayList<Individual> population) throws Exception {
		long winner = 0;
		FitnessComparator fitnessComp = new FitnessComparator();
		for (int j = 0; j < numberOfTourements; j++) {
			long before = System.currentTimeMillis();
			winner = Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent(population.get(j).chromosome));
			long after = System.currentTimeMillis();

			// If the Reflex agent wins then calculate the fitness of neural agent but if the neural agent wins it get a bonus for winning
			if (winner == 1) {
				population.get(j).fitness = (after - before);
				population.get(j).win = false;
			}
			else if (winner == - 1) {
				population.get(j).fitness = (100 + 100 / (after - before));
				population.get(j).win = true;
			}
			else {
				population.get(j).fitness = (after - before) / 2;
			}
		}
		population.sort(fitnessComp);
	}


	static void crossover(ArrayList<Individual> population) {
		Random r = new Random();
		if (crossoverRate > r.nextDouble()) {

		}
		else {

		}

	}

	static void naturalSelection(ArrayList<Individual> population) throws Exception {
		int winner = 0;
		Random r = new Random();
		Individual redAgent1 = population.get(r.nextInt(100));
		Individual redAgent2 = population.get(r.nextInt(100));
		for (int j = 0; j < numberOfTourements; j++) {
			long before = System.currentTimeMillis();
			winner = Controller.doBattleNoGui(new NeuralAgent(redAgent1.chromosome), new NeuralAgent(redAgent2.chromosome));
			long after = System.currentTimeMillis();

			if (winnerSurviveRate > r.nextDouble()) {
				if (winner == 1) {
					population.remove(redAgent2);
				}
				else if (winner == - 1) {
					population.remove(redAgent1);
				}
			}
			else {
				if (winner == 1) {
					population.remove(redAgent1);
				}
				else if (winner == - 1) {
					population.remove(redAgent2);
				}

			}

		}

	}

	static void selectParent(ArrayList<Individual> population) {


	}

	static void mutate(ArrayList<Individual> population) {


	}


	public static void main(String[] args) throws Exception {
		double[] w = evolveWeights();
		Controller.doBattle(new ReflexAgent(), new NeuralAgent(w));

	}

}

class FitnessComparator implements Comparator<Individual> {

	public int compare(Individual indiv1, Individual indiv2) {
		if (indiv1.fitness > indiv2.fitness) { return - 1; }
		else if (indiv1.fitness < indiv2.fitness) { return 1; }
		return 0;
	}

}

