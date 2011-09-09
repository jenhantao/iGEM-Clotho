package org.clothocad.tool.trumpet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.*;


import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

//import TreeLayoutDemo.Rings;
import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.EdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelRenderer;
import edu.uci.ics.jung.visualization.util.Animator;
import java.awt.Paint;



public class KeyTree extends JApplet {

	/**
     * the graph
     */
    Forest<String,InvEdge> graph;
    
        Factory<DirectedGraph<String,Integer>> graphFactory =
            new Factory<DirectedGraph<String,Integer>>()
            {
                public DirectedGraph<String, Integer> create() {
                        return new DirectedSparseMultigraph<String,Integer>();
                }
            };
			
	Factory<Tree<String,Integer>> treeFactory =
		new Factory<Tree<String,Integer>> () {

		public Tree<String, Integer> create() {
			return new DelegateTree<String,Integer>(graphFactory);
		}
	};
	
	Factory<Integer> edgeFactory = new Factory<Integer>() {
		int i=0;
		public Integer create() {
			return i++;
		}};
    
    Factory<String> vertexFactory = new Factory<String>() {
    	int i=0;
		public String create() {
			return "V"+i++;
		}};

//     MyMousePlugin mouse = new MyMousePlugin();

    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<String, InvEdge> vv;
    
    
    /**
     */
    VertexLabelRenderer vertexLabelRenderer;
    EdgeLabelRenderer edgeLabelRenderer;
    
    
    VisualizationServer.Paintable rings;
    
    String root;
    
    TreeLayout<String, InvEdge> treeLayout;
    
    RadialTreeLayout<String,InvEdge> radialLayout;

    public KeyTree(InvertSim sim, final String[][] keyArray, ArrayList<String> desiredPerms, MyMousePlugin mousePlugin) {
        
        // create a simple graph for the demo
        graph = new DelegateForest<String,InvEdge>();

        _model = sim;
//        final ArrayList<String> desiredPerms = new ArrayList<String>(0);
//        for (int i=1; i<keyArray.length; i++)
//        {
//            desiredPerms.add(keyArray[i][0]);
//        }

            _desiredPerms = desiredPerms;


        this._mousePlugin = mousePlugin;
        _edgeSet = new InvEdgeSet();
        _allPermsHit = new ArrayList<String>(0);

        createTree(sim,keyArray);
    
        treeLayout = new TreeLayout<String,InvEdge>(graph);
        radialLayout = new RadialTreeLayout<String,InvEdge>(graph);
        radialLayout.setSize(new Dimension(600,600));
        vv =  new VisualizationViewer<String,InvEdge>(treeLayout, new Dimension(600,600));
        vv.setBackground(Color.white);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        // define a new transformer for the two types of colors
        Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
            private final Color[] palette = {new Color(255,120,0), new Color(0,145,255)};

            public Paint transform(String i)
            {
                if (_desiredPerms.contains(removeEdgeString(((String)i))))
                    return palette[1];
                else
                    return palette[0];
                
            }
        };
        // use this tranformer to render the coloring of the invertase tree
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);

        // set the number of nodes
        _numberOfNodes = _allPermsHit.size();
        int total = graph.getVertexCount();
        
        vertexLabelRenderer = vv.getRenderContext().getVertexLabelRenderer();
        edgeLabelRenderer = vv.getRenderContext().getEdgeLabelRenderer();
        
        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<InvEdge, String>() {
            public String transform(InvEdge e) {
                return (/*e.toString() + " " + */e.getWeight());
            }
        });


        vv.getRenderContext().setVertexLabelTransformer(new Transformer<String, String>() {
            public String transform(String v) {
                String[] nameArray = v.split(" ");
                String name = "";
                for (int i=0; i<nameArray.length; i++)
                {
                    if (nameArray[i].startsWith("P"))
                        name = name.concat(nameArray[i]+" ");
                }
                
                return name;
            }
        });


        // add a listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller());
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
        rings = new Rings();

        Container content = getContentPane();
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel);
        
        /*final DefaultModalGraphMouse*/ graphMouse = new DefaultModalGraphMouse();

//        mousePlugin = new MyMousePlugin<InvEdge,String>();
        graphMouse.add(_mousePlugin);


//                final EditingModalGraphMouse<Number,Number> graphMouse =
//        	new EditingModalGraphMouse();//<Number,Number>(vv.getRenderContext(), vertexFactory, edgeFactory);

        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());

        
        final JComboBox modeBox = graphMouse.getModeComboBox();
        modeBox.addItemListener(graphMouse.getModeListener());
        modeBox.setFocusable(false);
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);



        final ScalingControl scaler = new CrossoverScalingControl();

        final JButton plus = new JButton("+");
        plus.setFocusable(false);
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
                                                

            }
        });
        final JButton minus = new JButton("-");
        minus.setFocusable(false);
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1/1.1f, vv.getCenter());
                                                
            }
        });
        
