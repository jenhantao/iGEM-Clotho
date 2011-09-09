/*
 * This class extends the InvertSim class.  It is a special type of InvertSim
 * which uses the "LinkSort" Algorithm to place invertases in particular positions
 * between parts, such that every permutation of parts has at least one invertase
 * key.
 *
 */


package org.clothocad.tool.trumpet;

import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author Craig LaBoda
 *
 */

public class LinkSort extends InvertSim {

    /**
     * Creates a LinkSort invertase design given the number of parts in the
     * design.
     *
     * @param n - The number of parts
     * @param comb - Whether the object discerns between inverted and non-ineverted parts
     */
    public LinkSort(int n, boolean comb)
    {
        // initialize the bitMapArray for this object
        flipBitMap = new Integer[n];
        for (int i=0; i<n; i++)
            flipBitMap[i] = 0;

        // combos represents whether this object discerns between inverted parts
        // and non-inverted parts
        combos = comb;
        
        // create integer currentPer which keeps track of the permutation of parts
        // by their integer value
        currentPerm = new Integer[n];

        // create the LinkSort design
        createDesign(n);

        // define the parts array
        for (int i=0;i<designArray.length;i++)
        {
            if (!(designArray[i].startsWith("I")))
                this.addPart(designArray[i]);
        }

        //initialize partPermutations with all possible part Permutations
        partPermutations = new ArrayList<String>(0);

        // now that the design array has been created, retain this original design
        // before the array is altered
        setOriginalDesign(this.designString());

    }



