/*
 Copyright (c) 2009 The Regents of the University of California.
 All rights reserved.
 Permission is hereby granted, without written agreement and without
 license or royalty fees, to use, copy, modify, and distribute this
 software and its documentation for any purpose, provided that the above
 copyright notice and the following two paragraphs appear in all copies
 of this software.

 IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 ENHANCEMENTS, OR MODIFICATIONS..
 */

package org.clothocad.tool.straineditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author J. Christopher Anderson
 */

public abstract class blueTextField extends javax.swing.JPanel implements FocusListener  {

    public blueTextField(String fieldname, String initialvalue) {
        fieldName = fieldname;
        if(initialvalue!=null) {
            oldValue = initialvalue;
        }
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(textfieldwidth+strainelwidth,2*textsize+2));
        setPreferredSize(new Dimension(textfieldwidth+strainelwidth,2*textsize+2));
        setOpaque(false);

        //Put in the JLabel
        label = new JLabel(fieldName);
        label.setForeground(Color.WHITE);
        label.setFont(new java.awt.Font("Arial", Font.BOLD, textsize));
        label.setPreferredSize(new Dimension(strainelwidth, 2*textsize));
        add(label, BorderLayout.WEST);

        //Put in the text field
        textField = new JTextField();
        textField.setBackground(navyblue);
        textField.setFont(new java.awt.Font("Arial", Font.PLAIN, textsize));
        textField.setForeground(Color.WHITE);
        textField.setBorder(null);
        textField.setText(initialvalue);
        textField.addFocusListener(this);
        textField.setPreferredSize(new Dimension(textfieldwidth, 2*textsize));
        add(textField, BorderLayout.EAST);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(greymode) {
            return;
        }
        textField.setBackground(Color.WHITE);
        textField.setFont(new java.awt.Font("Arial", 0, textsize));
        textField.setForeground(Color.BLACK);
        textField.setBorder(blackline);
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(greymode) {
            return;
        }
        textField.setBackground(navyblue);
        textField.setFont(new java.awt.Font("Arial", 0, textsize));
        textField.setForeground(Color.WHITE);
        textField.setBorder(null);
        if(oldValue.equals(this.getText())) {
                    return;
        }
        loseFocus();
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        oldValue = text;    //This is the new line
        textField.setText(text);
    }

    public void setGreyMode(boolean isit) {
        greymode = isit;
        if(greymode) {
            textField.setBackground(navyblue);
            textField.setForeground(Color.LIGHT_GRAY);
            textField.setEditable(false);
            label.setForeground(Color.LIGHT_GRAY);
        } else {
            textField.setEditable(true);
            label.setForeground(Color.WHITE);
            focusLost(null);
        }
    }

    public static void setTextSize(int size) {
        textsize = size;
    }

    public static void setStrainelWidth(int size) {
        strainelwidth = size;
    }

    public static void setTextFieldWidth(int size) {
        textfieldwidth = size;
    }
    public abstract void loseFocus();
    public abstract void dataUpdated();

/**-----------------
     variables
 -----------------*/
    private boolean greymode = false;
    private JTextField textField;
    private JLabel label;
    private String fieldName;
    public String oldValue="";

    static int strainelwidth = 170;
    static int textfieldwidth = 220;
    static int textsize = 14;

    private static Color navyblue = new Color(35, 48, 64);
    private static final Border blackline = BorderFactory.createLineBorder(Color.black);
    }