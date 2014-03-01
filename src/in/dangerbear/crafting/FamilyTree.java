/**
 * 
 */
package in.dangerbear.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;

/**
 * @author SWPhantom
 *
 */

public class FamilyTree {	
////DECLARATIONS///////////////////////////////////////////////////////////////
	////Statics
	public final static int FEMALE = 1;
	public final static int MALE = 0;
	public final static int MIN_REPRODUCTIVE_AGE = 15;
	
	////Class variables
	ArrayList<Human> humans;
	BitSet eligibilityList1;
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
		eligibilityList1 = new BitSet(people);
		String tempLasts[] = new String[families];
		String tempLastName;
		String tempFirstName;
		
		for(int i = 0; i < families; ++i){
			//
			// TODO: make sure last names don't repeat
			// 
			//  pop used last name from the list
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
		
		//Generate the ages of the populace.
		ages(people);
	}
	
	/**
	 * Method interconnect
	 * Starts attempting to create parent/child connections for every
	 * Human. Using the value returned by helper function makeConnection,
	 * this attempts to stop connections ASAP.
	 * 
	 * TODO: Transition to eligibility queues/lists.
	 * TODO: maxOffspring should be a config-based variable.
	 * 
	 * @param maxOffspring (int) Uses this is a ceiling for the number of kids
	 *        per human.
	 */
	public void interconnect(int maxOffspring) {
		for(int i = 0; i < humans.size(); ++i){
			Human parent = humans.get(i);
			//Hit the lowest age. No one after this can have children. End!
			if(parent.getAge() < MIN_REPRODUCTIVE_AGE){
				return;
			}
			
			//Set everything up to the current human's ID to false. They are all older.
			eligibilityList1.clear();
			eligibilityList1.set(i + 1, humans.size(), true);
			
			//Begin elimination of potential child connections.
			for(int j = i; j < humans.size(); ++j){
				Human child = humans.get(j);
				
				//Check for sameness.
				if(i == j){
					eligibilityList1.set(j, false);
				}
				//Check for age difference.
				if(parent.getAge() - child.getAge() < MIN_REPRODUCTIVE_AGE){
					eligibilityList1.set(j, false);
				}
				
				//Check for name difference.
				//TODO: Maybe throw in some randomness to this.
				//	Some people can adopt?
				if(!parent.getLastName().equals(child.getLastName())){
					eligibilityList1.set(j, false);
				}
				
				//Check for existing parenthood.
				if(parent.getGender() == MALE && child.getFatherID() != -1){
					eligibilityList1.set(j, false);
				}
				if(parent.getGender() == FEMALE && child.getMotherID() != -1){
					eligibilityList1.set(j, false);
				}
			}
			
			//If no other humans are eligible, move onto the next potential parent.
			if(eligibilityList1.cardinality() <= 0){
				continue;
			}
			
			//Create the list of ids of eligible chilren.
			ArrayList<Integer> eligibleChildren = new ArrayList<Integer>();
			for(int j = i; j < eligibilityList1.size(); ++j){
				if(eligibilityList1.get(j)){
					eligibleChildren.add(j);
				}
			}
			
			//Attempt to make a random amount of connections between the current
			//node and the eligible children nodes.
			for(int j = 0; j < rand.nextInt(maxOffspring); ++j){
				//There's only one eligible unit left! Connect and stop.
				if(eligibleChildren.size() == 1){
					makeConnection(i, eligibleChildren.get(0));
					break;
				}
				
				//Connect the ith person and someone from their eligible list.
				int index = rand.nextInt(eligibleChildren.size());
				makeConnection(i, eligibleChildren.get(index));
				eligibleChildren.remove(index);
			}
		}
	}

	
	/**
	 * Method nthConnections
	 * Returns a list of IDs that correspond to the target Human's N linked
	 * Human Nodes.
	 * ie. If you call this function on a child with two parents and no kids,
	 * the returned list will contain only his parents for N = 1.
	 * If you call the same function with N = 2, you will get a list of the
	 * child's parents AND grandparents.
	 * 
	 * @param humanID
	 * @return
	 */
	public ArrayList<Integer> nthConnections(int humanID, int n){
		eligibilityList1.clear();
		ArrayList<Integer> output = new ArrayList<Integer>();
		
		nthConnectionsHelper(humanID, n);
		
		
		for(int i = 0; i < humans.size(); ++i){
			if(eligibilityList1.get(i)){
				output.add(i);
			}
		}
		return output;
	}
	
	/**
	 * Method printNthConnections
	 * 
	 * 
	 * @param humanID
	 * @param n
	 */
	public void printNthConnections(int humanID, int n){
		System.out.println("Printing " + humans.get(humanID).firstName + " " + humans.get(humanID).lastName + "'s " + n + "connections.");
		ArrayList<Integer> outputList = this.nthConnections(humanID, n);
		
		for(Integer i: outputList){
			System.out.println(humans.get(i).toString());
		}
		System.out.println();
	}


	////Helpers
	
	/**
	 * Method makeConnection
	 * Connects the parent and child nodes, according to parameter IDs.
	 * Makes sure the genders match on the parent connections.
	 * 
	 * @param parent (int) referencing the ID number of parent.
	 * @param child (int) referencing the ID number of child.
	 * @return (int)  0: Failure.
	 * 				  1: Success.
	 */
	private int makeConnection(int parent, int child) {
		int parentGender = humans.get(parent).getGender();
		
		if(parentGender == MALE){
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
			//ages[i] = (rand.nextGaussian()/3);
		}
		
		Arrays.sort(ages);
		
		for(int i = 0; i < population; ++i){
			humans.get(i).setAge(ages[population - 1 - i]);
		}
	}
	
	/**
	 * Method nthConnectionsHelper
	 * 
	 * 
	 * @param humanID
	 * @param n
	 */
	private void nthConnectionsHelper(int humanID, int n){
		//If this Human has been accessed already, stop!
		if(eligibilityList1.get(humanID)){
			return;
		}
		
		//Add yourself to the list.
		eligibilityList1.set(humanID);
		
		//If you're the last connection to be checked, stop before querying your connections.
		if(n == 0){
			return;
		}
		
		//Stores the potential familiy to check.
		ArrayList<Integer> payload = new ArrayList<Integer>();
		
		//Get the parents.
		int temp = humans.get(humanID).getFatherID();
		if(temp != -1) payload.add(temp);
		temp = humans.get(humanID).getMotherID();
		if(temp != -1) payload.add(temp);
		
		//Time to add the children. Create a temp ArrayList.
		ArrayList<Integer> tempChildren = humans.get(humanID).getChildren();
		for(int i = 0; i < tempChildren.size(); ++i){
			payload.add(tempChildren.get(i));
		}
		
		for(int i = 0; i < payload.size(); ++i){
			nthConnectionsHelper(payload.get(i), n - 1);
		}
	}

	////Debug
	
	/**
	 * Method displayMemory
	 * Shows off the memory used at the moment.
	 * 
	 */
	public static void displayMemory(){
	    Runtime r=Runtime.getRuntime();
	    r.gc();
	    r.gc();
	    System.out.println("Memory Used="+(r.totalMemory()-r.freeMemory()));
	}
	
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
