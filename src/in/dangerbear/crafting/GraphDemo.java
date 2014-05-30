/**
 * 
 */
package in.dangerbear.crafting;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author SWPhantom
 * 
 */

public class GraphDemo{
////DECLARATIONS///////////////////////////////////////////////////////////////
	////Statics

	private static final boolean DEBUG = true;

	////Class variables
	BitSet eligibilityList1;
	Random rand = new Random();
	Parser parser;

////CONSTRUCTORS///////////////////////////////////////////////////////////////
	public GraphDemo(ArrayList<Human> humans){
		eligibilityList1 = new BitSet(humans.size());
	}

////METHODS////////////////////////////////////////////////////////////////////

	/**
	 * Method startRumor Gets an initial population of infected IDs and tries to
	 * infect all neighboring nodes.
	 * 
	 * TODO: Change this to be more API-like. Consider returning an ArrayList or
	 * the percentage of infection.
	 * 
	 * @param initialVectors
	 * @param infectionProbability
	 */
	public void startRumor(ArrayList<Human> humans,
			ArrayList<Integer> initialVectors, int infectionProbability, boolean reinfect){
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
			Human infector = humans.get(infectorID);
			ArrayList<Integer> infectorConnections = new ArrayList<Integer>();

			for(Human target: humans){
				if(!eligibilityList1.get(target.ID)){
					Iterator<Integer> it = infector.groups.iterator();
					while(it.hasNext()){
						if(target.isInGroup(it.next())){
							infectorConnections.add(target.ID);
							break;
						}
					}
				}else{
					//do nothing
				}
			}

			for(int i = 0; i < infectorConnections.size(); ++i){
				boolean infected = startRumorHelper(infectorID,
						infectorConnections.get(i), infectionProbability, reinfect);
				if(infected){
					infectionOrder.add(infectorConnections.get(i));
					++totalInfected;
				}
			}
		}

