package in.dangerbear.crafting;

public class Run {
	public final static int MALE = 0;
	public final static int FEMALE = 1;
	public final static int LAST = 2;
	public final static int FAMILIES = 5;
	public final static int POPULATION = 20;
	public final static int OFFSPRING_MAX = 20;
	
	public static void main(String[] args) {
		Parser parser = new Parser();
		FamilyTree tree = new FamilyTree();
		
		String filepath = "Names/Italy";
		
		parser.feedInput(filepath, MALE);
		parser.feedInput(filepath, FEMALE);
		parser.feedInput(filepath, LAST);
		
		tree.generate(FAMILIES, POPULATION, parser.getNameList(MALE), parser.getNameList(FEMALE), parser.getNameList(LAST));
		tree.interconnect(OFFSPRING_MAX);
		tree.DEBUG_PRINT();
	}

}