//        this.setFocusable(false);

        final JToggleButton radial = new JToggleButton("Radial");
//        radial.setFocusCycleRoot(rootPaneCheckingEnabled);
        radial.setFocusable(false);
        radial.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
//                                radial.setFocusable(true);
				if(e.getStateChange() == ItemEvent.SELECTED) {
					
					LayoutTransition<String, InvEdge> lt =
						new LayoutTransition<String,InvEdge>(vv, treeLayout, radialLayout);
					Animator animator = new Animator(lt);
					animator.start();
					vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
					vv.addPreRenderPaintable(rings);

				} else {
					LayoutTransition<String, InvEdge> lt =
						new LayoutTransition<String,InvEdge>(vv, radialLayout, treeLayout);
					Animator animator = new Animator(lt);
					animator.start();
					vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
					vv.removePreRenderPaintable(rings);
				}
				vv.repaint();
//                                radial.setFocusable(false);
//                                vv.requestFocus();
			}});

        JPanel scaleGrid = new JPanel(new GridLayout(1,0));
        scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));

        JPanel controls = new JPanel();
        scaleGrid.add(plus);
        scaleGrid.add(minus);
        controls.add(radial);
        controls.add(scaleGrid);
        controls.add(modeBox);

        content.add(controls, BorderLayout.SOUTH);


    }



    
    class Rings implements VisualizationServer.Paintable {
    	
    	Collection<Double> depths;


    	public Rings() {
    		depths = getDepths();
    	}
    	
    	private Collection<Double> getDepths() {
    		Set<Double> depths = new HashSet<Double>();
    		Map<String,PolarPoint> polarLocations = radialLayout.getPolarLocations();
    		for(String v : graph.getVertices()) {
    			PolarPoint pp = polarLocations.get(v);
                        if (!(pp==null))
    			depths.add(pp.getRadius());
    		}
    		return depths;
    	}

		public void paint(Graphics g) {
			g.setColor(Color.lightGray);
		
			Graphics2D g2d = (Graphics2D)g;
			Point2D center = radialLayout.getCenter();

			Ellipse2D ellipse = new Ellipse2D.Double();
			for(double d : depths) {
				ellipse.setFrameFromDiagonal(center.getX()-d, center.getY()-d, 
						center.getX()+d, center.getY()+d);
				Shape shape = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).transform(ellipse);
				g2d.draw(shape);
			}
		}

		public boolean useTransform() {
			return true;
		}
    }
    
    /**
     * 
     */
    private void createTree(InvertSim sim, String[][] keyArray) {
        
    	// create the key table
    	String[][] keyTable = keyArray;//sim.generateTailoredKeyTable(desiredPerms);

    	String originalDesign = sim.designString();
    		
    	// if there is only one node, make it
        if (keyArray.length==1)
            graph.addVertex(keyTable[0][0]+"  ");

    	// String array which split the first key up
    	String[] permKey = new String[0];
    	
    	// the size of the key table determines the number of nodes and permutations
    	int n = keyTable.length;
    	
    	// the top node will never change so create this final variable to reference it
    	final String topNode = keyTable[0][0]+"  ";
    	
    	String currentNode = "";
    	String lastNode = "";
    	
    	// loop through the total number of possible nodes
    	for (int i=1; i<n; i++)
    	{
    		// for each permutation, reset the current node to the top node
    		currentNode = topNode;
    		
    		//split up the key elements
    		permKey = keyTable[i][1].split(" ");

                String currentInv = "  ";
    		
    		// loop through this key
    		for (int j=0; j<permKey.length; j++)
    		{
    			// remember the currentNode
    			lastNode = currentNode;

                        if(j>0)
                            currentInv = permKey[j-1];
    			
    			// make the inversion
    			if (sim.isPossible(permKey[j]))
    					sim.invert();
    			
    			// get the current config string
    			currentNode = sim.partsString();
    			
    			//if(!(InvEdge.getEdge(lastNode,currentNode)))
    			//	graph.addEdge(new InvEdge(permKey[j],lastNode,currentNode), lastNode, currentNode);
    		    	
                        if (!(currentNode.equals(lastNode)))
                        {
                            if (!(_edgeSet.doesEdgeExist(lastNode, currentNode, permKey[j])))
                            {
                                InvEdge nextEdge = new InvEdge(permKey[j],lastNode+currentInv,currentNode+permKey[j]);
                                _edgeSet.addEdge(nextEdge);
                                graph.addEdge(nextEdge, lastNode+currentInv, currentNode+permKey[j]);
                                if (!(_allPermsHit.contains(currentNode)))
                                    _allPermsHit.add(currentNode);
                                if (!(_allPermsHit.contains(lastNode)))
                                    _allPermsHit.add(lastNode);
                            }
                        }

    		}
    		
    		// reset the original design
    		sim.setDesignArray(originalDesign);


    	}
//    	graph.addEdge(new InvEdge("42"), "A B C", "A' B C");
//    	graph.addEdge(edgeFactory.create(), "V0", "V1");   	
    
    }




    public ArrayList<String> getDesiredPerms()
    {
        return this._desiredPerms;
    }


    public String removeEdgeString(String vertexName)
    {
        String name = "";
        String nameArray[] = vertexName.split(" ");

        for (int i=0; i<nameArray.length; i++)
        {
            if (nameArray[i].startsWith("P"))
                name = name.concat(nameArray[i]+" ");
        }

        return name;
    }



