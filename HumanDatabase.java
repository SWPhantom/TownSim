import java.util.ArrayList;

public class HumanDatabase {
    //DECLARATIONS:
    ArrayList<DNDHuman> database = new ArrayList<DNDHuman>();
    
    //CONSTRUCTORS:
    public HumanDatabase(){}
    
    //METHODS:
    public void add(DNDHuman human){
        database.add(human);
    }
    public int getSize(){
        return database.size();
    }
    public ArrayList<DNDHuman> getHumans(){
        return database;
    }
    public DNDHuman getID(int requestedID){
        try{
            return database.get(requestedID);
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }
}