		//Check the percentage of infection.
		double percent = 100.00 * (double) totalInfected
				/ (double) humans.size();
		dp("Infection analysis: " + totalInfected + "/" + humans.size() + "("
				+ percent + "%)");
	}

	/**
	 * Method startRumorHelper 
	 * 
	 * TODO: The reason this takes in infectorID is to
	 * determine connection-specific infection probability (how much the pair of
	 * people like each other, etc).
	 * 
	 * @param infectorID
	 * @param targetID
	 * @param infectionProbability
	 * @return
	 */
	private boolean startRumorHelper(int infectorID, int targetID,
			int infectionProbability, boolean reinfect){
		//Check to see if the target is already infected.
		if(eligibilityList1.get(targetID)) return false;
		//Not infected already. See if infection occurs.
		if(rand.nextInt(100) >= infectionProbability){
			if(reinfect) eligibilityList1.set(targetID);
			return false;
		}

		//Infection has occurred. Add to the infected list.
		eligibilityList1.set(targetID);
		return true;
	}

	/**
	 * Method nthConnections Returns a list of IDs that correspond to the target
	 * Human's N linked Human Nodes. ie. If you call this function on a child
	 * with two parents and no kids, the returned list will contain only his
	 * parents for N = 1. If you call the same function with N = 2, you will get
	 * a list of the child's parents AND grandparents.
	 * 
	 * @param humanID
	 * @return
	 */
	public ArrayList<Integer> nthConnections(ArrayList<Human> humans,
			int humanID, int n){
		eligibilityList1.clear();
		ArrayList<Integer> output = new ArrayList<Integer>();

		nthConnectionsHelper(humans, humanID, n);

		for(int i = 0; i < humans.size(); ++i){
			if(eligibilityList1.get(i)){
				output.add(i);
			}
		}
		return output;
	}

	/**
	 * Method printNthConnections Printing function wrapper for
	 * getNthConnection.
	 * 
	 * @param humanID
	 * @param n
	 */
	public void printNthConnections(ArrayList<Human> humans, int humanID, int n){
		dp("Printing " + humans.get(humanID).getName() + "'s " + n
				+ "connections.");
		ArrayList<Integer> outputList = this.nthConnections(humans, humanID, n);

		for(Integer i: outputList){
			dp(humans.get(i).toString());
		}
		dp("");
	}

	/**
	 * Method hasPath The method that initializes the eligibility BitSet and
	 * calls the recursive hasPathHelper function.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean hasPath(ArrayList<Human> humans, int source, int target){
		eligibilityList1.clear();
		return hasPathHelper(humans, source, target);
	}

	////Helpers

	/**
	 * Method hasPathHelper Recursive method that does a depth-first search of
	 * the graph to see if the source Human is connected to the Target Human.
	 * 
	 * @param source (int) Index/ID of the current node.
	 * @param target (int) Index/ID of the node to be reached.
	 * @return
	 */
	private boolean hasPathHelper(ArrayList<Human> humans, int source,
			int target){
		// I've been here before. No good.
		if(eligibilityList1.get(source) == true){ return false; }
		// Make sure not to come here again.
		eligibilityList1.set(source, true);

		// If this function has happened upon the target, we're done!
		if(source == target){ return true; }

		// Start checking the parents and the children.
		Human a = humans.get(source);
		if(a.getMotherID() != -1
				&& hasPathHelper(humans, a.getMotherID(), target)) return true;
		if(a.getFatherID() != -1
				&& hasPathHelper(humans, a.getFatherID(), target)) return true;
		for(int i = 0; i < a.getNumChildren(); ++i){
			if(hasPathHelper(humans, a.getChildren().get(i), target)) return true;
		}
		return false;
	}

	/**
	 * Method nthConnectionsHelper Recursive function that sets the Eligibility
	 * BitSet indeces that correspond to the Humans that are within n links from
	 * a given Human.
	 * 
	 * @param humanID
	 * @param n
	 */
	private void nthConnectionsHelper(ArrayList<Human> humans, int humanID,
			int n){
		// If this Human has been accessed already, stop!
		if(eligibilityList1.get(humanID)){ return; }

		// Add yourself to the list.
		eligibilityList1.set(humanID);

		// If you're the last connection to be checked, stop before querying
		// your connections.
		if(n == 0){ return; }

		// Stores the potential family to check.
		ArrayList<Integer> payload = new ArrayList<Integer>();

		// Get the parents.
		int temp = humans.get(humanID).getFatherID();
		if(temp != -1) payload.add(temp);
		temp = humans.get(humanID).getMotherID();
		if(temp != -1) payload.add(temp);

		// Time to add the children. Create a temp ArrayList.
		ArrayList<Integer> tempChildren = humans.get(humanID).getChildren();
		for(int i = 0; i < tempChildren.size(); ++i){
			payload.add(tempChildren.get(i));
		}

		for(int i = 0; i < payload.size(); ++i){
			nthConnectionsHelper(humans, payload.get(i), n - 1);
		}
	}

	////Debug

	/**
	 * Method displayMemory Shows off the memory used at the moment.
	 * 
	 */
	public void displayMemory(){
		Runtime r = Runtime.getRuntime();
		r.gc();
		r.gc();
		dp("Memory Used=" + (r.totalMemory() - r.freeMemory()));
	}

	/**
	 * Method DEBUG_PRINT Current desired output:
	 * 
	 * Martinella Stranbii(99) Parents: F Man V Woman
	 * 
	 * Brighinzone Stranbii(90) Parents: Billman Toad Billwoman Toad Children:
	 * Baccone de Calce (80) Giollius de Calce (80) Giovanna de Calce (23)
	 * Cambius Simonis (23) Berardus Doberti (43)
	 * 
	 * Cambius Simonis(88) Parents: Ciaman Man Freudwoman Woman
	 */
	public void DEBUG_PRINT(ArrayList<Human> humans){
		for(Human target: humans){
			Human family;
			dp(target.getName() + "(" + target.getAge() + ")");
			dp(" Parents:");
			if(target.getFatherID() != -1){
				family = humans.get(target.getFatherID());
				dp("  " + family.getName() + "(" + family.getAge() + ")");
			}else{
				dp("  NO FATHER");
			}

			if(target.getMotherID() != -1){
				family = humans.get(target.getMotherID());
				dp("  " + family.getName() + "(" + family.getAge() + ")");
			}else{
				dp("  NO MOTHER");
			}

			if(target.getNumChildren() > 0){
				dp(" Children:");
				for(int j = 0; j < target.getNumChildren(); ++j){
					family = humans.get(target.children.get(j));
					dp("  " + family.getName() + "(" + family.getAge() + ")");
				}
			}
			dp("");
		}
	}

	/**
	 * Method DEBUG_CONNECTIONS_PRINT Picks two random nodes and runs hasPath on
	 * them. Prints the result.
	 * 
	 * @param tests
	 */
	public void DEBUG_PRINT_CONNECTIONS(ArrayList<Human> humans, int tests){
		dp("Making many random connection queries:");
		int a;
		int b;
		for(int i = 0; i < tests; ++i){
			a = rand.nextInt(humans.size());
			b = rand.nextInt(humans.size());
			dp("Are " + humans.get(a).getName() + " and "
					+ humans.get(b).getName() + " connected? ");
			if(hasPath(humans, a, b)){
				dp("Yes");
			}else{
				dp("No");
			}
		}
	}

	public void DEBUG_PRINT_GROUPS(ArrayList<Human> humans){
		for(Human target: humans){
			StringBuilder output = new StringBuilder();
			output.append(target.getName());
			output.append(": ");
			for(Integer group: target.groups){
				output.append(group);
				output.append(", ");
			}
			output.append("\n");
			dp(output.toString());
		}
	}

	private void dp(String input){
		if(DEBUG){
			System.out.println(input);
		}
	}

}
