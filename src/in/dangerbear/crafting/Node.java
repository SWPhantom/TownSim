/**
 * 
 */
package in.dangerbear.crafting;

/**
 * @author SWPhantom
 *
 */
public class Node {
////DECLARATIONS///////////////////////////////////////////////////////////////
	public static int GlobalID = 0;
	public int ID;
	
////CONSTRUCTORS///////////////////////////////////////////////////////////////
	public Node() {
		this.ID = GlobalID;
		GlobalID++;
	}
}
