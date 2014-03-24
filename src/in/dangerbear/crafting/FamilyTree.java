/**
 * 
 */
package in.dangerbear.crafting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author SWPhantom
 * 
 */

public class FamilyTree {
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
	BitSet eligibilityList2;
	ArrayList<String> males;
	ArrayList<String> females;
	ArrayList<String> lasts;
	Random rand = new Random();
	Parser parser;

////CONSTRUCTORS///////////////////////////////////////////////////////////////
	public FamilyTree() {
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
	public void generate() {
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
	}
	
	/**
	 * Method startRumor
	 * Gets an initial population of infected IDs and tries to infect all
	 * neighboring nodes.
	 * 
	 * TODO: Change this to be more API-like. Consider returning an ArrayList
	 * or the percentage of infection.
	 * 
	 * @param initialVectors
	 * @param infectionProbability
	 */
	public void startRumor(ArrayList<Integer> initialVectors, int infectionProbability){
		int totalInfected = initialVectors.size();
		//Create a queue to keep track of who is to try infecting.
		Queue<Integer> infectionOrder = new ConcurrentLinkedQueue<Integer>();
		
		//List of people that are infected.
		eligibilityList1.clear();
		for(int i = 0; i < initialVectors.size(); ++i){
			eligibilityList1.set(initialVectors.get(i));
			infectionOrder.add(initialVectors.get(i));
		}
		
		//XXX: Could have an issue with infinite loop if not careful. Check.
		while(!infectionOrder.isEmpty()){
			int infectorID = infectionOrder.remove();
			ArrayList<Integer> infectorConnections = humans.get(infectorID).getSocialConnections();
			for(int i = 0; i < infectorConnections.size(); ++i){
				boolean infected = startRumorHelper(infectorID, infectorConnections.get(i), infectionProbability);
				if(infected){
					infectionOrder.add(infectorConnections.get(i));
					++totalInfected;
				}
			}
		}
		
		//Check the percentage of infection.
		double percent = 100.00 * (double)totalInfected / (double)humans.size();
		System.out.println("Infection analysis: " + totalInfected + "/" + humans.size() +"(" + percent + "%)");
		
		
	}

	/**
	 * Method startRumorHelper
	 * TODO: The reason this takes in infectorID is to determine connection-specific
	 * infection probability (how much the pair of people like each other, etc).
	 * 
	 * @param infectorID
	 * @param targetID
	 * @param infectionProbability
	 * @return
	 */
	private boolean startRumorHelper(int infectorID, int targetID, int infectionProbability){
		//Check to see if the target is already infected.
		if(eligibilityList1.get(targetID)) return false;
		
		//Not infected already. See if infection occurs.
		if(rand.nextInt(100) >= infectionProbability) return false;
		
		//Infection has occurred. Add to the infected list.
		eligibilityList1.set(targetID);
		return true;
	}

	/**
	 * Method interconnectSocially
	 * Connects the humans randomly.
	 * TODO: Change to use MAX_SOCIAL_CLUSTER_SIZE (an integer, or percentage of total population.)
	 * 
	 */
	private void interconnectSocially(){
		
		for(int i = 0; i < humans.size(); ++i){
			int maxConnections = rand.nextInt(humans.size()/10); //20 should be MAX_SOCIAL_CLUSTER_SIZE
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
	 * Method nthConnections
	 * Returns a list of IDs that correspond to the target
	 * Human's N linked Human Nodes. ie. If you call this function on a child
	 * with two parents and no kids, the returned list will contain only his
	 * parents for N = 1. If you call the same function with N = 2, you will get
	 * a list of the child's parents AND grandparents.
	 * 
	 * @param humanID
	 * @return
	 */
	public ArrayList<Integer> nthConnections(int humanID, int n) {
		eligibilityList1.clear();
		ArrayList<Integer> output = new ArrayList<Integer>();

		nthConnectionsHelper(humanID, n);

		for (int i = 0; i < humans.size(); ++i) {
			if (eligibilityList1.get(i)) {
				output.add(i);
			}
		}
		return output;
	}

	/**
	 * Method printNthConnections
	 * Printing function wrapper for getNthConnection.
	 * 
	 * @param humanID
	 * @param n
	 */
	public void printNthConnections(int humanID, int n) {
		System.out.println("Printing " + humans.get(humanID).getName() + "'s "
				+ n + "connections.");
		ArrayList<Integer> outputList = this.nthConnections(humanID, n);

		for (Integer i : outputList) {
			System.out.println(humans.get(i).toString());
		}
		System.out.println();
	}

	/**
	 * Method hasPath
	 * The method that initializes the eligibility BitSet and calls the
	 * recursive hasPathHelper function.
	 * 
	 * @param source 
	 * @param target
	 * @return
	 */
	public boolean hasPath(int source, int target) {
		eligibilityList1.clear();
		return hasPathHelper(source, target);
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
	 * Method hasPathHelper
	 * Recursive method that does a depth-first search of the graph
	 * to see if the source Human is connected to the Target Human.
	 * 
	 * @param source (int) Index/ID of the current node.
	 * @param target (int) Index/ID of the node to be reached.
	 * @return
	 */
	private boolean hasPathHelper(int source, int target) {
		// I've been here before. No good.
		if (eligibilityList1.get(source) == true) {
			return false;
		}
		// Make sure not to come here again.
		eligibilityList1.set(source, true);

		// If this function has happened upon the target, we're done!
		if (source == target) {
			return true;
		}

		// Start checking the parents and the children.
		Human a = humans.get(source);
		if (a.getMotherID() != -1 && hasPathHelper(a.getMotherID(), target))
			return true;
		if (a.getFatherID() != -1 && hasPathHelper(a.getFatherID(), target))
			return true;
		for (int i = 0; i < a.getNumChildren(); ++i) {
			if (hasPathHelper(a.getChildren().get(i), target))
				return true;
		}
		return false;
	}

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

	/**
	 * Method nthConnectionsHelper
	 * Recursive function that sets the Eligibility BitSet indeces that
	 * correspond to the Humans that are within n links from a given
	 * Human.
	 * 
	 * @param humanID
	 * @param n
	 */
	private void nthConnectionsHelper(int humanID, int n) {
		// If this Human has been accessed already, stop!
		if (eligibilityList1.get(humanID)) {
			return;
		}

		// Add yourself to the list.
		eligibilityList1.set(humanID);

		// If you're the last connection to be checked, stop before querying
		// your connections.
		if (n == 0) {
			return;
		}

		// Stores the potential family to check.
		ArrayList<Integer> payload = new ArrayList<Integer>();

		// Get the parents.
		int temp = humans.get(humanID).getFatherID();
		if (temp != -1)
			payload.add(temp);
		temp = humans.get(humanID).getMotherID();
		if (temp != -1)
			payload.add(temp);

		// Time to add the children. Create a temp ArrayList.
		ArrayList<Integer> tempChildren = humans.get(humanID).getChildren();
		for (int i = 0; i < tempChildren.size(); ++i) {
			payload.add(tempChildren.get(i));
		}

		for (int i = 0; i < payload.size(); ++i) {
			nthConnectionsHelper(payload.get(i), n - 1);
		}
	}

	////Debug

	/**
	 * Method displayMemory Shows off the memory used at the moment.
	 * 
	 */
	public static void displayMemory() {
		Runtime r = Runtime.getRuntime();
		r.gc();
		r.gc();
		System.out.println("Memory Used=" + (r.totalMemory() - r.freeMemory()));
	}

	/**
	 * Method DEBUG_PRINT Current desired output:
	 * 
	 * Martinella Stranbii(99)
	 * 
	 * Brighinzone Stranbii(90)
	 *  Children:
	 *   Baccone de Calce (80)
	 *   Giollius de Calce (80)
	 *   Giovanna de Calce (23)
	 *   Cambius Simonis (23)
	 *   Berardus Doberti (43)
	 * 
	 * Cambius Simonis(88)
	 */
	public void DEBUG_PRINT() {
		for (int i = 0; i < humans.size(); ++i) {
			System.out.println(humans.get(i).getName() + "("
					+ humans.get(i).getAge() + ")");
			if (humans.get(i).getNumChildren() > 0) {
				System.out.println(" Children:");
				for (int j = 0; j < humans.get(i).getNumChildren(); ++j) {
					System.out.println("  "
							+ humans.get(humans.get(i).children.get(j))
									.getName() + "(" + humans.get(humans.get(i).children.get(j)).getAge() + ")");
				}
			}
			System.out.println();
		}
	}

	/**
	 * Method DEBUG_CONNECTIONS_PRINT
	 * Picks two random nodes and runs hasPath on them. Prints the result.
	 * 
	 * @param tests
	 */
	public void DEBUG_CONNECTIONS_PRINT(int tests) {
		System.out.println("Making many random connection queries:");
		int a;
		int b;
		for (int i = 0; i < tests; ++i) {
			a = rand.nextInt(humans.size());
			b = rand.nextInt(humans.size());
			System.out.print("Are " + humans.get(a).getName() + " and "
					+ humans.get(b).getName() + " connected? ");
			if (hasPath(a, b)) {
				System.out.println("Yes");
			} else {
				System.out.println("No");
			}
		}
	}
	
	private void dp(String input){
		if(DEBUG){
			System.out.println(input);
		}
	}

}
