package org.clothocad.tool.cello;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.shortestpath.MinimumSpanningForest2;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;



/**
 *
 * @author rozagh
 */
public class CelloGraphViewer extends  JApplet {
       Graph<DAGVertex,DAGEdge> graph;
    Forest<DAGVertex,DAGEdge> tree;

    Factory<DirectedGraph<DAGVertex,DAGEdge>> graphFactory =
    	new Factory<DirectedGraph<DAGVertex,DAGEdge>>() {

			public DirectedGraph<DAGVertex,DAGEdge> create() {
				return new DirectedSparseMultigraph<DAGVertex,DAGEdge>();
			}
		};



	Factory<DAGEdge> edgeFactory = new Factory<DAGEdge>() {
		int i=0;
		public DAGEdge create() {
			return new DAGEdge();
		}};

    Factory<DAGVertex> vertexFactory = new Factory<DAGVertex>() {
    	int i=0;
		public DAGVertex create() {
			return new DAGVertex();
		}};

    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<DAGVertex,DAGEdge> vv;

    VisualizationServer.Paintable rings;

    String root;

    VisualizationViewer<String,Number> vv1;


    DAGLayout<DAGVertex,DAGEdge> dagLayout;


    public CelloGraphViewer( DAGraph g , Dimension d )
    {
        graph = new DelegateForest<DAGVertex,DAGEdge>();
        createTree( g);


        MinimumSpanningForest2<DAGVertex,DAGEdge> prim =
        	new MinimumSpanningForest2<DAGVertex,DAGEdge>(graph,
        		new DelegateForest<DAGVertex,DAGEdge>(), DelegateTree.<DAGVertex,DAGEdge>getFactory(),
        		new ConstantTransformer(1.0));

        tree = prim.getForest();


        Dimension  newD = new Dimension(d.width, d.height-100);
        dagLayout = new DAGLayout<DAGVertex,DAGEdge>(graph);
        dagLayout.setRoot(graph);
        //dagLayout.setRoot(g.getParent());
        Layout<DAGVertex,DAGEdge> layout1 = new TreeLayout<DAGVertex,DAGEdge>(tree, 50);
        Layout<DAGVertex,DAGEdge> layout2 = new StaticLayout<DAGVertex,DAGEdge>(graph, layout1, newD);
        
        //vv1 = new VisualizationViewer<DAGVertex,DAGEdge>(vm1, preferredSizeRect);

        //Dimension  newD = new Dimension(d.width, d.height-100);
        vv = new VisualizationViewer<DAGVertex,DAGEdge>(layout2,newD);
        vv.setBackground(Color.white);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        Container content = getContentPane();
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel, BorderLayout.CENTER);



       // final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

       // vv.setGraphMouse(graphMouse);

        vv.setVertexToolTipTransformer(new Transformer<DAGVertex, String>() {

            @Override
            public String transform(DAGVertex i) {
                if (i.Feature!= null)
                    if (i.Feature._feature!= null)
                        return i.Feature._feature.getName();
                return "";
            }
        });

        final DefaultModalGraphMouse<DAGVertex, DAGEdge> graphMouse =
                new DefaultModalGraphMouse<DAGVertex, DAGEdge>();

        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());

        JComboBox modeBox = graphMouse.getModeComboBox();
        modeBox.addItemListener(graphMouse.getModeListener());
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

        final ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1/1.1f, vv.getCenter());
            }
        });


        JPanel scaleGrid = new JPanel(new GridLayout(1,0));
        scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));

        JPanel controls = new JPanel();
        scaleGrid.add(plus);
        scaleGrid.add(minus);
        controls.add(scaleGrid);
        controls.add(modeBox);

        content.add(controls, BorderLayout.SOUTH);
    }

    private void createTree(DAGraph g) {

        //DAGVertex v1 = new DAGVertex("v1", "", null);
        //DAGVertex v2 = new DAGVertex("v2", "", null);
        //graph.addVertex(v1);
        //graph.addVertex(v2);

int i = 0;
        for (DAGVertex v: g.Vertices)
            graph.addVertex(v);

        for( DAGEdge e: g.Edges) if (e.From!= null & e.To!= null)
            graph.addEdge(e, e.From, e.To);
        else
            i += 1;
        
        //graph.addEdge(new DAGEdge(v1, v2, null), v1, v2);
        /*
        graph.addVertex("V0");
        //graph.addVertex("V1");
    	graph.addEdge(edgeFactory.create(), "V0", "V1");
        //graph.addEdge(edgeFactory.create(), "V1", "V0");
    	graph.addEdge(edgeFactory.create(), "V0", "V2");
    	graph.addEdge(edgeFactory.create(), "V1", "V4");
         *
         */


    }

        public static void main(String[] args) {
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        content.add(new CelloGraphViewer(null, frame.getSize()));
        frame.pack();
        frame.setVisible(true);
    }

}