    /**
     * Creates the actual design given the number of parts, n.  It also sets
     * the invertSim design array to this generated design.
     * 
     * @param n - Number of parts
     */
    private void createDesign(int n)
    {
    // =========================== VARIABLES ===================================

            // string that will be returned to the user
            String linkSortDesign = "";

            //  stores each part's prefix for each layer
            ArrayList<String> layerPrefix = new ArrayList<String>(n);
            for (int i=0; i<n; i++)
                layerPrefix.add("");

            //  stores each part's prefix for each layer
            ArrayList<String> partPrefix = new ArrayList<String>(n);
            for (int i=0; i<n; i++)
                partPrefix.add("");

            ArrayList<String> layerSuffix = new ArrayList<String>(n);
            for (int i=0; i<n; i++)
                layerSuffix.add("");

            ArrayList<String> partSuffix = new ArrayList<String>(n);
            for (int i=0; i<n; i++)
                partSuffix.add("");

            // currentInv keeps track of the current invertase being added
            int currentInv = 1;

            // linkInv is used to place the linked invertases around each part
            int linkInv = 0;

    // ============================ ALGORITHM ==================================

            // initialize linksort to the prefix for link sort
            for (int i=0; i<n-1; i++)
                // make sure to increment the current invertase number
                linkSortDesign = linkSortDesign.concat("I"+(currentInv++)+" ");

            // loop through each layer, there are n-2 layers
            for (int layer=1; layer<=(n-2); layer++)
            {
                // loop through each part and create each part's layer
                for (int i = 0; i<n; i++)
                {
                    // make a copy of the current Inv used for add the link invertases
                    linkInv = currentInv;

                    // for each part, each layer begins with (n-1) invertases
                    for (int j=1; j<= (n-1); j++)
                        // stores the 4(n-1) invertases that begin each part's string
                        layerPrefix.set(i,layerPrefix.get(i).concat("I"+(currentInv++)+" "));

                    // loop through the parts and create their link Prefxes
                    for (int j=0; j<n; j++)
                    {
                        if (!(j==i))
                            layerSuffix.set(j,(layerSuffix.get(j).concat("I"+(linkInv++)+"' ")));
                    }

                    // clear linkInv counter for next layer
                    linkInv = 0;
                }

                // before moving on to the next layer assemble the current layer's
                // prefix by appending the layerprefix for each part with that
                // part's linkPrefix
                for (int i=0; i<n; i++)
                {
                    // add the orienting invertase for the prefix
                    layerPrefix.set(i,"I"+(currentInv)+" ".concat(layerPrefix.get(i)));

                    // add it's complement to the end of the suffix
                    layerSuffix.set(i,layerSuffix.get(i).concat("I"+(currentInv++)+"' "));

                    // now append the temporary layer suffix and prefix to the part prefix and suffix
                    partPrefix.set(i,partPrefix.get(i).concat(layerPrefix.get(i)));
                    partSuffix.set(i,layerSuffix.get(i).concat(partSuffix.get(i)));

                    // clear the layerPrefix and the layerSuffix
                    layerPrefix.set(i,"");
                    layerSuffix.set(i,"");
                }
            }

            // finally compose the complete design
            // loop through each part
            for (int i=0; i<n; i++)
            {
                // add the prefix, part name, suffix and, first layer switch
                linkSortDesign = linkSortDesign.concat(partPrefix.get(i));
                if (combos)
                    linkSortDesign = linkSortDesign.concat("I"+(currentInv)+" ");
                linkSortDesign = linkSortDesign.concat("P"+(i+1)+" ");
                if (combos)
                    linkSortDesign = linkSortDesign.concat("I"+(currentInv++)+"' ");
                linkSortDesign = linkSortDesign.concat(partSuffix.get(i));
                if (!(i==0))
                    linkSortDesign = linkSortDesign.concat("I"+(n-i)+"' ");
            }

            // the design array to this this design
            setDesignArray(linkSortDesign);
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

        // n represents the number of elements in the configuration
        int n = targetPerm.length;

        // current value will be the value of the element stored in the current
        // index of the flip being made
        int currentValue = 0;

        // target value will be the value of the element that current value will
        // be linked to
        int targetValue = 0;

        // invertase will be the value of the invertase that gets appended to
        // the invertase key
        int invertase = 0;

        int actualLayer=0;

        // there are n-1 switches that should be made
        // this loop iterates through those switches
        for (int i=0; i<n-1; i++)
        {
            // find the current value
            currentValue = currentPerm[i];

            // find the target value
            targetValue = targetPerm[i];

            // first check if the current value is the correct place
            if (currentValue!=targetValue)
            {
                // the first switch is very simple
                if (i==0)
                {
                    // calculate the invertase
                    invertase = (n+1)-targetValue;

                    // append this to the invertase key
                    invertaseKey = invertaseKey.concat(invertase+" ");

                    // make the switch
                    if (this.isPossible(Integer.toString(invertase)))
                        this.invert();

                    // increment actual layer since a switch needs to be made
                    actualLayer++;
                }

                // if it is not the first switch, then the invertase calculations
                // are different
                else
                {
                    // does the current value's part construct need to be flipped
                    if (getBitMapValue(currentValue-1)==1)
                    {
                        // calculate the invertase necessary to flip this
//                        invertase = ((n-1)+n*(n-1)*(i)+(i-1)*n)+currentValue;
                        invertase = ((n-1)+n*(n-1)*(actualLayer)+(actualLayer-1)*n)+currentValue;

                        // appennd this to the invertase key
                        invertaseKey = invertaseKey.concat(invertase+" ");

                        // make the flip
                        if (this.isPossible(Integer.toString(invertase)))
                        this.invert();
                    }

                    // does the target value's part construct need to be flipped
                    if (getBitMapValue(targetValue-1)==1)
                    {
                        // calculate the invertase necessary to flip this
//                        invertase = ((n-1)+n*(n-1)*(i)+(i-1)*n)+targetValue;
                        invertase = ((n-1)+n*(n-1)*(actualLayer)+(actualLayer-1)*n)+targetValue;
                        
                        // appennd this to the invertase key
                        invertaseKey = invertaseKey.concat(invertase+" ");

                        // make the flip
                        if (this.isPossible(Integer.toString(invertase)))
                        this.invert();        
                    }

                    // calculate the swap invertase
//                    invertase = (n+(i-1)*(n*(n-1)+n)-1)+(currentValue-1)*(n-1);
                    invertase = (n+(actualLayer-1)*(n*(n-1)+n)-1)+(currentValue-1)*(n-1);

                    // counter keeps track of the invertase value that will be
                    // added to the final swap invertase
                    int counter = 1;

                    // tempInvertase keeps track of which invertase we check
                    // in the following loop
                    int tempInvertase = 1;

                    // loop through the invertase values and determine what
                    // linking invertase should be added to the key
                    while(tempInvertase!=targetValue)
                    {
                        if (!(tempInvertase==currentValue))
                           counter++;

                        tempInvertase++;                            
                    }

                    // add the counter to finalize the invertase
                    invertase = invertase+counter;

                    // append this to the invertase key
                    invertaseKey = invertaseKey.concat(invertase+" ");

                    // make the switch
                    if (this.isPossible(Integer.toString(invertase)))
                        this.invert();

                    // increment actual layer since a switch needs to be made
                    actualLayer++;
                }
            }
            if(actualLayer==0)
                actualLayer++;
        }

        // if the object discerns between inverted and non-inverted parts, then
        // make sure that all of the parts are in the correct orientation
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
                        invertase = (n-2)*(n-1)*n+n-1+n*(n-2)+(primedIndex+1);
                        invertaseKey = invertaseKey.concat(Integer.toString(invertase)+" ");
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
                        invertase = (n-2)*(n-1)*n+n-1+n*(n-2)+(primedIndex+1);
                        invertaseKey = invertaseKey.concat(Integer.toString(invertase)+" ");
                    }
                }
            }
        }

        // reset the original design
        this.setDesignArray(oldDesign);

        // return the finished invertase key
        return invertaseKey;
    }



    /**
     * Returns the algorithm used to create the design array for this InvertSim.
     */
    public String getAlgorithm()
    {
        return "LinkSort";
    }



    /**
     * Creates a "fresh" copy of this InvertSim object.  The returned LinkSort
     * object has the original design as it's current design array.
     *
     * @return
     */
    public LinkSort cloneFresh()
    {
        return new LinkSort(this.getNumberOfParts(),this.isCombos());
    }
}
