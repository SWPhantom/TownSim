package in.dangerbear.crafting;

public class Run {
	public final static int MALE = 0;
	public final static int FEMALE = 1;
	public final static int LAST = 2;
	
	public final static int FAMILIES = 80;
	public final static int POPULATION = 2000;
	public final static int OFFSPRING_MAX = 20;
	public final static String NAME_FILEPATH = "Names/Italy";
	
	public static void main(String[] args) {
		Parser parser = new Parser();
		FamilyTree tree = new FamilyTree();
		
		//Choose and parse name files.
		parser.feedInput(NAME_FILEPATH, MALE);
		parser.feedInput(NAME_FILEPATH, FEMALE);
		parser.feedInput(NAME_FILEPATH, LAST);
		
		//Generation segment.
		tree.generate(FAMILIES, POPULATION, parser.getNameList(MALE), parser.getNameList(FEMALE), parser.getNameList(LAST));
		tree.interconnect(OFFSPRING_MAX);
		
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
		
	}
}
