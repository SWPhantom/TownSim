//This class reresents human units.

import java.util.*;
public class Human
{
    //DECLARATIONS:
    public final int FEMALE = 0;
    public final int MALE = 1;
    public ArrayList<Human> hateList;
    public ArrayList<Human> likeList;
    public static int GlobalID =0;
    public int ID;
    public Name name;
    public int gender;

    //CONSTRUCTORS:
    public Human() 
    {
        this.name = new Name("default");
        this.ID = GlobalID;
        ++GlobalID;
        hateList = new ArrayList<Human>();
        likeList = new ArrayList<Human>();
    }

    public Human(String name, int gender){
        this.name = new Name(name);
        this.ID = GlobalID;
        ++GlobalID;
        this.gender = gender;
        hateList = new ArrayList<Human>();
        likeList = new ArrayList<Human>();
    }

    public Human(String fName, String lName, int gender){
        this.name = new Name(fName, lName);
        this.ID = GlobalID;
        ++GlobalID;
        this.gender = gender;
        hateList = new ArrayList<Human>();
        likeList = new ArrayList<Human>();
    }

    //METHODS:
    //Setters:
    public void setFirstName(String name){
        this.name.setFirstName(name);
    }

    public void setLastName(String lName){
        this.name.setLastName(lName);
    }

    //Getters:
    public int getID(){
        return ID;
    }

    public int getGlobalID(){
        return GlobalID;
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

    //Relations:
    public void addHate(Human a){
        hateList.add(a);
    }

    public void addLike(Human a){
        likeList.add(a);
    }

    /**
     * Method relate
     * This method is meant to see whether this Human object
     * likes, hates, or neither Human a.
     *
     * @param a (Human) Another Human object to check relations to.
     * @return (String) A string saying how the objects are related.
     */
    public String relate(Human a){
        if(hateList.size() > 0){
            for(int i = 0; i < hateList.size(); ++i){
                if(a.getID() == hateList.get(i).getID()){
                    return this.getFullName()+" hates "+ a.getFullName();
                }
            }
        }
        if(likeList.size() > 0){    
            for(int i = 0; i <likeList.size(); ++i){
                if(a.getID() == likeList.get(i).getID()){
                    return this.getFullName()+" likes "+ a.getFullName();
                }
            }
        }
        return "No relation between "+this.getFullName()+" and "+a.getFullName();
    }

    //Debug:
    public String toString(){
        String output = "DEBUG : ID: "+this.ID+" ; NAME: "+this.getFullName()+" ; HATE SIZE: "+hateList.size()+" ; LIKE SIZE: "+likeList.size();
        return output;
    }
}
