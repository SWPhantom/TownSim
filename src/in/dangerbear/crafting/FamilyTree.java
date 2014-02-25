/**
 * 
 */
package in.dangerbear.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author SWPhantom
 *
 */



/**
 * @author zfrolov
 *
 */
public class FamilyTree {	
////DECLARATIONS///////////////////////////////////////////////////////////////
	////Statics
	public final static int FEMALE = 1;
	public final static int MALE = 0;
	public final static int MIN_REPRORUCTIVE_AGE = 15;
	
	////Class variables
	ArrayList<Human> humans;
	ArrayList<String> males;
	ArrayList<String> females;
	ArrayList<String> lasts;
	Random rand = new Random();
	
////CONSTRUCTORS///////////////////////////////////////////////////////////////
	public FamilyTree() {
		humans = new ArrayList<Human>();
		males = new ArrayList<String>();
		females = new ArrayList<String>();
		lasts = new ArrayList<String>();
		
	}
	
////METHODS////////////////////////////////////////////////////////////////////
	
	/**
	 * Method generate
	 * Generates the eligible last names, generates the Humans with random
	 * gender assignment and names.
	 * 
	 * @param families (int) The number of families to generate (last names).
	 * @param people (int) The number of total people to generate.
	 * @param males (ArrayList<String>) List of male first names.
	 * @param females (ArrayList<String>) List of female first names.
	 * @param lasts (ArrayList<String>) List of last names.
	 */
	public void generate(int families, int people, ArrayList<String> males, ArrayList<String> females, ArrayList<String> lasts) {
		this.males = males;
		this.females = females;
		this.lasts = lasts;
		String tempLasts[] = new String[families];
		String tempLastName;
		String tempFirstName;
		
		for(int i = 0; i < families; ++i){
			tempLasts[i] = lasts.get(rand.nextInt(lasts.size()));
		}
		lasts.clear();
		
		for(int i = 0; i < people; ++i){
			tempLastName = tempLasts[rand.nextInt(tempLasts.length)];
			if(rand.nextInt(2) == 0){//Male created
				tempFirstName = males.get(rand.nextInt(males.size()));
				humans.add(new Human(tempLastName, tempFirstName, MALE));
			}else{//Female created
				tempFirstName = females.get(rand.nextInt(females.size()));
				humans.add(new Human(tempLastName, tempFirstName, FEMALE));
			}
		}
		males.clear();
		females.clear();
		
		ages(people);
	}
	
	/**
	 * Method interconnect
	 * Starts attempting to create parent/child connections for every
	 * Human. Using the value returned by helper function makeConnection,
	 * this attempts to stop connections ASAP.
	 * 
	 * TODO: Transition to eligibility queues/lists.
	 * 
	 * @param maxOffspring (int) Uses this is a ceiling for the number of kids
	 *        per human.
	 */
	public void interconnect(int maxOffspring) {
		for(int i = 0; i < humans.size(); ++i){
			for(int j = 0; j < rand.nextInt(maxOffspring); ++j){
				boolean keepGoing = true;
				int tempCounter = 0;
				while(keepGoing){
					int result = makeConnection(i, rand.nextInt(humans.size()));
					if(result == -1 || result == 1){//If the parent is not eligible to be a parent OR there was a successful pairing.
						keepGoing = false;
					}
					if(result == 0){//A single failure occurred
						tempCounter++;
					}
					if(tempCounter == 50){//Too many failures
						keepGoing = false;
					}
				}
			}
		}
	}

	////Helpers
	
	/**
	 * Method makeConnection
	 * Checks to see if the parent and child candidates are eligible for 
	 * pairing.
	 *  If not, return a non-'1' int. the caller function uses the return for
	 * action.
	 *  If so, connect both Humans via parentID and childID fields and
	 * return 1.
	 * 
	 * TODO: Transition to eligibility queues/lists.
	 * 
	 * @param parent (int) referencing the ID number of parent.
	 * @param child (int) referencing the ID number of child.
	 * @return (int) The following is what the caller function knows:
	 * 				-1: Failure: End attempts to connect these two now.
	 * 				 0: Failure: Try to attempt connections again.
	 * 				 1: Success: End attempts to connect these two now.
	 */
	private int makeConnection(int parent, int child) {
		boolean fatherless = humans.get(child).getFatherID() == -1;
		boolean motherless = humans.get(child).getMotherID() == -1;
		int parentGender = humans.get(parent).getGender();
		
		//If the parent is not of eligible reproductive age, terminate now.
		if(humans.get(parent).getAge() < MIN_REPRORUCTIVE_AGE){
			return -1;
		}
		if((!motherless && !fatherless) || (!fatherless && parentGender == MALE) || (!motherless && parentGender == FEMALE)){
			return 0;
		}else if(parentGender == MALE){
			humans.get(child).setFatherID(parent);
			humans.get(parent).addChild(child);
			return 1;
		}else if(parent == FEMALE){
			humans.get(child).setMotherID(parent);
			humans.get(parent).addChild(child);
			return 1;
		}
		return 0;
	}
	
	
	/**
	 * Method ages
	 * This creates a distribution of ages and assigns them to the Human
	 * objects.
	 * 
	 * TODO: Change 100 to a static final field, defined elsewhere. This is
	 *       to simulate different age ranges.
	 * 
	 * @param population (int) defines how many ages to generate.
	 */
	private void ages(int population){
		int ages[] = new int[population];
		for(int i = 0; i < population; ++i){
			ages[i] = rand.nextInt(100);
		}
		
		Arrays.sort(ages);
		
		for(int i = 0; i < population; ++i){
			humans.get(i).setAge(ages[population - 1 - i]); 
			
		}
	}

	////Debug
	
	/**
	 * Method DEBUG_PRINT
	 * Current desired output:
	 * 
	 * Martinella Stranbii(99)
	 *
	 * Brighinzone Stranbii(90)
	 *  Children:
  	 *   Baccone de Calce
	 *   Giollius de Calce
	 *   Giovanna de Calce
	 *   Cambius Simonis
	 *   Berardus Doberti
	 * 
	 * Cambius Simonis(88)
	 */
	public void DEBUG_PRINT() {
		for(int i = 0; i < humans.size(); ++i){
			System.out.println(humans.get(i).getName() + "(" + humans.get(i).getAge() + ")");
			if(humans.get(i).getNumChildren() > 0){
				System.out.println(" Children:");
				for(int j = 0; j < humans.get(i).getNumChildren(); ++j){
					System.out.println("  " + humans.get(humans.get(i).children.get(j)).getName());
				}
			}
			System.out.println();
		}
	}

}
