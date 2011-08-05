/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package org.clothocad.tool.cello;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.IOException;
import java.lang.annotation.Target;
import java.util.List;
import org.antlr.misc.Graph;
import org.antlr.runtime.*;
import org.antlr.runtime.debug.ParseTreeBuilder;
import org.antlr.runtime.tree.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.naming.spi.DirStateFactory.Result;
import javax.print.DocFlavor.STRING;
import org.clothocore.api.data.Feature;
import org.clothocore.api.data.Part;
import sun.awt.geom.Curve;
import sun.security.provider.certpath.Vertex;


//import org.antlr.runtime.tree.Tree;
/**
 *
 * @author rozagh
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, RecognitionException {
       /* System.out.print("hello!");

       // ANTLRInputStream input = new ANTLRInputStream(System.in);
        CharStream input = new ANTLRFileStream(args[0]);
        VerilogLexer lexer = new VerilogLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        VerilogParser parser = new VerilogParser(tokens);
       VerilogParser.module_return m = parser.module();
       if (m.tree != null)
       {
           System.out.println(((Tree)m.tree).toStringTree());
           ((CommonTree)m.tree).sanityCheckParentAndChildIndexes();
       }
        *
        */


       /*  System.out.print("Test module start\n");

        CelloGeneTable gt = new CelloGeneTable();
        CelloIPromoterTable ipt = new CelloIPromoterTable();
        CelloRPromoterTable rpt = new CelloRPromoterTable();
        CelloRBSTable rbst = new CelloRBSTable();
        */


        addPartList();


/*
        gt.add(new CelloGene("CAT", 0));
        gt.add(new CelloGene("ACT", 0));

        ipt.add(new CelloIPromoter("GGG", 0));
        ipt.add(new CelloIPromoter("TTT", 0));

        rpt.add(new CelloRPromoter("CCC", 0));

        rbst.add(new CelloRBS("TAC", 0));
        rbst.add(new CelloRBS("TCA", 0));
 *
 */

/*
        CelloNOR nor = new CelloNOR();
        nor.setGene1(gt.get(0));
        nor.setGene2(gt.get(1));

        nor.setIPromoter1(ipt.get(0));
        nor.setIPromoter2(ipt.get(1));

        nor.setRPromoter(rpt.get(0));

        nor.setRBS1(rbst.get(0));
        nor.setRBS2(rbst.get(1));
*/

      //  System.out.print(nor.print());
      //  System.out.print(nor.printDNA() + "\n");


    }


    /**
     * This function reads from the Verilog file and run the Lexer and Parser functions of the
     * ANTLR and generates a flat AST. AST is stored in a CommonTree object.
     */
    public static String AST_Generate (String Path , String[] error) throws IOException, RecognitionException
    {


        CharStream input = new ANTLRFileStream(Path);

        VerilogALexer lexerA = new VerilogALexer(input);
        CommonTokenStream tokensA = new CommonTokenStream(lexerA);
        VerilogAParser parserA = new VerilogAParser(tokensA);

       VerilogAParser.module_return m = parserA.module();

       String result = "";
      if (m.tree != null)
       {
           result =  ((Tree)m.tree).toStringTree();
           ((CommonTree)m.tree).sanityCheckParentAndChildIndexes();
       }
       result += "\n**********************************\n";

       /*
        for (Integer i = 1; i< m.tree.getChildCount(); i++)
       {
          
           result += "\n" + i.toString() + " = " + m.tree.getChild(i).getText();
           Integer childi = m.tree.getChild(i).getChildIndex();

          result += "-->"+ childi.toString();
        }
        *
        */
       
       result += printCTree((CommonTree)m.tree, 0);
       result += "\n**********************************\n";

       g = new DAGraph();

       //find assign node

       CommonTree t = (CommonTree)m.tree;
        CommonTree assign  = new CommonTree();

         //t = (CommonTree) t.getChild(0); //now we are in module

             for (int i=0; i< t.getChildCount(); i++)
             {
                 if (t.getChild(i).getText().equals("assign"))
                 {
                     assign = (CommonTree) t.getChild(i);
                     translateAST(assign, g, null);
                 }
             }

        connectNodes(g);

        inputOutputList = new HashMap<String, HashMap<String, Feature>>();
        makeInputOutputList ((CommonTree) t, inputOutputList);
        error [0] = checkInputOutput(g, inputOutputList );
       result+= g.PrintGraph();

       implementations.clear();
       CelloCircuit newds = new CelloCircuit();
       newds.setInputGraph(g.Copy());
       newds.setIsOptimized(false);
       newds.setIsTransferred(false);
       implementations.put("AST", newds);
       return result;
    }

    private static void makeInputOutputList(CommonTree t, HashMap<String, HashMap<String, Feature>> inputOutputList) {

       HashMap<String, Feature> inputs = new HashMap<String, Feature>();
       HashMap<String, Feature> outputs = new HashMap<String, Feature>();

         for (int i=0; i< t.getChildCount(); i++)
             {
                 if (t.getChild(i).getText().equals("input"))
                 {
                     
                     inputs.put(t.getChild(i).getChild(0).getText(), null);
                     //new String[]{t.getChild(i).getChild(0).getText(), ""});
                 }
                 else if(t.getChild(i).getText().equals("output"))
                 {
                     outputs.put(t.getChild(i).getChild(0).getText(), null);
                    //new String[]{t.getChild(i).getChild(0).getText(), ""});
                 }
        }
       inputOutputList.put("input", inputs);
       inputOutputList.put("output", outputs);

    }

     private static String checkInputOutput(DAGraph g,  HashMap<String, HashMap<String, Feature>> inputOutputList) {

         ArrayList<DAGVertex> leafs = g.findLeaf();
         ArrayList<DAGVertex> roots = g.findRoots();
         String error = "";

         for (int i =0; i<leafs.size(); i++)
         {
             Iterator<String> ins = inputOutputList.get("input").keySet().iterator();
             boolean flag = false;
             while (ins.hasNext())
             {
                 if ( ins.next().equals(leafs.get(i).Name))   //ins.next()[0].equals(leafs.get(i).Name))
                 {
                     flag = true;
                     break;
                 }
             }
             if (! flag)
                 error+="invalid input port " + leafs.get(i).Name + " in node# " + String.valueOf(leafs.get(i).Index) +"\n";
         }

         for (int i =0; i<roots.size(); i++)
         {
             Iterator<String> ins = inputOutputList.get("output").keySet().iterator();
             boolean flag = false;
             while (ins.hasNext())
             {
                 if (ins.next().equals(roots.get(i).Name))
                 {
                     flag = true;
                     break;
                 }
             }
             if (! flag)
                 error+="invalid output port " + roots.get(i).Name + " in node# " + String.valueOf(roots.get(i).Index) +"\n";
         }

         return error;

    }


     private static void connectNodes(DAGraph g) {
        //connect leaves
        HashMap<String, ArrayList<DAGVertex>> ilist = new HashMap<String, ArrayList<DAGVertex>>();

        for (int i = 0; i< g.Vertices.size(); i++)
        {
            DAGVertex v = g.Vertices.get(i);
            if (v.Type.equals("19"))//identifier
            {
                if (ilist.containsKey(v.Name))
                {
                    ilist.get(v.Name).add(v);
                }
                else
                {
                    ArrayList<DAGVertex> iv = new ArrayList<DAGVertex>();
                    iv.add(v);
                    ilist.put(v.Name, iv);
                }

            }

        }

        ArrayList<String> keys = new ArrayList<String>();
      //  keys = (ArrayList<String>) ilist.keySet();


       Iterator iterator = ilist.keySet().iterator();

        while(iterator.hasNext()){
            keys.add((String) iterator.next());
        }
        for (int i =0; i< keys.size(); i++)
        {
            String k = keys.get(i);
            ArrayList<DAGVertex> vl = new ArrayList<DAGVertex>();
            vl = ilist.get(k);
            for (int j=1; j<vl.size(); j++ )
            {
                g.ReplaceVertex(vl.get(j), vl.get(0));
                g.Vertices.remove(vl.get(j));
            }

        }
    }

    /**
     * This function reads from the Verilog file and run the Lexer and Parser functions of the
     * ANTLR and generates an un-filtered parse tree. Becuase of the many junk nodes in the tree
     * is not used right now.
     */
    public static String ParseT_Generate (String Path) throws IOException, RecognitionException
    {
         CharStream input = new ANTLRFileStream(Path);
         VerilogLexer lexer = new VerilogLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
         ParseTreeBuilder builder = new ParseTreeBuilder("module");
        // create the parser attached to the token buffer
        // and tell it which debug event listener to use
        VerilogParser parser2 = new VerilogParser(tokens, builder);
        // launch the parser starting at rule prog
        parser2.module();
        String result = "";
        //result = builder.getTree().toStringTree();
      ParseTree pt = new ParseTree(builder.getTree());
/*
        for (Integer i = 1; i< pt.getChildCount(); i++)
           result +=  "\n" + i.toString()+" = " +pt.getChild(i).getText();
          
*/

setIndex((ParseTree)pt.payload, "");
        result +='\n'+ printPTree((ParseTree)pt.payload, 4);


        result +="\n///////////////////////////////////////\n";
       
         HashMap<Integer, ArrayList <String>> ssindex  = new HashMap<Integer, ArrayList <String>>();
         DAGraph g = new DAGraph();

        //Filter_PTree((ParseTree)pt.payload, g, ssindex);
        Convert_PTree((ParseTree)pt.payload, g);
        result+= "\n\n\n++++++++++++++\n"+ g.PrintGraph();

        return result;
    }


/**
     * This function prints the Parse tree by traversing all of its nodes.
     */
    public static String printPTree(ParseTree t, int indent) {

        String Result = "";
	if ( t != null ) {
		StringBuffer sb = new StringBuffer(indent);
		for ( int i = 0; i < indent; i++ )
			sb = sb.append("\t");

		for ( Integer i = 0; i < t.getChildCount(); i++ ) {
			Result +='\n' + sb.toString() + t.getChild(i).getText()+" --> "+ ((ParseTree)t.getChild(i)).hiddenTokens.get(0);
			Result += printPTree((ParseTree)t.getChild(i), indent+1);
		}
	}
        return Result;
    }

