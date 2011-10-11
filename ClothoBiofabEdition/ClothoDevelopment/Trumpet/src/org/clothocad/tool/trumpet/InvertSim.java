package org.clothocad.tool.trumpet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Craig LaBoda
 *
 */
public abstract class InvertSim {


    public InvertSim()
    {
        // define the parts array
        parts = new ArrayList<String>(0);

        // set the current sites
        currentSites = new int[2];
        currentSites[0] = -1;
        currentSites[1] = -1;

        // set isPossible
        isPossible = false;

        isPermsGenerated = false;
    }



    /**
     * Checks whether it is possible to make an inversion, given the input
     * invertase string.  If it is possible, this method sets the currentSites
     * values necessary for making the inversion using the invert() method.
     * If it is impossible, this method returns false.
     */
    public boolean isPossible(String invertase)
      {
        // defines the strings we search for
        String openInv = "I"+invertase;
        String closeInv = "I"+invertase+"'";

        for (int i=0;i<designArray.length;i++){
        // go through design String array and search for the invertase

            // if the design contains either string then store that info
            if (designArray[i].equals(openInv))
               currentSites[0] = i;

           if (designArray[i].equals(closeInv))
               currentSites[1] = i;
        }

        // if either of the sites are still -1 then the switch cannot be mad
        if ((currentSites[0]!=-1) && (currentSites[1]!=-1))
            isPossible = true;

        // if not, return false and reset the current sites
        else
        {
            isPossible = false;
            currentSites[0]=-1;
            currentSites[1]=-1;
        }

        // return whether the switch can be made
        return isPossible;
    }



    /**
     * Once the current sites are set using the isPossible method, call this
     * method to make the actual inversion using those sites
     */
    public void invert()
    {
        // distinguish between the two cases (FORWARD AND REVERSE)
        if (currentSites[1]<currentSites[0]){
            int temp = currentSites[0];
            currentSites[0] = currentSites[1];
            currentSites[1] = temp;
        }

        // complement parts
        for(int i=currentSites[0]; i <= currentSites[1]; i++){

            // check for inversion of part
            if (designArray[i].endsWith("'"))
            {
                // if a part is encountered, flip it's bit map value
                if (designArray[i].startsWith("P"))
                {
                    int index = Integer.parseInt(designArray[i].substring(1,designArray[i].length()-1))-1;
                    setBitMap(index);
                }
                designArray[i] = designArray[i].substring(0,designArray[i].length()-1);
            }

            // not already inverted, then invert the part
            else
            {
                // if a part is encountered, flip it's bit map value
                if (designArray[i].startsWith("P"))
                {
                    int index = Integer.parseInt(designArray[i].substring(1,designArray[i].length()))-1;
                    setBitMap(index);
                }
                designArray[i]=designArray[i].concat("'");
            }
         }

         // temporary array used for reversing our origianl array
         String[] temp = new String[currentSites[1]-currentSites[0]+1];

         // invert parts
         for (int i=0; i <= currentSites[1]-currentSites[0]; i++)
            temp[i] = designArray[currentSites[1]-i];

          // now replace the original with temp
          for (int i = 0; i<= currentSites[1]-currentSites[0];i++)
              designArray[currentSites[0]+i] = temp[i];

         // now reset the current sites
         currentSites[0] = -1;
         currentSites[1] = -1;

         // reset isPossible
         isPossible = false;

         // now that the flip has been made, set the current part permutation
         // field for the object
         setCurrentPerm();
    }



    /**
     * Returns the table of invertase keys for the permutations specified.  It
     * also shortens the design and keeps only the necessary invertases.  The
     * invertases which are kept are renumbered accordingly.
     *
     * @param permutations - a list of permutations which should be achieved
     * @return
     */
    public String[][] tailorDesign(ArrayList<String> permutations)
    {
        String[][] tailoredKeyTable = this.generateTailoredKeyTable(permutations);

//        String[] key = new String[0];
//
//        ArrayList<String> uniqueInvertases = new ArrayList<String>(0);
//
//        // loop through the tailored key table's second column and get all unique invertases
//        for (int i=1; i<tailoredKeyTable.length; i++)
//        {
//            key = tailoredKeyTable[i][1].split(" ");
//
//            for (int j=0; j<key.length; j++)
//            {
//                if (!(uniqueInvertases.contains(key[j])))
//                    uniqueInvertases.add(key[j]);
//            }
//        }

        // now we know which invertases we need to keep, we can delete all the other invertases
        this.removeInvertases(tailoredKeyTable/*uniqueInvertases*/);

        return tailoredKeyTable;
    }



