package in.dangerbear.crafting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import java.util.Scanner;

public class GraphGenerator {
////DECLARATIONS///////////////////////////////////////////////////////////////
	////Statics
	public final static int MALE = 0;
	public final static int FEMALE = 1;
	public final static int LAST = 2;

	public static int FAMILIES = 80;
	public static int POPULATION = 2000;
	public static int MAX_OFFSPRING = 20;
	public static int MIN_REPRODUCTIVE_AGE = 15;
	public static int MAX_AGE = 100;
	public static String NAME_FILEPATH = "US";
	
	private static final boolean DEBUG = true;

	////Class variables
	ArrayList<Human> humans;
	BitSet eligibilityList1;
	ArrayList<String> males;
	ArrayList<String> females;
	ArrayList<String> lasts;
	Random rand = new Random();
	Parser parser;

////CONSTRUCTORS///////////////////////////////////////////////////////////////
	public GraphGenerator() {
		humans = new ArrayList<Human>();
		parser = new Parser();

		readConfig();
		// Choose and parse name files.
		parser.feedInput(NAME_FILEPATH, MALE);
		parser.feedInput(NAME_FILEPATH, FEMALE);
		parser.feedInput(NAME_FILEPATH, LAST);

	}

////METHODS////////////////////////////////////////////////////////////////////
	
	/**
	 * Method generate
	 * Generates the eligible last names and the Humans
	 * with random gender assignment and names.
	 * 
	 * @param families
	 *            (int) The number of families to generate (last names).
	 * @param people
	 *            (int) The number of total people to generate.
	 * @param males
	 *            (ArrayList<String>) List of male first names.
	 * @param females
	 *            (ArrayList<String>) List of female first names.
	 * @param lasts
	 *            (ArrayList<String>) List of last names.
	 */
	public ArrayList<Human> generate() {
		dp("DEBUG : In the generate function.");
		this.males = parser.getNameList(MALE);
		this.females = parser.getNameList(FEMALE);
		this.lasts = parser.getNameList(LAST);

		eligibilityList1 = new BitSet(POPULATION);
		String tempLasts[] = new String[FAMILIES];
		String tempLastName;
		String tempFirstName;

		dp("Making families.");
		for (int i = 0; i < FAMILIES; ++i) {
			// TODO: Implement a less costly no-repeat functionality.
			int choice = rand.nextInt(lasts.size());
			tempLasts[i] = lasts.get(choice);
			lasts.remove(choice);
		}
		lasts.clear();

		for (int i = 0; i < POPULATION; ++i) {
			tempLastName = tempLasts[rand.nextInt(tempLasts.length)];
			if (rand.nextInt(2) == 0) {// Male created
				tempFirstName = males.get(rand.nextInt(males.size()));
				humans.add(new Human(tempLastName, tempFirstName, MALE));
			} else {// Female created
				tempFirstName = females.get(rand.nextInt(females.size()));
				humans.add(new Human(tempLastName, tempFirstName, FEMALE));
			}
		}
		males.clear();
		females.clear();

		// Generate the ages of the populace.
		dp("DEBUG : Generating ages.");
		ages();
		dp("DEBUG : Making genetic interconnections.");
		interconnectGenetically();
		dp("DEBUG : Making social interconnections.");
		interconnectSocially();
		
		return humans;
	}


	/**
	 * Method interconnectSocially
	 * Connects the humans randomly.
	 * TODO: Change to use MAX_SOCIAL_CLUSTER_SIZE (an integer, or percentage of total population.)
	 * 
	 */
	private void interconnectSocially(){
		for(int i = 0; i < humans.size(); ++i){
			int maxConnections = rand.nextInt(30); //20 should be MAX_SOCIAL_CLUSTER_SIZE
			//create an array that stores unique connections. Protects against redundancy later.
			ArrayList<Integer> madeConnections = new ArrayList<Integer>();
			interconnectSociallyHelper(madeConnections, maxConnections, i);
			for(int j = 0; j < madeConnections.size(); ++j){
				makeRelationshipConnection(i, madeConnections.get(j));
			}
		}
	}
	
