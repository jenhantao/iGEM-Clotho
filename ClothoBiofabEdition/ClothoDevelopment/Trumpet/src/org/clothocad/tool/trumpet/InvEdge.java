package org.clothocad.tool.trumpet;

import java.util.ArrayList;

/**
 *
 * @author Craig LaBoda
 *
 */
  public class InvEdge {
        String invNum;   // should be private for good practice
        String source;
        String target;
        int id;
        
        public InvEdge(String invNum, String source, String target) {
            this.id = edgeCount++; // This is defined in the outer class.
            this.invNum = invNum;
            this.source = source;
            this.target = target;
        } 
        
        public String getWeight()
        {
        	return this.invNum;
        }

        
        
        public String toString() { // Always good for debugging
            return (this.source+" "+this.target);
        }

    
        private static int edgeCount = 0;
       //private static ArrayList<String> edgeSet = new ArrayList<String>(0);
   }
