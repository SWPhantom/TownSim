package in.dangerbear.crafting;

import com.mxgraph.layout.*;
import com.mxgraph.swing.*;

import java.awt.*;

import javax.swing.*;

import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

public class Run extends JApplet {
	private static final long serialVersionUID = 2202072534703043194L;
	private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);
	private JGraphXAdapter<String, DefaultEdge> jgxAdapter;

	public static FamilyTree tree = new FamilyTree();

	public static void main(String[] args) {
		// Generation segment.
		tree.generate();

		// Output data of generated populace.
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

		tree.DEBUG_CONNECTIONS_PRINT(50);

		// Graph segment.
		Run applet = new Run();
		applet.init();

		JFrame frame = new JFrame();
		frame.getContentPane().add(applet);
		frame.setTitle("JGraphT Adapter to JGraph Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public void init() {
		// create a JGraphT graph
		ListenableGraph<String, DefaultEdge> g = new ListenableDirectedGraph<String, DefaultEdge>(
				DefaultEdge.class);

		// create a visualization using JGraph, via an adapter
		jgxAdapter = new JGraphXAdapter<String, DefaultEdge>(g);

		getContentPane().add(new mxGraphComponent(jgxAdapter));
		resize(DEFAULT_SIZE);
		
		for (int i = 0; i < tree.humans.size(); ++i) {
			g.addVertex(tree.humans.get(i).getFirstName());
		}

		for (int i = 0; i < tree.humans.size(); ++i) {
			for (int j = 0; j < tree.humans.get(i).getNumChildren(); ++j) {
				Human a = tree.humans.get(i);
				g.addEdge(a.getFirstName(), tree.humans.get(a.children.get(j))
						.getFirstName());
			}
		}

		// positioning via jgraphx layouts
		mxFastOrganicLayout layout = new mxFastOrganicLayout(jgxAdapter);
		layout.execute(jgxAdapter.getDefaultParent());
	}

}
