//This class reresents human units.

import java.util.*;
public class Human
{
	//DECLARATIONS:
	public final byte FEMALE = 0;
	public final byte MALE = 1;
	public final byte NONE = -1;
	public static long GlobalID = 0;
	public long ID;
	public Name name;
	public byte gender;
	public int age;
	public long mother;
	public long father;

	//CONSTRUCTORS:
	public Human(){
		this.name = new Name("default");
		this.ID = GlobalID;
		++GlobalID;
	}

/*	public Human(String name, byte gender){
		this.name = new Name(name);
		this.ID = GlobalID;
		++GlobalID;
		this.gender = gender;
	}
*/

	public Human(String fName, String lName, byte gender){
		this.name = new Name(fName, lName);
		this.ID = GlobalID;
		++GlobalID;
		this.gender = gender;
	}

	//METHODS:
	//Setters:
	public void setFirstName(String name){
		this.name.setFirstName(name);
	}


	public void setLastName(String lName){
		this.name.setLastName(lName);
	}

	public void setAge(int age){
		this.age = age;
	}

	//Getters:
	public long getID(){
		return ID;
	}

	public long getGlobalID(){
		return GlobalID;
	}

	public int getAge(){
		return age;
	}

	public int getGender(){
		return gender;
	}

	public String getFirstName(){
		return name.getFirstName();
	}

	public String getLastName(){
		return name.getLastName();
	}

	public String getFullName(){
		return name.getFullName();
	}

	//Debug:
	public String toString(){
		String output = "DEBUG : ID: "+this.ID+" ; NAME: "+this.getFullName();
		return output;
	}
}
