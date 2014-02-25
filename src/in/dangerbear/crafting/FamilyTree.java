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



public class FamilyTree {
	public final static int FEMALE = 1;
	public final static int MALE = 0;
	public final static int MIN_REPRORUCTIVE_AGE = 15;
	
	ArrayList<Human> humans;
	ArrayList<String> males;
	ArrayList<String> females;
	ArrayList<String> lasts;
	Random rand = new Random();
	
	public FamilyTree() {
		humans = new ArrayList<Human>();
		males = new ArrayList<String>();
		females = new ArrayList<String>();
		lasts = new ArrayList<String>();
		
	}
	
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

	private int makeConnection(int parent, int child) {
		boolean fatherless = humans.get(child).getFatherID() == -1;
		boolean motherless = humans.get(child).getMotherID() == -1;
		int parentGender = humans.get(parent).getGender();
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

	public void DEBUG_PRINT() {
		System.out.println("Mark 1" + humans.size());
		for(int i = 0; i < humans.size(); ++i){
			System.out.println(humans.get(i).getName() + "(" + humans.get(i).getAge() + ")\n Children:");
			for(int j = 0; j < humans.get(i).getNumChildren(); ++j){
				System.out.println("  " + humans.get(humans.get(i).children.get(j)).getName());
			}
		}
	}

}
