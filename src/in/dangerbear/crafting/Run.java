package in.dangerbear.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/*
import java.util.HashMap;
import java.util.Map;
import com.mxgraph.layout.*;
import com.mxgraph.swing.*; 
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;
import java.awt.*;
import javax.swing.*;
import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;
import org.jgraph.graph.GraphLayoutCache;
*/

public class Run{ // extends JApplet {
	/*
	private static final long serialVersionUID = 2202072534703043194L;
	private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);
	private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
	*/
	public static ArrayList<Human> humans = new ArrayList<Human>();
	public static GraphGenerator tree = new GraphGenerator();
	public static GraphDemo treeDemo;

	public static void main(String[] args){
		waitToBegin();
		//Generation segment.
		humans = tree.generate();
		treeDemo = new GraphDemo(humans);

		System.out.println("Finished generating!");

		//Run tests.
		//test1(); //PrintNthConnection test.
		//test2(); //Rumor spread test.
		//graphics(); //Start visualization.

		treeDemo.DEBUG_PRINT(humans);
	}

	private static void waitToBegin(){
		System.out.println("Press Enter when you want to start.");
		Scanner key = new Scanner(System.in);
		key.next();
		key.close();
		key = null;
	}

	private static void graphics(){
		/*
		Run applet = new Run();
		applet.init();
		JFrame frame = new JFrame();
		frame.getContentPane().add(applet);
		frame.setTitle("JGraphT Adapter to JGraph Demo");
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		*/
	}

	private static void test1(){
		treeDemo.printNthConnections(humans, 0, 1);
		treeDemo.printNthConnections(humans, 0, 2);
		treeDemo.printNthConnections(humans, 0, 3);
		treeDemo.printNthConnections(humans, 1, 1);
		treeDemo.printNthConnections(humans, 1, 2);
		treeDemo.printNthConnections(humans, 1, 3);
		treeDemo.printNthConnections(humans, 2, 1);
		treeDemo.printNthConnections(humans, 2, 2);
		treeDemo.printNthConnections(humans, 2, 3);
	}

	private static void test2(){
		//Rumor spreading tests.
		ArrayList<Integer> vectors = new ArrayList<Integer>();
		int TESTS = 5;
		Random rand = new Random();
		for(int i = 0; i < TESTS; ++i){
			vectors.clear();
			int initVecs = rand.nextInt(tree.humans.size() / 10);
			System.out.println("Initial rumor vectors (" + initVecs + "): ");
			//XXX: This may add the same rumor vector to the ArrayList. NO GOOD.
			for(int j = 0; j < initVecs; ++j){
				int choice = rand.nextInt(tree.humans.size());
				vectors.add(choice);
			}

			//Sort the vectors.
			Collections.sort(vectors);

			//Remove duplicates.
			for(int j = 0; j < vectors.size() - 1; ++j){
				while((j + 1) != vectors.size()
						&& vectors.get(j).equals(vectors.get(j + 1))){
					vectors.remove(j);
				}
			}

			for(int j = 0; j < vectors.size(); ++j){
				System.out.print(vectors.get(j) + ", ");
			}

			System.out.println();
			System.out.println("1% chance to propagate.");
			treeDemo.startRumor(humans, vectors, 1);
			System.out.println("2% chance to propagate.");
			treeDemo.startRumor(humans, vectors, 2);
			System.out.println("4% chance to propagate.");
			treeDemo.startRumor(humans, vectors, 4);
			System.out.println("5 chance to propagate.");
			treeDemo.startRumor(humans, vectors, 5);
			System.out.println("10 chance to propagate.");
			treeDemo.startRumor(humans, vectors, 10);
			System.out.println("20% chance to propagate.");
			treeDemo.startRumor(humans, vectors, 20);
			System.out.println("30% chance to propagate.");
			treeDemo.startRumor(humans, vectors, 30);
			System.out.println("40% chance to propagate.");
			treeDemo.startRumor(humans, vectors, 40);
			System.out.println("50% chance to propagate.");
			treeDemo.startRumor(humans, vectors, 50);
			System.out.println("60% chance to propagate.");
			treeDemo.startRumor(humans, vectors, 60);
			System.out.println();
		}
	}

	public void init(){
		/*
		// create a JGraphT graph
		ListenableGraph<String, DefaultEdge> g = new ListenableDirectedGraph<String, DefaultEdge>( DefaultEdge.class);
		
		// create a visualization using JGraph, via an adapter
		jgxAdapter = new JGraphXAdapter<String, DefaultEdge>(g);
		
		getContentPane().add(new mxGraphComponent(jgxAdapter));
		resize(DEFAULT_SIZE);
		
		for(Human person: humans){
			g.addVertex(person.getName() + "(" + person.getAge() + ")");
		}
		
		for(Human person: humans){
			for(int j = 0; j < person.getNumChildren(); ++j){
			Human child = person.children.get(j);
			String source = person.getName() + "(" + person.getAge() + ")";
			String target = child.getName() + "(" + child.getAge() + ")";
				g.addEdge(source, target);
			}
		}
		
		Map<String, Object> edgeStyle = new HashMap<String, Object>();
		//edgeStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);
		edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		edgeStyle.put(mxConstants.STYLE_FONTSIZE, 0);
		edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff");
		mxStylesheet stylesheet = new mxStylesheet();
		stylesheet.setDefaultEdgeStyle(edgeStyle);
		jgxAdapter.setStylesheet(stylesheet);
		
		//positioning via jgraphx layouts
		mxFastOrganicLayout layout = new mxFastOrganicLayout(jgxAdapter);
		layout.execute(jgxAdapter.getDefaultParent());
		*/
	}

}
