package in.dangerbear.crafting;


import java.io.*;

import java.util.*;
public class Parser
{
	//STATICS:
	public final static int MALE = 0;
	public final static int FEMALE = 1;
	public final static int LAST = 2;
	
	//DECLARATIONS:
	public ArrayList<String> lastNames;
	public ArrayList<String> firstNamesMale;
	public ArrayList<String> firstNamesFemale;
	Random randomGen;

	//CONSTRUCTORS:
	/**
	 * Parser Constructor
	 * Creates a Parser object and initializes the name ArrayLists and
	 * a Random object.
	 */
	public Parser(){
		lastNames = new ArrayList<String>();
		firstNamesMale = new ArrayList<String>();
		firstNamesFemale = new ArrayList<String>();
		randomGen = new Random();
	}

	//METHODS:
	//Names:
	
	public ArrayList<String> getNameList(int choice){
		if(choice == MALE){//Male first names
			return firstNamesMale;
		}else if(choice == FEMALE){//Female first names
			return firstNamesFemale;
		}else if(choice == LAST){//Last names
			return lastNames;
		}else{
			System.out.println("Problem with Parser->getNameList");
			return null;
		}
	}
	
	//Helpers:
	/**
	 * Method feedInput
	 * Fills Specified ArrayList with names parsed from a text file.
	 * Each last name/first name is on a new line.
	 * 
	 * @param filepath (String) A filepath to a file filled with last names or first names.
	 * @param choice (int) A number that specifies which name ArrayList to dump the data into.
	 */
	public void feedInput(String filepath, int choice){
		String fullPath = filepath;
		if(choice == MALE){//Male first names
			fullPath += "_FirstNames_Male.txt";
		}else if(choice == FEMALE){//Female first names
			fullPath += "_FirstNames_Female.txt";
		}else if(choice == LAST){//Last names
			fullPath += "_LastNames.txt";
		}
		File file = new File(fullPath);
		try{
			Scanner s = new Scanner(file);

			if(choice == MALE){
				firstNamesMale.clear();
				while (s.hasNext()){
					firstNamesMale.add(s.nextLine());
				}
			}
			if(choice == FEMALE){
				firstNamesFemale.clear();
				while (s.hasNext()){
					firstNamesFemale.add(s.nextLine());
				}
			}
			if(choice == LAST){
				lastNames.clear();
				while (s.hasNext()){
					lastNames.add(s.nextLine());
				}
			}
			s.close();
		}catch(FileNotFoundException x){
			System.out.println("Error in Parser feedInput!");
			x.printStackTrace();
		}
	}

	//Debug:
	public String DEBUG_arraySizes(){
		return "DEBUG : Last names size: "+lastNames.size()+". Male names size: "+firstNamesMale.size()+". Female names size: "+firstNamesFemale.size();
	}
}