    /**
     * Removes all invertases from the design except for the invertases provided
     * in the second column of the table.
     *
     * @param tailoredKeyTabel - Table of permutations and invertase keys
     */
    public void removeInvertases(String[][] tailoredKeyTable/*ArrayList<String> uniqueInvertases*/)
    {

        String[] key = new String[0];

        ArrayList<String> uniqueInvertases = new ArrayList<String>(0);

        // loop through the tailored key table's second column and get all unique invertases
        for (int i=1; i<tailoredKeyTable.length; i++)
        {
            key = tailoredKeyTable[i][1].split(" ");

            for (int j=0; j<key.length; j++)
            {
                if (!(uniqueInvertases.contains(key[j])))
                    uniqueInvertases.add(key[j]);
            }
        }

        // get the original design
        String[] originalDesignArray = this.getDesignArray();/*this.originalDesign.split(" ");*/

        // copy the original into an array list
        ArrayList<String> shortenedDesign = new ArrayList<String>(0);
        shortenedDesign.addAll(Arrays.asList(originalDesignArray));

        // string used to store each component in the design as we check the comp.
        String tempComponent = "";
        // string which keeps track of the unique invertase we are checking for
        String tempInv = "";
        // determines whether the element should be deleted
        boolean deleteInv = true;

        // loop through the invertases that we want to keep
        for (int i=0; i<shortenedDesign.size(); i++)
        {
            // get the design component
            tempComponent = shortenedDesign.get(i);

            // if an invertase is encountered
            if (tempComponent.startsWith("I"))
            {

                // loop through the unique invertases and test the temp component
                // against each one
                for (int j=0; j<uniqueInvertases.size(); j++)
                {
                    // create the invertase string we want to check for
                    tempInv = uniqueInvertases.get(j);

                    // don't delete the element if it belons to the set we are keeping
                    if (tempComponent.equals("I"+tempInv))
                        deleteInv = false;
                    if (tempComponent.equals("I"+tempInv+"'"))
                        deleteInv = false;
                }

                // if the element should be deleted, set that element to null
                if (deleteInv)
                {
                    shortenedDesign.set(i, null);
                }

                // reset the boolean for the next element
                deleteInv = true;
            }
        }

        // fixedDesign will be delivered to the user
        String fixedDesign = "";

        // eliminate elements which were set to null
        for (int i=0; i<shortenedDesign.size(); i++)
        {
            if (!(shortenedDesign.get(i) == null))
                fixedDesign = fixedDesign.concat(shortenedDesign.get(i)+" ");
        }

        this.setDesignArray(fixedDesign);
    }



    /**
     * Returns a key table which includes the listings for only the permutations
     * specified.
     *
     * @param permutations
     * @return
     */
    public String[][] generateTailoredKeyTable(ArrayList<String> permutations)
    {
        // the +1 accounts for the starting permutation
        int length = permutations.size()+1;
        String[][] tailoredKeyTable = new String[length][2];

        tailoredKeyTable[0][0] = this.partsString();
        tailoredKeyTable[0][1] = "Starting Construct";

        for (int i=1; i<length; i++)
        {
            tailoredKeyTable[i][0] = permutations.get(i-1);
            tailoredKeyTable[i][1] = this.generateKey(permutations.get(i-1));
        }
        String c = this.designString();
        if (c.equals(this.getOriginalDesign()))
            return tailoredKeyTable;
        else
        {
            // remove the beginning piece of each key
            // first get the construct key
            String constructKeyString = this.generateKey(this.partsString());
            String[] constructKey = constructKeyString.split(" ");
            for (int i=1; i<length; i++)
            {
                String[] key = tailoredKeyTable[i][1].split(" ");
                String editedKey = "";
                for (int j=0; j<constructKey.length; j++)
                    key[j] = null;
                for (int j=0; j<key.length; j++)
                {
                    if (key[j]!=null)
                        editedKey = editedKey.concat(key[j]+" ");
                }
                // finally replace the original with the edited
                tailoredKeyTable[i][1] = editedKey;
            }
            return tailoredKeyTable;
        }
    }



