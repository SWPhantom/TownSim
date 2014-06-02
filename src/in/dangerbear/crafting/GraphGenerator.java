package in.dangerbear.crafting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GraphGenerator{
////DECLARATIONS///////////////////////////////////////////////////////////////
	////Statics
	public final static int MALE = 0;
	public final static int FEMALE = 1;
	public final static int LAST = 2;

	public static int FAMILIES = 80;
	public static int POPULATION = 2000;
	public static float MALE_BIRTH_CHANCE = .5f;
	public static int MAX_OFFSPRING = 20;
	public static int MIN_REPRODUCTIVE_AGE = 15;
	public static int MAX_REPRODUCTIVE_AGE = 40;
	public static int MAX_AGE = 100;
	public static int MAX_GROUPS = 10;
	public static int TOTAL_GROUPS = 50;
	public static int ADOPTION_PROBABILITY = 10; //Percent
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
	public GraphGenerator(){
		humans = new ArrayList<Human>();
		parser = new Parser();
		

		readConfig();
		eligibilityList1 =  new BitSet(POPULATION);
		// Choose and parse name files.
		parser.feedInput(NAME_FILEPATH, MALE);
		parser.feedInput(NAME_FILEPATH, FEMALE);
		parser.feedInput(NAME_FILEPATH, LAST);

	}

////METHODS////////////////////////////////////////////////////////////////////

	/**
	 * Method generate
	 * Generates the eligible last names and the Humans with random gender
	 * assignment and names.
	 */
	public ArrayList<Human> generate(){
		dp("DEBUG : In the generate function.");
		this.males = parser.getNameList(MALE);
		this.females = parser.getNameList(FEMALE);
		this.lasts = parser.getNameList(LAST);

		TreeSet<Integer> nameIndeces = new TreeSet<Integer>();
		String tempLasts[] = new String[FAMILIES];
		String tempLastName;
		String tempFirstName;

		dp("DEBUG : Picking unique family names.");
		int numberOfLastNames = lasts.size();
		int iter = FAMILIES;
		while(iter > 0){
			if(nameIndeces.add(rand.nextInt(numberOfLastNames))){
				--iter;
			}
		}
		for(int i = 0; i < FAMILIES; ++i){
			int first = nameIndeces.first();
			nameIndeces.remove(first);
			tempLasts[i] = lasts.get(first);
		}
		lasts.clear();

		for(int i = 0; i < POPULATION; ++i){
			tempLastName = tempLasts[rand.nextInt(tempLasts.length)];
			if(rand.nextFloat() <= MALE_BIRTH_CHANCE){// Male created
				tempFirstName = males.get(rand.nextInt(males.size()));
				humans.add(new Human(tempLastName, tempFirstName, MALE));
			}else{// Female created
				tempFirstName = females.get(rand.nextInt(females.size()));
				humans.add(new Human(tempLastName, tempFirstName, FEMALE));
			}
		}
		males.clear();
		females.clear();

		dp("DEBUG : Generating ages.");
		ages();
		dp("DEBUG : Making genetic interconnections.");
		interconnectGenetically();
		dp("DEBUG : Making social interconnections.");
		interconnectSocially();

		return humans;
	}

	/**
	 * Method interconnectGenetically
	 * Starts attempting to create parent/child connections for every Human.
	 * Using the value returned by helper function makeConnection, this
	 * attempts to stop connections ASAP.
	 * 
	 * XXX: Some bad things are making too many disparate clusters. Need to fix.
	 * TODO: Change genetic interconnection by having lastName-> ArrayList<Integer>
	 *       mapping to make things easier. Remove EligibilityList use.
	 */
	private void interconnectGenetically(){
		for(Human parent: humans){
			int parentAge = parent.getAge();
			int parentGender = parent.getGender();
			// Hit the lowest age. No one after this can have children. End!
			if(parentAge < MIN_REPRODUCTIVE_AGE){
				break;
			}

			// Set everything up to the current human's ID to false. They are
			// all older.
			eligibilityList1.clear();
			eligibilityList1.set(parent.getID(), humans.size(), true);

			// Begin elimination of potential child connections.
			for(int j = parent.getID(); j < humans.size(); ++j){
				Human child = humans.get(j);
				int childAge = child.getAge();
				//These booleans help with the eligibility checking.
				boolean ageMin = parentAge - childAge< MIN_REPRODUCTIVE_AGE;
				boolean ageMax = parentGender == FEMALE && parentAge - childAge > MAX_REPRODUCTIVE_AGE;
				boolean matchingFather = parentGender == MALE && child.getFatherID() != -1;
				boolean matchingMother = parentGender == FEMALE && child.getMotherID() != -1;
				boolean sameFamily = parent.getLastName().equals(child.getLastName());
				
				// Check for age difference.
				if(ageMin || ageMax || matchingFather || matchingMother){
					eligibilityList1.set(j, false);
					continue;
				}

				// Check for name difference.
				if(!sameFamily){
					//Adoption eligibility
					if(rand.nextInt(100) + 1 >= ADOPTION_PROBABILITY){
						eligibilityList1.set(j, false);
						continue;
					}
				}
			}

			// If no other humans are eligible, move onto the next potential parent.
			if(eligibilityList1.cardinality() <= 0){
				continue;
			}

			// Create the list of IDs of eligible children.
			Queue<Integer> eligibleChildren = new ConcurrentLinkedQueue<Integer>();
			for(int j = parent.getID(); j < eligibilityList1.size(); ++j){
				if(eligibilityList1.get(j)){
					eligibleChildren.add(j);
				}
			}

			/* Attempt to make a random amount of connections between the
			 * current node and the eligible children nodes.
			 */
			int maxOffspring = rand.nextInt(MAX_OFFSPRING + 1);
			while(maxOffspring > 0 && eligibleChildren.size() > 0){
				makeGeneticConnection(parent.getID(), eligibleChildren.remove());
				--maxOffspring;
			}
		}

		//Make sure most nodes have parents. Youngest first.
		ListIterator<Human> li = humans.listIterator(humans.size());
		while(li.hasPrevious()){
			Human child = li.previous();
			//Obtain mother.
			if(child.getMotherID() == -1){
				getParent(child, FEMALE);
			}
			//Obtain father.
			if(child.getFatherID() == -1){
				getParent(child, MALE);
			}
		}
	}

	private void getParent(Human child, int gender){
		int iter = child.getID();
		int childAge = child.getAge();
		Human parent;

		while(iter-- > 0){
			parent = humans.get(iter);
			if(parent.getGender() != gender){
				continue;
			}
			if(parent.getAge() - childAge < MIN_REPRODUCTIVE_AGE){
				continue;
			}
			if(!parent.getLastName().equals(child.getLastName())){
				continue;
			}
			makeGeneticConnection(parent.getID(), child.getID());
			break;
		}
	}

	/**
	 * Method interconnectSocially
	 * Connects the humans randomly by putting them into different groups.
	 */
	private void interconnectSocially(){
		for(Human target: humans){
			int maxGroups = rand.nextInt(MAX_GROUPS) + 1;
			int groupChoice;
			
			for(int j = 0; j < maxGroups; ++j){
				groupChoice = rand.nextInt(TOTAL_GROUPS);
				target.addToGroup(groupChoice);
			}
			
		}
	}

	////Helpers
	/**
	 * Method makeConnection
	 * Connects the parent and child nodes, according to
	 * parameter IDs. Makes sure the genders match on the parent connections.
	 * 
	 * @param parent (int) referencing the ID number of parent.
	 * @param child (int) referencing the ID number of child.
	 * @return (int) 0: Failure. 1: Success.
	 */
	private int makeGeneticConnection(int parent, int child){
		int parentGender = humans.get(parent).getGender();

		if(parentGender == MALE){
			humans.get(child).setFatherID(parent);
			humans.get(parent).addChild(child);
			return 1;
		}else if(parentGender == FEMALE){
			humans.get(child).setMotherID(parent);
			humans.get(parent).addChild(child);
			return 1;
		}
		return 0;
	}

	/**
	 * Method ages
	 * This creates a distribution of ages and assigns them to the
	 * Human objects. Does this in a greatest -> least order.
	 * 
	 * TODO: Add distribution functions. Could use
	 * http://www.ckollars.org/population-distribution-age-sex-desktop.html
	 */
	private void ages(){
		int ages[] = new int[POPULATION];
		for(int i = 0; i < POPULATION; ++i){
			ages[i] = rand.nextInt(MAX_AGE + 1);
			// ages[i] = (rand.nextGaussian()/3);
		}

		Arrays.sort(ages);

		//Set the first humans to the largest ages.
		for(int i = 0; i < POPULATION; ++i){
			humans.get(i).setAge(ages[POPULATION - 1 - i]);
		}
	}

	/**
	 * Method readConfig
	 * Simple routine to read the Genetics config files.
	 * Populates generational variables.
	 */
	public static void readConfig(){
		File file = new File("Configs/Genetics.ini");
		try{
			Scanner s = new Scanner(file);
			while(s.hasNext()){
				String inputToken = s.next();
				switch(inputToken){
				case "POPULATION:":
					POPULATION = s.nextInt();
					break;
				case "MALE_BIRTH_CHANCE:":
					MALE_BIRTH_CHANCE = s.nextFloat();
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
				case "MAX_GROUPS:":
					MAX_GROUPS = s.nextInt();
					break;
				case "TOTAL_GROUPS:":
					TOTAL_GROUPS = s.nextInt();
					break;
				case "ADOPTION_PROBABILITY:":
					ADOPTION_PROBABILITY = s.nextInt();
					break;
				case "MIN_REPRODUCTIVE_AGE:":
					MIN_REPRODUCTIVE_AGE = s.nextInt();
					break;
				case "MAX_REPRODUCTIVE_AGE:":
					MAX_REPRODUCTIVE_AGE = s.nextInt();
					break;
				default:
					break;
				}
			}
			s.close();
		}catch(FileNotFoundException e){
		}
	}

	private void dp(String input){
		if(DEBUG){
			System.out.println(input);
		}
	}
}
