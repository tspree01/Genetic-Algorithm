import java.util.ArrayList;
import java.util.Random;

class Game {

	private static double sigmoid(double x)
	{
		return 1 / (1 + Math.exp(-x));
	}

	static double[] evolveWeights() {
		// Create a random initial population
		Random r = new Random();
		Matrix population = new Matrix(100, 291);

		for (int i = 0; i < 100; i++) {
			double[] chromosome = population.row(i);

			for (int j = 0; j < chromosome.length; j++) {
				chromosome[j] = Math.round(sigmoid(0.03 * r.nextGaussian()));
			}

		}

		//Each element in m_data is a row or a chromosome

		//Fitness function
		//Maybe the solution is you keep evolving the population/chromosomes until the red team wins


		// Evolve the population
		// todo: YOUR CODE WILL START HERE.
		//       Please write some code to evolve this population.
		//       (For tournament selection, you will need to call Controller.doBattleNoGui(agent1, agent2).)


		// Return an arbitrary member from the population
		return population.row(0);
	}


	public static void main(String[] args) throws Exception {
		double[] w = evolveWeights();
		Controller.doBattle(new ReflexAgent(), new NeuralAgent(w));

	}

}