    /**
     * Generates entire table of keys.  Each entry has two columns.  The first
     * is the permutation of parts.  The second is the invertase key which leads
     * to that permutation.
     * @return
     */
    public String[][] generateKeyTable()
    {
        int length = partPermutations.size();
        String[][] keyTable = new String[length][2];

        for (int i=0; i<length; i++)
        {
            keyTable[i][0] = partPermutations.get(i);
            keyTable[i][1] = this.generateKey(partPermutations.get(i));
        }

        return keyTable;
    }



    /**
     * This bare bones permutation algorithm spits out all permutations of any
     * ArrayList of type string.  It is currently set to also call the generate
     * subPerms so as to include all permutations of parts.
     * @param inputArray
     * @param next
     */
    public void generatePartPermutations(ArrayList<String> inputArray, String next)
    {
        // base case
        if (inputArray.isEmpty())
        {
            // add the current permutation
            partPermutations.add(next);

            // if we want all of the extra combinations
            if (combos)
                generateSubPerms(next);

            // always return
            return;
        }

        // general case
        else
        {
            for (int i=0; i<inputArray.size(); i++)
            {
               // make a copy because strings are passed by reference
               String copy = next.concat(inputArray.get(i)+" ");

               // make another copy
               String temp = inputArray.get(i);

               // remove the element
               inputArray.remove(i);

               // recursively call the permutation function again
               generatePartPermutations(inputArray,copy);

               // add the element back
               inputArray.add(i, temp);
            }
        }
    }



    /**
     * Adds all sub-permutations of the current part permutation.  This distinguishes
     * between a a permutation with complements and a permutation without
     * complements.
     * @param next
     */
    private void generateSubPerms(String next)
    {

        // parse the string
        String[] currentPartPerm = next.split(" ");

        // use the length
        int n = currentPartPerm.length;

        String newPerm = "";

        Integer[] bitRep = new Integer[0];

        int sizeOfBitMap = (int) Math.pow(2,n);

        // take that permutation and find each sub-permutation
        for (int i=1; i<sizeOfBitMap; i++)
        {
                bitRep = intToBitRep(i,n);

                for (int j=0; j<n; j++)
                {
                        if (bitRep[j]==1)
                                newPerm = newPerm.concat(currentPartPerm[j]+"' ");
                        else
                                newPerm = newPerm.concat(currentPartPerm[j]+" ");
                }

                partPermutations.add(newPerm);
                newPerm = "";
        }
    }



    /**
     * Converts a decimal number to it's binary representations.  NumberOfBits
     * specifies how many bits long the return Integer[] representation will be.
     * @param decimalInt
     * @param numberOfBits
     * @return
     */
    private Integer[] intToBitRep(int decimalInt,int numberOfBits)
    {
            Integer[] bitRep = new Integer[numberOfBits];

            int bitMask = 1;

            int value = 0;

            for (int i=0; i<numberOfBits; i++)
            {
                    // output of the bitmask
                    value = (decimalInt&bitMask);

                    // if the value is not 0, then append a 1
                    if (value!=0)
                            bitRep[i]=1;
                    // if the value is a 0, then append a 0
                    else if (value==0)
                            bitRep[i]=0;

                    bitMask=bitMask*2;
            }
            return bitRep;
    }
    
    
    
    /**
     * Checks whether the integer representation of the part permutation
     * contains all of the correct elements, and each element only once.
     * @param permutation
     * @throws PermutationException
     */
    private void checkPermutation(Integer[] permutation) throws PermutationException
    {

        int n = permutation.length;

        ArrayList<Integer> check = new ArrayList<Integer>(0);

        // loop through each element of check
        for (int i=0; i<n; i++)
        {
            if (!(check.contains(permutation[i])))
                check.add(permutation[i]);
        }

        // check if the integer permutation contains duplicates
        if (check.size()!=n)
            throw new PermutationException("Permutation does not make sense!");

        // sort the array
        Integer[] sorted = new Integer[n];
        check.toArray(sorted);
        Arrays.sort(sorted);

        // check that elements are not repeated
        for (int i=0; i<n; i++)
        {
            if (sorted[i]!=(i+1))
                throw new PermutationException("Permutation does not make sense!");
        }
    }



