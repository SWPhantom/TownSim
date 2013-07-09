//Class that oversees family creation, designations, and family tree creation.

import java.util.*;
public class LineageGenerator
{
    //DECLARATIONS:
    ArrayList<ArrayList<DNDHuman>> families = new ArrayList<ArrayList<DNDHuman>>();
    HumanDatabase database = new HumanDatabase();
    Random randNum = new Random();

    //CONSTRUCTORS:
    public LineageGenerator(){}

    //METHODS:
    //Helpers:
    public void generateAll(int famNum, int popNum, Parser runtime){
        generateFamilies(famNum, runtime);
        generateHumans(famNum, popNum, runtime);
    }

    /**
     * Method generateFamilies
     * Seeds one Human per family/household.
     *
     * @param famNum The number families that has been designated.
     * @param runtime This is the Parser object that gets names.
     */
    public void generateFamilies(int famNum, Parser runtime){

        //System.out.println("DEBUG : Family names generating...");
        for(int i = 0 ; i < famNum ; ++i){
            int temp = randNum.nextInt(1);
            DNDHuman a = new DNDHuman(runtime.getFirstName(), runtime.getLastName(), temp);
            //GC OVERHEAD LIMIT EXCEEDED FOR ARRAYLIST WHEN ADDING IN EXCESS OF 2 000 000 PEOPLE.
            families.add(new ArrayList<DNDHuman>());
            families.get(i).add(a);
            database.add(a);
        }
        //System.out.println("DEBUG : Family names generating...DONE!");
    }

    /**
     * Method generateHumans
     * Seeds the ArrayLists that already have a single Human with the
     * rest of the Humans.
     *
     * @param famNum (int) The number families that has been designated.
     * @param popNum (int) The number people that has been designated.
     * @param runtime (Parser) This is the Parser object that gets names.
     */
    public void generateHumans(int famNum, int popNum, Parser runtime){
        //System.out.println("DEBUG : Filling families...");
        for(int i = 0 ; i < popNum-famNum; ++i){
            int temp = randNum.nextInt(famNum);
            String lastName = families.get(temp).get(0).getLastName();
            int genderTemp = randNum.nextInt(2);
            DNDHuman a = new DNDHuman(runtime.getFirstName(genderTemp), lastName ,genderTemp);
            database.add(a);
            families.get(temp).add(a);
        }
        //System.out.println("DEBUG : Filling families...DONE!");
    }

    /**
     * Method setMassClassification
     * Assigns a new classification to multiple DNDHumans. Will only affect DNDHumans 
     * with current classification set to "None."
     *
     * @param numberToBeAssigned (int) The number of people that will have the new Designation.
     * @param newClassification (String) The designation that will be assigned to DNDHuman objects.
     */
    public void setMassClassification(int numberToBeAssigned, String newClassification){
        //Set up a list of eligible people for classification. 
        //Eligible people are those with classification set to "None"
        ArrayList<Integer> eligibleIDs = new ArrayList<Integer>();
        for (DNDHuman human:database.getHumans()){
            if (human.getClassification().equals("None")){
                eligibleIDs.add(human.getID());
            }
        }

        //If there's not enough eligible people, do nothing and break out
        if (eligibleIDs.size() < numberToBeAssigned){
            return;
        }

        //Next block actually assigns classifications to humans
        ArrayList<Integer> ineligibleIDs = new ArrayList<Integer>();
        for (int i=0; i<numberToBeAssigned;){
            boolean eligibleID=false;
            int chosenHumanIndex=randNum.nextInt(eligibleIDs.size());
            int chosenHumanID=eligibleIDs.get(chosenHumanIndex);
            while (eligibleID==false){
                if (ineligibleIDs.contains(chosenHumanID) == false){
                    eligibleID=true;
                }
                else{
                    chosenHumanIndex=randNum.nextInt(eligibleIDs.size());
                    chosenHumanID=eligibleIDs.get(chosenHumanIndex);
                }
            }
            database.getID(chosenHumanID).setClassification(newClassification);
            ineligibleIDs.add(chosenHumanID);
            i++;
        }

    }

    /**
     * Method clearArray
     * Clears all the elements in the nested ArrayLists.
     */
    public void clearArray(){
        for(int i = families.size()-1; i >= 0; --i){
            families.get(i).clear();
        }
        families.clear();
    }

    //Getters:
    public ArrayList<ArrayList<DNDHuman>> getFullList(){
        return families;
    }

    //Ouput:
    public String displayAll(){
        String output = "";
        for(ArrayList<DNDHuman> a : families){
            output += "Family of "+a.get(0).getLastName()+"\n";
            for(DNDHuman human : a){
                output += human.toString();
            }
            output += "\n";
        }
        return output;
    }

    public String outputAll(){
        String output = "";
        for(ArrayList<DNDHuman> a : families){
            for(DNDHuman human : a){
                output += human.toOutput();
            }
        }
        return output;
    }

    public String displayAllDatabase(){
        String output = "BEGIN DEBUG\n";
        for (DNDHuman human: database.getHumans()){
            output += human.toString();
        }
        return output;
    }
}
