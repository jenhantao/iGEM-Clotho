/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moo;

/**
 *
 * @author Henry
 */
public class Main {

    public static void split(String s) {
//        String[] tokens = s.split("[\\s]+");
        String[] tokens = s.split("[\\s[\\p{Punct}]]+");
        System.out.println(s);
        for (int i = 0; i < tokens.length; i++) {
            System.out.println(tokens[i]);//first is name, second is start offset, last is end offset
        }
        System.out.println("");
    }

    public static void split2(String s) {
//        String[] tokens = s.split("[\\s]+");
        String[] tokens = s.split("[\\p{Punct}]+");
        System.out.println(s);
        for (int i = 0; i < tokens.length; i++) {
            System.out.println(tokens[i]);//first is name, second is start offset, last is end offset
        }
        System.out.println("");
    }

    public static void detectLocation(String s) {
        if (s.substring(s.length()-1).matches("\\d")) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

    public static void main(String[] args) {
        detectLocation("source          1..247");
        split2("/gene=\"thrL\"");
    }
}
