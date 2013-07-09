//Class meant for parsing files and putting them into usable data structures.
//Used to obtain random and constrained names and the like.

import java.io.*;
import java.util.*;
public class Parser
{
    //DECLARATIONS:
    ArrayList<String> lastNamesDB = new ArrayList<String>();
    ArrayList<String> firstNamesFemaleDB = new ArrayList<String>();
    ArrayList<String> firstNamesMaleDB = new ArrayList<String>();
    public ArrayList<String> lastNames;
    public ArrayList<String> firstNamesMale;
    public ArrayList<String> firstNamesFemale;
    Random randomGen;

    //CONSTRUCTORS:
    /**
     * Parser Constructor
     * Creates a Parser object and initializes the name ArrayLists and
     * a Random object. Also puts all filepaths into relevant ArrayLists.
     */
    public Parser(){
        //Push all filenames into the correct ArrayLists.
        //First index
        lastNamesDB.add("Names/US_LastNames.txt");
        firstNamesFemaleDB.add("Names/US_FirstNames_Female.txt");
        firstNamesMaleDB.add("Names/US_FirstNames_Male.txt");
        //Second
        lastNamesDB.add("Names/SouthEast_LastNames.txt");
        firstNamesFemaleDB.add("Names/SouthEast_FirstNames_Female.txt");
        firstNamesMaleDB.add("Names/SouthEast_FirstNames_Male.txt");
        //Third
        lastNamesDB.add("Names/Italy_LastNames.txt");
        firstNamesFemaleDB.add("Names/Italy_FirstNames_Female.txt");
        firstNamesMaleDB.add("Names/Italy_FirstNames_Male.txt");
        //Fourth
        lastNamesDB.add("Names/NorthAfrica_LastNames.txt");
        firstNamesFemaleDB.add("Names/NorthAfrica_FirstNames_Female.txt");
        firstNamesMaleDB.add("Names/NorthAfrica_FirstNames_Male.txt");
        //Fifth
        lastNamesDB.add("Names/SSQCH_LastNames.txt");
        firstNamesFemaleDB.add("Names/SSQCH_FirstNames_Female.txt");
        firstNamesMaleDB.add("Names/SSQCH_FirstNames_Male.txt");

        lastNames = new ArrayList<String>();
        firstNamesMale = new ArrayList<String>();
        firstNamesFemale = new ArrayList<String>();
        randomGen = new Random();
        this.selectDatabase(0);
    }

    //METHODS:
    //Names:
    public String getLastName(){
        return lastNames.get(randomGen.nextInt(lastNames.size()));
    }

    public String getFirstName(){
        int temp = randomGen.nextInt(2);
        if(temp == 0){
            return firstNamesFemale.get(randomGen.nextInt(firstNamesFemale.size()));
        }else{
            return firstNamesMale.get(randomGen.nextInt(firstNamesMale.size()));
        }
    }


    public String getFirstName(int a){
        if(a == 0){
            return firstNamesFemale.get(randomGen.nextInt(firstNamesFemale.size()));
        }else{
            return firstNamesMale.get(randomGen.nextInt(firstNamesMale.size()));
        }
    }


    public String getFirstName(String lastName){
        int temp = randomGen.nextInt(2);
        if(temp == 0){
            return firstNamesFemale.get(randomGen.nextInt(firstNamesFemale.size())) 
            + " " + lastName;
        }else{
            return firstNamesMale.get(randomGen.nextInt(firstNamesMale.size())) 
            + " " + lastName;
        }
    }


    public String getFirstName(String lastName, int a){
        if(a == 0){
            return firstNamesFemale.get(randomGen.nextInt(firstNamesFemale.size()));
        }else{
            return firstNamesMale.get(randomGen.nextInt(firstNamesMale.size()));
        }
    }

    //Helpers:

    /**
     * Method selectDatabase
     * Fills all ArrayLists with the same database.
     * 
     * @param choice (int) Specifies which databases to use.
     */
    public void selectDatabase(int choice){
        File fileLN = new File(lastNamesDB.get(choice));
        File fileFNF = new File(firstNamesFemaleDB.get(choice));
        File fileFNM = new File(firstNamesMaleDB.get(choice));
        try{
            Scanner s1 = new Scanner(fileLN);
            Scanner s2 = new Scanner(fileFNF);
            Scanner s3 = new Scanner(fileFNM);

            lastNames.clear();
            while (s1.hasNext()){
                lastNames.add(s1.nextLine());
            }
            firstNamesFemale.clear();
            while (s2.hasNext()){
                firstNamesFemale.add(s2.nextLine());
            }
            firstNamesMale.clear();
            while (s3.hasNext()){
                firstNamesMale.add(s3.nextLine());
            }
            s1.close();
            s2.close();
            s3.close();

        }catch(FileNotFoundException x){
            x.printStackTrace();
        }catch (IOException x){
            x.printStackTrace();
        }
    }

    /**
     * Method selectIndividualDatabase
     * Lets user choose a particular name database for the last name/first names.
     * 
     * @param choiceDB (int) Specifies which database to use.
     * @param choice (int) A number that specifies which name ArrayList to dump the data into.
     */
    public void selectIndividualDatabase(int choiceDB, int choice){
        File file;
        try{
            Scanner s;
            if(choice == 0){
                file = new File(lastNames.get(choiceDB));
                s = new Scanner(file);
                lastNames.clear();
                while (s.hasNext()){
                    lastNames.add(s.nextLine());
                }
                s.close();
            }
            if(choice == 1){
                file = new File(firstNamesFemale.get(choiceDB));
                s = new Scanner(file);
                firstNamesFemale.clear();
                while (s.hasNext()){
                    firstNamesFemale.add(s.nextLine());
                }
                s.close();
            }
            if(choice == 2){
                file = new File(firstNamesMale.get(choiceDB));
                s = new Scanner(file);
                firstNamesMale.clear();
                while (s.hasNext()){
                    firstNamesMale.add(s.nextLine());
                }
                s.close();
            }                

        }catch(FileNotFoundException x){
            x.printStackTrace();
        }catch (IOException x){
            x.printStackTrace();
        }
    }

    //Debug:
    public String DEBUG_arraySizes(){
        return "DEBUG : Last names size: "+lastNames.size()
        +". Male names size: "+firstNamesMale.size()
        +". Female names size: "+firstNamesFemale.size();
    }

    public String getFemName(){
        String temp = "";
        temp += firstNamesFemale.get(randomGen.nextInt(firstNamesFemale.size()));
        temp += " ";
        temp += lastNames.get(randomGen.nextInt(lastNames.size()));
        return temp;
    }


    public String getMaleName(){
        String temp = "";
        temp += firstNamesMale.get(randomGen.nextInt(firstNamesMale.size()));
        temp += " ";
        temp += lastNames.get(randomGen.nextInt(lastNames.size()));
        return temp;
    }


    /**
     * Method getFemaleName
     *
     * @param lastName (String) Specified last name. Forces next name to have this last name
     * @return (String) A full name (female).
     */
    public String getFemName(String lastName){
        String temp = "";
        temp += firstNamesFemale.get(randomGen.nextInt(firstNamesFemale.size()));
        temp += " ";
        temp += lastName;
        return temp;
    }


    /**
     * Method getMaleName
     *
     * @param lastName (String) Specified last name. Forces next name to have this last name
     * @return (String) A full name (male).
     */
    public String getMaleName(String lastName){
        String temp = "";
        temp += firstNamesMale.get(randomGen.nextInt(firstNamesMale.size()));
        temp += " ";
        temp += lastName;
        return temp;
    }
}