/**
     * This function prints the AST (CommonTree object) which is a flat tree.
     */
    public static String printCTree(CommonTree t, int indent) {
	String Result ="";
        if ( t != null ) {
		StringBuffer sb = new StringBuffer(indent);
		for ( int i = 0; i < indent; i++ )
			sb = sb.append("   ");
		for ( Integer i = 0; i < t.getChildCount(); i++ ) {
			Result += '\n'+ sb.toString() + t.getChild(i).toString() + "\t-->"+ String.valueOf( t.getChild(i).getType());
			Result += printCTree((CommonTree)t.getChild(i), indent+4);
		}
	}
        return Result;
    }


    /**
     * This function sets indexes for the nodes in the Parse tree to be able to
     * find the hierarchy of the tree .
     */
    public static void setIndex(ParseTree t, String index)
    {

        if ( t != null ) {

		for ( Integer i = 0; i < t.getChildCount(); i++ ) {
                    if (t.getChild(i).getChildCount()==0)
                    {
                        ((ParseTree)  t.getChild(i)).hiddenTokens = new ArrayList();
                      ((ParseTree)  t.getChild(i)).hiddenTokens.add(index+i.toString());
                    }
                     else
                    {
			setIndex((ParseTree)t.getChild(i), index+i.toString()+"_");
                        ((ParseTree)  t.getChild(i)).hiddenTokens = new ArrayList();
                      ((ParseTree)  t.getChild(i)).hiddenTokens.add(index+i.toString());
                    }
		}
                
                //index += t.getChildCount();
	}
    }


    /**
     * This function converts the Parse tree to a Directed Acyclic Graph (DAGraph).
     */
    public static void Convert_PTree ( ParseTree t, DAGraph g)
    {
         if ( t != null ) {

		for ( Integer i = 0; i < t.getChildCount(); i++ ) {
                    if (t.getChild(i).getChildCount()==0)
                    {
                        DAGVertex v = new DAGVertex(t.getChild(i).getText(),((ParseTree) t.getChild(i)).hiddenTokens.get(0).toString() , null);
                        g.Vertices.add(v);
                    }
                     else
                    {
                        Convert_PTree((ParseTree) t.getChild(i), g);
                        DAGVertex v = new DAGVertex(t.getChild(i).getText(),((ParseTree) t.getChild(i)).hiddenTokens.get(0).toString() , null);
                        for (int j = g.Vertices.size()-1; j> (g.Vertices.size()-1)- t.getChild(i).getChildCount(); j--)
                        {
                            DAGEdge e = new DAGEdge(v, g.Vertices.get(j), null);
                            g.Edges.add(e);
                        }
                        g.Vertices.add(v);
                    }
		}

	}
    }


    /**
     * This function filters the unnecessary nodes in the Parse tree which is
     * indeed a complete AST. (Incomplete - Unused)
     */
    public static void Filter_PTree (ParseTree p, DAGraph g, HashMap<Integer, ArrayList <String>> ssindex  )
    {


        for ( Integer i = 0; i < p.getChildCount(); i++ ) {


            boolean flag = true;
               for(int j=0; j< RLables.length; j++)
               {
                   String s = p.getChild(i).getText();
                   if (RLables[j].equals(s))
                   {
                       flag = false;
                       break;
                   }
            }
            if (flag)
            {
                // p.getChild(i).getParent().getText()
                DAGVertex v = new DAGVertex(p.getChild(i).getText(),((ParseTree) p.getChild(i)).hiddenTokens.get(0).toString() , null);
                if (ssindex.isEmpty())
                {
                    g.Vertices.add(v);
                    int x = p.getChildIndex();
                    ArrayList<String> ars = new ArrayList<String>();
                    ars.add(((ParseTree) p.getChild(i)).hiddenTokens.get(0).toString());
                    ssindex.put(i, ars );
                }
                else
                {
                    int n = -1;
                    int max = 0;
                    for (int m = 0; m< ssindex.size(); m++)
                    {

                        for (int mm=0; mm< ssindex.get(m).size(); mm++)
                        {
                        int compare = compareString(((ParseTree) p.getChild(i)).hiddenTokens.get(0).toString(),ssindex.get(m).get(mm),"_");
                         if (Math.abs( compare) >= Math.abs( max))
                       {
                            n = m;
                            max =compare;
                       }
                        }
                    }

                    if (n!= -1)
                    {
                        if (max >=0 )
                        {
                            //g.AddChild(g.Vertices.get(n), v);
                            g.Vertices.add(v);
                            DAGEdge e = new DAGEdge(g.Vertices.get(n), v, null);
                            g.Edges.add(e);
                        }
                        else
                        {
                         //   g.AddChild( v, g.Vertices.get(n));
                             g.Vertices.add(v);
                            DAGEdge e = new DAGEdge( v, g.Vertices.get(n), null);
                            g.ChangeChild(g.Vertices.get(n), v);
                            g.Edges.add(e);
                        }

                         ArrayList<String> ars = new ArrayList<String>();
                    ars.add(((ParseTree) p.getChild(i)).hiddenTokens.get(0).toString());
                        ssindex.put(g.Vertices.size()-1,ars);
                    }

                }
 

                }

            else
            {
                 int n = -1;
                 int max = 0;
                for (int m = 0; m< ssindex.size(); m++)
                    {
                    for (int mm=0;   mm< ssindex.get(m).size(); mm++)
                        {
                        int compare = compareString(((ParseTree) p.getChild(i)).hiddenTokens.get(0).toString(),ssindex.get(m).get(mm),"_");
                         if (Math.abs( compare) >= Math.abs( max))
                       {
                            n = m;
                            max =compare;
                       }
                    }
                }
                if (n!= -1)
                {
                 ssindex.get(n).add(((ParseTree) p.getChild(i)).hiddenTokens.get(0).toString());
                }

            }
                if (p.getChild(i).getChildCount()>0)
                    Filter_PTree((ParseTree)p.getChild(i), g, ssindex);
        }
    }


    /**
     * This function compares to Strings and returns the number of unmatched strings
     * in two. A string needs to be sent to define the splitting elements of the strings.
     * The result will be less than zero if the second input string is longer than the first one.
     */
    private static int compareString (String s1, String s2, String comparator)
    {

        String[] sa1 = s1.split(comparator);
        String[] sa2 = s2.split(comparator);
        int result = 0;

        for (int i = 0; i< sa1.length & i<sa2.length ; i++)
        {
            if (sa1[i].equals( sa2[i]))
                result++;
            else
                break;
        }

        if (sa1.length < sa2.length)
            result = -1 * result;
        return result;

    }


    
     public static void translateAST (CommonTree t, DAGraph g, DAGVertex parent )
    {
         ArrayList<String> notNeededNodes = new ArrayList<String>();
        notNeededNodes.add(";");
        notNeededNodes.add(")");
        notNeededNodes.add("(");
        notNeededNodes.add(",");

        if ( t != null ) {

            for ( Integer i = 0; i < t.getChildCount(); i++ ) {
                if (!notNeededNodes.contains(t.getChild(i).getText())){
                    String nodename = ((CommonTree) t.getChild(i)).getText();
                    String nodetype = String.valueOf(((CommonTree) t.getChild(i)).getType());
                    DAGVertex v = new DAGVertex(nodename ,nodetype, null);
                    g.Vertices.add(v);
                     if ( parent!= null){
                            DAGEdge e = new DAGEdge( parent, v,  null);
                            g.Edges.add(e);
                    }

                    if (t.getChild(i).getChildCount()>0)
                    {
                         translateAST((CommonTree) t.getChild(i), g, v );
                    }
                }
            }
        }
    }
     
    /**
     * This function translates the Abstract Syntax Tree to a Directed Acyclic Graph.
     * The hierarchy of the tree is distinguished based on the token variables defined in
     * Verilog.tokens and the flat AST generated by ANTLR parser.
     *
     */

    public static void old_TranslateAST (CommonTree ct, DAGraph g )
    {
        ArrayList<DAGVertex> vlist = new ArrayList<DAGVertex>();


        for (int i = 0; i< ct.getChildCount(); i++)
        {
            DAGVertex v = new DAGVertex(ct.getChild(i).getText(), String.valueOf( ct.getChild(i).getType()), null);
            v.Index = i;
            vlist.add(v);
        }

        for (int i = 0; i<vlist.size(); i++)
        {
            DAGVertex v = vlist.get(i);
            if (v.Type.equals("67") ) //module
                g.AddVertex(v);
            if (v.Type.equals("19"))//variable or identifier
            {
                if (vlist.get(i-1).Type.equals("67")) //module name
                {
                    g.Vertices.add(v);
                    DAGEdge e = new DAGEdge(vlist.get(i-1), v, null);
                    g.Edges.add(e);

                }
                if (vlist.get(i-1).Type.equals("17")) //assign
                {
                    g.Vertices.add(v);
                }
                if (vlist.get(i-1).Type.equals("5")) //( pranteses
                {
                    g.Vertices.add(v);
                }
                if ( 30<= Integer.valueOf( vlist.get(i-1).Type) & Integer.valueOf( vlist.get(i-1).Type) <= 51 ) //after an operator
                {
                    g.Vertices.add(v);
                }
            }


            if (v.Type.equals("14")) //assigment sign =
            {
                //right hand side
                if ( vlist.get(i-1).Type.equals("19"))
                {
                    g.Vertices.add(v);
                    DAGEdge e = new DAGEdge(vlist.get(i-1), v, null);
                    g.Edges.add(e);
                }
                //left hand side

                if ( vlist.get(i+1).Type.equals("19")) //identifier
                {
                    if (32<= Integer.valueOf( vlist.get(i+2).Type) & Integer.valueOf(vlist.get(i+2).Type) <= 51)
                    {
                        
                        DAGEdge e = new DAGEdge( v, vlist.get(i+2),null);
                        g.Edges.add(e);
                    }
                    else
                    {
                        
                        DAGEdge e = new DAGEdge( v, vlist.get(i+1),null);
                        g.Edges.add(e);
                    }
                }
                else
                {
                    if (30<= Integer.valueOf( vlist.get(i+1).Type) & Integer.valueOf(vlist.get(i+1).Type) <= 31)
                    {

                        DAGEdge e = new DAGEdge( v,vlist.get(i+1), null);
                        g.Edges.add(e);
                    }

                    else
                    {

                         if (vlist.get(i+1).Type.equals("5")) //OPEN PRANTESES
                         {
                             int pran = 1;

                                for (int j = i+2; j<vlist.size() ; j++) //assuming the code syntax is correct
                                {
                                    if (pran==1)
                                    {
                                         if  ( 30<= Integer.valueOf( vlist.get(j).Type) & Integer.valueOf( vlist.get(j).Type) <= 51) //binary operator
                                        {

                                            DAGEdge e = new DAGEdge(v, vlist.get(j), null);
                                            g.Edges.add(e);
                                            break;
                                        }
                                         else if (vlist.get(j).Type.equals("19")) //identifier and the prantess : (i)
                                         {
                                            if (vlist.get(j+1).Type.equals("7"))
                                            {

                                                DAGEdge e = new DAGEdge(v, vlist.get(j), null);
                                                g.Edges.add(e);
                                                break;
                                            }
                                         }
                                    }
                                    else
                                    {
                                        if (vlist.get(j).Type.equals("5")) //nested pranteses
                                            pran++;
                                        else if (vlist.get(j).Type.equals("7"))
                                            pran--;
                                    }

                                }

                         }
                    }
                }
            }

            

            if (v.Type.equals("31")) // NOT sign ~ UNARY
            {

                if (vlist.get(i+1).Type.equals("19")) //identifier
                {
                    g.Vertices.add(v);
                    DAGEdge e = new DAGEdge( v,vlist.get(i+1), null);
                    g.Edges.add(e);
                }
                else
                {

                     if (vlist.get(i+1).Type.equals("5")) //OPEN PRANTESES
                     {
                         int pran = 1;

                            for (int j = i+2; j<vlist.size() ; j++) //assuming the code syntax is correct
                            {
                                if (pran==1)
                                {
                                     if  ( 32<= Integer.valueOf( vlist.get(j).Type) & Integer.valueOf( vlist.get(j).Type) <= 51) //binary operator
                                    {
                                        g.Vertices.add(v);
                                        DAGEdge e = new DAGEdge(v, vlist.get(j), null);
                                        g.Edges.add(e);
                                        break;
                                    }
                                     else if (vlist.get(j).Type.equals("19")) //identifier and the prantess : (i)
                                     {
                                        if (vlist.get(j+1).Type.equals("7"))
                                        {
                                            g.Vertices.add(v);
                                            DAGEdge e = new DAGEdge(v, vlist.get(j), null);
                                            g.Edges.add(e);
                                            break;
                                        }
                                     }
                                }
                                
                                    if (vlist.get(j).Type.equals("5")) //nested pranteses
                                        pran++;
                                    else if (vlist.get(j).Type.equals("7"))
                                        pran--;
                                

                            }
                        
                     }
                }

            }

            if (32<= Integer.valueOf( v.Type) & Integer.valueOf( v.Type) <= 51) //binary operator
            {

                //left side the operator
                if ( vlist.get(i-1).Type.equals("19")) //ifdentifier
                {
                    g.Vertices.add(v);
                    DAGEdge e = new DAGEdge(v, vlist.get(i-1), null);
                    g.Edges.add(e);
                }

                else
                {

                     if (vlist.get(i-1).Type.equals("7")) //close PRANTESES
                     {
                         int pran = 1;

                            for (int j = i-2; j>0 ; j--) //assuming the code syntax is correct
                            {
                                if (pran==1)
                                {
                                     if  ( 30<= Integer.valueOf( vlist.get(j).Type) & Integer.valueOf( vlist.get(j).Type) <= 51) //binary operator
                                    {
                                        g.Vertices.add(v);
                                        DAGEdge e = new DAGEdge(v, vlist.get(j), null);
                                        g.Edges.add(e);
                                        break;
                                    }
                                     else if (vlist.get(j).Type.equals("19")) //identifier and the prantess : (i)
                                     {
                                        if (vlist.get(j-1).Type.equals("5"))
                                        {
                                            g.Vertices.add(v);
                                            DAGEdge e = new DAGEdge(v, vlist.get(j), null);
                                            g.Edges.add(e);
                                            break;
                                        }
                                     }
                                }
                                
                                    if (vlist.get(j).Type.equals("7")) //nested pranteses
                                        pran++;
                                    else if (vlist.get(j).Type.equals("5"))
                                        pran--;
                                

                            }

                     }
                }
                //right side of the operator
                if ( vlist.get(i+1).Type.equals("19"))
                {
                    
                    DAGEdge e = new DAGEdge(v, vlist.get(i+1), null);
                    g.Edges.add(e);
                }

                else
                {

                     if (vlist.get(i+1).Type.equals("5")) //OPEN PRANTESES
                     {
                         int pran = 1;

                            for (int j = i+2; j<vlist.size() ; j++) //assuming the code syntax is correct
                            {
                                if (pran==1)
                                {
                                     if  ( 30<= Integer.valueOf( vlist.get(j).Type) & Integer.valueOf( vlist.get(j).Type) <= 51) //binary operator
                                    {
                                        DAGEdge e = new DAGEdge(v, vlist.get(j), null);
                                        g.Edges.add(e);
                                        break;
                                    }
                                     else if (vlist.get(j).Type.equals("19")) //identifier and the prantess : (i)
                                     {
                                        if (vlist.get(j+1).Type.equals("7"))
                                        {
                                            DAGEdge e = new DAGEdge(v, vlist.get(j), null);
                                            g.Edges.add(e);
                                            break;
                                        }
                                     }
                                }
                                
                                    if (vlist.get(j).Type.equals("5")) //nested pranteses
                                        pran++;
                                    else if (vlist.get(j).Type.equals("7"))
                                        pran--;
                                

                            }

                     }
                }
            }

        }

        //connect leaves
        HashMap<String, ArrayList<DAGVertex>> ilist = new HashMap<String, ArrayList<DAGVertex>>();

        for (int i = 0; i< vlist.size(); i++)
        {
            DAGVertex v = vlist.get(i);
            if (v.Type.equals("19"))//identifier
            {
                if (ilist.containsKey(v.Name))
                {
                    ilist.get(v.Name).add(v);
                }
                else
                {
                    ArrayList<DAGVertex> iv = new ArrayList<DAGVertex>();
                    iv.add(v);
                    ilist.put(v.Name, iv);
                }

            }

        }

        ArrayList<String> keys = new ArrayList<String>();
      //  keys = (ArrayList<String>) ilist.keySet();
        

       Iterator iterator = ilist.keySet().iterator();

        while(iterator.hasNext()){
            keys.add((String) iterator.next());
        }
        for (int i =0; i< keys.size(); i++)
        {
            String k = keys.get(i);
            ArrayList<DAGVertex> vl = new ArrayList<DAGVertex>();
            vl = ilist.get(k);
            for (int j=1; j<vl.size(); j++ )
            {
                g.ReplaceVertex(vl.get(j), vl.get(0));
                g.Vertices.remove(vl.get(j));
            }

        }


    }



    public static String gateLevelMapping (String[] error)
    {
        HashMap<String, DAGraph> gateMap  = new HashMap<String, DAGraph>();
        generateGateMap(gateMap);

        DAGraph transG = new DAGraph();
        transG = g.Copy();

        
        for (int i =0; i< transG.Vertices.size(); i++)
        {

            DAGVertex vi = transG.Vertices.get(i);
            if (! vi.Type.equals("19")) //if not a variable
            {
                if (gateMap.containsKey(vi.Type) )
                {
                    boolean flag = true;
                    if (vi.Type.equals("34"))
                    {
                        for (int ei = 0; ei<transG.Edges.size(); ei++)
                        {
                            DAGEdge cure = transG.Edges.get(ei);
                            if (cure.To != null & cure.From != null)
                            {
                                if (cure.To.equals(vi) && cure.From.Type.equals("31")) //this shows it is already a NOR not an OR
                                {
                                    flag = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (flag)
                        vi.Cover = 0;
                } else
                {
                    error[0] +="Not transferable Operator ( " + vi.Name + ") !\n";
                }
                }
//end of vertex covering

        }

       ArrayList< DAGVertex> newVertices = new ArrayList<DAGVertex>();

        for (int i =0; i< transG.Vertices.size(); i++)
        {
            DAGVertex vi = transG.Vertices.get(i);
            if (!vi.Type.equals("19")) //if not a variable
            {
                if (vi.Cover != -1)
                {

                        DAGraph coverg = gateMap.get(vi.Type).Copy();
                        ArrayList<DAGVertex> CoverRoots = coverg.findRoots();
                        ArrayList<DAGVertex> CoverLeafs = coverg.findLeaf();


                        for (int j = 0; j< transG.Edges.size(); j++)
                        {
                            DAGEdge cure = transG.Edges.get(j);
                            int edgetype  = 0; //0==nothing, 1==root, 3== interiur, 2== leaf
                            if (cure.To != null & cure.From != null)
                            {
                                if (cure.To.equals(vi))
                                {
                                    edgetype += 1;
                                }
                                if (cure.From.equals(vi))
                                {
                                    edgetype +=2;
                                }
                            }

                            if (edgetype == 1) //this is OK if the opretor's input's place is unimportant
                            {
                                cure.To = CoverRoots.get(0);
                                CoverRoots.remove(0);
                            }
                            if (edgetype == 2)//this is OK if the opretor's input's place is unimportant
                            {

                                cure.From = CoverLeafs.get(0);
                                if (cure.Next != null)
                                    cure.Next = null;
                                if (CoverLeafs.get(0).Outgoing == null)
                                CoverLeafs.get(0).Outgoing = cure;
                                else
                                     CoverLeafs.get(0).Outgoing.Next = cure;
                                
                                if (CoverLeafs.get(0).Type.equals("31"))
                                    CoverLeafs.remove(0);
                            }
                            if (edgetype == 3)//this never happens for this case
                            {
                                cure.From = null;
                                cure.To = null;
                            }

                         }

                        for (int k = 0; k< coverg.Vertices.size(); k++)
                        {
                             coverg.Vertices.get(k).Index = ++DAGVertex.numberofvertex;
                        }

                        for (int k=0; k< coverg.Edges.size(); k++)
                        {
                            coverg.Edges.get(k).Index = ++DAGEdge.numberofedges;
                        }

                        newVertices.addAll(coverg.Vertices);
                        transG.Edges.addAll(coverg.Edges);

                }

            }
            if (vi.Cover == -1)
                newVertices.add(vi);

        }

       ArrayList<DAGEdge> newEdges = new ArrayList<DAGEdge>();
       for (int i =0; i< transG.Edges.size(); i++ )
       {
           DAGEdge cure = transG.Edges.get(i);
           if (cure.From!= null & cure.To != null)
               newEdges.add(cure);
       }

       transG.Edges = newEdges;
       transG.Vertices = newVertices;


       String Result =  "\n\n\n&&&&&&&&&&&&&&&&&  NOR Technology Mapping  &&&&&&&&&&&&&&&&&&&&&\n";
       Result += transG.PrintGraph();
       g = transG;

       CelloCircuit newds = new CelloCircuit();
       newds.setInputGraph(transG.Copy());
       newds.setIsOptimized(false);
       newds.setIsTransferred(true);
       implementations.put("Transformed", newds);
       return Result;

    }


     private static void generateGateMap(HashMap<String, DAGraph> gateMap) {


         DAGVertex ORv = new DAGVertex("|", "34", null);
         DAGVertex NOTv = new DAGVertex("~", "31" , null);

         DAGraph andEqui = new DAGraph();
         andEqui.Vertices.add( NOTv.Copy());
         andEqui.Vertices.add(ORv.Copy());
         andEqui.Vertices.add( NOTv.Copy());
         andEqui.Vertices.add(NOTv.Copy());
         andEqui.Vertices.get(0).Index= ++DAGVertex.numberofvertex;
         andEqui.Vertices.get(1).Index= ++DAGVertex.numberofvertex;
         andEqui.Vertices.get(2).Index= ++DAGVertex.numberofvertex;
         andEqui.Vertices.get(3).Index= ++DAGVertex.numberofvertex;
         andEqui.setParent(andEqui.Vertices.get(0));
         andEqui.Edges.add(new DAGEdge(andEqui.Vertices.get(0), andEqui.Vertices.get(1), null));
         andEqui.Edges.add(new DAGEdge(andEqui.Vertices.get(1), andEqui.Vertices.get(2), null));
         andEqui.Edges.add(new DAGEdge(andEqui.Vertices.get(1), andEqui.Vertices.get(3), null));
         gateMap.put("32",andEqui ); // token for AND in VerilogA.tokens

         DAGraph orEqui = new DAGraph();
         orEqui.Vertices.add( NOTv.Copy());
         orEqui.Vertices.add( NOTv.Copy());
         orEqui.Vertices.add(ORv.Copy());
         orEqui.Vertices.get(0).Index= ++DAGVertex.numberofvertex;
         orEqui.Vertices.get(1).Index= ++DAGVertex.numberofvertex;
         orEqui.Vertices.get(2).Index= ++DAGVertex.numberofvertex;
         orEqui.setParent(orEqui.Vertices.get(0));
         orEqui.Edges.add(new DAGEdge(orEqui.Vertices.get(0), orEqui.Vertices.get(1), null));
         orEqui.Edges.add(new DAGEdge(orEqui.Vertices.get(1), orEqui.Vertices.get(2), null));
         gateMap.put("34",orEqui ); // token for AND in VerilogA.tokens



    }



     public  static String gateLevelOptimization(String[] error, boolean doubleInverters, boolean NORs)
    {
         String result = "";
         DAGraph optG = new DAGraph();

         if (implementations.containsKey(NORBased))
         {
             CelloCircuit newds =  new CelloCircuit();
             //implementations.get(NORBased);
             newds.setInputGraph(implementations.get(NORBased).getInputGraph().Copy());
            optG = newds.getInputGraph();//g.Copy();

            if (doubleInverters){
                result+= removeDoubleInverter(optG, error, NORs);
                g = optG;

                newds.setIsOptimized(true);
                newds.setIsTransferred(true);
                
                implementations.put("GOpt-" + NORBased, newds);

             }

        }

             if (implementations.containsKey(AST))
             {
                 CelloCircuit newds =  new CelloCircuit();
             //implementations.get(NORBased);
             newds.setInputGraph(implementations.get(AST).getInputGraph().Copy());
                optG = newds.getInputGraph();//g.Copy();

                if (doubleInverters){
                    result+= removeDoubleInverter(optG, error, true);
                    g = optG;
                    newds.setInputGraph(optG);
                    newds.setIsOptimized(true);
                    newds.setIsTransferred(false);
                    implementations.put("GOpt-" + AST, newds);
                 }
        }
         if (implementations.isEmpty()){
                 error[0] += "No compiled input!\n";
             }

       
             return result;
     }

     private static String removeDoubleInverter(DAGraph optg, String[] message, boolean considerNORs)
    {
         String result = "";
        DAGraph doubleInv = new DAGraph();
        DAGVertex NOTv1 = new DAGVertex("~", "31" , null);
        DAGVertex NOTv2 = new DAGVertex("~", "31" , null);
        DAGEdge v1tov2 = new DAGEdge(NOTv1, NOTv2, null);
        doubleInv.Vertices.add(NOTv1);
        doubleInv.Vertices.add(NOTv2);
        doubleInv.Edges.add(v1tov2);
        //ArrayList<DAGraph> reductionList = new ArrayList<DAGraph>();
        //reductionList.add(buffer);


        ArrayList<DAGVertex> heads = new ArrayList<DAGVertex>();
        ArrayList<DAGVertex> toes = new ArrayList<DAGVertex>();

        int counter = 0;
        for (int i =0; i< optg.Vertices.size(); i++)
        {
            DAGVertex vi = optg.Vertices.get(i);
            
                if (vi.Type.equals("31"))
                    {

                        DAGEdge hvie = vi.Outgoing; //when having branches
                        boolean flag = false;

                        if (vi.Outgoing!=null)
                        {
                            if (vi.Outgoing.Next==null)
                            {
                            DAGVertex vi2 = vi.Outgoing.To;
                            if (vi2.Type.equals("31"))
                            {
                                if (vi2.Outgoing!= null)
                                {
                                    if (!considerNORs) //CHECK IF WE NEED TO PACK norS AND DO NOT REOVE THE INVERTORS THAT ARE COPRRCUPTING nor STRUCTURE OR NOT
                                    {
                                        flag = true;
                                    }else{
                                        if (!vi2.Outgoing.To.Type.equals("34"))
                                        {
                                            flag = true;
                                        }
                                    }

                                }
                            }

                            if (flag)
                            {
                                for(int j=0; j<optg.Edges.size(); j++)
                                {
                                     if (optg.Edges.get(j).To != null){
                                    if (optg.Edges.get(j).To.Index == vi.Index)
                                    {
                                        heads.add(optg.Edges.get(j).From);
                                        break;
                                    }
                                    }
                                }
                                vi.Type = "del";
                                vi.Outgoing.From = null;
                                vi.Outgoing.To = null;
                                vi2.Type = "del";
                                toes.add(vi2.Outgoing.To);
                                counter++;

                            }

                        }
                    }
                }
            }
        

        message[0] += String.valueOf(counter)+" double inverters found and removed!\n";


        for (int i=0; i< toes.size(); i++)
        {
            DAGVertex ct = toes.get(i);
            for (int j=0; j< optg.Edges.size(); j++)
            {
                DAGEdge ce = optg.Edges.get(j);
                if (ce.To!= null & ce.From != null)
                {
                    if (ce.To.Index== ct.Index & ce.From.Type.equals("del"))
                    {
                        ce.From=null;
                        ce.To = null;
                        ce.Next = null; //it should be already null because ~ has only one outgoing edge
                    }
                }
            }
        }

        for (int i=0; i< heads.size(); i++)
        {
            DAGEdge ce = heads.get(i).Outgoing;
            while (ce!= null)
            {
                boolean flag = false;
                if (ce.To!= null)
                {
                    if (ce.To.Type.equals("del"))
                    {
                        ce.To = toes.get(i);
                        flag = true;
                    }
                }
                if (flag)
                    break;
                ce = ce.Next;
            }
        }
       
       ArrayList<DAGEdge> newEdges = new ArrayList<DAGEdge>();
       for (int i =0; i< optg.Edges.size(); i++ )
       {
           DAGEdge cure = optg.Edges.get(i);
           if (cure.From!= null & cure.To != null)
               newEdges.add(cure);
       }


        ArrayList< DAGVertex> newVertices = new ArrayList<DAGVertex>();
        for (int i =0; i< optg.Vertices.size(); i++ )
       {
           DAGVertex curv = optg.Vertices.get(i);
           if (! curv.Type.equals("del"))
               newVertices.add(curv);
       }

       optg.Edges = newEdges;
       optg.Vertices = newVertices;


       result += "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n";
       result += optg.PrintGraph();

       return result;
     }

/**
     * This function is for adding sequences and making motifs.
     */
    public static void addPartList ()
    {/*
        gt.add(new CelloGene("CI", 0));
        gt.add(new CelloGene("Cro", 0));
        gt.add(new CelloGene("TetR", 0));
        gt.add(new CelloGene("Gene4", 0));

        ogt.add(new CelloGene("GFP", 0));
        ogt.add(new CelloGene("YFP", 0));

        ipt.add(new CelloIPromoter("Plux", 0)); //Plux
        ipt.add(new CelloIPromoter("Ptac", 0));
        ipt.add(new CelloIPromoter("PLacI", 0));
        ipt.add(new CelloIPromoter("PBad", 0));

        rpt.add(new CelloRPromoter("CI_RPromoter", 0));
        rpt.add(new CelloRPromoter("Cro_RPromoter", 0));
        rpt.add(new CelloRPromoter("TetR_RPromoter", 0));
        rpt.add(new CelloRPromoter("Gene4_RPromoter", 0));

        rgpt.add(rpt.get(0), gt.get(0));
        rgpt.add(rpt.get(1), gt.get(1));
        rgpt.add(rpt.get(2), gt.get(2));
        rgpt.add(rpt.get(3), gt.get(3));

        rbst.add(new CelloRBS("RBS0", 0));
        rbst.add(new CelloRBS("RBS1", 0));
        rbst.add(new CelloRBS("RBS2", 0));
        rbst.add(new CelloRBS("RBS3", 0));
        rbst.add(new CelloRBS("RBS4", 0));

        tt.add(new CelloTerminator("Terminator", 0));

      *
      */

        PartList.clear();

        CelloNOR nor = new CelloNOR();

        CelloNot not = new CelloNot();
        PartList.add(nor);

        PartList.add(not);
        /*nor.setGene1(gt.get(0));
        nor.setGene2(gt.get(1));

        nor.setIPromoter1(ipt.get(0));
        nor.setIPromoter2(ipt.get(1));

        nor.setRPromoter(rpt.get(0));

        nor.setRBS1(rbst.get(0));
        nor.setRBS2(rbst.get(1));
         *
         */

    }



    public static String motifLevelMapping(String impType,  boolean nor, boolean nor3 )//boolean ifTransferred, boolean ifOptimized)
    {
        String result = "";
        ArrayList<CelloGates> PartListNOR2 = new  ArrayList<CelloGates>();
                CelloNOR cNor = new CelloNOR();
                CelloNot cNot = new CelloNot();
                PartListNOR2.add(cNor);
                PartListNOR2.add(cNot);
       ArrayList<CelloGates> PartListNORn = new  ArrayList<CelloGates>();
                CelloNOR3 cNor3 = new CelloNOR3();
                PartListNORn.add(cNor3);
                PartListNORn.add(cNor);
                PartListNORn.add(cNot);
        if (implementations.containsKey(impType))
        {
            CelloCircuit newds = implementations.get(impType);
            CelloCircuit newds2 =  new CelloCircuit();
            newds2.setInputGraph(newds.getInputGraph().Copy());
            newds2.setIsOptimized(newds.isOptimized());
            newds2.setIsTransferred(newds.isTransferred());

            if (nor)
            {

                result += AssignParts(newds, PartListNOR2);
            }
            if (nor3)
            {

                result+= AssignPartsNorn(newds2, PartListNORn);
                implementations.put(NORnBased+"-"+ impType, newds2);
            }
        }
                /*
        else if (implementations.containsKey(AST))
        {
            CelloCircuit newds = implementations.get(AST);
            CelloCircuit newds2 =  new CelloCircuit();
            newds2.setInputGraph(newds.getInputGraph().Copy());
            newds2.setIsOptimized(newds.isOptimized());
            newds2.setIsTransferred(newds.isTransferred());

            if (nor)
                result += AssignParts(newds, PartListNOR2);
            if (nor3)
            {
                result+= AssignPartsNorn(newds2, PartListNORn);
                implementations.put(NORnBased, newds2);
            }
        }
                 * 
                 */
        else{
            result+= "No Compiled input!\n";
        }

        return result;
    }

      public static String AssignPartsNorn(CelloCircuit imp ,  ArrayList<CelloGates> PartList2)
    {

        mappedg = new DAGraph();
        mappedg = imp.getInputGraph().Copy(); //g.Copy();
        //addPartList();
        int cindex =1;

        /*
         ArrayList<CelloGates> PartList2 = new  ArrayList<CelloGates>();
        CelloNOR3 nor3 = new CelloNOR3();
        PartList2.add(nor3);
        CelloNOR nor = new CelloNOR();
        PartList2.add(nor);
        CelloNot not = new CelloNot();
        PartList2.add(not);

         *
         */
        for (int i =0; i< mappedg.Vertices.size(); i++)
        {

            DAGVertex vi = mappedg.Vertices.get(i);
            if (! vi.Type.equals("19")) //if not a variable
            {
                for (int j=0; j< PartList2.size(); j++)
                {
                    DAGraph equi = PartList2.get(j).getEquiGraphe();
                    if (equi.getParent().Name.equals (vi.Name) & vi.Cover== -1)
                    {


                        ArrayList<DAGVertex> cover = new ArrayList<DAGVertex>(); //for saving covered vertices
                        cover.add(vi);
                        DAGEdge e = equi.getParent().Outgoing;
                        DAGEdge vie = vi.Outgoing;
                        boolean flag = true;
                        while (e!= null)
                        {
                            while (e!= null && vie != null )
                            {
                                if (e.To.Name.equals(vie.To.Name))
                                {
                                    cover.add(vie.To);
                                    if (e.Next!= null)
                                    {
                                        e = e.Next;
                                        vie = vie.Next;
                                    }else
                                        break;
                                }
                                else
                                {
                                    flag = false;
                                    break;
                                }

                            }

                            if (flag && e != null)
                            {
                                e  = e.To.Outgoing;
                                vie = vie.To.Outgoing;
                            }
                            else
                                break;

                        }

                        if (flag)
                        {
                            cover.get(0).Type= "head";
                            for(int k=0; k< cover.size(); k++)
                            {
                                cover.get(k).Cover = j;
                                cover.get(k).subCover = cindex;
                            }
                            cindex++;
                            break;
                        }

                    }
                }
//end of vertex covering

            }



        }

       ArrayList< DAGVertex> newVertices = new ArrayList<DAGVertex>();

        for (int i =0; i< mappedg.Vertices.size(); i++)
        {
            DAGVertex vi = mappedg.Vertices.get(i);
            if (!vi.Type.equals("19")) //if not a variable
            {
                if (vi.Cover != -1)
                {

                    if (PartList2.get(vi.Cover).getEquiGraphe().getParent().Name.equals(vi.Name) & vi.Type.equals("head")) //if it is the head
                    {
                        DAGraph coverg = PartList2.get(vi.Cover).getGraphe().Copy();
                        ArrayList<DAGVertex> CoverRoots = coverg.findRoots();
                        ArrayList<DAGVertex> CoverLeafs = coverg.findLeaf();


                        for (int j = 0; j< mappedg.Edges.size(); j++)
                        {
                            DAGEdge cure = mappedg.Edges.get(j);
                            int edgetype  = 0; //0==nothing, 1==root, 3== interiur, 2== leaf
                            if (cure.To != null & cure.From != null)
                            {
                                if (cure.To.Cover == vi.Cover && cure.To.subCover == vi.subCover)
                                {
                                    edgetype += 1;
                                }
                                if (cure.From.Cover == vi.Cover && cure.From.subCover == vi.subCover)
                                {
                                    edgetype +=2;
                                }
                            }

                            if (edgetype == 1) //this is OK if the opretor's input's place is unimportant
                            {
                                cure.To = CoverRoots.get(0);
                                CoverRoots.remove(0);
                            }
                            if (edgetype == 2)//this is OK if the opretor's input's place is unimportant
                            {
                                cure.From = CoverLeafs.get(0);
                                if (cure.Next != null)
                                    cure.Next = null;
                                CoverLeafs.get(0).Outgoing = cure;
                                CoverLeafs.remove(0);
                            }
                            if (edgetype == 3)
                            {
                                cure.From = null;
                                cure.To = null;
                            }

                         }

                        for (int k = 0; k< coverg.Vertices.size(); k++)
                        {
                            coverg.Vertices.get(k).Cover =vi.Cover;
                            coverg.Vertices.get(k).subCover = vi.subCover;
                             coverg.Vertices.get(k).Index = ++DAGVertex.numberofvertex;
                        }

                        for (int k=0; k< coverg.Edges.size(); k++)
                        {
                            coverg.Edges.get(k).Index = ++DAGEdge.numberofedges;
                        }

                        newVertices.addAll(coverg.Vertices);
                        mappedg.Edges.addAll(coverg.Edges);

                    }

                }

            }
            if (vi.Cover == -1)
                newVertices.add(vi);

        }

       ArrayList<DAGEdge> newEdges = new ArrayList<DAGEdge>();
       for (int i =0; i< mappedg.Edges.size(); i++ )
       {
           DAGEdge cure = mappedg.Edges.get(i);
           if (cure.From!= null & cure.To != null)
               newEdges.add(cure);
       }

       mappedg.Edges = newEdges;
       mappedg.Vertices = newVertices;



      // superg = creatSuperGraph (mappedg);

        String Result = "\n\n\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n";
        Result += g.PrintGraph();
         Result += "\n\n\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n";
       Result += mappedg.PrintGraph();

       imp.setMappedGraph(mappedg.Copy());

       return Result;

    }

    /**
     * This function assigns parts to graph and replaces the functional nodes of the graph
     * which is covered by the part (e.g. the NOR part can cover the pattern | --> ~ in
     * the graph) and replaces the covered nodes with the part graph which is a graph itself.
     */
    public static String AssignParts(CelloCircuit imp,  ArrayList<CelloGates> PartList)
    {

        mappedg = new DAGraph();
        mappedg = imp.getInputGraph().Copy(); //g.Copy();
        //addPartList();
        int cindex =1;
        
        for (int i =0; i< mappedg.Vertices.size(); i++)
        {

            DAGVertex vi = mappedg.Vertices.get(i);
            if (! vi.Type.equals("19")) //if not a variable
            {
                for (int j=0; j< PartList.size(); j++)
                {
                    DAGraph equi = PartList.get(j).getEquiGraphe();
                    if (equi.getParent().Name.equals (vi.Name))
                    {
                        

                        ArrayList<DAGVertex> cover = new ArrayList<DAGVertex>(); //for saving covered vertices
                        cover.add(vi);
                        DAGEdge e = equi.getParent().Outgoing;
                        DAGEdge vie = vi.Outgoing;
                        boolean flag = true;
                        while (e!= null)
                        {
                            while (e!= null && vie != null )
                            {
                                if (e.To.Name.equals(vie.To.Name))
                                {
                                    cover.add(vie.To);
                                    e = e.Next;
                                    vie = vie.Next;
                                }
                                else
                                {
                                    flag = false;
                                    break;
                                }

                            }

                            if (flag && e != null)
                                e  = e.To.Outgoing;
                            else
                                break;

                        }

                        if (flag)
                        {
                            for(int k=0; k< cover.size(); k++)
                            {
                                cover.get(k).Cover = j;
                                cover.get(k).subCover = cindex;
                            }
                            cindex++;
                            break;
                        }

                    }
                }
//end of vertex covering

            }



        }

       ArrayList< DAGVertex> newVertices = new ArrayList<DAGVertex>();

        for (int i =0; i< mappedg.Vertices.size(); i++)
        {
            DAGVertex vi = mappedg.Vertices.get(i);
            if (!vi.Type.equals("19")) //if not a variable
            {
                if (vi.Cover != -1)
                {

                    if (PartList.get(vi.Cover).getEquiGraphe().getParent().Name.equals(vi.Name)) //if it is the head
                    {
                        DAGraph coverg = PartList.get(vi.Cover).getGraphe().Copy();
                        ArrayList<DAGVertex> CoverRoots = coverg.findRoots();
                        ArrayList<DAGVertex> CoverLeafs = coverg.findLeaf();


                        for (int j = 0; j< mappedg.Edges.size(); j++)
                        {
                            DAGEdge cure = mappedg.Edges.get(j);
                            int edgetype  = 0; //0==nothing, 1==root, 3== interiur, 2== leaf
                            if (cure.To != null & cure.From != null)
                            {
                                if (cure.To.Cover == vi.Cover && cure.To.subCover == vi.subCover)
                                {
                                    edgetype += 1;
                                }
                                if (cure.From.Cover == vi.Cover && cure.From.subCover == vi.subCover)
                                {
                                    edgetype +=2;
                                }
                            }

                            if (edgetype == 1) //this is OK if the opretor's input's place is unimportant
                            {
                                cure.To = CoverRoots.get(0);
                                CoverRoots.remove(0);
                            }
                            if (edgetype == 2)//this is OK if the opretor's input's place is unimportant
                            {
                                cure.From = CoverLeafs.get(0);
                                if (cure.Next != null)
                                    cure.Next = null;
                                CoverLeafs.get(0).Outgoing = cure;
                                CoverLeafs.remove(0);
                            }
                            if (edgetype == 3)
                            {
                                cure.From = null;
                                cure.To = null;
                            }

                         }

                        for (int k = 0; k< coverg.Vertices.size(); k++)
                        {
                            coverg.Vertices.get(k).Cover =vi.Cover;
                            coverg.Vertices.get(k).subCover = vi.subCover;
                             coverg.Vertices.get(k).Index = ++DAGVertex.numberofvertex;
                        }

                        for (int k=0; k< coverg.Edges.size(); k++)
                        {
                            coverg.Edges.get(k).Index = ++DAGEdge.numberofedges;
                        }

                        newVertices.addAll(coverg.Vertices);
                        mappedg.Edges.addAll(coverg.Edges);

                    }

                }

            }
            if (vi.Cover == -1)
                newVertices.add(vi);

        }

       ArrayList<DAGEdge> newEdges = new ArrayList<DAGEdge>();
       for (int i =0; i< mappedg.Edges.size(); i++ )
       {
           DAGEdge cure = mappedg.Edges.get(i);
           if (cure.From!= null & cure.To != null)
               newEdges.add(cure);
       }

       mappedg.Edges = newEdges;
       mappedg.Vertices = newVertices;



      // superg = creatSuperGraph (mappedg);

        String Result = "\n\n\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n";
        Result += g.PrintGraph();
         Result += "\n\n\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n";
       Result += mappedg.PrintGraph();

       imp.setMappedGraph(mappedg.Copy());
       
       return Result;

    }

    /**
     * This function creates the super graph which is basically shows the relationship
     * between different parts. This is required for printing final sequence.
     */

    private static DAGraph creatSuperGraph(DAGraph mg) {
       DAGVertex v = mg.getParent() ;
       DAGEdge e = v.Outgoing;
       DAGraph result = new DAGraph();

       while (e != null)
       {
           
       }

       return result;
    }


    public static String motifLevelOptimization( String key,  String[] message)//boolean ifTransferred, boolean ifOptimized)
    {
        String result = "";
         if (implementations.containsKey(key))
        {
            CelloCircuit newImp = new CelloCircuit();
            if (implementations.get(key).getMappedGraph().Vertices.isEmpty())
                message[0] = "Selected implementation was not mapped previously!\nSelect a mappled implementation.\n";
            else{
            result+= optimizeMappedGraphe(message, implementations.get(key), newImp);
            implementations.put( "MOpt-"+key, newImp);
            }
         }else
             message[0] = "Incorrect implementation key was selected!\n";
        /*
        if (implementations.containsKey(NORBased))
        {
             CelloCircuit newImp = new CelloCircuit();
          result+= optimizeMappedGraphe(message, implementations.get(NORBased), newImp);
          implementations.put( "MOpt-"+NORBased , newImp);
        }
        else if (implementations.containsKey(AST))
        {
             CelloCircuit newImp = new CelloCircuit();
            result+= optimizeMappedGraphe(message, implementations.get(AST), newImp);
            implementations.put(AST + "-MotifOpt", newImp);
        }
         *
         */

        return result;
    }

    /**
     * This function removes the redundant paths in the mapped graph (graph with motifs).
     * The redundant path is generated by calling makeBufferGraph. In this version only one redundant
     * path exists; however, these function can be applied over more than one path.
     */

    public static String optimizeMappedGraphe ( String[] message, CelloCircuit imp,  CelloCircuit newImp )
    {
        String result = "";
        DAGraph buffer = makeBufferGraph();
        //ArrayList<DAGraph> reductionList = new ArrayList<DAGraph>();
        //reductionList.add(buffer);

        optMappedg = imp.getMappedGraph().Copy(); //mappedg.Copy();

        ArrayList<DAGVertex> heads = new ArrayList<DAGVertex>();
        ArrayList<DAGVertex> toes = new ArrayList<DAGVertex>();

        for (int i =0; i< optMappedg.Vertices.size(); i++)
        {
            DAGVertex vi = optMappedg.Vertices.get(i);
            if (vi.Feature != null)
            {
                if (buffer.getParent().Feature._type.equals(vi.Feature._type))
                    {
                       
                        DAGEdge hvie = vi.Outgoing; //when having branches
                        boolean flag = true;
                        while (hvie!= null)
                        {
                            ArrayList<DAGVertex> cover = new ArrayList<DAGVertex>(); //for saving covered vertices

                         cover.add(vi);
                         
                         DAGEdge vie = hvie;
                         DAGEdge e = buffer.getParent().Outgoing;

                         while (e!= null && vie != null )
                            {
                                if (vie.To.Feature != null)
                                {
                                    if (e.To.Feature._type.equals(vie.To.Feature._type))
                                    {
                                        cover.add(vie.To);
                                        e = e.To.Outgoing;
                                        vie = vie.To.Outgoing;
                                    }
                                    else
                                    {
                                        flag = false;
                                        break;
                                    }
                                }
                                else
                                    {
                                        //flag = false;
                                        break;
                                    }
                                 

                            }


                        if (flag & e == null)
                        {
                            for(int k=1; k< cover.size()-1; k++) //we need to keep head and toe
                            {
                                cover.get(k).Type = "del";
                            }

                            //For showing that the toe is now the input for the gate that head belogns to
                            cover.get(cover.size()-1).Cover = cover.get(0).Cover;
                            cover.get(cover.size()-1).subCover = cover.get(0).subCover;
                            
                            heads.add(cover.get(0));

                            toes.add(cover.get(cover.size()-1));
                        }
                         
                         hvie = hvie.Next;
                        }

                    }
            }

        }


        message[0] += String.valueOf( heads.size())+" paths found and reduced!\n";

 ArrayList<DAGEdge> newRootEdges = new ArrayList<DAGEdge>();
        for (int j = 0; j< optMappedg.Edges.size(); j++)
        {
            DAGEdge cure = optMappedg.Edges.get(j);
            int edgetype  = 0; //0==nothing, 1==root, 3== interiur, 2== leaf
            if (cure.To != null & cure.From != null)
            {
                if (cure.To.Type.equals("del") )
                {
                    edgetype += 1;
                }
                if (cure.From.Type.equals("del") )
                {
                    edgetype +=2;
                }
            }

            if (edgetype == 1) //if this is the output of first node in buffer then connect it to last node
            {
                /*
                for (int k=0; k< heads.size(); k++)
                    if (heads.get(k).Index == cure.From.Index)
                    {
                        if (cure.To.Index != toes.get(k).Index)
                        {
                            cure.To = toes.get(k);
                            break;
                        }
                        else
                        {
                            if (cure.Next != null)
                                cure = cure.Next;
                        }
                    }
                 *
                 */

               // if (cure.Next != null)
               //     cure.Next = null;
                cure.From = null;
                cure.To = null;

            }
            if (edgetype == 2)//this is OK if the opretor's input's place is unimportant
            {
                boolean flag = false;
                for (int k=0; k< toes.size(); k++){

                    if (toes.get(k).Index == cure.To.Index){

                        if (heads.get(k).Outgoing!= null){
                            if (heads.get(k).Outgoing.From == null & heads.get(k).Outgoing.To == null){
                                heads.get(k).Outgoing.From = heads.get(k);
                                heads.get(k).Outgoing.To = toes.get(k);
                                cure.From = null;
                            }else{
                                DAGEdge cedge = heads.get(k).Outgoing;
                                boolean assigned = false;
                                while (cedge.Next != null ){ //!!!!this will work only if we have maximum two output edges for each node.
                                    if (cedge.Next.From == null &cedge.Next.To == null  ) {
                                        cedge.Next.From = heads.get(k);
                                        cedge.Next.To = toes.get(k);
                                        assigned  = true;
                                        cure.From = null;
                                        break;
                                    }
                                    cedge = cedge.Next;
                                }

                                if(!assigned)
                                    {
                                        cure = new DAGEdge(heads.get(k), toes.get(k), null);
                                        cedge.Next = cure;
                                        flag=true;
                                    }

                                }
                            }
                        
                         else{
                            cure = new DAGEdge(heads.get(k), toes.get(k),null);
                            flag = true;
                        }

                        if (flag)
                            newRootEdges.add(cure);
                        break;
                    }
                }
            }
            if (edgetype == 3)
            {
                cure.From = null;
                cure.To = null;
                if (cure.Next != null)
                    cure.Next = null;
            }

         }



       ArrayList<DAGEdge> newEdges = new ArrayList<DAGEdge>();
       for (int i =0; i< optMappedg.Edges.size(); i++ )
       {
           DAGEdge cure = optMappedg.Edges.get(i);
           if (cure.From!= null & cure.To != null)
               newEdges.add(cure);
       }


       newEdges.addAll(newRootEdges);
       

        ArrayList< DAGVertex> newVertices = new ArrayList<DAGVertex>();
        for (int i =0; i< optMappedg.Vertices.size(); i++ )
       {
           DAGVertex curv = optMappedg.Vertices.get(i);
           if (! curv.Type.equals("del"))
               newVertices.add(curv);
       }

       optMappedg.Edges = newEdges;
       optMappedg.Vertices = newVertices;

       result += "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n";
       result += optMappedg.PrintGraph();

       //imp.setMappedGraph(optMappedg.Copy());

       //newImp = new CelloCircuit();
       newImp.setInputGraph(imp.getInputGraph().Copy());
       newImp.setMappedGraph(optMappedg.Copy());
       newImp.setIsOptimized(true);
       newImp.setIsTransferred(imp.isTransferred());
        return result;
    }


    /**
     * This function creates the redundant path graph for a buffer which is in fact
     * the structure of rpromoter->rbs1->gene->term->ipromoter->rbs . The main buffer is from
     * from rbs1 to ipromoter. However, by defining the path this way we can check if the path is
     * connected to another input and output or not.
     */

    public static DAGraph makeBufferGraph ()
    {
        DAGraph buffer = new DAGraph();
        DAGVertex vrprompoter = new DAGVertex("rpromoter", "", null, new CelloRPromoter());
        DAGVertex vrbs1 = new DAGVertex("rbs1", null, null, new CelloRBS());
        DAGVertex vgene = new DAGVertex("gene", null, null, new CelloGene());
        DAGVertex vt = new DAGVertex("terminator",null, null, new CelloTerminator());
        DAGVertex vipromoter = new DAGVertex("ipromoter", null, null, new CelloIPromoter());
        DAGVertex vrbs2 = new DAGVertex("rbs2", null, null, new CelloRBS());

        DAGEdge erptorbs1 = new DAGEdge( vrbs1,vrprompoter, null);
        DAGEdge erbs1togene = new DAGEdge( vgene,vrbs1, null);
        DAGEdge egenetot = new DAGEdge( vt,vgene, null);
        DAGEdge ettoipromoter  = new DAGEdge(vipromoter, vt, null);
        DAGEdge eipromotertorbs2 = new DAGEdge(vrbs2, vipromoter, null);

        buffer.Vertices.add(vrprompoter);
        buffer.Vertices.add(vrbs1);
        buffer.Vertices.add(vgene);
        buffer.Vertices.add(vt);
        buffer.Vertices.add(vipromoter);
        buffer.Vertices.add(vrbs2);

        buffer.Edges.add(erptorbs1);
        buffer.Edges.add(erbs1togene);
        buffer.Edges.add(egenetot);
        buffer.Edges.add(ettoipromoter);
        buffer.Edges.add(eipromotertorbs2);

        buffer.setParent(vrbs2);
        return buffer;


    }

    /**
     * This function assigns sequences to the motifs in the graph. The order is :
     * Input promoter, Repressible promoters, (repressor )Genes, Terminators, and RBS sites.
     * The order can be changed.
     */
    public static String assignSequence (String impKey, String[] error)
    {
        String result = "";


        DAGraph assignedgraph = implementations.get(impKey).getMappedGraph().Copy(); //optMappedg.Copy();

        for(DAGVertex v : assignedgraph.Vertices){
            if (v.Type.equals("assigned")){
                v.Type = "";
                v.Feature._feature = null;
            }
        }

        if (assignedgraph == null)
        {
            error[0]+= "The selected implementation was not mapped!\n";
            return  "";

        }

        if (assignedgraph.Vertices.isEmpty())
        {
            error[0]+= "The selected implementation was not mapped!\n";
            return  "";
        }
        //String [] e1 ="", e2="", e3="", e4="", e5 = "";

       tfRelationTable = dbConnection.createTables(TFList, inputOutputList , gt, ipt, rpt, rbst, tt, ogt, rgpt);

       assignIO(assignedgraph, error);
       
        assignIPromoter(assignedgraph, error);
        
        assignRPromoter (assignedgraph, error);
        
        assignGene (assignedgraph, error);
        
        assignTerminator (assignedgraph, error);

        assignRBS (assignedgraph, error);

        //error[0]= e1+e2+e3+e4+e5;
        
        result += assignedgraph.printSequence();

        CelloCircuit newImp = new CelloCircuit();
        newImp.setInputGraph(implementations.get(impKey).getInputGraph().Copy());
        newImp.setMappedGraph(assignedgraph);
        newImp.setIsOptimized(implementations.get(impKey).isOptimized());
        newImp.setIsTransferred(true);

        implementations.put("Assigned-"+impKey, newImp);

        return result;
    }


    /**
     * This function is for assigning RBS sites to the graph which is the final step
     * of assignment.
     * The assignment here is just fetching from the stack of the predefined rbs sequences "rbst".
     * This version does not have the RBS calculation yet, but the function can be added
     * here in the future.
     */
     private static void assignRBS (DAGraph ag, String [] error) {

         int rbstIndex = 0;
        for (int i=0; i< ag.Vertices.size(); i++)
        {
            DAGVertex curv = ag.Vertices.get(i);
            if (curv.Feature != null)
                if (curv.Feature._type == CelloPrimitiveType.RBS)
                {
                    if (rbstIndex<rbst.Size())
                    {
                        curv.Feature = rbst.get(rbstIndex);
                        //rbstIndex++;
                    }
                    else
                    {
                        error[0] += "Not enough RBS!\n";
                    }


                }
         }
    }


    /**
     * This function assigns terminator sequences from the Terminator table "tt".
     *
     */
    private static void assignTerminator (DAGraph ag, String[] error) {

        for (int i=0; i< ag.Vertices.size(); i++)
        {
            DAGVertex curv = ag.Vertices.get(i);
            if (curv.Feature != null)
                if (curv.Feature._type == CelloPrimitiveType.Terminator)
                {
                    if (tt.Size()>0)
                    curv.Feature = tt.get(0);
                    else
                         error[0] += "Not enough Terminator!\n";
                }
        }
    }


    /**
     * This function finds the repressor gene sequences in gene table "gt" based on the repressible promoter
     * coming afterward. The Repressor gene Promoter table "rgpt" has the indexes of the
     * compatible genes and repressor (assuming they are all orthogonal to each other).
     * Hence, the function for finding orthogonal rpromoters just requires to write in the rgpt table.
     */
     private static void assignGene (DAGraph ag, String[] error) {

         int ogtindex = 0;

        for (int i=0; i< ag.Vertices.size(); i++)
        {
            DAGVertex curv = ag.Vertices.get(i);
            if (curv.Feature != null)
                if (curv.Type!= null)
                if (!curv.Type.equals("assigned"))
                if (curv.Feature._type == CelloPrimitiveType.Gene)
                {
                    DAGVertex fpred = new DAGVertex();
                    for (int ei = 0; ei<ag.Edges.size(); ei++)
                    {
                        if (ag.Edges.get(ei).To.Index == curv.Index)
                        {
                            fpred = ag.Edges.get(ei).From;
                            break;
                        }
                    }

                    DAGVertex spred = new DAGVertex();
                    for (int ei = 0; ei<ag.Edges.size(); ei++)
                    {
                        if (ag.Edges.get(ei).To.Index == fpred.Index)
                        {
                            spred = ag.Edges.get(ei).From;
                            break;
                        }
                    }
                    if (spred.Feature != null)
                    {

                        if ( spred.Feature._type == CelloPrimitiveType.RPromoter)
                        {
                            try
                            {
                            curv.Feature= gt.get(rgpt.getGene( (CelloRPromoter) spred.Feature));
                            curv.Type = "assigned";
                            }
                            catch (Exception e)
                            {
                                error[0] += "Not enough Gene!\n";
                            }

                        }
                    }

                    else //gene not repressing anything
                    {
                        if (ogtindex< ogt.Size())
                        {
                        curv.Feature = ogt.get(ogtindex);
                        curv.Type = "assigned";
                        ogtindex++;
                        }
                        else
                        {
                            error[0] += "Not enough Output Gene!\n";
                        }
                    }


                }
         }
     }

     /**
     * This function assigns repressible promoters sequences from the RPromoter table "rpt".
     * In this version the assignment is in order without checking any characterization data.
     *
     */
     private static void assignRPromoter(DAGraph ag, String[] error) {
        
        int rptindex = 0;
        int orgptindex = 0;
        for (int i=0; i< ag.Vertices.size(); i++)
        {
            DAGVertex curv = ag.Vertices.get(i);
            if (curv.Feature != null)
                if (curv.Type!= null)
                if (!curv.Type.equals("assigned"))
                if (curv.Feature._type == CelloPrimitiveType.RPromoter)
                {

                    boolean flag = false;
                    if (curv.Outgoing!= null)
                        if(curv.Outgoing.To.Feature!= null)
                        if (curv.Outgoing.To.Feature._type == CelloPrimitiveType.Terminator)
                            if (curv.Outgoing.To.Outgoing != null)
                                if (curv.Outgoing.To.Outgoing.To.Feature != null)
                                if (curv.Outgoing.To.Outgoing.To.Feature._type == CelloPrimitiveType.Gene)
                                {
                                    flag = true;
                                    if (orgptindex< rgpt.getTable().keySet().size()){
                                    curv.Feature  = (CelloRPromoter) rgpt.getTable().keySet().toArray()[orgptindex++];
                                    rpt.getTable().remove((CelloRPromoter)curv.Feature);
                                    }
                                    else
                                    {
                                        error[0] += "Not enough paired RPromoters!\n";
                                        break;

                                    }
                                    
                                }
                    if (!flag){
                    if (rptindex < ipt.Size())
                    {
                        curv.Feature = rpt.get(rptindex);
                        curv.Type = "assigned";
                        rptindex++;
                    }
                    else
                    {
                        error[0] += "Not enough RPromoters!\n";
                        break;
                        
                    }
                    }
                }
            
        }
        
    }


    /**
     * This function assigns inducible promoters (usually inputs) sequences from the IPromoter table "ipt".
     * In this version the assignment is in order without checking any characterization data.
     */
    private static void assignIPromoter(DAGraph ag, String[] error) {

        int iptindex = 0;
        for (int i=0; i< ag.Vertices.size(); i++)
        {
            DAGVertex curv = ag.Vertices.get(i);
            if (curv.Feature != null)
                if (curv.Type!= null)
                if (!curv.Type.equals("assigned"))
                if (curv.Feature._type == CelloPrimitiveType.IPromoter)
                {
                    DAGVertex fpred = new DAGVertex();
                    for (int ei = 0; ei<ag.Edges.size(); ei++)
                    {
                        if (ag.Edges.get(ei).To.Index == curv.Index)
                        {
                            fpred = ag.Edges.get(ei).From;
                            break;
                        }
                    }

                    if (inputOutputList.get("input").containsKey(fpred.Name))
                    {
                       // curv.Feature
                    }
                    if (iptindex < ipt.Size())
                    {
                        curv.Feature = ipt.get(iptindex);
                        curv.Type = "assigned";
                        iptindex++;
                    }
                    else
                    {
                        error[0] += "Not enough IPromoters!\n";
                        break;

                    }
                }



        }


    }


  ///////////////////////////Global Variables////////////////////////////////////////////////////

    //This array includes all unnecessary nodes' lables in the Parse Tree of ANTLR. This is nor currently used.
    private static String [] RLables = {",","NoViableAltException(86@[])","unary_operator","binary_operator", "NoViableAltException(85@[])", "(", ")", ";", "name_of_module", "local_identifier","list_of_ports","port", "NoViableAltException", ";", "module_item","continuous_assign", "list_of_assignments", "assignment", "lvalue", "identifier", "identifier_path","expression", "exp0",  "exp7","exp8", "exp9", "exp10", "exp11", "endmodule"};

  /**
   * These tables includes the sequences for genes, promoters, rbs sites and terminators.
   * The values inside the tables are assigned hard coded.
   *
   * The actual version needs to assign values to these tables from the Clotho DB
   *
   */
    private static CelloGeneTable gt = new CelloGeneTable();
    private static CelloIPromoterTable ipt = new CelloIPromoterTable();
    private static CelloRPromoterTable rpt = new CelloRPromoterTable();
    private static CelloRBSTable rbst = new CelloRBSTable();
    private static CelloTerminatorTable tt = new CelloTerminatorTable();
    private static CelloRGenePromoterTable rgpt = new CelloRGenePromoterTable();
    private static CelloGeneTable ogt = new CelloGeneTable();


    private static DAGraph mappedg = new DAGraph(); //This graph is intialized after technology mapping phase (i.e. AssignParts function)
                                                     //This shows the primary graph with connected motifs (genetic parts).

    private static DAGraph optMappedg = new DAGraph(); //This graph is initiated after running optimization function ("optimizeMappedGraph") over mappedg
    private static DAGraph g; //This is the AST graph generated from CommonTree and Token files.
    private static DAGraph superg = new DAGraph(); //The super graph shows the interconnections between Genetic Gates

    public static ArrayList<CelloGates> PartList= new ArrayList<CelloGates>(); //List of gates which is initialized by the user from GUI

    public static HashMap<String, HashMap<String, Feature>> inputOutputList = new HashMap<String, HashMap<String, Feature>>();

    private static HashMap<Feature, CelloPrimitive> tfRelationTable = new HashMap<Feature, CelloPrimitive>();

    public static HashMap<String, CelloCircuit> implementations = new LinkedHashMap<String, CelloCircuit>();
    //public static ArrayList<CelloCircuit> implementations = new ArrayList<CelloCircuit>();

    public  static String AST = "AST";
    public  static String NORBased  = "Transformed";
    public  static String NORnBased = "NORnBased";

    private static ArrayList<Feature> TFList= new ArrayList<Feature>();

    static ArrayList<String> CreateTFList() {

        ArrayList<String> result = new ArrayList<String>();
        TFList = dbConnection.fetchTranscriptionFactors();
        Iterator<Feature> iter = TFList.iterator();
        while (iter.hasNext())
        {
            result.add(iter.next().getName());
        }
        return result;
    }

    static void setTFList(int[] selected) {

        TFList = dbConnection.fetchTranscriptionFactors();
        ArrayList<Feature>  newList = new ArrayList<Feature>();
        for (int i =0; i< selected.length; i++)
        {
            newList.add(TFList.get(selected[i]));
        }

        TFList = newList;
    }
    static void setTFList(ArrayList<Feature> input)
    {
        TFList = input;
    }

    public static  ArrayList<Feature> getTFList ()
    {
        return TFList;
    }

    public static HashMap<String, HashMap<String, Feature>> getIOList()
    {
        return inputOutputList;
    }
    public static void setIOList(HashMap<String, HashMap<String, Feature>> l)
    {
        inputOutputList = l;
    }

    public static Set<String> getImplementation ()
    {
        return implementations.keySet();
    }

    private static void assignIO(DAGraph assignedgraph, String[] error) {
        ArrayList<DAGVertex> inputs = assignedgraph.findLeaf();

        Integer buffNum = 0;


        for(DAGVertex currV: inputs)
        {
            Feature currTF = inputOutputList.get("input").get(currV.Name);
            if (!tfRelationTable.containsKey(currTF))
            {
                error[0]+="No available promoter for input " + currV.Name + "!\n";

            }else
            {
                
                ArrayList<DAGVertex> promoters = new ArrayList<DAGVertex>();
                for ( DAGEdge e : assignedgraph.Edges)
                {
                    if (e.To == currV)
                        promoters.add(e.From);
                }
                CelloPrimitiveType type = tfRelationTable.get(currTF)._type;

                for(DAGVertex prom: promoters)
                {
                    if (prom.Feature._type != type){
                        prom = addBuffer(assignedgraph, currV, prom, type , buffNum);
                        buffNum++;
                        error[0]+= "Buffer added for input " + currV.Name +"! \n";
                    }

                    if (type == CelloPrimitiveType.IPromoter){
                        prom.Feature =  (CelloIPromoter) tfRelationTable.get(currTF);
                        prom.Type = "assigned";
                    }
                    else if (type == CelloPrimitiveType.RPromoter){
                        prom.Feature =  (CelloRPromoter) tfRelationTable.get(currTF);
                        prom.Type = "assigned";
                    }
                }

            }
        }

        ArrayList<DAGVertex> outputs = assignedgraph.findRoots();

        for (DAGVertex currV : outputs) //assuming an output is connected to a Terminator and then a Gene which we want to assign to
        {
           Feature currTF = inputOutputList.get("output").get(currV.Name);
           if (!tfRelationTable.containsKey(currTF))
           {
               error[0]+="No available gene for output " + currV.Name + "!\n";
           }else
           {
               //output format ==> output -> = -> term -> gene
                DAGVertex outAssign= currV.Outgoing.To; //assuming fan-ins ==  1
                DAGVertex outTerm = outAssign.Outgoing.To;
                DAGVertex outGene  = outTerm.Outgoing.To;
                outGene.Feature = (CelloGene) tfRelationTable.get(currTF);
                outGene.Type = "assigned";
           }
        }
        

    }

    private static DAGVertex addBuffer(DAGraph assignedgraph, DAGVertex currV, DAGVertex prom, CelloPrimitiveType type, Integer bufNum) {
        DAGEdge output = prom.Outgoing;
        while(output!= null)
        {
            if (output.To== currV)
                break;
            output= output.Next;
        }

        assignedgraph.Edges.remove(output);
        assignedgraph.Vertices.remove(prom);

        if (type== CelloPrimitiveType.IPromoter)
        {
            DAGVertex vRPrompoter = new DAGVertex("rpromoter", "", null, new CelloRPromoter());
            DAGVertex vT = new DAGVertex("terminator","", null, new CelloTerminator());
            DAGVertex vGene = new DAGVertex("gene", "", null, new CelloGene());
            DAGVertex vRBS = new DAGVertex("rbs1", "", null, new CelloRBS());
            DAGVertex vIPromoter = new DAGVertex("ipromoter", "", null, new CelloIPromoter());


            DAGEdge eRPtoT = new DAGEdge( vRPrompoter, vT, null);
            eRPtoT.Index = DAGEdge.numberofedges++;
            DAGEdge eTtoGen = new DAGEdge( vT, vGene, null);
            eTtoGen.Index = DAGEdge.numberofedges++;
            DAGEdge eGenetoRBS = new DAGEdge( vGene,vRBS, null);
            eGenetoRBS.Index = DAGEdge.numberofedges++;
            DAGEdge eRBStoIP = new DAGEdge(vRBS, vIPromoter, null);
            eRBStoIP.Index = DAGEdge.numberofedges++;
            DAGEdge eIPtoInp = new DAGEdge(vIPromoter, currV, null);
            eIPtoInp.Index = DAGEdge.numberofedges++;

            ArrayList<DAGVertex> newVers  = new ArrayList<DAGVertex>();
            newVers.add(vRPrompoter);
            newVers.add(vT);
            newVers.add(vGene);
            newVers.add(vRBS);
           newVers.add(vIPromoter);

           for(DAGVertex v2: newVers)
           {
               v2.Index = DAGVertex.numberofvertex++;
               v2.Cover = 3;
               v2.subCover = bufNum;
           }

           vRPrompoter.Cover = prom.Cover;
           vRPrompoter.subCover = prom.subCover;

           assignedgraph.Vertices.addAll(newVers);
           
            assignedgraph.Edges.add(eRPtoT);
            assignedgraph.Edges.add(eTtoGen);
            assignedgraph.Edges.add(eGenetoRBS);
            assignedgraph.Edges.add(eRBStoIP);
            assignedgraph.Edges.add(eIPtoInp);

            for(DAGEdge e : assignedgraph.Edges)
            {
                if (e.To == prom)
                    e.To = vRPrompoter;
                    
            }

            return vIPromoter;

        }else
         if(type== CelloPrimitiveType.RPromoter)
         {
            DAGVertex vRPrompoter = new DAGVertex("rpromoter", "", null, new CelloRPromoter());
            DAGVertex vT = new DAGVertex("terminator","", null, new CelloTerminator());
            DAGVertex vGene = new DAGVertex("gene", "", null, new CelloGene());
            DAGVertex vRBS = new DAGVertex("rbs1", "", null, new CelloRBS());
            DAGVertex vRPromoter2 = new DAGVertex("ipromoter", "", null, new CelloRPromoter());

            DAGEdge eRPtoT = new DAGEdge( vRPrompoter, vT, null);
            eRPtoT.Index = DAGEdge.numberofedges++;
            DAGEdge eTtoGen = new DAGEdge( vT, vGene, null);
            eTtoGen.Index = DAGEdge.numberofedges++;
            DAGEdge eGenetoRBS = new DAGEdge( vGene,vRBS, null);
            eGenetoRBS.Index = DAGEdge.numberofedges++;
            DAGEdge eRBStoIP = new DAGEdge(vRBS, vRPromoter2, null);
            eRBStoIP.Index = DAGEdge.numberofedges++;
            DAGEdge eIPtoInp = new DAGEdge(vRPromoter2, currV, null);
            eIPtoInp.Index = DAGEdge.numberofedges++;

            ArrayList<DAGVertex> newVers  = new ArrayList<DAGVertex>();
            newVers.add(vRPrompoter);
            newVers.add(vT);
            newVers.add(vGene);
            newVers.add(vRBS);
           newVers.add(vRPromoter2);

           for(DAGVertex v2: newVers)
           {
               v2.Index = DAGVertex.numberofvertex++;
               v2.Cover = 4;
               v2.subCover = bufNum;
           }

           vRPrompoter.Cover = prom.Cover;
           vRPrompoter.subCover = prom.subCover;

           assignedgraph.Vertices.addAll(newVers);
            assignedgraph.Edges.add(eRPtoT);
            assignedgraph.Edges.add(eTtoGen);
            assignedgraph.Edges.add(eGenetoRBS);
            assignedgraph.Edges.add(eRBStoIP);
            assignedgraph.Edges.add(eIPtoInp);

            for(DAGEdge e : assignedgraph.Edges)
            {
                if (e.To == prom)
                    e.To = vRPrompoter;

            }

            return  vRPromoter2;
            }

        return prom;
    }

    static String MakeCompositePart(String Key) {
        String result = "";

        DAGraph selectedG = implementations.get(Key).getMappedGraph();

        HashMap<String, ArrayList<Part>> cpMap = selectedG.makeCompositePart();



        return result;
    }

    static void findBestSolution(int criteria, int target , String[] error) {
        if (criteria== -1 | target==-1 )
        {
            error[0]= "The criteria is not defined for finding the best solution!\n";
            return;
        }
        String bestKey ="" ;
        int minCount = Integer.MAX_VALUE;
        int maxCount = Integer.MIN_VALUE;
        for(String key: implementations.keySet())
        {
            int count = 0;
            CelloCircuit curC = implementations.get(key);
            if (!curC.getMappedGraph().Vertices.isEmpty()){
                DAGraph curG = curC.getMappedGraph();
                for (DAGVertex v : curG.Vertices)
                {
                    if (criteria==0 ){ //All primitives need to be counted
                        if (v.Feature!= null)
                            count++;
                    }else if (criteria==1){//All reppressor promoters needs to be counted
                        if (v.Feature!=null)
                            if (v.Feature._type == CelloPrimitiveType.RPromoter)
                                count++;
                    }else if (criteria==2){//All inducible promoters need to be counted
                        if (v.Feature!= null)
                            if (v.Feature._type == CelloPrimitiveType.IPromoter)
                                count++;
                    }
                }

                if (target == 0){
                    if (minCount>count)
                    {
                        minCount = count;
                        bestKey = key;
                    }
                }else if(target==1){
                    if (maxCount<count)
                    {
                        maxCount = count;
                        bestKey = key;
                    }
                }
            }
        }
        
        if (!implementations.containsKey(bestKey))
        {
            error[0]= "No best solution found! Check if mapping has been done!\n";
            return;
        }
        
        CelloCircuit bestC = new CelloCircuit();
        bestC.setInputGraph(implementations.get(bestKey).getInputGraph().Copy());
        bestC.setMappedGraph(implementations.get(bestKey).getMappedGraph().Copy());
        bestC.setIsOptimized(implementations.get(bestKey).isOptimized());
        bestC.setIsTransferred(implementations.get(bestKey).isTransferred());
        
        implementations.put("Best Solution", bestC);
        
        error[0]="The best implementation based on the seleted critera is " + bestKey +"!\n";
        
    }



       


}
