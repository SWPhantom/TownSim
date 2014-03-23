/**
 * 
 */
package in.dangerbear.crafting;

import java.util.ArrayList;


public class Human extends Node {
////DECLARATIONS///////////////////////////////////////////////////////////////
	////Statics
	public final static int FEMALE = 1;
	public final static int MALE = 0;
	
	////Class Variables
	public String lastName;
	public String firstName;
	public int gender;
	public int age;
	public int fatherID = -1;
	public int motherID = -1;
	public ArrayList<Integer> children;
	public ArrayList<Integer> relations;
	
	
////CONSTRUCTORS///////////////////////////////////////////////////////////////
	public Human() {
		super();
		children = new ArrayList<Integer>();
	}
	
	/**
	 * @param lastName
	 * @param firstName
	 */
	public Human(String lastName, String firstName, int gender) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.gender = gender;
		children = new ArrayList<Integer>();
		relations = new ArrayList<Integer>();
	}
	
	/**
	 * @param lastName
	 */
	public Human(String lastName) {
		super();
		this.lastName = lastName;
		children = new ArrayList<Integer>();
		relations = new ArrayList<Integer>();
	}
	
	
////METHODS////////////////////////////////////////////////////////////////////
	public int getFatherID() {
		return fatherID;
	}

	public int getMotherID() {
		return motherID;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	
	/**
	 * @return (String) Full name of the human.
	 */
	public String getName() {
		return firstName + " " + lastName;
	}
	
	
	/**
	 * @return (ArrayList<int>) A list of IDs, which are object's offspring.
	 */
	public ArrayList<Integer> getChildren() {
		return children;
	}
	
	public int getNumChildren() {
		return children.size();
	}
	
	public ArrayList<Integer> getSocialConnections(){
		return relations;
	}
	
	public int getAge() {
		return age;
	}
	
	public int getGender() {
		return gender;
	}
	
	public int getNumFamilyConnections(){
		int output = children.size();
		if(this.getMotherID() != -1){
			output++;
		}
		if(this.getFatherID() != -1){
			output++;
		}
		return output;
	}
	
	public boolean hasRelationship(int target) {
		for(int i = 0; i < relations.size(); ++i){
			if(relations.get(i) == target) return true;
		}
		return false;
	}

	public void setFatherID(int fatherID) {
		this.fatherID = fatherID;
	}

	public void setMotherID(int motherID) {
		this.motherID = motherID;
	}
	
	public void addChild(int childID) {
		children.add(childID);
	}
	
	public void addRelationship(int humanID) {
		relations.add(humanID);
		
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Human [lastName=" + lastName + ", firstName=" + firstName + "]";
	}

	
}
