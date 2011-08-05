/*
 * This class extends the InvertSim class.  It is a special type of InvertSim
 * which uses the "Pancake" Algorithm to place invertases in particular positions
 * between parts, such that every permutation of parts has at least one invertase
 * key.
 *
 */
package org.clothocad.tool.trumpet;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Craig
 * 
 */
public class Pancake extends InvertSim {


        /**
         * Creates a Pancake invertase design given the number of parts in the
         * design.
         *
         * @param n - The number of parts
         * @param comb - Whether the object discerns between inverted and non-ineverted parts
        */
        public Pancake(int n, boolean comb)
	{

            // initialize the bitMapArray for this object
            flipBitMap = new Integer[n];
            for (int i=0; i<n; i++)
                flipBitMap[i] = 0;

            // boolean which describes wether inverted parts are recognized separately
            // from non-inverted parts
            combos = comb;

            // create integer currentPer which keeps track of the permutation of parts
            // by their integer value
            currentPerm = new Integer[n];

            // create the actual LinkSort Design
            createDesign(n);

            // define the parts array
            for (int i=0;i<designArray.length;i++)
            {
                if (!(designArray[i].startsWith("I")))
                    this.addPart(designArray[i]);
            }

            //initialize partPermutations with all possible part Permutations
            partPermutations = new ArrayList<String>(0);

            // now that the object has been made, set the original design field before
            // the design array is changed
            setOriginalDesign(this.designString());
	}



    /**
     * Creates the actual design given the number of parts, n.  It also sets
     * the invertSim design array to this generated design.
     *
     * @param n - Number of parts
     */
    public void createDesign(int n)
    {
        // this will hold the entire design string
        String design = "";

        // create the variables we will need
        ArrayList<String> constructPrefix = new ArrayList<String>(0);
        ArrayList<String> partPrefix = new ArrayList<String>(n);
        ArrayList<String> partSuffix = new ArrayList<String>(n);
        ArrayList<String> parts = new ArrayList<String>(0);

        // initialize these array lists
        for (int i=0; i<n; i++)
        {
            partPrefix.add("");
            partSuffix.add("");
        }

        // create the construct prefix
        for (int i=1; i<((4*n*n)-(6*n)); i+=2)
        {
            constructPrefix.add("I"+i+" ");
        }

        int currentInvNum = 1;
        String invsToAdd = "";
        // create each part's suffix
        for (int i=0; i<(2*n-3); i++)
        {
            for (int j=0; j<n; j++)
            {
                invsToAdd = "I"+(currentInvNum++)+"' I"+(currentInvNum++)+"' ";
                partSuffix.set(j, partSuffix.get(j).concat(invsToAdd));
            }
        }

        // create each part's prefix
        currentInvNum--;
        for (int i=0; i<(2*n-3); i++)
        {
            for (int j=n; j>=1; j--)
            {
                partPrefix.set(j-1, partPrefix.get(j-1).concat("I"+currentInvNum+" "));
                currentInvNum = currentInvNum-2;
            }
        }

        // create the parts
        for (int i=1; i<=n; i++)
        {
            parts.add("P"+i+" ");
        }

        // assemble the whole design
        // start with the construct prefix
        for (int i=0; i<constructPrefix.size(); i++)
        {
            design = design.concat(constructPrefix.get(i));
        }

        // reset current inv for combos
        currentInvNum = (4*n*n)-(6*n)+1;

        // now add each parts prefix, part, and suffix
        for (int i=0; i<n; i++)
        {
            design = design.concat(partPrefix.get(i));
            if (combos)
                design = design.concat("I"+(currentInvNum)+" ");
            design = design.concat(parts.get(i));
            if (combos)
                design = design.concat("I"+(currentInvNum++)+"' ");
            design = design.concat(partSuffix.get(i));
        }

        setDesignArray(design);
    }



