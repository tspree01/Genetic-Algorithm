import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

class Game {
	private static int numberOfTourements = 100;
	static Random rand = new Random();
	//private static double mutationRate = 0.02 * rand.nextGaussian();
	private static double adverageDeviation = 1.5;
	private static double winnerSurviveRate = 0.7;
	private static double crossoverRate = 0.8;


	static double[] evolveWeights() throws Exception {
		// Create a random initial population
		Random r = new Random();
		Matrix p = new Matrix(100, 291);
		FitnessComparator fitnessComp = new FitnessComparator();
		ArrayList<Individual> population = new ArrayList<>();
		ArrayList<Individual> chads = new ArrayList<>();
		double[] test = new double[291];
		Individual chad = new Individual(test, 0);

		for (int i = 0; i < 100; i++) {
			double[] chromosome = p.row(i);
			Individual individual = new Individual(p.row(i), 0);

			for (int j = 0; j < chromosome.length; j++) {
				chromosome[j] = 0.09 * r.nextGaussian();
			}
			for (int j = 0; j < individual.chromosome.length; j++) {
				individual.chromosome[j] = 0.09 * r.nextGaussian();
			}
			population.add(individual);

		}
		for (int i = 0; i < 50; i++) {
			//calculate the fitness of each chromosome in the population
			// either do tourement or do mutate
			calculatePopulationFitness(population);

/*			if (chad != null && chad.win){
				chads.add(chad);
			}*/
			naturalSelection(population);
			mutate(population);
			population.size();
		}
/*		for (Individual individual : population) {
			System.out.println("fitness: " + chad.fitness);
			System.out.println("Tie: " + chad.tie);
			System.out.println("Win: " + chad.win);
			System.out.println();
		}*/

		//call setWeights when you do the tournament
		// Evolve the population

		//       Please write some code to evolve this population.

		//       (For tournament selection, you will need to call Controller.doBattleNoGui(agent1, agent2).)
		// Return an arbitrary member from the population
		population.sort(fitnessComp);
		chads.sort(fitnessComp);
/*		for (Individual chad : chads) {
			if (chad.tie){
				return chad.chromosome;
			}
		}*/
		return chad.chromosome;
	}

	static void calculatePopulationFitness(ArrayList<Individual> population) throws Exception {
		long winner = 0;
		FitnessComparator fitnessComp = new FitnessComparator();
		double[] chromosome = new double[291];
		Individual individual = new Individual(chromosome, 0);
		for (int j = 0; j < numberOfTourements; j++) {
			long before = System.currentTimeMillis();
			winner = Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent(population.get(j).chromosome));
			long after = System.currentTimeMillis();

			// If the Reflex agent wins then calculate the fitness of neural agent but if the neural agent wins it get a bonus for winning
			if (winner == 1) {
				population.get(j).fitness = (after - before);
				population.get(j).win = false;
				population.get(j).tie = false;
			}
			else if (winner == - 1) {
				population.get(j).fitness = (10000 + 10000 / (after - before));
				population.get(j).win = true;
				population.get(j).tie = false;
			}
			else if (winner == 0) {
				population.get(j).fitness = (after - before);
				population.get(j).tie = true;
				population.get(j).win = false;
			}
		}
		population.sort(fitnessComp);
	}
/*
	static Individual calculateIndividualFitness(Individual individual){
		long before = System.currentTimeMillis();
		Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent());
		long after = System.currentTimeMillis();

	}*/

	static void crossover(ArrayList<Individual> population, Individual winnerOfFight) {
		Random r = new Random();
		double[] chromosome = new double[291];
		Individual offspring = new Individual(chromosome, 0);
		boolean notOver291 = true;
		Individual parent2 = selectParents(population, winnerOfFight);

		//Make sure that you dont get over 100%
		//while (notOver291) {
		int percentOfParent1 = (r.nextInt(winnerOfFight.chromosome.length));
		//int percentOfParent2 = (r.nextInt(winnerOfFight.chromosome.length - percentOfParent1));
/*		if (percentOfParent1 + percentOfParent1 < 291) {
			//notOver291 = false;
			}*/
		//}

		if (crossoverRate > r.nextDouble()) {
			for (int i = 0; i < winnerOfFight.chromosome.length; i++) {
				if (i < percentOfParent1) {
					offspring.chromosome[i] = winnerOfFight.chromosome[i];
				}
				else if (i > percentOfParent1) {
					offspring.chromosome[i] = parent2.chromosome[i];
				}
			}
			population.add(offspring);
		}
		else {
			//TODO:This might be a bug
			population.add(winnerOfFight);
		}
	}

	static void naturalSelection(ArrayList<Individual> population) throws Exception {
		Random r = new Random();
		double randomPercent = r.nextDouble();

		int randomParent1 = r.nextInt(population.size());
		int randomParent2 = r.nextInt(population.size());
		Individual redAgent1 = population.get(randomParent1);
		Individual redAgent2 = population.get(randomParent2);

		if (redAgent1.fitness > redAgent2.fitness && winnerSurviveRate > randomPercent) {
			population.remove(redAgent2);
			crossover(population, redAgent1);
		}
		else {
			population.remove(redAgent1);
			crossover(population, redAgent2);
		}

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
		Individual parent2;
		WeightComparator weightComp = new WeightComparator();
		ArrayList<Individual> parents = new ArrayList<>();

		double sumOfParentWeight = 0.0;

		boolean notFoundParent = true;

		//continue to pick parents until one is close to the first parent's fitness
		//while (notFoundParent) {
		//check to see if the parent is close in fitness to the first parent picked
		for (int i = 0; i < 5; i++) {
			parent2 = population.get(r.nextInt(population.size()));
			for (int j = 0; j < parent2.chromosome.length; j++) {
				sumOfParentWeight += Math.abs((winnerOfFight.chromosome[j] - parent2.chromosome[j]));
			}
			parent2.sumOfWeights = sumOfParentWeight;
			parents.add(parent2);
		}
		parents.sort(weightComp);
		return parents.get(0);
	}

	static void mutate(ArrayList<Individual> population) {
		Random r = new Random();
		Individual individual;
		double mutationRate = 0.0;
		for (int i = 0; i < population.size(); i++) {
			//int test = r.nextInt(population.get(i).chromosome.length);
			mutationRate = 0.1;
			double randomPercent = r.nextDouble();
			if (mutationRate > randomPercent) {
				population.get(i).chromosome[r.nextInt(population.get(i).chromosome.length)] += (r.nextGaussian() * adverageDeviation);
			}
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

class WeightComparator implements Comparator<Individual> {

	public int compare(Individual indiv1, Individual indiv2) {
		if (indiv1.sumOfWeights > indiv2.sumOfWeights) { return 1; }
		else if (indiv1.sumOfWeights < indiv2.sumOfWeights) { return - 1; }
		return 0;
	}

}