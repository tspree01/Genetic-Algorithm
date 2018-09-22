import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

class Game {
	private static int numberOfTourements = 100;
	static Random rand = new Random();
	//Individual individual = new Individual(chromosome,0);
	//private static double mutationRate = 0.02 * rand.nextGaussian();
	private static double adverageDeviation = 0.02;
	private static double winnerSurviveRate = 0.90;
	private static double crossoverRate = 0.8;
	static double[] chad = new double[291];

	// Marshal mario into a json object
	static Json toJson(double chromosome) {
		Json ob = Json.newObject();
		ob.add("chromosome", chromosome);

		return ob;
	}

	static double[] evolveWeights() throws Exception {
		// Create a random initial population
		Random r = new Random();
		Matrix p = new Matrix(100, 291);
		FitnessComparator fitnessComp = new FitnessComparator();
		WeightComparator weightComp = new WeightComparator();
		ArrayList<Individual> population = new ArrayList<>();
		ArrayList<Individual> chads = new ArrayList<>();

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
		for (int i = 0; i < 1000; i++) {
			//calculate the fitness of each chromosome in the population
			// either do tourement or do mutate

			naturalSelection(population);
			mutate(population);
			population.sort(fitnessComp);
			System.out.println(population.get(0).fitness);
		}
		population.sort(fitnessComp);
		for (int i = 0; i < population.size(); i++) {
			if (population.get(i).win == true) {
				chads.add(population.get(i));
			}
			else {

			}
		}

		//chads.sort(fitnessComp);
		if (! chads.isEmpty()) {
			chad = chads.get(0).chromosome;
			return chads.get(0).chromosome;
		}
		return population.get(0).chromosome;
	}

/*	static void calculatePopulationFitness(ArrayList<Individual> population) throws Exception {
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
				//System.out.println("I had a loser");
				population.get(j).fitness = (after - before);
				population.get(j).win = false;
				population.get(j).tie = false;
			}
			else if (winner == - 1) {
				System.out.println("I had a winner");
				population.get(j).fitness = (10000 + 10000 / (after - before));
				population.get(j).win = true;
				population.get(j).tie = false;
			}
			else if (winner == 0) {
				//System.out.println("I had a tied");
				population.get(j).fitness = (after - before);
				population.get(j).tie = true;
				population.get(j).win = false;
			}
		}
		population.sort(fitnessComp);
	}*/
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
		Individual parent2 = selectParents(population, winnerOfFight);
		int percentOfParent1 = (r.nextInt(winnerOfFight.chromosome.length));
		int percentOfParent2 = (r.nextInt(winnerOfFight.chromosome.length - percentOfParent1));

