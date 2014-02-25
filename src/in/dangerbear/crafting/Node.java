/**
 * 
 */
package in.dangerbear.crafting;

/**
 * @author SWPhantom
 *
 */
public class Node {
	public static int GlobalID = 0;
	public int ID;
	
	public Node() {
		this.ID = GlobalID;
		GlobalID++;
	}
}
