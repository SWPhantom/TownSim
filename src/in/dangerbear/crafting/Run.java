package in.dangerbear.crafting;



import java.awt.*;

import javax.swing.*;

import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;

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

		

		// Graph segment.
		/*Run applet = new Run();
		applet.init();

		JFrame frame = new JFrame();
		frame.getContentPane().add(applet);
		frame.setTitle("JGraphT Adapter to JGraph Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		*/
	}

	public void init() {
		// create a JGraphT graph
		ListenableGraph<String, DefaultEdge> g = new ListenableDirectedGraph<String, DefaultEdge>(
				DefaultEdge.class);
		
		// create a visualization using JGraph, via an adapter
		jgxAdapter = new JGraphXAdapter<String, DefaultEdge>(g);

		getContentPane().add(new mxGraphComponent(jgxAdapter));
		resize(DEFAULT_SIZE);
		//graph.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
		
		for (int i = 0; i < tree.humans.size(); ++i) {
			g.addVertex(tree.humans.get(i).getName());
		}

		for (int i = 0; i < tree.humans.size(); ++i) {
			Human a = tree.humans.get(i);
			for (int j = 0; j < a.relations.size(); ++j) {
				g.addEdge(a.getName(), tree.humans.get(a.relations.get(j))
						.getName());
			}
		}

		// positioning via jgraphx layouts
		mxFastOrganicLayout layout = new mxFastOrganicLayout(jgxAdapter);
		layout.setDisableEdgeStyle(true);
		layout.execute(jgxAdapter.getDefaultParent());
	}

}