    /**
     * Allows InvertSim extensions to access the parts list when creating a
     * design.
     * @param part
     */
    public void addPart(String part)
    {
        parts.add(part);
    }



    /**
     * Sets the current part permutation.  This should only be called from
     * invert().
     */
    public void setCurrentPerm()
    {
        String currentPartConfiguration = this.getPartPerm();

        String[] tempPartPerm = currentPartConfiguration.split(" ");

        String currentPart = "";

        for (int i=0; i<tempPartPerm.length; i++)
        {
            currentPart = tempPartPerm[i];
            currentPerm[i] = Integer.parseInt(currentPart.substring(1,currentPart.length()));
        }

    }



    /**
     * Sets the bit of index i.  If the bit is 0, then it is set to 1.  If the
     * bit is 1, then it is set to 0.
     * @param i Index of the bit map that will be flipped.
     */
    public void setBitMap(int i)
    {
        if (flipBitMap[i]==0)
            flipBitMap[i]=1;
        else
            flipBitMap[i]=0;
    }



    /**
    * Takes a string input, with each part separated by spaces
    * splits this string and sets it to the current design array
    */
    public void setDesignArray(String design)
    {
        designArray = design.split(" ");
        this.clearBitMap();
        this.setCurrentPerm();
    }



    /**
     * Allows extensions of InvertSim to specify the original design.
     */
    public void setOriginalDesign(String design)
    {
        originalDesign = design;
    }



    /**
     * Sets all values of the Flip Bit Map to 0.  Used at the end of the key
     * generator.
     */
    public void clearBitMap()
    {
        for (int i=0; i<flipBitMap.length; i++)
        {
            flipBitMap[i] = 0;
        }
    }



    /**
     * Returns a list of all invertases within the current design array.
     * @return
     */
    public ArrayList<String> getInvertases()
    {
        ArrayList<String> invertases = new ArrayList<String>(0);

        String inv = "";

        for (int i=0; i<designArray.length; i++)
        {
            if (designArray[i].startsWith("I"))
            {
                if (designArray[i].endsWith("'"))
                {
                    inv = designArray[i].substring(0,designArray[i].length()-1);
                }
                else
                    inv = designArray[i].substring(0,designArray[i].length());

                if (!(invertases.contains(inv)))
                    invertases.add(inv);
            }
        }
        return invertases;
    }



    /**
     * Returns an integer array of the current permutation of parts.  For InvertSim
     * objects, the parts always begin with P and end with a number.  The
     * returned permutation is the order of the parts based on these numbers.
     */
    public Integer[] getCurrentPerm()
    {
        // used in the following loop to create a substring of each part
        String currentPart = "";

        int n= this.getNumberOfParts();

        // loop through the parts and determine the current configuration
        for (int i=0; i<n; i++)
        {
            // for clarity, first get current part
            currentPart = this.getPart(i);

            // then only get the substring (get rid of the "P" at the beginning
            // and the " " at the end of each par
            currentPerm[i] = Integer.parseInt(currentPart.substring(1,currentPart.length()));
        }

        return currentPerm;
    }



    /**
     * Converts the string representation of the part permutation into an integer
     * representation.
     */
    public Integer[] getIntPartPerm(String[] permutationArray) throws PermutationException
    {
        int n = permutationArray.length;
        Integer[] intPerm = new Integer[n];

            for (int i=0; i<n; i++)
            {
                // all parts for must begin with P
                if (!(permutationArray[i].startsWith("P")))
                    throw new PermutationException("Each part needs to begin with \"P\"!");

                // assemble the integer array
                if (permutationArray[i].endsWith("'"))
                    intPerm[i]=Integer.parseInt(permutationArray[i].substring(1,permutationArray[i].length()-1));
                else
                    intPerm[i]=Integer.parseInt(permutationArray[i].substring(1,permutationArray[i].length()));
            }

        // check the integer array
        this.checkPermutation(intPerm);

        return intPerm;
    }