	/**
	 * Method interconnectGenetically
	 * Starts attempting to create parent/child connections
	 * for every Human. Using the value returned by helper function
	 * makeConnection, this attempts to stop connections ASAP.
	 * 
	 */
	private void interconnectGenetically() {
		for (int i = 0; i < humans.size(); ++i) {
			Human parent = humans.get(i);
			// Hit the lowest age. No one after this can have children. End!
			if (parent.getAge() < MIN_REPRODUCTIVE_AGE) {
				return;
			}

			// Set everything up to the current human's ID to false. They are
			// all older.
			eligibilityList1.clear();
			eligibilityList1.set(i + 1, humans.size(), true);

			// Begin elimination of potential child connections.
			for (int j = i; j < humans.size(); ++j) {
				Human child = humans.get(j);

				// Check for sameness.
				if (i == j) {
					eligibilityList1.set(j, false);
				}
				// Check for age difference.
				if (parent.getAge() - child.getAge() < MIN_REPRODUCTIVE_AGE) {
					eligibilityList1.set(j, false);
				}

				// Check for name difference.
				// TODO: This should be altered by a ADOPTION_PROBABILITY input.
				// Some people can adopt?
				if (!parent.getLastName().equals(child.getLastName())) {
					//Adoption eligibility
					if(rand.nextInt(100) < 80){//80 should be ADOPTION_PROBABILITY
						eligibilityList1.set(j, false);
					}
				}

				// Check for existing parenthood.
				if (parent.getGender() == MALE && child.getFatherID() != -1) {
					eligibilityList1.set(j, false);
				}
				if (parent.getGender() == FEMALE && child.getMotherID() != -1) {
					eligibilityList1.set(j, false);
				}
			}

			// If no other humans are eligible, move onto the next potential
			// parent.
			if (eligibilityList1.cardinality() <= 0) {
				continue;
			}

			// Create the list of ids of eligible chilren.
			ArrayList<Integer> eligibleChildren = new ArrayList<Integer>();
			for (int j = i; j < eligibilityList1.size(); ++j) {
				if (eligibilityList1.get(j)) {
					eligibleChildren.add(j);
				}
			}

			// Attempt to make a random amount of connections between the
			// current
			// node and the eligible children nodes.
			int maxOffspring = rand.nextInt(MAX_OFFSPRING);
			for (int j = 0; j < maxOffspring; ++j) {
				// There's only one eligible unit left! Connect and stop.
				if (eligibleChildren.size() == 1) {
					makeGeneticConnection(i, eligibleChildren.get(0));
					break;
				}

				// Connect the ith person and someone from their eligible list.
				int index = rand.nextInt(eligibleChildren.size());
				makeGeneticConnection(i, eligibleChildren.get(index));
				eligibleChildren.remove(index);
			}
		}
	}

	/**
	 * Method readConfig
	 * Simple routine to read the Genetics config files.
	 * Populates generational variables.
	 * 
	 */
	public static void readConfig() {
		File file = new File("Configs/Genetics.ini");
		try {
			Scanner s = new Scanner(file);
			while (s.hasNext()) {
				String inputToken = s.next();
				switch (inputToken) {
				case "POPULATION:":
					POPULATION = s.nextInt();
					break;
				case "FAMILIES:":
					FAMILIES = s.nextInt();
					break;
				case "MAX_OFFSPRING:":
					MAX_OFFSPRING = s.nextInt();
					break;
				case "MAX_AGE:":
					MAX_AGE = s.nextInt();
					break;
				case "NAME_FILEPATH:":
					NAME_FILEPATH = s.next();
					break;
				case "MIN_REPRODUCTIVE_AGE:":
					MIN_REPRODUCTIVE_AGE = s.nextInt();
					break;
				default:
					break;
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	////Helpers

	/**
	 * Method makeConnection Connects the parent and child nodes, according to
	 * parameter IDs. Makes sure the genders match on the parent connections.
	 * 
	 * @param parent
	 *            (int) referencing the ID number of parent.
	 * @param child
	 *            (int) referencing the ID number of child.
	 * @return (int) 0: Failure. 1: Success.
	 */
	private int makeGeneticConnection(int parent, int child) {
		int parentGender = humans.get(parent).getGender();

		if (parentGender == MALE) {
			humans.get(child).setFatherID(parent);
			humans.get(parent).addChild(child);
			return 1;
		} else if (parent == FEMALE) {
			humans.get(child).setMotherID(parent);
			humans.get(parent).addChild(child);
			return 1;
		}
		return 0;
	}
	
	/**
	 * Method interconnectSociallyHelper
	 * TODO: Potential problem with this never finding a valid ID.
	 * Need to rethink this later, especially with small populations.
	 * 
	 * @param uniques
	 * @param maxConnections
	 * @param sourceID
	 */
	private void interconnectSociallyHelper(ArrayList<Integer> uniques, int maxConnections, int sourceID){
		int i = 0;
		int target = -1;
		while(i < maxConnections){
			//Generate potential connection ID
			target = rand.nextInt(humans.size());
			
			//Check for validity.
			if(target == sourceID) continue;
			if(humans.get(sourceID).hasRelationship(target)) continue;
			
			//Target has passed the gauntlet. Add to uniques arraylist.
			uniques.add(target);
			++i;
		}
	}
	
	
	/**
	 * Method makeRelationshipConnection
	 * TODO: Make a reasonable relationship generator. Based on family/owing money/etc.
	 * 
	 * @param a (int) Represents some human by id.
	 * @param b (int) Represents some human by id.
	 * @return (int) 0: Failure. 1: Success.
	 */
	private int makeRelationshipConnection(int a, int b){
		humans.get(a).addRelationship(b);
		humans.get(b).addRelationship(a);
		return 1;
	}

	/**
	 * Method ages This creates a distribution of ages and assigns them to the
	 * Human objects. Does this in a greatest -> least order.
	 * 
	 * TODO: Add distribution functions.
	 * 
	 * 
	 */
	private void ages() {
		int ages[] = new int[POPULATION];
		for (int i = 0; i < POPULATION; ++i) {
			ages[i] = rand.nextInt(MAX_AGE + 1);
			// ages[i] = (rand.nextGaussian()/3);
		}

		Arrays.sort(ages);

		//Set the first humans to the largest ages.
		for (int i = 0; i < POPULATION; ++i) {
			humans.get(i).setAge(ages[POPULATION - 1 - i]);
		}
	}
	
	private void dp(String input){
		if(DEBUG){
			System.out.println(input);
		}
	}

}
