package in.dangerbear.crafting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Run {
	public final static int MALE = 0;
	public final static int FEMALE = 1;
	public final static int LAST = 2;
	
	public static int FAMILIES = 80;
	public static int POPULATION = 2000;
	public static int OFFSPRING_MAX = 20;
	
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
