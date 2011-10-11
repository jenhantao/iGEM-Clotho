package org.clothocad.tool.trumpet;


/*
Java Swing, 2nd Edition
By Marc Loy, Robert Eckstein, Dave Wood, James Elliott, Brian Cole
ISBN: 0-596-00408-7
Publisher: O'Reilly
*/
// ColorPane.java
//A simple extension of JTextPane that allows the user to easily append
//colored text to the document.
//

// modified by Craig LaBoda for Trumpet

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ColorPane extends JTextPane {

//  public void appendNaive(Color c, String s) { // naive implementation
//    // bad: instiantiates a new AttributeSet object on each call
//    SimpleAttributeSet aset = new SimpleAttributeSet();
//    StyleConstants.setForeground(aset, c);
//
//    int len = getText().length();
//    setCaretPosition(len); // place caret at the end (with no selection)
//    setCharacterAttributes(aset, false);
//    replaceSelection(s); // there is no selection, so inserts at caret
//  }



//  public void append(Color c, String s) { // better implementation--uses
//                      // StyleContext
//
//    setEditable(true);
//
//    StyleContext sc = StyleContext.getDefaultStyleContext();
//    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
//        StyleConstants.Foreground, c);
//
//    int len = getDocument().getLength(); // same value as
//                       // getText().length();
//    setCaretPosition(len); // place caret at the end (with no selection)
//    setCharacterAttributes(aset, false);
//    replaceSelection(s); // there is no selection, so inserts at caret
//
//    setEditable(false);
//  }



    public void append(Color c, String s, int size) { // better implementation--uses
                      // StyleContext

    setEditable(true);

    SimpleAttributeSet aset = new SimpleAttributeSet();
    SimpleAttributeSet bset = new SimpleAttributeSet();
    
    StyleConstants.setAlignment(bset, StyleConstants.ALIGN_LEFT);
    StyleConstants.setFontFamily(bset, "Arial");

    StyleConstants.setForeground(aset, c);
    StyleConstants.setFontSize(aset, size);

    int len = getDocument().getLength(); // same value as
                       // getText().length();
    setCaretPosition(len); // place caret at the end (with no selection)
    setCharacterAttributes(aset, true);
    setParagraphAttributes(bset,true);
    replaceSelection(s); // there is no selection, so inserts at caret

    setEditable(false);
  }






  public void appendLabel(Color c, String s, int size) { // better implementation--uses
                      // StyleContext

    setEditable(true);

    SimpleAttributeSet aset = new SimpleAttributeSet();
    SimpleAttributeSet bset = new SimpleAttributeSet();

    StyleConstants.setAlignment(bset, StyleConstants.ALIGN_LEFT);
    StyleConstants.setFontFamily(bset, "Times New Roman");
    
    
    StyleConstants.setBold(aset, true);
    StyleConstants.setForeground(aset, c);
    StyleConstants.setFontSize(aset, size);

    int len = getDocument().getLength(); // same value as
                       // getText().length();
    setCaretPosition(len); // place caret at the end (with no selection)
    setCharacterAttributes(aset, true);
    setParagraphAttributes(bset,true);
    replaceSelection(s); // there is no selection, so inserts at caret


    setEditable(false);
  }


    public void appendHeader(Color c, String s, int size, boolean bold, boolean underline) { // better implementation--uses
                      // StyleContext

    setEditable(true);

    s = "\n"+s+"\n";

    // set attributes
    SimpleAttributeSet aset = new SimpleAttributeSet();
    SimpleAttributeSet bset = new SimpleAttributeSet();

//    StyleConstants.setAlignment(bset, StyleConstants.ALIGN_CENTER);
    StyleConstants.setFontFamily(bset, "Arial");

    StyleConstants.setBold(aset, bold);
    StyleConstants.setUnderline(aset, underline);
    StyleConstants.setFontSize(aset, size);
    StyleConstants.setBold(aset, true);
    StyleConstants.setForeground(aset, c);

    int len = getDocument().getLength(); // same value as
                       // getText().length();
    setCaretPosition(len); // place caret at the end (with no selection)
    setCharacterAttributes(aset, true);
    setParagraphAttributes(bset,true);
    replaceSelection(s); // there is no selection, so inserts at caret

    setEditable(false);
  }


}