    /**
     * Generates the necessary invertase key for obtaining the specified
     * permutation.  This key always describes how to get to the desired target
     * from the ORIGINAL DESIGN!
     *
     * @param target - target permutation (For example, "P1 P3 P2'")
     */
    public String generateKey(String target)
    {

        // string of invertase numbers that will be delivered to the user
        String invertaseKey = "";

        String[] targetStringPerm = target.split(" ");

        Integer[] targetPerm = new Integer[0];

        // check the targetPerm and set the integer representation of this
        // permutation
        try
        {
            targetPerm = this.getIntPartPerm(targetStringPerm);
        }
        catch(PermutationException e)
        {
            e.printStackTrace();
        }

        // create the sorted configuration
        Integer[] sortedConfig = targetPerm.clone();
        Arrays.sort(sortedConfig);

        // record the design of the object
        String oldDesign = this.designString();
        this.setDesignArray(this.getOriginalDesign());

        int targetValue = 0;
        int currentValue = 0;
        int index = -1;
        int tempInvertase = -1;
        int n = this.getNumberOfParts();
        boolean usedLayer = false;

        // there are 2*n-3 possible flips
        int layer = 2*n-3;
        
        // there are n-1 sets, start at the last place, since this will be the
        // target index
        for (int i=n-1; i>0; i--)
        {
            usedLayer = false;

            targetValue = targetPerm[i];
            currentValue = currentPerm[i];

            if (currentValue!=targetValue)
            {
                // if the target value is not at the beginning, bring it there
                if (targetValue!=currentPerm[0])
                {
                    // flip the target value to the front
                    usedLayer = true;

                    // first check if the value's bit map is flipped
                    if (getBitMapValue(targetValue-1)==1)
                    {
                        // if it has been flipped, then flip it's construct\
                        tempInvertase = (layer*n*2)-(2*(n-targetValue));

                        // add this to the invertase key
                        invertaseKey = invertaseKey.concat(tempInvertase+" ");

                        // make the flip
                        if (this.isPossible(Integer.toString(tempInvertase)))
                            this.invert();
                    }

                    // now that the part is oriented correctly, flip it to the front

                    // first calculate the invertase for doing so
                    tempInvertase = (layer*n*2)-(2*(n-targetValue))-1;

                    // add this to the key
                    invertaseKey = invertaseKey.concat(tempInvertase+" ");

                    // make the flip
                    if (this.isPossible(Integer.toString(tempInvertase)))
                        this.invert();
                }

                // now that the part is at the beginning of the current perm
                if (usedLayer)
                    // substract a layer
                    layer = layer -1;

                // find the value at the i index , since this is the value we will
                // use to flip, store this in currentValue
                currentValue = currentPerm[i];

                // calculate the invertase for this flip

                // first check if the part is flipped again
                if (getBitMapValue(currentValue-1)==1)
                {
                    // if it has been flipped, then flip it's construct
                        tempInvertase = (layer*n*2)-(2*(n-currentValue));

                        // add this to the invertase key
                        invertaseKey = invertaseKey.concat(tempInvertase+" ");

                        // make the flip
                        if (this.isPossible(Integer.toString(tempInvertase)))
                            this.invert();
                }

                // now that the part is oriented correctly, flip it to the i position

                    // first calculate the invertase for doing so
                    tempInvertase = (layer*n*2)-(2*(n-currentValue))-1;

                    // add this to the key
                    invertaseKey = invertaseKey.concat(tempInvertase+" ");

                    // make the flip
                    if (this.isPossible(Integer.toString(tempInvertase)))
                        this.invert();
            }

            if (!usedLayer)
                layer = layer-2;
            else
                // subtract from the layer every again
                layer = layer -1;
        }

        // if the object discerns between inverted and non-inverted parts, then
        // make sure the parts are in the correct orientation
        if (combos)
        {
            // used for determining the index for flipping a part in the next loop
            int primedIndex = 0;

            //finally, add any extra inversions to invert parts only
            for (int i=0; i<n; i++)
            {
                // if the part should be primed
                if(targetStringPerm[i].endsWith("'"))
                {
                    // get the index
                    primedIndex = Integer.parseInt(targetStringPerm[i].substring(1,targetStringPerm[i].length()-1))-1;

                    // but the part is not primed
                    if (this.getBitMapValue(primedIndex)==0)
                    {
                        // add invertase to flip the part
                        tempInvertase = ((4*n*n)-(6*n))+(primedIndex+1);
                        invertaseKey = invertaseKey.concat(Integer.toString(tempInvertase)+" ");
                    }

                }
                // if the part should not be primed
                else
                {
                    // again get the part index
                    primedIndex = Integer.parseInt(targetStringPerm[i].substring(1,targetStringPerm[i].length()))-1;

                    // check if the part is primed yet
                    if (this.getBitMapValue(primedIndex)==1)
                    {
                        // add invertase to flip the part
                        tempInvertase = ((4*n*n)-(6*n))+(primedIndex+1);
                        invertaseKey = invertaseKey.concat(Integer.toString(tempInvertase)+" ");
                    }
                }
            }
        }

        // reset the original design
        this.setDesignArray(oldDesign);

        // return the final key
        return invertaseKey;
    }



    /**
     * Returns the algorithm used to create the design array for this InvertSim.
     */
    public String getAlgorithm()
    {
        return "Pancake";
    }



    /**
     * Creates a "fresh" copy of this InvertSim object.  The returned Pancake
     * object has the original design as it's current design array.
     *
     * @return
     */
    public Pancake cloneFresh()
    {
        return new Pancake(this.getNumberOfParts(),this.isCombos());
    }
}