		for (int i = 0; i < offspring.chromosome.length; i++) {
			double randomPercent = r.nextDouble();
			if (randomPercent < crossoverRate) {
				offspring.chromosome[i] = winnerOfFight.chromosome[i];
			}
			else if (randomPercent > crossoverRate) {
				offspring.chromosome[i] = parent2.chromosome[i];
			}
		}
/*		for (int i = 0; i < offspring.chromosome.length; i++) {
			double randomPercent = r.nextDouble();
			if (i < percentOfParent1) {
				offspring.chromosome[i] = winnerOfFight.chromosome[i];
			}
			else if (i > percentOfParent1) {
				offspring.chromosome[i] = parent2.chromosome[i];
			}
		}*/
		population.add(offspring);
	}

	static void naturalSelection(ArrayList<Individual> population) throws Exception {
		Random r = new Random();
		double randomPercent = r.nextDouble();
		long winner = 0;

		int randomParent1 = r.nextInt(population.size());
		int randomParent2 = r.nextInt(population.size());
		Individual redAgent1 = population.get(randomParent1);
		Individual redAgent2 = population.get(randomParent2);

		//long timeBeforeAgent1 = System.currentTimeMillis();
		winner = Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent(redAgent1.chromosome));
		//long timeAfterAgent1 = System.currentTimeMillis();

		// If the Reflex agent wins then calculate the fitness of neural agent but if the neural agent wins it get a bonus for winning
		if (winner > 0) {
			//System.out.println("I had a loser");
			redAgent1.fitness = winner;
			redAgent1.win = false;
			redAgent1.tie = false;
		}
		else if (winner < 0) {
			//System.out.println("I had a winner");

			redAgent1.fitness = (10000 + (10000 / winner * - 1));
			redAgent1.win = true;
			redAgent1.tie = false;
		}
		else {
			//System.out.println("I had a tied");
			redAgent1.fitness = winner;
			redAgent1.tie = true;
			redAgent1.win = false;
		}

		//long timeBeforeAgent2 = System.currentTimeMillis();
		winner = Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent(redAgent2.chromosome));
		//long timeAfterAgent2 = System.currentTimeMillis();

		// If the Reflex agent wins then calculate the fitness of neural agent but if the neural agent wins it get a bonus for winning
		if (winner > 0) {
			//System.out.println("I had a loser");
			redAgent2.fitness = winner;
			redAgent2.win = false;
			redAgent2.tie = false;
		}
		else if (winner < 0) {
			//System.out.println("I had a winner");

			redAgent2.fitness = (10000 + (10000 / winner * - 1));
			redAgent2.win = true;
			redAgent2.tie = false;
		}
		else {
			//System.out.println("I had a tied");
			redAgent2.fitness = winner;
			redAgent2.tie = true;
			redAgent2.win = false;
		}

		if (redAgent1.fitness > redAgent2.fitness && winnerSurviveRate > randomPercent) {
			population.remove(redAgent2);
			crossover(population, redAgent1);
		}
		else {
			population.remove(redAgent1);
			crossover(population, redAgent2);
		}
	}

	static Individual selectParents(ArrayList<Individual> population, Individual winnerOfFight) {
		Random r = new Random();
		Individual parent2;
		WeightComparator weightComp = new WeightComparator();
		//ArrayList<Individual> parents = new ArrayList<>();

		double sumOfParentWeight = 0.0;

		//check to see if the parent is close in fitness to the first parent picked
		double lowest_weight_diff = 9999999;
		Individual lowest_parent = null;
		for (int i = 0; i < 5; i++) {
			parent2 = population.get(r.nextInt(population.size()));
			for (int j = 0; j < parent2.chromosome.length; j++) {
				sumOfParentWeight += Math.abs((winnerOfFight.chromosome[j] - parent2.chromosome[j]));
			}
			if (sumOfParentWeight < lowest_weight_diff || lowest_parent == null) {
				lowest_weight_diff = sumOfParentWeight;
				lowest_parent = parent2;
			}
		}
		return lowest_parent;
	}

	static double getChad() {
		double[] chadsChromosomes = new double[291];
		chadsChromosomes[0] = 0.30915799863218185;
		chadsChromosomes[0] = - 0.023665377318833405;
		chadsChromosomes[1] = - 6.412417139925245E-4;
		chadsChromosomes[2] = 0.003545709647539246;
		chadsChromosomes[3] = 0.06712930879437848;
		chadsChromosomes[4] = - 0.08238858381754569;
		chadsChromosomes[5] = 0.1063709065438231;
		chadsChromosomes[6] = 0.015089951183553323;
		chadsChromosomes[7] = 0.025837220108388226;
		chadsChromosomes[8] = 0.05618566168201028;
		chadsChromosomes[9] = 0.1120361110241865;
		chadsChromosomes[10] = 0.1322064820165173;
		chadsChromosomes[11] = 0.1187072161294761;
		chadsChromosomes[12] = - 0.04861069973200256;
		chadsChromosomes[13] = 0.24671598606136128;
		chadsChromosomes[14] = - 0.008747248705595024;
		chadsChromosomes[15] = - 0.03690971374366226;
		chadsChromosomes[16] = 0.011159121736912244;
		chadsChromosomes[17] = 0.006710086290130475;
		chadsChromosomes[18] = 0.0717414481619273;
		chadsChromosomes[19] = 0.135812209831837;
		chadsChromosomes[20] = - 0.14818829102294023;
		chadsChromosomes[21] = 0.02424488437180153;
		chadsChromosomes[22] = - 0.15073833266475248;
		chadsChromosomes[23] = 0.04173647019312072;
		chadsChromosomes[24] = - 0.17743242561531197;
		chadsChromosomes[25] = - 9.352377464087666E-4;
		chadsChromosomes[26] = 0.13970795172258055;
		chadsChromosomes[27] = - 0.08436357957690599;
		chadsChromosomes[28] = - 0.04707111023428768;
		chadsChromosomes[29] = - 0.17845625682967226;
		chadsChromosomes[30] = 0.13501147244991019;
		chadsChromosomes[31] = - 0.18591261267171072;
		chadsChromosomes[32] = 0.2085591917123752;
		chadsChromosomes[33] = - 0.03467047437913299;
		chadsChromosomes[34] = 0.02566774985627412;
		chadsChromosomes[35] = - 0.12573799120130932;
		chadsChromosomes[36] = 1.940617546545599E-4;
		chadsChromosomes[37] = 0.09027346352668968;
		chadsChromosomes[38] = 0.05105605716735092;
		chadsChromosomes[39] = 0.04894069984220424;
		chadsChromosomes[40] = 0.09790983270283073;
		chadsChromosomes[41] = - 0.08336084029511705;
		chadsChromosomes[42] = - 1.340952506494979E-4;
		chadsChromosomes[43] = - 0.15741535322824873;
		chadsChromosomes[44] = 0.03718507502485617;
		chadsChromosomes[45] = - 0.03414756409714954;
		chadsChromosomes[46] = - 0.026011116258292485;
		chadsChromosomes[47] = 0.09669279541065703;
		chadsChromosomes[48] = - 0.007412408129757064;
		chadsChromosomes[49] = - 0.0336800954546824;
		chadsChromosomes[50] = 0.21327839623594796;
		chadsChromosomes[51] = - 0.013242568093442946;
		chadsChromosomes[52] = 0.08758217077987356;
		chadsChromosomes[53] = - 0.0023635792255386236;
		chadsChromosomes[54] = 0.11458844233999961;
		chadsChromosomes[55] = - 0.15238344907701862;
		chadsChromosomes[56] = 0.12856581103080636;
		chadsChromosomes[57] = - 0.012390657467151978;
		chadsChromosomes[58] = 0.12934212193225783;
		chadsChromosomes[59] = 0.0640364995052193;
		chadsChromosomes[60] = - 0.0788017493306274;
		chadsChromosomes[61] = 0.00394207587821608;
		chadsChromosomes[62] = - 0.13893829847900874;
		chadsChromosomes[63] = - 0.09431591288655625;
		chadsChromosomes[64] = 0.13256173996532647;
		chadsChromosomes[65] = - 0.07101516347308001;
		chadsChromosomes[66] = 0.1133915103932099;
		chadsChromosomes[67] = - 0.15021192292244084;
		chadsChromosomes[68] = 0.03397414204036628;
		chadsChromosomes[69] = 0.024307016356614006;
		chadsChromosomes[70] = 0.043762318470383685;
		chadsChromosomes[71] = - 0.021698384352078992;
		chadsChromosomes[72] = - 0.06670558532534175;
		chadsChromosomes[73] = - 0.00662454823806372;
		chadsChromosomes[74] = - 0.17600852842648046;
		chadsChromosomes[75] = 0.09023720873246538;
		chadsChromosomes[76] = - 0.07541763476288521;
		chadsChromosomes[77] = - 4.3422815885772736E-4;
		chadsChromosomes[78] = 0.05479841218044221;
		chadsChromosomes[79] = - 0.10055476106495609;
		chadsChromosomes[80] = 0.09681215515698752;
		chadsChromosomes[81] = - 0.21552050137337642;
		chadsChromosomes[82] = 0.008533155788019763;
		chadsChromosomes[83] = 0.10460199737873195;
		chadsChromosomes[84] = - 0.12189702912949638;
		chadsChromosomes[85] = 0.006011706156451583;
		chadsChromosomes[86] = 0.028453057187215587;
		chadsChromosomes[87] = - 0.17183741495400418;
		chadsChromosomes[88] = 0.03506259174236026;
		chadsChromosomes[89] = 0.16192196235693238;
		chadsChromosomes[90] = - 0.02518810168994673;
		chadsChromosomes[91] = - 0.001030939775119144;
		chadsChromosomes[92] = 0.15112722478859808;
		chadsChromosomes[93] = 0.06220971508876833;
		chadsChromosomes[94] = - 0.23846372182102793;
		chadsChromosomes[95] = - 0.053493561209455205;
		chadsChromosomes[96] = - 0.19659129221155014;
		chadsChromosomes[97] = - 0.15921153844064218;
		chadsChromosomes[98] = - 0.01700390167521792;
		chadsChromosomes[99] = - 0.03540756434696837;
		chadsChromosomes[100] = - 0.15974636331660277;
		chadsChromosomes[101] = - 0.18305120276405562;
		chadsChromosomes[102] = 0.08150997116002834;
		chadsChromosomes[103] = 0.04862240832076246;
		chadsChromosomes[104] = - 0.018296065324158454;
		chadsChromosomes[105] = - 0.027433739033749686;
		chadsChromosomes[106] = - 0.08073317574471525;
		chadsChromosomes[107] = 0.01930092756189373;
		chadsChromosomes[108] = - 0.11634906629181418;
		chadsChromosomes[109] = - 0.01370700843091366;
		chadsChromosomes[110] = - 0.09330588509718615;
		chadsChromosomes[111] = 0.06868715552914123;
		chadsChromosomes[112] = - 0.08446599817567138;
		chadsChromosomes[113] = - 0.09847707211700826;
		chadsChromosomes[114] = 0.05246689993654585;
		chadsChromosomes[115] = - 0.028043607380350735;
		chadsChromosomes[116] = 0.026279110290191;
		chadsChromosomes[117] = 0.0010801448401791837;
		chadsChromosomes[118] = - 0.07978548821375495;
		chadsChromosomes[119] = 0.08346701928755706;
		chadsChromosomes[120] = 0.016254325859909956;
		chadsChromosomes[121] = - 0.020077606976367172;
		chadsChromosomes[122] = 0.02928704183533974;
		chadsChromosomes[123] = - 0.07539061007279285;
		chadsChromosomes[124] = 0.009755006551335633;
		chadsChromosomes[125] = - 0.18257341416494793;
		chadsChromosomes[126] = - 0.04685248943005993;
		chadsChromosomes[127] = 0.11019440296762432;
		chadsChromosomes[128] = - 0.03487729385574844;
		chadsChromosomes[129] = 0.00430671601755057;
		chadsChromosomes[130] = - 0.02647929530236645;
		chadsChromosomes[131] = 0.02949373820704222;
		chadsChromosomes[132] = 0.021497308153774007;
		chadsChromosomes[133] = 0.10126088047902532;
		chadsChromosomes[134] = - 0.027298167865215404;
		chadsChromosomes[135] = - 0.00507987973371242;
		chadsChromosomes[136] = 0.09881905084278586;
		chadsChromosomes[137] = - 0.09681722209431137;
		chadsChromosomes[138] = - 0.1219784771185357;
		chadsChromosomes[139] = - 0.09371036150152452;
		chadsChromosomes[140] = 0.014015238335549442;
		chadsChromosomes[141] = 0.060339040155029305;
		chadsChromosomes[142] = 0.032945939989779065;
		chadsChromosomes[143] = 0.04495974906768996;
		chadsChromosomes[144] = 0.016987460707963562;
		chadsChromosomes[145] = - 0.05740577588144441;
		chadsChromosomes[146] = 0.022482169172883426;
		chadsChromosomes[147] = 0.09042428429284843;
		chadsChromosomes[148] = - 0.010165937034930456;
		chadsChromosomes[149] = - 0.002042573775608904;
		chadsChromosomes[150] = - 0.029691162036476117;
		chadsChromosomes[151] = - 0.06837571509553522;
		chadsChromosomes[152] = - 0.09509737011579515;
		chadsChromosomes[153] = - 0.03212883107693246;
		chadsChromosomes[154] = 0.165220639791407;
		chadsChromosomes[155] = - 0.0589210604944981;
		chadsChromosomes[156] = 0.041838935521722326;
		chadsChromosomes[157] = - 0.14109254256354206;
		chadsChromosomes[158] = 0.04145071347036784;
		chadsChromosomes[159] = 0.07971711210674831;
		chadsChromosomes[160] = 0.1108885767516989;
		chadsChromosomes[161] = 0.09192526713589645;
		chadsChromosomes[162] = 0.037673758699755096;
		chadsChromosomes[163] = - 0.06193944942696321;
		chadsChromosomes[164] = 0.06005189783396497;
		chadsChromosomes[165] = 0.07966340142115771;
		chadsChromosomes[166] = - 0.10048392662458244;
		chadsChromosomes[167] = - 0.02924985469061632;
		chadsChromosomes[168] = 0.11292427224533211;
		chadsChromosomes[169] = 0.002502263520398212;
		chadsChromosomes[170] = 0.030426149369121785;
		chadsChromosomes[171] = 0.07832126410749864;
		chadsChromosomes[172] = 0.07549687461227929;
		chadsChromosomes[173] = - 0.08248377803711729;
		chadsChromosomes[174] = 0.002033473699321616;
		chadsChromosomes[175] = - 0.2052290987654636;
		chadsChromosomes[176] = - 0.2739982862487792;
		chadsChromosomes[177] = 0.012667744313234456;
		chadsChromosomes[178] = 0.08722133730594335;
		chadsChromosomes[179] = 0.07790529243379521;
		chadsChromosomes[180] = 0.09845419653030689;
		chadsChromosomes[181] = 0.08825646553266879;
		chadsChromosomes[182] = - 0.05947408538313571;
		chadsChromosomes[183] = 0.05208297011381963;
		chadsChromosomes[184] = - 0.10056481145037963;
		chadsChromosomes[185] = - 0.029524691106006844;
		chadsChromosomes[186] = 0.06564219575466744;
		chadsChromosomes[187] = - 0.008920216300009519;
		chadsChromosomes[188] = 0.067387556580839;
		chadsChromosomes[189] = 0.014988523770740938;
		chadsChromosomes[190] = - 0.03987966728314959;
		chadsChromosomes[191] = - 0.07959114534906316;
		chadsChromosomes[192] = 0.0134026848543491;
		chadsChromosomes[193] = - 0.10744306110974611;
		chadsChromosomes[194] = 0.007872424178297695;
		chadsChromosomes[195] = 0.03754212905808522;
		chadsChromosomes[196] = 0.0011116951908731658;
		chadsChromosomes[197] = 0.08619265792058561;
		chadsChromosomes[198] = 0.09102808675637164;
		chadsChromosomes[199] = - 0.02707821809784724;
		chadsChromosomes[200] = - 0.0896907940819234;
		chadsChromosomes[201] = - 0.0013734806426404494;
		chadsChromosomes[202] = 0.016141633275972924;
		chadsChromosomes[203] = 0.16218315502720118;
		chadsChromosomes[204] = 0.03201658078048501;
		chadsChromosomes[205] = - 0.016875256304805276;
		chadsChromosomes[206] = - 0.02988871839872084;
		chadsChromosomes[207] = - 0.05332897658981603;
		chadsChromosomes[208] = 0.021204176189354884;
		chadsChromosomes[209] = - 0.021970383399290933;
		chadsChromosomes[210] = - 0.06518036425804669;
		chadsChromosomes[211] = 0.07388782969843008;
		chadsChromosomes[212] = 0.0654241814194243;
		chadsChromosomes[213] = 0.09001038599449292;
		chadsChromosomes[214] = 0.05325093089888146;
		chadsChromosomes[215] = - 0.1449004041788359;
		chadsChromosomes[216] = 0.052633599580119074;
		chadsChromosomes[217] = - 0.11217103647791785;
		chadsChromosomes[218] = 0.013110642305748084;
		chadsChromosomes[219] = - 0.030435436528091546;
		chadsChromosomes[220] = - 0.10113338029754061;
		chadsChromosomes[221] = - 0.097620064285389;
		chadsChromosomes[222] = 0.03815838349440649;
		chadsChromosomes[223] = 0.009401979796101105;
		chadsChromosomes[224] = - 0.07360354823881358;
		chadsChromosomes[225] = 0.10478422521934064;
		chadsChromosomes[226] = - 0.14864139269181653;
		chadsChromosomes[227] = - 0.002504103326025839;
		chadsChromosomes[228] = 0.027290023293256672;
		chadsChromosomes[229] = 0.005828997998006597;
		chadsChromosomes[230] = 0.12520959274165844;
		chadsChromosomes[231] = - 0.10523217309906063;
		chadsChromosomes[232] = 0.029607698331705443;
		chadsChromosomes[233] = - 0.03654857425236517;
		chadsChromosomes[234] = 0.016854505490357215;
		chadsChromosomes[235] = - 0.07148740523544501;
		chadsChromosomes[236] = 0.15180344148055838;
		chadsChromosomes[237] = 0.006405658571300419;
		chadsChromosomes[238] = 0.1877599331477982;
		chadsChromosomes[239] = - 0.006042608163259225;
		chadsChromosomes[240] = - 0.02212078677975827;
		chadsChromosomes[241] = - 0.0122271416778017;
		chadsChromosomes[242] = - 0.18777275095399615;
		chadsChromosomes[243] = - 0.10700902146530221;
		chadsChromosomes[244] = 0.05848211482745552;
		chadsChromosomes[245] = 0.006502410041429316;
		chadsChromosomes[246] = - 0.0708669725039548;
		chadsChromosomes[247] = - 0.018748656502834228;
		chadsChromosomes[248] = 0.2284402758166241;
		chadsChromosomes[249] = - 0.0381946823984318;
		chadsChromosomes[250] = 0.039799155045910244;
		chadsChromosomes[251] = 0.14225585958964015;
		chadsChromosomes[252] = - 0.07596775661738779;
		chadsChromosomes[253] = - 0.013610020499154232;
		chadsChromosomes[254] = 0.0184158504301365;
		chadsChromosomes[255] = 0.060389529275263386;
		chadsChromosomes[256] = - 0.013374850161755915;
		chadsChromosomes[257] = 0.029182644072573014;
		chadsChromosomes[258] = 0.14854846177876904;
		chadsChromosomes[259] = - 0.13703740149974183;
		chadsChromosomes[260] = 0.08180844990028842;
		chadsChromosomes[261] = - 0.027370625302252156;
		chadsChromosomes[262] = - 0.10925916318896435;
		chadsChromosomes[263] = 7.712772189067474E-4;
		chadsChromosomes[264] = - 0.23549462494734116;
		chadsChromosomes[265] = 0.15203724281428305;
		chadsChromosomes[266] = 0.12421687719225238;
		chadsChromosomes[267] = - 0.186733687017053;
		chadsChromosomes[268] = - 0.03178267987265151;
		chadsChromosomes[269] = 0.020571028573734104;
		chadsChromosomes[270] = 0.04200790929471053;
		chadsChromosomes[271] = 0.08675790852605031;
		chadsChromosomes[272] = - 0.10425327211024005;
		chadsChromosomes[273] = 0.06892534794085611;
		chadsChromosomes[274] = 0.0856995077760962;
		chadsChromosomes[275] = - 0.06887236263320119;
		chadsChromosomes[276] = 0.16869429389526197;
		chadsChromosomes[277] = 0.08260721780159455;
		chadsChromosomes[278] = 0.07511176911176083;
		chadsChromosomes[279] = 0.03937694456570634;
		chadsChromosomes[280] = - 0.10349440836667755;
		chadsChromosomes[281] = 0.020239040999149055;
		chadsChromosomes[282] = - 0.09359848544313379;
		chadsChromosomes[283] = 0.14482644718323326;
		chadsChromosomes[284] = - 0.07739066852031486;
		chadsChromosomes[285] = 0.11192629668724255;
		chadsChromosomes[286] = 0.011449458391284311;
		chadsChromosomes[287] = 0.010316396952270844;
		chadsChromosomes[288] = - 0.10691591417026317;
		chadsChromosomes[289] = - 0.14968021394199316;
	}

	static void mutate(ArrayList<Individual> population) {
		Random r = new Random();
		Individual individual;
		double mutationRate = 0.2;
		for (int i = 0; i < population.size(); i++) {
			//int test = r.nextInt(population.get(i).chromosome.length);
			double randomPercent = r.nextDouble();
			if (mutationRate > randomPercent) {
				population.get(i).chromosome[r.nextInt(population.get(i).chromosome.length)] += (adverageDeviation * r.nextGaussian());
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
		if (indiv1.win) { return 1; }
		else if (indiv2.win) { return - 1; }
		return 0;
	}

}