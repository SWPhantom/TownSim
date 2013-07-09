//A class that keeps track of the name of any Human.

public class Name
{
    // instance variables - replace the example below with your own
    public String firstName;
    public String lastName;

    //CONSTRUCTORS:
    public Name()
    {
        firstName = "";
        lastName = "";
    }

    public Name(String first){
        firstName = first;
        lastName = "";
    }

    public Name(String first, String last){
        firstName = first;
        lastName = last;
    }

    //METHODS:
    //setters
    public void setFirstName(String name){
        firstName = name;
    }

    public void setLastName(String name){
        lastName = name;
    }

    public void setFullName(String fname, String lname){
        firstName = fname;
        lastName = lname;
    }

    //getters
    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }
}
