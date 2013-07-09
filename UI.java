//This class does some user interface work, along with pushing name
//filepaths into ArrayLists (SHOULD BE PUT ELSEWHERE, PROBABLY, OR
//MERGED WITH SOME CLASS).

import java.util.*;
public class UI
{
    //DECLARATIONS:
    final int LAST_NAMES = 0;
    final int FIRST_NAMES_FEMALE = 1;
    final int FIRST_NAMES_MALE = 2;
    ArrayList<String> lastNames = new ArrayList<String>();
    ArrayList<String> firstNamesFemale = new ArrayList<String>();
    ArrayList<String> firstNamesMale = new ArrayList<String>();
    Scanner key = new Scanner(System.in);
    Parser runtime = new Parser();
    LineageGenerator famGen = new LineageGenerator();

    //CONSTRUCTORS:
    public UI(){}

    //METHODS:
    /**
     * Method generatePeople
     * Helper method that asks for user input and puts said input into
     * use in the LineageGenerator object.
     */
    public void generatePeople(){
        famGen.clearArray();
        System.out.println("\n");
        System.out.println("Enter the number of families:");
        int familyNumber = key.nextInt();
        System.out.println("Enter the number of people:");
        int popSize = key.nextInt();
        this.generatePeople(familyNumber, popSize);
    }

    public void generatePeople(int familyNumber, int popSize){
        famGen.clearArray();
        famGen.generateAll(familyNumber, popSize, runtime);
    }

    /**
     * Method chooseNameList
     * A method that asks for user input and switches the active name
     * ArrayLists depending on the choice.
     */
    public void chooseNameList(){
        this.internal_output_databaseChoices();
        int input = key.nextInt();
        while(input < 1 || input > 5){
            System.out.println("Wrong entry. Enter a number (1-5).");
            System.out.println("Try again.");
            input = key.nextInt();
        }
        runtime.selectDatabase(input-1);
    }

    public String debugOutput(){
        return famGen.displayAll();
    }
   
    public String output(){
        return famGen.outputAll();
    }
    

    public String outputDatabase(){
        return famGen.displayAllDatabase();
    }

    //INTERNAL METHODS:
    public void internal_output_databaseChoices(){
        System.out.println("\n\n\nChoose which names module to use.");
        System.out.println("Enter number from 1 to 5):");
        System.out.println("1 - Common US names.");
        System.out.println("2 - South-Eastern names.");
        System.out.println("3 - Italian names.");
        System.out.println("4 - North African names.");
        System.out.println("5 - SSQCH names.");
    }

}
