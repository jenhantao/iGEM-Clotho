/*
 * sequenceViewPartExport.java
 *
 * Created on July 7, 2008, 11:40 AM
 */

/*
Copyright (c) 2008 The Regents of the University of California.
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
package org.clothocad.tool.sequenceview;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Collection;
import org.clothocore.api.data.Feature;
import org.clothocore.api.data.Format;
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.data.ObjLink;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.Oligo;
import org.clothocore.api.data.Part;
import org.clothocore.api.data.Vector;
import org.clothocore.util.dialog.ClothoDialogBox;
import org.openide.util.Exceptions;
//import org.clothocad.util.ClothoDialogBox;

/**
 *
 * @author  Douglas Densmore, Jenhan Tao
 */
public class SequenceViewPartExport extends javax.swing.JFrame {

    /** Creates new form sequenceViewPartExport */
    public SequenceViewPartExport() {
        initComponents();
    }

    /** Creates new form sequenceViewPartExport */
    public SequenceViewPartExport(SequenceView sv) {
        initComponents();
        _sv = sv;
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                ClothoDialogBox db = new ClothoDialogBox("Clotho: Exit Check", "Are you sure you want to close the packager?");

                if (db.show_optionDialog(javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
    }

    public SequenceViewPartExport(SequenceView sv, JTextPane jtp) {
        initComponents();
        _sv = sv;
//        addWindowListener(new WindowAdapter(){
//        @Override
//        public void windowClosing(WindowEvent we){
//            ClothoDialogBox db = new ClothoDialogBox("Clotho: Exit Check", "Are you sure you want to close the packager?");
//        
//            if(db.show_optionDialog(javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION)
//                dispose();
//            }
//        });
        dataTextPane.setText(jtp.getText());
        if (jtp.getSelectedText() != null) {
            intervalRadioButton.setSelected(true);
            fromTextField.setText(Integer.toString(jtp.getSelectionStart()));
            fromTextField.getDocument().addDocumentListener(new SelectionListener());
            toTextField.setText(Integer.toString(jtp.getSelectionEnd()));
            toTextField.getDocument().addDocumentListener(new SelectionListener());

            try {
                dataTextPane.getHighlighter().addHighlight(jtp.getSelectionStart(), jtp.getSelectionEnd(), new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(jtp.getSelectionColor()));
            } catch (BadLocationException e) {
            }
        } else {
            try {
                dataTextPane.getHighlighter().addHighlight(jtp.getSelectionStart(), jtp.getSelectionEnd(), new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(jtp.getSelectionColor()));
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
            fromTextField.setText("0");
            toTextField.setText(Integer.toString(jtp.getText().length()));
            wsRadioButton.setSelected(true);
            try {
                dataTextPane.getHighlighter().addHighlight(0, dataTextPane.getText().length() - 1, new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(dataTextPane.getSelectionColor()));
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        ArrayList<ObjLink> collectionLinks = Collector.getAllLinksOf(ObjType.COLLECTION);
        collectionComboBox.removeAllItems();
        for (ObjLink oj : collectionLinks) {
            collectionComboBox.addItem(oj);
        }
        collectionComboBox.setSelectedIndex(-1);
        formatComboBox.removeAllItems();
        ArrayList<ObjLink> formatLinks = Collector.getAllLinksOf(ObjType.FORMAT);
        for (ObjLink oj : formatLinks) {
            formatComboBox.addItem(oj);
        }
        formatComboBox.setSelectedIndex(-1);
        objectTypeComboBox.removeAllItems();
        objectTypeComboBox.addItem("Feature");
        objectTypeComboBox.addItem("Oligo");
        objectTypeComboBox.addItem("Part");
        objectTypeComboBox.addItem("Vector");
        objectTypeComboBox.setSelectedIndex(-1);
        formatComboBox.setEnabled(false);
        formatLabel.setEnabled(false);
        this.setVisible(true);
    }

    public boolean wholeSeqSel() {
        return wsRadioButton.isSelected();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        importbuttonGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTextPane = new javax.swing.JTextPane();
        objectTypeComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        fromTextField = new javax.swing.JTextField();
        toTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        wsRadioButton = new javax.swing.JRadioButton();
        intervalRadioButton = new javax.swing.JRadioButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        collectionComboBox = new javax.swing.JComboBox();
        nameTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        formatComboBox = new javax.swing.JComboBox();
        formatLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Save as Clotho Object");

        dataTextPane.setEditable(false);
        dataTextPane.setToolTipText("Data to package as a part");
        jScrollPane1.setViewportView(dataTextPane);

        objectTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        objectTypeComboBox.setToolTipText("Which field should the data represent in the new part?");
        objectTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                objectTypeComboBoxActionPerformed(evt);
            }
        });

        jLabel2.setText("Save as: ");

        fromTextField.setToolTipText("Starting interval of the sequence data to import");
        fromTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromTextFieldActionPerformed(evt);
            }
        });
        fromTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fromTextFieldKeyTyped(evt);
            }
        });

        toTextField.setToolTipText("End interval of the sequence data to import");
        toTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toTextFieldActionPerformed(evt);
            }
        });
        toTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                toTextFieldKeyTyped(evt);
            }
        });

        jLabel4.setText("From");

        jLabel5.setText("To");

        saveButton.setText("Save");
        saveButton.setToolTipText("Package the part for the selected connection and data");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        importbuttonGroup.add(wsRadioButton);
        wsRadioButton.setText("Whole Sequence");
        wsRadioButton.setToolTipText("Import the whole sequence");

        importbuttonGroup.add(intervalRadioButton);
        intervalRadioButton.setText("Interval");
        intervalRadioButton.setToolTipText("Import the provided interval");

        cancelButton.setText("Cancel");
        cancelButton.setMaximumSize(new java.awt.Dimension(57, 23));
        cancelButton.setMinimumSize(new java.awt.Dimension(57, 23));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Save in Collection: ");

        collectionComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("File Name: ");

        formatComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        formatComboBox.setToolTipText("Which field should the data represent in the new part?");
        formatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formatComboBoxActionPerformed(evt);
            }
        });

        formatLabel.setText("Format");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(collectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(wsRadioButton)
                    .addComponent(intervalRadioButton)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(objectTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(formatLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(formatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {fromTextField, toTextField});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {collectionComboBox, formatComboBox, formatLabel, jLabel1, jLabel2, jLabel3, nameTextField, objectTypeComboBox});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(wsRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(intervalRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(fromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(toTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(objectTypeComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(formatComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                            .addComponent(formatLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(collectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saveButton)
                            .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {fromTextField, toTextField});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {collectionComboBox, formatComboBox, formatLabel, jLabel1, jLabel2, jLabel3, nameTextField, objectTypeComboBox});

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed

    String name = nameTextField.getText();
    String seq = null;
    System.out.println("saving: " + name);
    if (intervalRadioButton.isSelected()) {
        try {

            seq = dataTextPane.getText().substring(Integer.parseInt(fromTextField.getText()), Integer.parseInt(toTextField.getText()));
        } catch (NumberFormatException e) {
            System.out.println("tried to use invalid sequence interval to generate Clotho object");
            return;
        }
    } else {
        seq = dataTextPane.getText();
    }
    Format form = null;
    if (objectTypeComboBox.getSelectedIndex() != 1) {
        form = Collector.getFormat(((ObjLink) formatComboBox.getSelectedItem()).uuid);
    }
    Collection coll = Collector.getCollection(((ObjLink) collectionComboBox.getSelectedItem()).uuid);
    Boolean saved = false;
    ObjBase newObject = null;
    if (objectTypeComboBox.getSelectedIndex() == 0) {
        //feature
        if (seq.toUpperCase().indexOf("ATG") != -1) {
            newObject = Feature.generateFeature(name, seq, Collector.getCurrentUser(), true);
        } else {
            newObject = Feature.generateFeature(name, seq, Collector.getCurrentUser(), true);
        }
    } else if (objectTypeComboBox.getSelectedIndex() == 1) {
        newObject = new Oligo(name, "", Collector.getCurrentUser(), seq);
    } else if (objectTypeComboBox.getSelectedIndex() == 2) {
        //part
        newObject = Part.generateBasic(name, "", seq, form, Collector.getCurrentUser());

    } else if (objectTypeComboBox.getSelectedIndex() == 3) {
        Vector.generateVector(name, "", seq, form, Collector.getCurrentUser());
    } else {
        System.out.println("error saving new Clotho object, check selected fields");
        this.dispose();
        return;
    }
    System.out.println("saving: " + newObject.getName());
    saved = newObject.saveDefault();
    coll.addObject(newObject);
    if (saved) {
        System.out.println("Successful save!");     
        this.dispose();
    }
}//GEN-LAST:event_saveButtonActionPerformed

private void objectTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_objectTypeComboBoxActionPerformed
    if (objectTypeComboBox.getSelectedItem() != null) {
        if (!(objectTypeComboBox.getSelectedItem()).equals("Oligo")) {
            formatLabel.setEnabled(true);
            formatComboBox.setEnabled(true);
        } else {
            formatLabel.setEnabled(false);
            formatComboBox.setEnabled(false);
        }
    }
}//GEN-LAST:event_objectTypeComboBoxActionPerformed

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    this.dispose();
}//GEN-LAST:event_cancelButtonActionPerformed

private void formatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formatComboBoxActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_formatComboBoxActionPerformed

private void fromTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromTextFieldActionPerformed
}//GEN-LAST:event_fromTextFieldActionPerformed

private void toTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toTextFieldActionPerformed
}//GEN-LAST:event_toTextFieldActionPerformed

private void fromTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fromTextFieldKeyTyped
}//GEN-LAST:event_fromTextFieldKeyTyped

private void toTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_toTextFieldKeyTyped
}//GEN-LAST:event_toTextFieldKeyTyped

    /**
     * @param args the command line arguments
     */
    public void updateSelection() {
        try {
            dataTextPane.setSelectionEnd(Integer.parseInt(toTextField.getText().trim()));
            dataTextPane.setSelectionStart(Integer.parseInt(fromTextField.getText().trim()));
        } catch (NumberFormatException e) {
            return;
        }
        try {
            dataTextPane.getHighlighter().removeAllHighlights();
            dataTextPane.getHighlighter().addHighlight(dataTextPane.getSelectionStart(), dataTextPane.getSelectionEnd(), new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(dataTextPane.getSelectionColor()));
        } catch (BadLocationException e) {
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new SequenceViewPartExport().setVisible(true);
            }
        });
    }
    private SequenceView _sv;
    private boolean _visible;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox collectionComboBox;
    private javax.swing.JTextPane dataTextPane;
    private javax.swing.JComboBox formatComboBox;
    private javax.swing.JLabel formatLabel;
    private javax.swing.JTextField fromTextField;
    private javax.swing.ButtonGroup importbuttonGroup;
    private javax.swing.JRadioButton intervalRadioButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JComboBox objectTypeComboBox;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField toTextField;
    private javax.swing.JRadioButton wsRadioButton;
    // End of variables declaration//GEN-END:variables

    private class SelectionListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateSelection();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateSelection();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateSelection();
        }
    }
}
