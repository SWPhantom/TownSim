public class DNDHuman extends Human
{
    //DECLARATIONS:
    public int level;
    public String classification = "None";

    //CONSTRUCTORS:
    public DNDHuman()
    {
        super();
    }

    //a = fname, b = lname, c = gender, d = classifcation
    public DNDHuman(String a, String b, int c){
        super(a, b, c);
    }


    public DNDHuman(String a, int c){
        super(a, c);
    }

    public DNDHuman(String a, String b, int c, String d){
        super(a,b,c);
        this.classification=d;
    }

    public String getClassification(){
        return classification;
    }

    public void setClassification(String newClassification){
        classification=newClassification;
    }

    //METHODS:
    //Debug:
    public String toString(){
        String output = "ID: "+this.ID+" ; NAME: "+this.getFullName()+ "; CLASSIFICATION: " + this.getClassification() + "\n";
        return output;
    }
    public String toOutput(){
        return this.getFullName()+": "+this.getClassification()+"\n";
    }
}
