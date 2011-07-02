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
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 *
 * @author J. Christopher Anderson
 */

public abstract class blueTextArea extends javax.swing.JPanel implements FocusListener  {

    public blueTextArea(String fieldname, String initialvalue, int height) {
        fieldName = fieldname;
        oldValue = initialvalue;
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(textfieldwidth+labelwidth,height));
        setPreferredSize(new Dimension(textfieldwidth+labelwidth,height));
        setOpaque(false);

        //Put in the label
        label = new JLabel(fieldName);
        label.setForeground(Color.WHITE);
        label.setFont(new java.awt.Font("Arial", Font.BOLD, textsize));
        label.setPreferredSize(new Dimension(labelwidth, 2*textsize));

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(label);
        add(panel, BorderLayout.WEST);

        //Put in the text field
        textArea = new JTextArea();
        textArea.setBackground(navyblue);
        textArea.setFont(new java.awt.Font("Arial", Font.PLAIN, textsize));
        textArea.setForeground(Color.WHITE);
        textArea.setBorder(null);
        textArea.setText(initialvalue);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.addFocusListener(this);
        textArea.setPreferredSize(new Dimension(textfieldwidth, 2*textsize));
        add(textArea, BorderLayout.EAST);
    }

    @Override
    public void focusGained(FocusEvent e) {
        textArea.setBackground(Color.WHITE);
        textArea.setFont(new java.awt.Font("Arial", 0, textsize));
        textArea.setForeground(Color.BLACK);
        textArea.setBorder(blackline);
    }

    @Override
    public void focusLost(FocusEvent e) {
        textArea.setBackground(navyblue);
        textArea.setFont(new java.awt.Font("Arial", 0, textsize));
        textArea.setForeground(Color.WHITE);
        textArea.setBorder(null);
        if(oldValue.equals(this.getText())) {
                    return;
        }
        loseFocus();
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public static void setTextSize(int size) {
        textsize = size;
    }

    public static void setLabelWidth(int size) {
        labelwidth = size;
    }

    public static void setTextAreaWidth(int size) {
        textfieldwidth = size;
    }
    public abstract void loseFocus();
    public abstract void dataUpdated();

/**-----------------
     variables
 -----------------*/
    private JTextArea textArea;
    private JLabel label;
    private String fieldName;
    public String oldValue="";

    static int labelwidth = 170;
    static int textfieldwidth = 220;
    static int textsize = 14;

    private static Color navyblue = new Color(35, 48, 64);
    private static final Border blackline = BorderFactory.createLineBorder(Color.black);
    }