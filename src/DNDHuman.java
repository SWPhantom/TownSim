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

	public DNDHuman(String firstName, String lastName, byte gender){
		super(firstName, lastName, gender);
	}


	public DNDHuman(String firstName, byte gender){
		super(firstName, gender);
	}

	public DNDHuman(String firstName, String lastName, byte gender, String classification){
		super(firstName, lastName, gender);
		this.classification = classification;
	}

	public String getClassification(){
		return classification;
	}

	public void setClassification(String newClassification){
		classification = newClassification;
	}

	//METHODS:
	//Debug:
	public String toString(){
		String output = "ID: " + this.ID + " ; NAME: "+this.getFullName() + "; CLASSIFICATION: " + this.getClassification() + "\n";
		return output;
	}
	public String toOutput(){
		return this.getFullName() + ": " + this.getClassification() + "\n";
	}
}