    /**
     * Determines the current configuration of parts and returns this as a
     * string.  This method disregards the complements of parts (i.e. "'").
     */
    public String getPartPerm()
    {
        // define the string used for output
        String currentConfig = "";

        // loop through the designArray
        for (int i=0; i<designArray.length;i++)
        {
            // check if the element is a part or it is an invertase
            if (!(designArray[i].startsWith("I")))
            {
                // if it is a part, check whether it is complement
                if (designArray[i].endsWith("'"))
                    currentConfig = currentConfig.concat(designArray[i].substring(0,designArray[i].length()-1)+" ");
                else
                    currentConfig = currentConfig.concat(designArray[i]+" ");
            }
        }
        // return the string
        return currentConfig;
    }



    /**
     * Allows extensions of InvertSim to get the original design.
     */
    public String getOriginalDesign()
    {
        return originalDesign;
    }



    /**
     *
     * Returns the bit map value of the supplied index.  If this method returns
     * a 1, then the part construct is currently flipped.  If it returns a 0,
     * the part construct has not been flipped.
     *
     */
    public int getBitMapValue(int index)
    {
        return flipBitMap[index];
    }



    /**
     * Returns the current flipBitMap which indicates which elements in the
     * design are currently flipped.
     *
     */
    public Integer[] getBitMap()
    {
        return flipBitMap;
    }



    /**
     * Allows other algorithms to quickly determine the number of parts.
     * @return
     */
    public int getNumberOfParts()
    {
        return parts.size();
    }



    /**
     * Allows other algorithms to access the design array.
     * @return
     */
    public String[] getDesignArray()
    {
        return this.designArray;
    }



    /**
     * Allows other algorithms to access the entire parts array.
     * @return
     */
    public ArrayList<String> getParts()
    {
        return parts;
    }



    /**
     * Enables access to a particular part in the array of parts.
     *
     */
    public String getPart(int index)
    {
        return parts.get(index);
    }



    /**
     * Returns all part permutations for this InvertSim object.
     * @return
     */
    public ArrayList<String> getAllPartPermutations()
    {
        if (!this.isPermsGenerated)
        {
            this.generatePartPermutations(this.getParts(), "");
            this.isPermsGenerated = false;
        }
        return this.partPermutations;
    }



    /**
     * Returns the current design array as a string.  This includes invertases
     * and parts.
     */
    public String designString()
    {
        String design = "";

        for (int i = 0; i< designArray.length;i++)
        {
            design = design.concat(designArray[i].toString()+" ");
        }

        return design;
    }



    /**
     * Prints the current part configuration as a string.  This includes the
     * complements (i.e. "'").
     */
    public String partsString()
    {
         // define the string used for output
        String currentConfig = "";

        // loop through the designArray
        for (int i=0; i<designArray.length;i++)
        {
            // check if the element is a part or it is an invertase
            if (!(designArray[i].startsWith("I")))
            {
                currentConfig = currentConfig.concat(designArray[i]+" ");
            }
        }
        // return the string
        return currentConfig;
    }



    /**
     * Returns true if this InvertSim object distinguishes between inverted parts
     * and non-inverted parts.  Otherwise, this method returns false.
     * @return
     */
    public boolean isCombos()
    {
        if (combos)
            return true;
        else
            return false;
    }




//\\//\\//\\//\\//\\//\\//\\/  ABSTRACT METHODS  \//\\//\\//\\//\\//\\//\\//\\//

    public abstract String generateKey(String target);
    public abstract String getAlgorithm();
    public abstract InvertSim cloneFresh();
           

//\\//\\//\\//\\//\\//\\//\\//\\//  FIELDS  \\//\\//\\//\\//\\//\\//\\//\\//\\//

    public int[]    currentSites;                   
        // keeps track of the sites being used to make an inversion
    public boolean  isPossible;
        // based on the current sites, evaluates whether an inversion is possible
    public String[] designArray;
        // stores entire design
    private ArrayList<String> parts;
        // stores only parts which must be represented as P
    public Integer[] flipBitMap;
        // bit map representation of which parts are currently flipped
    public Integer[] currentPerm;
        // retains the current permutation of parts by their integer representation
    public ArrayList<String> partPermutations;
        // array list containing all part permutations
    public boolean combos;
        // specifies whether this InvertSim object distinguishes between inverted and non-inverted parts
    private String originalDesign;
        // the original design of any algorithm before it is tailored
        // stored for key generation purposes
    private boolean isPermsGenerated;
        // all part permutations are not generated in the constructor because this can be time consuming
        // this field indicates whether they have been calculated yet
}
