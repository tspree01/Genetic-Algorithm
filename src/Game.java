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

	static double[] getChad() {
		double[] chadsChromosomes = new double[291];
		chadsChromosomes[0] = 0.05463477108158558;
		chadsChromosomes[1] = 0.009039581922350584;
		chadsChromosomes[2] = 0.16862561706318818;
		chadsChromosomes[3] = 0.12968885079474557;
		chadsChromosomes[4] = - 0.11011104424027843;
		chadsChromosomes[5] = 0.013092121322881768;
		chadsChromosomes[6] = - 0.04209967316770259;
		chadsChromosomes[7] = - 0.033690459312693696;
		chadsChromosomes[8] = - 0.09144635312741567;
		chadsChromosomes[9] = - 0.15881071527368554;
		chadsChromosomes[10] = - 0.10176690646235731;
		chadsChromosomes[11] = 0.0033289429861703777;
		chadsChromosomes[12] = - 0.1523706885879325;
		chadsChromosomes[13] = 0.013194700705900328;
		chadsChromosomes[14] = 0.10764872017600138;
		chadsChromosomes[15] = - 0.07828483822716101;
		chadsChromosomes[16] = - 0.020309107669595008;
		chadsChromosomes[17] = - 0.08775783818838194;
		chadsChromosomes[18] = - 0.18896487446764842;
		chadsChromosomes[19] = - 0.17854708674230324;
		chadsChromosomes[20] = - 0.11554782128225385;
		chadsChromosomes[21] = 0.0930959142912299;
		chadsChromosomes[22] = - 0.05330413160074948;
		chadsChromosomes[23] = - 0.010459998964574977;
		chadsChromosomes[24] = 0.027930205739553817;
		chadsChromosomes[25] = 0.07202539448361694;
		chadsChromosomes[26] = - 0.11374583036744716;
		chadsChromosomes[27] = 0.053212232578621864;
		chadsChromosomes[28] = 0.09181337574803657;
		chadsChromosomes[29] = - 0.059066868166586635;
		chadsChromosomes[30] = 0.12443829289692004;
		chadsChromosomes[31] = - 0.1652709625921017;
		chadsChromosomes[32] = - 0.04055901768496474;
		chadsChromosomes[33] = - 0.0556371206177585;
		chadsChromosomes[34] = - 0.009472254815781945;
		chadsChromosomes[35] = - 0.08768190106790645;
		chadsChromosomes[36] = 0.011502542532014833;
		chadsChromosomes[37] = 0.015090607983786279;
		chadsChromosomes[38] = - 0.03520735591118717;
		chadsChromosomes[39] = 0.042685474001443395;
		chadsChromosomes[40] = 0.043175851360020064;
		chadsChromosomes[41] = 0.03379003848298982;
		chadsChromosomes[42] = - 0.020320211576912748;
		chadsChromosomes[43] = 0.11469352458200287;
		chadsChromosomes[44] = - 0.016187519049300728;
		chadsChromosomes[45] = 0.06042423904117513;
		chadsChromosomes[46] = 0.012571210866512191;
		chadsChromosomes[47] = - 0.056180375186644184;
		chadsChromosomes[48] = 0.03544350246283932;
		chadsChromosomes[49] = 0.062387187980554384;
		chadsChromosomes[50] = 0.02838239674145307;
		chadsChromosomes[51] = - 0.11358685470468152;
		chadsChromosomes[52] = 0.060578979441306906;
		chadsChromosomes[53] = 0.07018267568753946;
		chadsChromosomes[54] = - 0.053814775054096875;
		chadsChromosomes[55] = - 0.02974902711709836;
		chadsChromosomes[56] = 0.02761671959542084;
		chadsChromosomes[57] = - 0.038905797644459346;
		chadsChromosomes[58] = - 0.04500645146113211;
		chadsChromosomes[59] = - 0.009760474155267429;
		chadsChromosomes[60] = - 0.044332574269303864;
		chadsChromosomes[61] = - 0.08506848060160856;
		chadsChromosomes[62] = - 0.011863881850073808;
		chadsChromosomes[63] = - 0.052928436172265086;
		chadsChromosomes[64] = 0.13360444136079652;
		chadsChromosomes[65] = 0.022210988149170625;
		chadsChromosomes[66] = 0.18235394216307385;
		chadsChromosomes[67] = - 0.06405833284559023;
		chadsChromosomes[68] = 0.07703021231756169;
		chadsChromosomes[69] = - 0.05836521374089135;
		chadsChromosomes[70] = 0.17826080361340066;
		chadsChromosomes[71] = 0.025205277592216333;
		chadsChromosomes[72] = - 0.0749681394666254;
		chadsChromosomes[73] = - 0.0462236634229715;
		chadsChromosomes[74] = 0.049230691180403505;
		chadsChromosomes[75] = - 0.06594810544444404;
		chadsChromosomes[76] = 0.19294608715580913;
		chadsChromosomes[77] = - 0.11924434482875285;
		chadsChromosomes[78] = - 0.10399634052402426;
		chadsChromosomes[79] = - 0.010291089914905818;
		chadsChromosomes[80] = 0.06414090136279839;
		chadsChromosomes[81] = 0.026041205312095226;
		chadsChromosomes[82] = 0.08303267349251918;
		chadsChromosomes[83] = - 0.18577020799838834;
		chadsChromosomes[84] = - 0.1851002624348679;
		chadsChromosomes[85] = - 0.12521523521728142;
		chadsChromosomes[86] = 0.020162219236974907;
		chadsChromosomes[87] = - 0.15588364750560552;
		chadsChromosomes[88] = 0.1792479557177529;
		chadsChromosomes[89] = - 0.0831249815542643;
		chadsChromosomes[90] = 0.13065632044451625;
		chadsChromosomes[91] = 0.18831972619871534;
		chadsChromosomes[92] = - 0.07286694278171857;
		chadsChromosomes[93] = 0.14389888562166206;
		chadsChromosomes[94] = 0.12445734186344343;
		chadsChromosomes[95] = - 0.07326366101606306;
		chadsChromosomes[96] = 0.02941686890890121;
		chadsChromosomes[97] = 0.030635205488452565;
		chadsChromosomes[98] = 0.03992019842892152;
		chadsChromosomes[99] = 0.0023479005116715523;
		chadsChromosomes[100] = 0.04204344572113148;
		chadsChromosomes[101] = 0.030378507042452266;
		chadsChromosomes[102] = - 0.01971427582657406;
		chadsChromosomes[103] = - 0.014163940447896406;
		chadsChromosomes[104] = - 0.15991650629732926;
		chadsChromosomes[105] = - 0.02600137114583792;
		chadsChromosomes[106] = 0.1372888485502984;
		chadsChromosomes[107] = 0.008896371376956421;
		chadsChromosomes[108] = - 0.0533616108768463;
		chadsChromosomes[109] = - 0.08838533278787315;
		chadsChromosomes[110] = 0.026908278483382852;
		chadsChromosomes[111] = 0.21466470524746378;
		chadsChromosomes[112] = 0.052780485279235415;
		chadsChromosomes[113] = - 0.07152965709510088;
		chadsChromosomes[114] = - 0.0031182272678009926;
		chadsChromosomes[115] = - 0.07728924732896467;
		chadsChromosomes[116] = 0.08096459184745217;
		chadsChromosomes[117] = 0.08501831397777096;
		chadsChromosomes[118] = - 0.05533319954386387;
		chadsChromosomes[119] = - 0.023131347825081958;
		chadsChromosomes[120] = - 0.02675670399283202;
		chadsChromosomes[121] = - 0.04504891436281822;
		chadsChromosomes[122] = 0.04859491958813823;
		chadsChromosomes[123] = - 0.017134691484485237;
		chadsChromosomes[124] = 0.09664987783980616;
		chadsChromosomes[125] = - 0.21198831115295408;
		chadsChromosomes[126] = - 0.11949265183399838;
		chadsChromosomes[127] = 0.02692492176021463;
		chadsChromosomes[128] = 0.06438442858774482;
		chadsChromosomes[129] = - 0.08610490036416343;
		chadsChromosomes[130] = - 5.284778210018108E-4;
		chadsChromosomes[131] = 0.017575990384700733;
		chadsChromosomes[132] = - 0.08176897389260636;
		chadsChromosomes[133] = - 0.01569270016773745;
		chadsChromosomes[134] = - 0.03592583917897824;
		chadsChromosomes[135] = - 0.003699198923951789;
		chadsChromosomes[136] = 0.053437064118817636;
		chadsChromosomes[137] = 0.02240489674991426;
		chadsChromosomes[138] = 0.02372132747230145;
		chadsChromosomes[139] = 0.04739897278266543;
		chadsChromosomes[140] = - 0.1985721002076134;
		chadsChromosomes[141] = - 0.042951310757205596;
		chadsChromosomes[142] = - 0.2432483530685249;
		chadsChromosomes[143] = 0.0338433454979622;
		chadsChromosomes[144] = 0.04794808160600596;
		chadsChromosomes[145] = - 0.05128607898007509;
		chadsChromosomes[146] = - 0.1006216059549265;
		chadsChromosomes[147] = - 0.030125228561875956;
		chadsChromosomes[148] = - 0.09847600213782856;
		chadsChromosomes[149] = 0.07071836046052409;
		chadsChromosomes[150] = - 0.03532765904784637;
		chadsChromosomes[151] = 0.1689490470321644;
		chadsChromosomes[152] = - 0.07335985466785934;
		chadsChromosomes[153] = 0.1406886489225376;
		chadsChromosomes[154] = - 0.23485709536100563;
		chadsChromosomes[155] = - 0.04409537120747267;
		chadsChromosomes[156] = - 0.2580176727527996;
		chadsChromosomes[157] = - 0.00945808608368739;
		chadsChromosomes[158] = - 0.18961350184682715;
		chadsChromosomes[159] = 0.0648216843356344;
		chadsChromosomes[160] = - 0.034745783636739754;
		chadsChromosomes[161] = 0.08930696723031291;
		chadsChromosomes[162] = 0.030761476147559808;
		chadsChromosomes[163] = - 0.014444237691569408;
		chadsChromosomes[164] = 0.05194405826192606;
		chadsChromosomes[165] = - 0.19340134685106877;
		chadsChromosomes[166] = 0.048031927582149685;
		chadsChromosomes[167] = 0.0726634065712817;
		chadsChromosomes[168] = 0.0641381547694493;
		chadsChromosomes[169] = - 0.06364545434086746;
		chadsChromosomes[170] = 0.06100961825541137;
		chadsChromosomes[171] = - 0.01895989368209688;
		chadsChromosomes[172] = 0.013540309038488135;
		chadsChromosomes[173] = - 0.03957876953497163;
		chadsChromosomes[174] = 0.13925166026477398;
		chadsChromosomes[175] = 0.0634260863226427;
		chadsChromosomes[176] = 0.11822125176224753;
		chadsChromosomes[177] = 0.10299227265879404;
		chadsChromosomes[178] = 0.14683172772626712;
		chadsChromosomes[179] = 0.08960871878340465;
		chadsChromosomes[180] = 0.03949393152812525;
		chadsChromosomes[181] = - 0.17620824249255854;
		chadsChromosomes[182] = - 6.465337321513269E-4;
		chadsChromosomes[183] = 0.064158782267017;
		chadsChromosomes[184] = 0.08227621486134959;
		chadsChromosomes[185] = 0.11857234282577687;
		chadsChromosomes[186] = - 0.22464039359431218;
		chadsChromosomes[187] = 0.08684549710459906;
		chadsChromosomes[188] = - 0.13667174746444202;
		chadsChromosomes[189] = - 0.036245353737124064;
		chadsChromosomes[190] = 0.11636606280303141;
		chadsChromosomes[191] = 0.03128757677773581;
		chadsChromosomes[192] = 0.06800749916925684;
		chadsChromosomes[193] = - 0.02100774890260753;
		chadsChromosomes[194] = 0.14279914155573714;
		chadsChromosomes[195] = 0.13171734107932906;
		chadsChromosomes[196] = 0.057147033380148614;
		chadsChromosomes[197] = 0.11222742229716907;
		chadsChromosomes[198] = 0.2133687774499231;
		chadsChromosomes[199] = - 0.06724476433758653;
		chadsChromosomes[200] = - 0.022518132082502397;
		chadsChromosomes[201] = - 0.10280042090134775;
		chadsChromosomes[202] = - 0.08842856199280219;
		chadsChromosomes[203] = - 0.04175833057599977;
		chadsChromosomes[204] = 0.04782559211022556;
		chadsChromosomes[205] = - 0.029683497406812823;
		chadsChromosomes[206] = 0.15580852629471853;
		chadsChromosomes[207] = - 0.0753773132447775;
		chadsChromosomes[208] = - 0.008734882303767186;
		chadsChromosomes[209] = 0.04789560386465862;
		chadsChromosomes[210] = - 0.005986725665954763;
		chadsChromosomes[211] = 0.03301395767356807;
		chadsChromosomes[212] = - 0.07632009648499728;
		chadsChromosomes[213] = - 0.035284104925172276;
		chadsChromosomes[214] = 0.16788916727082015;
		chadsChromosomes[215] = 0.1317175957782334;
		chadsChromosomes[216] = - 0.05572200463141068;
		chadsChromosomes[217] = 0.0834322871041796;
		chadsChromosomes[218] = - 0.03516348922821469;
		chadsChromosomes[219] = - 0.004841646016873461;
		chadsChromosomes[220] = 0.10659238621503016;
		chadsChromosomes[221] = - 0.1731046015697667;
		chadsChromosomes[222] = 0.024664912946881855;
		chadsChromosomes[223] = 0.08041536355345272;
		chadsChromosomes[224] = - 0.021142456543170856;
		chadsChromosomes[225] = - 0.062099750332013995;
		chadsChromosomes[226] = - 0.12712817195087564;
		chadsChromosomes[227] = 0.021579163247418508;
		chadsChromosomes[228] = - 0.056765011702117146;
		chadsChromosomes[229] = 0.024718143450667883;
		chadsChromosomes[230] = 0.046406462862928065;
		chadsChromosomes[231] = - 0.05263149363797437;
		chadsChromosomes[232] = 0.005223505080003073;
		chadsChromosomes[233] = - 0.011254022598609775;
		chadsChromosomes[234] = - 0.14586749225102974;
		chadsChromosomes[235] = - 0.009169324175798063;
		chadsChromosomes[236] = - 0.08033678509744341;
		chadsChromosomes[237] = 0.1845128263915903;
		chadsChromosomes[238] = - 0.001176603464721118;
		chadsChromosomes[239] = 0.04510734212128166;
		chadsChromosomes[240] = 0.007392256751769468;
		chadsChromosomes[241] = 0.04576280689254269;
		chadsChromosomes[242] = 0.09702316807618772;
		chadsChromosomes[243] = 0.06123697768708267;
		chadsChromosomes[244] = 0.030289765570718528;
		chadsChromosomes[245] = 0.07245801396148173;
		chadsChromosomes[246] = 0.0346611730994033;
		chadsChromosomes[247] = 0.08763656085876513;
		chadsChromosomes[248] = 0.05881300369391813;
		chadsChromosomes[249] = - 0.07305911718047957;
		chadsChromosomes[250] = 0.06374049040536599;
		chadsChromosomes[251] = - 0.06055455715433951;
		chadsChromosomes[252] = 0.04416129160285641;
		chadsChromosomes[253] = - 0.0641054330672871;
		chadsChromosomes[254] = 0.20101282502430484;
		chadsChromosomes[255] = 0.01195163007601216;
		chadsChromosomes[256] = - 0.03366708913002194;
		chadsChromosomes[257] = - 0.1812661916607215;
		chadsChromosomes[258] = 0.02467743702503586;
		chadsChromosomes[259] = 0.22649336180963284;
		chadsChromosomes[260] = 0.07231997529547614;
		chadsChromosomes[261] = - 0.14151935101099067;
		chadsChromosomes[262] = 0.08644132778579303;
		chadsChromosomes[263] = 0.05237220493622673;
		chadsChromosomes[264] = - 0.03442160249855097;
		chadsChromosomes[265] = 0.054346625888591356;
		chadsChromosomes[266] = - 0.18974726917834514;
		chadsChromosomes[267] = 0.057049546491407276;
		chadsChromosomes[268] = 0.06955398581718182;
		chadsChromosomes[269] = - 0.07732720723240812;
		chadsChromosomes[270] = 0.15211885857335852;
		chadsChromosomes[271] = 0.03355158783428656;
		chadsChromosomes[272] = 0.14014686923198513;
		chadsChromosomes[273] = 0.0957042005065317;
		chadsChromosomes[274] = 0.06922863571088124;
		chadsChromosomes[275] = - 0.13712888879142848;
		chadsChromosomes[276] = - 0.06280574582240692;
		chadsChromosomes[277] = 0.023115722833113465;
		chadsChromosomes[278] = - 0.1402686803139947;
		chadsChromosomes[279] = - 0.09212573976604667;
		chadsChromosomes[280] = 0.08024558204477825;
		chadsChromosomes[281] = 0.02967942577629053;
		chadsChromosomes[282] = - 0.05503204780467719;
		chadsChromosomes[283] = 0.06810870245337594;
		chadsChromosomes[284] = 0.030753741938785908;
		chadsChromosomes[285] = - 0.09460711230419558;
		chadsChromosomes[286] = 0.07145496443128198;
		chadsChromosomes[287] = - 0.09925247253950169;
		chadsChromosomes[288] = 0.13218044175473434;
		chadsChromosomes[289] = 0.23466354081260687;
		chadsChromosomes[290] = 0.0464524319739817;
		return chadsChromosomes;

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