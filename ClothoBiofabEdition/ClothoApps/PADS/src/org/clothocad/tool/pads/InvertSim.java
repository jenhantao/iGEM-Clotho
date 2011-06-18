/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.pads;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Craig LaBoda
 *
 */
public class InvertSim {



    public InvertSim(String design, String input)
        // constructor for simulation

    {
        // define the design array
        designArray = design.split(" ");

        // define the input array
        inputArray = input.split(" ");

        // define the parts array
        parts = new ArrayList<String>(0);
        for (int i=0;i<designArray.length;i++)
        {
            if (!(designArray[i].startsWith("I")))
                parts.add(designArray[i]);
        }

        // initialize the list for permuted inputs
        permutedParts = new ArrayList<String>(0);

        // initialize the object array for permutation information
        permutationCheck = new Object[0][0];

        // set the current sites
        currentSites = new int[2];
        currentSites[0] = -1;
        currentSites[1] = -1;

        // set isPossible
        isPossible = false;

    }


    
    public void invert()
        // makes the inversion given the sites and the proper input

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
                designArray[i] = designArray[i].substring(0,designArray[i].length()-1);

            // not already inverted, then invert the part
            else
                designArray[i]=designArray[i].concat("'");

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

    }


    
    public void smartInput()
    {
        
        // define the string used for output
        String allInputs = "";
        String check = "";

        // loop through the designArray
        for (int i=0; i<designArray.length;i++)
        {
            // check if the element is an invertase
            if ((designArray[i].startsWith("I"))) //&& !(designArray[i].endsWith("'")))
            {
                if (designArray[i].endsWith("'"))
                    check = designArray[i].substring(1,designArray[i].length()-1);
                    
                else
                    check = designArray[i].substring(1,designArray[i].length());
                
                // check if this input has been added before
                //if (allInputs.indexOf(designArray[i].charAt(1))==-1)
                if (allInputs.indexOf(check)==-1)
                // if it has not then add it
                    allInputs = allInputs.concat(check+" ");
            }
        }
        // set the input string using all inputs
        setInputArray(allInputs);
    }



    public void testDesign(boolean autoInput)
    // tests every input permutation of a particular design
    // results are stored in permutationCheck

    {

        permuteParts();
        if (autoInput)
            smartInput();
        permuteInput();
        
    }



    public void permuteInput()
    // permutes the input array
    // does not store every text input vector

    {
        // first convert input string array to list
        ArrayList<String> inputList = new ArrayList();
        inputList.addAll(Arrays.asList(inputArray));

        // then call the permutation function
        String next = "";
        generatePerms(inputList,next,true);
    }



   
    public void permuteParts()
    // calls the permutation function for parts
    // also creates permutation check array

    {
        String next = "" ;

        // permute the parts
        generatePerms(parts,next,false);

        // retrieve the number of parts for each permutation
        int n = permutedParts.size();

        // use this to set the size of the permutation check array
        permutationCheck = new Object[n][4];
        for (int i=0;i<n;i++)
        {
            // copy permuted parts into first slot of permutationCheck
            permutationCheck[i][0] = permutedParts.get(i);

            // set all boolean to false since they haven't been hit yet
            // except for the first permutation which must exist
            if (i==0)
            {
                permutationCheck[i][1] = "Starting Configuration";
                permutationCheck[i][2] = "No Inputs Necessary";
                permutationCheck[i][3] = 0;
            }
            else
            {
               permutationCheck[i][1] = "";
               permutationCheck[i][3] = 0;
            }
        }
    }



    public void generatePerms(ArrayList<String> inputArray, String next, boolean comb)
    // computes the actual permutations

    {
        // check if we are including combinations
        // only true for permuting the inputs
        if (comb)
        {
            if(next!="")
                testInputPerm(next);
        }
        // base case-
        if (inputArray.isEmpty())
        {
            // for parts
            if(!comb)
                permutedParts.add(next);

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
               generatePerms(inputArray,copy, comb);

               // add the element back
               inputArray.add(i, temp);
            }
        }
    }



    public void testInputPerm(String currentInput)
    // tests a single input string by setting it to the current input array

    {
        // keep original design
        String originalDesign = designString();

        // set the current input
        setInputArray(currentInput);

        // leave loop if one invertase can not be used
            int j = 0;

            while((j<inputArray.length) && (isPossible(inputArray[j])))
            {
                    invert();
                    j++;
            }

            // update permuation information
            containPerm();

            // reset origianl design
            setDesignArray(originalDesign);

    }



    public void containPerm()
    // takes current design and updates check information

    {
        // first create current permutation
        String partPerm = getPartPerm();

        // loop through the part permutations
        for (int i=1; i<permutedParts.size();i++)
        {
            
            // find the correct permutation entry
            if (permutationCheck[i][0].equals(partPerm))
            {


                 // if the current length is shower than the already stored length
                 if((inputArray.length < (Integer) permutationCheck[i][3]) || ((Integer) permutationCheck[i][3]==0))
                 {
                     // put in actual configuration
                     permutationCheck[i][1] = partsString();

                     // update the input
                     permutationCheck[i][2]=inputString();

                     // put down an entry for the input array length
                     permutationCheck[i][3]=inputArray.length;
                }
            }
        }
    }


    
    public String getPartPerm()
    // determines the current configuration of parts without "'"

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



    public String designString()
    // prints the designArray as a string with spaces in between each part

    {
        String design = "";

        for (int i = 0; i< designArray.length;i++)
        {
            design = design.concat(designArray[i].toString()+" ");
        }

        return design;
    }


    
    public String inputString()
    // prints the inputArray as a string with spaces in between each input

    {
        String input = "";

        for (int i=0; i<inputArray.length;i++)
        {
            input = input.concat(inputArray[i]);
        }

        return input;
    }


    
    public String partsString()
    // prints the parts as a string, with complements included
    // puts a space in between each part

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



    public void setInputArray(String input)
    // takes a string input, with each input separated by spaces
    // splits this up and set it to the current input array

    {
        inputArray = input.split(" ");
    }



    public void setDesignArray(String design)
    // takes a string input, with each part separated by spaces
    // splits this string and sets it to the current design array

    {
        designArray = design.split(" ");
    }



      public boolean isPossible(String invertase)
      // checks whether it is possible to make the inversion
      // given a particular input invertase, or one of the input array elements

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
        else
            isPossible = false;

        // return whether the switch can be made
        return isPossible;
    }



    public boolean isCompleteSet()
    // loops through the permutation check object array and returns
    // whether or not every permutation has been hit

    {
        // complete will be used to determine whether every permutation 
        // has been hit
        boolean complete = true;

        // check is a temporary string used to store the object pulled from
        // the permutation check array
        String check = "";

        // run a loop to check whether all the permutations were hit
        int i = 0;

        do
        {
            // set the temporary check equal to the object from the array
            check = (String) permutationCheck[i][1];

            // check whether this string has been set
            if (check.equals(""))
                // if it has not then leave the loop and return false
                complete = false;

            i++;
        }
        // continue looping until either a permutation has not been hit
        // or the entire set has been checked
        while(complete && (i < permutedParts.size()));

        // return our answer
        return complete;

    }

    public int getRemaining()
    {
        // set starting number of permutation
        int remaining = permutedParts.size();

        // loop through the permutation information and count how many
        // permutations were not hit

        for (int i =0; i<permutedParts.size(); i++)
        {
            // if the permutation has been hit, then decrement remaining counter
            if (!(permutationCheck[i][1].equals("")))
                remaining--;
        }

        return remaining;
    }
    

    // VARIABLES
    public int[]    currentSites;                   // keeps track of current sites
    public boolean  isPossible;                     // based on current sites determines if possible
    public String[] inputArray;                     // input array
    public String[] designArray;                    // stores entire design
    public ArrayList<String> parts;                 // stores only parts
    public ArrayList<String> permutedParts;         // stores permutations of parts
    public Object[][] permutationCheck;           // stores info about the success of the design
        // the first is the permutation - String
        // the second is actual configuration with the complement - String
        // the third stores the input sequence - String
        // the fourth stores the length of the shortest path
}
