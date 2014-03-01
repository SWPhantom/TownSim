package in.dangerbear.crafting;

public class Run {
	
	public static void main(String[] args) {
		FamilyTree tree = new FamilyTree();
		
		//Generation segment.
		tree.generate();
		
		//Output data of generated populace.
		tree.printNthConnections(0, 1);
		tree.printNthConnections(0, 2);
		tree.printNthConnections(0, 3);
		tree.printNthConnections(1, 1);
		tree.printNthConnections(1, 2);
		tree.printNthConnections(1, 3);
		tree.printNthConnections(2, 1);
		tree.printNthConnections(2, 2);
		tree.printNthConnections(2, 3);
		
		tree.DEBUG_PRINT();
		
	}
	
}
