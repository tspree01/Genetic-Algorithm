import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;

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

		for (int i = 0; i < 100; i++) {
			double[] chromosome = population.row(i);

			for (int j = 0; j < chromosome.length; j++) {
				chromosome[j] = 0.03 * r.nextGaussian();
			}
		}

		for (int i = 0; i < 500 ; i++) {
			//calculate the fitness of each chromosome in the population
			long before = System.currentTimeMillis();
			winner = Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent(population.row(i)));
			long after = System.currentTimeMillis();
			System.out.println("how long it took to do the tournament: " + (after - before) );
		}
		//Each element in m_data is a row or a chromosome

		//call setWeights when you do the tournament


		// Evolve the population

		//       Please write some code to evolve this population.

		//       (For tournament selection, you will need to call Controller.doBattleNoGui(agent1, agent2).)


		// Return an arbitrary member from the population
		return population.row(0);
	}

	static void mutate(Matrix population){


	}


	public static void main(String[] args) throws Exception {
		double[] w = evolveWeights();
		Controller.doBattle(new ReflexAgent(), new NeuralAgent(w));

	}

}
