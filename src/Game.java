import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

class Game {
	private static int numberOfTourements = 100;
	static Random rand = new Random();
	private static double mutationRate = 0.02 * rand.nextGaussian();
	private static double adverageDeviation = 1.0;
	private static double winnerSurviveRate = 0.7;
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
				individual.chromosome[j] = 0.09 * r.nextGaussian();
			}
			population.add(individual);

		}
		//for (int i = 0; i < 500; i++) {
		//calculate the fitness of each chromosome in the population
		// either do tourement or do mutate
		calculateFitness(population);

		naturalSelection(population);
		crossover(population);
		//}

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
				population.get(j).tie = true;
			}
		}
		population.sort(fitnessComp);
	}


	static void crossover(ArrayList<Individual> population, Individual winnerOfFight) {
		Random r = new Random();
		Individual offspring;
		boolean notOver291 = true;

		//Make sure that you dont get over 100%
		while (notOver291) {
			int percentOfParent1 = (r.nextInt(winnerOfFight.chromosome.length));
			int percentOfParent2 = (r.nextInt());
			if (percentOfParent1 + percentOfParent2 < 291) {
				notOver291 = false;
			}
		}

		if (crossoverRate > r.nextDouble()) {


		}
		else {

		}
	}

	static void naturalSelection(ArrayList<Individual> population) throws Exception {
		int winner = 0;
		Random r = new Random();
		double randomPercent = r.nextDouble();

		int randomParent1 = r.nextInt(population.size());
		int randomParent2 = r.nextInt(population.size());
		Individual redAgent1 = population.get(randomParent1);
		Individual redAgent2 = population.get(randomParent2);

		long before = System.currentTimeMillis();
		if (redAgent1.fitness > redAgent2.fitness && winnerSurviveRate > randomPercent) {
			population.remove(redAgent2);
		}
		else {
			population.remove(redAgent1);
		}
		long after = System.currentTimeMillis();

/*		if (winnerSurviveRate > randomPercent) {
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
		}*/
	}

	static Individual selectParents(ArrayList<Individual> population, Individual winnerOfFight) {
		Random r = new Random();
		Individual offspring;

		boolean notFoundParent = true;

		winnerOfFight = population.get(r.nextInt(population.size()));

		//continue to pick parents until one is close to the first parent's fitness
		while (notFoundParent) {
			//check to see if the parent is close in fitness to the first parent picked
			for (int i = 0; i < 3; i++) {
				if (winnerOfFight.fitness - population.get(r.nextInt(population.size())).fitness < 5) {
					offspring = population.get(r.nextInt(population.size()));
					notFoundParent = false;
					return offspring;
				}
				else {

				}
			}
		}
		return null;
	}

	static void mutate(ArrayList<Individual> population) {
		for (int i = 0; i < population.size(); i++) {

		}

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