//    public KeyTree createSubTree(String parentNode)
//    {
//
//       // get all the children nodes
//       Collection<String> allNodes = new ArrayList<String>(0);
//       this.getSubNodes(parentNode, allNodes);
//
//       // get the current model object
//       InvertSim oldModel = this._model;
//
//       InvertSim newModel = null;
//
//       // create a new model that has not yet been tailored
//       if (oldModel.getAlgorithm().equals("LinkSort"))
//       {
//           newModel = new LinkSort(oldModel.getNumberOfParts(),oldModel.isCombos());
//       }
//        else if (oldModel.getAlgorithm().equals("Pancake"))
//       {
//            newModel = new Pancake(oldModel.getNumberOfParts(),oldModel.isCombos());
//       }
//       if (newModel!=null)
//       {
//
//           // get the key to this parent node
//           String parentKey = oldModel.generateKey(parentNode);
//
//           // apply this key
//           String[] key = parentKey.split(" ");
//           for (int i=0; i<key.length; i++)
//           {
//               if (newModel.isPossible(key[i]))
//                   newModel.invert();
//           }
//
//           // now that we have a correct Model, we need to generate a tree using this model
//           // first, we can get a tailored key table
//
//
//
//
//        }
//       return this;
//    }


    public void getSubNodes(String parentNode,Collection<String> allNodes)
    {
        if (graph.getChildren(parentNode).isEmpty())
        {
            allNodes.add(removeEdgeString(parentNode));
            return;
        }
        else
        {
            allNodes.add(removeEdgeString(parentNode));
            Collection<String> currentNodes = graph.getChildren(parentNode);
            Object[] currentNodeArray = currentNodes.toArray();
            for (int i=0; i<currentNodeArray.length; i++)
            {
                getSubNodes((String)currentNodeArray[i],allNodes);
            }
        }
    }


    public String[][] getSubTreeTable(String parentNode)
    {
        ArrayList<String> nodes = new ArrayList<String>(0);
        ArrayList<String> paths = new ArrayList<String>(0);

        getTree(parentNode, nodes, paths);


        String[][] treeTable = new String[nodes.size()][2];


        for (int i=0; i<nodes.size(); i++)
        {
            treeTable[i][0] = nodes.get(i);
            treeTable[i][1] = paths.get(i);

        }

        treeTable[0][1] = "Starting Construct";

        return treeTable;
    }


    public void getTree(String parentNode, Collection<String> allNodes, Collection<String> paths)
    {
        if (graph.getChildren(parentNode).isEmpty())
        {
            allNodes.add(removeEdgeString(parentNode));
            paths.add(this.getPath(parentNode));
            return;
        }
        else
        {
            allNodes.add(removeEdgeString(parentNode));
            paths.add(this.getPath(parentNode));
            Collection<String> currentNodes = graph.getChildren(parentNode);
            Object[] currentNodeArray = currentNodes.toArray();
            for (int i=0; i<currentNodeArray.length; i++)
            {
                getTree((String)currentNodeArray[i],allNodes, paths);
            }
        }
    }



    public String getPath(String parentNode)
    {
        String path = "";

        ArrayList<String> invs = new ArrayList<String>(0);
        getPathArray(invs, parentNode);

        for (int i=0; i<invs.size();i++)
        {
            path = path.concat(invs.get(i)+" ");
        }

        return path;
    }


    public void getPathArray(ArrayList<String> invs, String parentNode)
    {
//        if (!(graph.getParent(parentNode)==null))
        String[] parentNodeArray = parentNode.split(" ");
        if (!(parentNodeArray[parentNodeArray.length-1].startsWith("P")))

        {
            String nextParent = graph.getParent(parentNode);
            invs.add(0,this._edgeSet.getEdge(nextParent, parentNode).getWeight());
            getPathArray(invs,nextParent);

        }
        else
            return;
    }


    private InvEdgeSet _edgeSet;
    public int _numberOfNodes;
    public DefaultModalGraphMouse graphMouse;
    public MyMousePlugin _mousePlugin;
    public InvertSim _model;
    public ArrayList<String> _allPermsHit;
    public ArrayList<String> _desiredPerms;
    public String constructKey;
}
