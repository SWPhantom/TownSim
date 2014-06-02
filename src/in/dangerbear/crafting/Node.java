/**
 * 
 */
package in.dangerbear.crafting;

/**
 * @author SWPhantom
 * 
 */
public class Node{
////DECLARATIONS///////////////////////////////////////////////////////////////
	public static int GlobalID = 0;
	protected int ID;

////CONSTRUCTORS///////////////////////////////////////////////////////////////
	public Node(){
		this.ID = GlobalID;
		GlobalID++;
	}

////METHODS////////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object other){
		if(other == null) return false;
		if(other.getClass() != this.getClass()) return false;
		if(this == other) return true;
		if(this.ID == ((Node) other).ID) return true;
		return false;
	}

}
