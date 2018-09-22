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
		chadsChromosomes[0] = - 0.060971161859666716;
		chadsChromosomes[1] = - 0.023429248432510277;
		chadsChromosomes[2] = - 0.17515701820181884;
		chadsChromosomes[3] = 0.18664606768466221;
		chadsChromosomes[4] = 0.034462923900413084;
		chadsChromosomes[5] = - 0.0672693706587075;
		chadsChromosomes[6] = - 0.11572739323082049;
		chadsChromosomes[7] = 0.04504512350409741;
		chadsChromosomes[8] = 0.11698574840511543;
		chadsChromosomes[9] = 0.08461072082235643;
		chadsChromosomes[10] = - 0.00476369541807207;
		chadsChromosomes[11] = - 0.009278708318361887;
		chadsChromosomes[12] = - 0.021979469161168844;
		chadsChromosomes[13] = - 0.080995132976188;
		chadsChromosomes[14] = 0.024437263939777607;
		chadsChromosomes[15] = - 0.06759859117383658;
		chadsChromosomes[16] = 0.1048890050579671;
		chadsChromosomes[17] = - 0.020236281575301555;
		chadsChromosomes[18] = 0.10970064292696115;
		chadsChromosomes[19] = 0.0847059616267244;
		chadsChromosomes[20] = 0.02405499228473524;
		chadsChromosomes[21] = - 0.0836566386617387;
		chadsChromosomes[22] = 0.0814584026420585;
		chadsChromosomes[23] = - 0.12225020204477058;
		chadsChromosomes[24] = - 0.029827970742794085;
		chadsChromosomes[25] = - 0.0757180509752514;
		chadsChromosomes[26] = - 0.04788893087786079;
		chadsChromosomes[27] = - 0.07773246576704751;
		chadsChromosomes[28] = - 0.057452298896758645;
		chadsChromosomes[29] = - 6.307358853490562E-4;
		chadsChromosomes[30] = 0.003938061584498532;
		chadsChromosomes[31] = - 0.00432795507065901;
		chadsChromosomes[32] = - 0.06307517246665083;
		chadsChromosomes[33] = - 0.03105199440929554;
		chadsChromosomes[34] = 0.028367593000846777;
		chadsChromosomes[35] = - 0.08239681701811077;
		chadsChromosomes[36] = 0.03518293456623173;
		chadsChromosomes[37] = 0.056215216589984546;
		chadsChromosomes[38] = 0.12765105306821536;
		chadsChromosomes[39] = - 0.018069068352069617;
		chadsChromosomes[40] = 0.14394174658044098;
		chadsChromosomes[41] = 0.13199138161116172;
		chadsChromosomes[42] = 0.011315348576388579;
		chadsChromosomes[43] = 0.011050257123533871;
		chadsChromosomes[44] = 0.11898765765757206;
		chadsChromosomes[45] = 0.2197301488075142;
		chadsChromosomes[46] = - 0.1027241387992805;
		chadsChromosomes[47] = - 0.09439845288961976;
		chadsChromosomes[48] = - 0.011666380962552573;
		chadsChromosomes[49] = - 0.07315789448178515;
		chadsChromosomes[50] = - 0.06579246021858;
		chadsChromosomes[51] = 0.0026540854268602247;
		chadsChromosomes[52] = - 0.12134590737552854;
		chadsChromosomes[53] = 0.015024643265434767;
		chadsChromosomes[54] = 0.12060145295618864;
		chadsChromosomes[55] = - 0.03267733547821008;
		chadsChromosomes[56] = - 0.03241814402349264;
		chadsChromosomes[57] = 0.07511296332533893;
		chadsChromosomes[58] = - 0.027190265556348257;
		chadsChromosomes[59] = 0.12127777612490716;
		chadsChromosomes[60] = 0.10010570337488221;
		chadsChromosomes[61] = 0.330047696697129;
		chadsChromosomes[62] = 0.10911639755647683;
		chadsChromosomes[63] = - 0.052901542504427454;
		chadsChromosomes[64] = 1.298228680791397E-4;
		chadsChromosomes[65] = - 0.05658863850664638;
		chadsChromosomes[66] = - 0.14909802622060514;
		chadsChromosomes[67] = - 0.13670109745652512;
		chadsChromosomes[68] = 0.05011402939992994;
		chadsChromosomes[69] = 0.044329291431152995;
		chadsChromosomes[70] = 0.10225109692810075;
		chadsChromosomes[71] = 0.14961396921054904;
		chadsChromosomes[72] = 0.05630023752824946;
		chadsChromosomes[73] = - 0.021993542198268323;
		chadsChromosomes[74] = - 0.060411623732665065;
		chadsChromosomes[75] = - 0.030369881910982423;
		chadsChromosomes[76] = - 0.025266823953472364;
		chadsChromosomes[77] = - 0.01110480882985518;
		chadsChromosomes[78] = 0.03018033321329365;
		chadsChromosomes[79] = 0.12092627263363877;
		chadsChromosomes[80] = - 0.06206509678215429;
		chadsChromosomes[81] = 0.04560470013268794;
		chadsChromosomes[82] = 0.006976000920819906;
		chadsChromosomes[83] = - 0.062381282071433786;
		chadsChromosomes[84] = 0.15399654585034864;
		chadsChromosomes[85] = 0.05699676755296331;
		chadsChromosomes[86] = - 0.04592708613404743;
		chadsChromosomes[87] = - 0.012179422360545818;
		chadsChromosomes[88] = 0.06919683552031695;
		chadsChromosomes[89] = - 0.04511599787840336;
		chadsChromosomes[90] = - 0.15948695242805536;
		chadsChromosomes[91] = 0.04441822632757171;
		chadsChromosomes[92] = 0.01683680690935215;
		chadsChromosomes[93] = 0.12211372863386295;
		chadsChromosomes[94] = - 0.03957417694941826;
		chadsChromosomes[95] = - 6.582801025025418E-5;
		chadsChromosomes[96] = 0.049668473297116884;
		chadsChromosomes[97] = - 0.1442498865817443;
		chadsChromosomes[98] = 0.08374848501458772;
		chadsChromosomes[99] = - 0.03922673839392351;
		chadsChromosomes[100] = - 0.03346495822035821;
		chadsChromosomes[101] = - 0.20460366678270583;
		chadsChromosomes[102] = - 0.10149079644949222;
		chadsChromosomes[103] = 0.02580524842394114;
		chadsChromosomes[104] = 0.011085499461317634;
		chadsChromosomes[105] = 0.08636516345817352;
		chadsChromosomes[106] = 0.025841397515542076;
		chadsChromosomes[107] = - 0.05091432338130468;
		chadsChromosomes[108] = - 0.09916786585167314;
		chadsChromosomes[109] = - 0.01575360132786987;
		chadsChromosomes[110] = - 0.03420449075255057;
		chadsChromosomes[111] = - 0.15663130520559035;
		chadsChromosomes[112] = 0.016156145913172756;
		chadsChromosomes[113] = 0.04252435807205346;
		chadsChromosomes[114] = - 0.057387445907447626;
		chadsChromosomes[115] = - 0.14846790565345358;
		chadsChromosomes[116] = - 0.02995770585105277;
		chadsChromosomes[117] = 0.031203639921881285;
		chadsChromosomes[118] = 0.05613256595510241;
		chadsChromosomes[119] = 0.15475951982907132;
		chadsChromosomes[120] = - 0.0025926762085769417;
		chadsChromosomes[121] = - 0.012222771503339058;
		chadsChromosomes[122] = 0.1586082525894114;
		chadsChromosomes[123] = 0.11946118175994057;
		chadsChromosomes[124] = - 0.005095365880477378;
		chadsChromosomes[125] = - 0.0529210067498627;
		chadsChromosomes[126] = - 5.258035794110348E-4;
		chadsChromosomes[127] = - 0.004057725768725334;
		chadsChromosomes[128] = - 0.018807702757225975;
		chadsChromosomes[129] = - 0.13918043167877084;
		chadsChromosomes[130] = - 0.06701066288409709;
		chadsChromosomes[131] = - 0.1507808894439869;
		chadsChromosomes[132] = - 0.013320771430171047;
		chadsChromosomes[133] = - 0.019470223646927187;
		chadsChromosomes[134] = - 0.017384496983254873;
		chadsChromosomes[135] = 0.04306441226109341;
		chadsChromosomes[136] = 0.010025875526436887;
		chadsChromosomes[137] = - 0.09735586450133374;
		chadsChromosomes[138] = - 0.14585203416101722;
		chadsChromosomes[139] = 0.041959298885711496;
		chadsChromosomes[140] = - 0.003078694091899937;
		chadsChromosomes[141] = - 0.014953827835047558;
		chadsChromosomes[142] = 3.3582289435435224E-4;
		chadsChromosomes[143] = - 0.15328300818533597;
		chadsChromosomes[144] = 0.08686739230741299;
		chadsChromosomes[145] = - 0.0730557401410915;
		chadsChromosomes[146] = - 0.2118908689326084;
		chadsChromosomes[147] = 0.007114301865847114;
		chadsChromosomes[148] = - 0.14271488950169328;
		chadsChromosomes[149] = 0.002240023335625063;
		chadsChromosomes[150] = 0.14210124517636394;
		chadsChromosomes[151] = 0.09202911501058268;
		chadsChromosomes[152] = - 0.0471333711537509;
		chadsChromosomes[153] = - 0.09275883384097652;
		chadsChromosomes[154] = - 0.080154468288472;
		chadsChromosomes[155] = - 0.13278622652267932;
		chadsChromosomes[156] = 0.09302259132113351;
		chadsChromosomes[157] = - 0.13084678391403237;
		chadsChromosomes[158] = 0.09129557677293969;
		chadsChromosomes[159] = - 0.0030207744969512748;
		chadsChromosomes[160] = - 0.0071686587640486035;
		chadsChromosomes[161] = - 0.0254358575315392;
		chadsChromosomes[162] = - 0.028969180722054014;
		chadsChromosomes[163] = 0.124011907664246;
		chadsChromosomes[164] = - 0.0021007433940419167;
		chadsChromosomes[165] = - 0.09328905034721674;
		chadsChromosomes[166] = - 0.0744374999381904;
		chadsChromosomes[167] = - 0.07365251630919453;
		chadsChromosomes[168] = 0.024617780094186435;
		chadsChromosomes[169] = - 0.08180673201093461;
		chadsChromosomes[170] = 0.0368258893746571;
		chadsChromosomes[171] = - 0.08988934393462987;
		chadsChromosomes[172] = - 0.09375021827867536;
		chadsChromosomes[173] = - 0.024811762079816158;
		chadsChromosomes[174] = - 0.056371826979133094;
		chadsChromosomes[175] = - 0.08621223866489243;
		chadsChromosomes[176] = - 0.11732342481836398;
		chadsChromosomes[177] = 0.06531884250463336;
		chadsChromosomes[178] = 0.02435937161457887;
		chadsChromosomes[179] = 0.016598982392344707;
		chadsChromosomes[180] = - 0.009816414404732805;
		chadsChromosomes[181] = 0.006101987441820686;
		chadsChromosomes[182] = 0.13324592005156277;
		chadsChromosomes[183] = 0.025864464420340086;
		chadsChromosomes[184] = - 0.0010750156098973146;
		chadsChromosomes[185] = - 0.07812131704443033;
		chadsChromosomes[186] = 0.05755359170573859;
		chadsChromosomes[187] = - 0.005642139873843525;
		chadsChromosomes[188] = - 0.011401804344996054;
		chadsChromosomes[189] = 0.15054091683532486;
		chadsChromosomes[190] = 0.008452690912600588;
		chadsChromosomes[191] = 0.07436754299015703;
		chadsChromosomes[192] = 0.06692674632002314;
		chadsChromosomes[193] = 0.030978586592179894;
		chadsChromosomes[194] = - 0.1438675826042238;
		chadsChromosomes[195] = - 0.11383604241517566;
		chadsChromosomes[196] = 0.12408691487854738;
		chadsChromosomes[197] = 0.019414200440725296;
		chadsChromosomes[198] = - 0.18529547387758022;
		chadsChromosomes[199] = 0.007055308733194196;
		chadsChromosomes[200] = - 0.08548170982563351;
		chadsChromosomes[201] = 0.0856990259385046;
		chadsChromosomes[202] = - 0.01247565996080957;
		chadsChromosomes[203] = - 0.015162905811218685;
		chadsChromosomes[204] = - 0.032384995591981075;
		chadsChromosomes[205] = 0.026569264432243934;
		chadsChromosomes[206] = - 0.08055615653011962;
		chadsChromosomes[207] = 0.08022233054487457;
		chadsChromosomes[208] = 0.06741711691770821;
		chadsChromosomes[209] = - 0.03245839156723003;
		chadsChromosomes[210] = - 0.08024617594168935;
		chadsChromosomes[211] = - 0.06963011267055859;
		chadsChromosomes[212] = 0.013350557935721367;
		chadsChromosomes[213] = 0.09053837494855187;
		chadsChromosomes[214] = - 0.08613315830817665;
		chadsChromosomes[215] = - 0.022248789860697626;
		chadsChromosomes[216] = - 0.0016316990214558346;
		chadsChromosomes[217] = - 0.14183961590985258;
		chadsChromosomes[218] = 0.030759358675739246;
		chadsChromosomes[219] = - 0.011444427494203801;
		chadsChromosomes[220] = - 0.027177252840140706;
		chadsChromosomes[221] = 0.006940491021745894;
		chadsChromosomes[222] = 0.0672034429390751;
		chadsChromosomes[223] = - 0.07882026536021308;
		chadsChromosomes[224] = 0.06626196547020372;
		chadsChromosomes[225] = 0.12582467122936086;
		chadsChromosomes[226] = - 0.03470022836551267;
		chadsChromosomes[227] = 0.002106077595201604;
		chadsChromosomes[228] = - 0.025127075559833827;
		chadsChromosomes[229] = 0.08279619493330095;
		chadsChromosomes[230] = 0.12145314009875527;
		chadsChromosomes[231] = - 0.18084731661385228;
		chadsChromosomes[232] = 0.09169947599720055;
		chadsChromosomes[233] = 0.11556559103725494;
		chadsChromosomes[234] = - 0.16440281794920772;
		chadsChromosomes[235] = 0.16140682112510393;
		chadsChromosomes[236] = 0.06402487224237341;
		chadsChromosomes[237] = - 0.00522851025369382;
		chadsChromosomes[238] = - 0.07228487212846203;
		chadsChromosomes[239] = - 0.006964778504362501;
		chadsChromosomes[240] = 0.014382038264026911;
		chadsChromosomes[241] = 0.02224039362039868;
		chadsChromosomes[242] = 0.12479374249966123;
		chadsChromosomes[243] = - 0.03954935806061687;
		chadsChromosomes[244] = - 0.1267101911131671;
		chadsChromosomes[245] = 0.0030685443517557837;
		chadsChromosomes[246] = 0.01666937849491286;
		chadsChromosomes[247] = - 0.13554293739082302;
		chadsChromosomes[248] = 0.10110121903138211;
		chadsChromosomes[249] = 0.011790193070797468;
		chadsChromosomes[250] = - 0.1683242457405941;
		chadsChromosomes[251] = - 0.1669800423741512;
		chadsChromosomes[252] = 0.029648975501086368;
		chadsChromosomes[253] = - 0.050938700729111094;
		chadsChromosomes[254] = 0.16950609830597155;
		chadsChromosomes[255] = 0.029891606122732096;
		chadsChromosomes[256] = 0.07581568381458369;
		chadsChromosomes[257] = - 0.05206791726458454;
		chadsChromosomes[258] = 0.06726546567991107;
		chadsChromosomes[259] = 0.2455871456906748;
		chadsChromosomes[260] = 0.23480465094866912;
		chadsChromosomes[261] = 0.026596838591732737;
		chadsChromosomes[262] = 0.12014241382297544;
		chadsChromosomes[263] = 0.01799401001131238;
		chadsChromosomes[264] = - 0.05607726957444013;
		chadsChromosomes[265] = - 0.06739130035465797;
		chadsChromosomes[266] = - 0.15036194113759743;
		chadsChromosomes[267] = - 0.0813347933789081;
		chadsChromosomes[268] = 0.01940659480356379;
		chadsChromosomes[269] = 0.11471705646689813;
		chadsChromosomes[270] = - 0.41885925725840567;
		chadsChromosomes[271] = 0.08290259676895018;
		chadsChromosomes[272] = - 0.004481048208797267;
		chadsChromosomes[273] = - 0.023074408290742733;
		chadsChromosomes[274] = - 0.1128819202760583;
		chadsChromosomes[275] = 0.048674881323990185;
		chadsChromosomes[276] = 0.2146795561428676;
		chadsChromosomes[277] = 0.014728344659881723;
		chadsChromosomes[278] = - 0.027526967542055525;
		chadsChromosomes[279] = - 0.021793291919828043;
		chadsChromosomes[280] = 0.06011183102726404;
		chadsChromosomes[281] = - 0.0927308019468941;
		chadsChromosomes[282] = 0.06061367681448283;
		chadsChromosomes[283] = 0.050772718896720935;
		chadsChromosomes[284] = 0.07519435377744603;
		chadsChromosomes[285] = 0.028224090169252737;
		chadsChromosomes[286] = 0.005052191158556955;
		chadsChromosomes[287] = - 0.0805644654979864;
		chadsChromosomes[288] = 0.0390970462085325;
		chadsChromosomes[289] = 0.15773368240368946;
		chadsChromosomes[290] = 0.1572086385518003;
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
		double[] w = getChad();
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