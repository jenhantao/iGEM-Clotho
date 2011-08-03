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

/*
 * PropertiesDialog.java
 *
 * Created on Jul 13, 2009, 6:03:10 PM
 */

package org.clothocad.tool.spectacles.ui.frames;

import eugene.Primitive;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import org.clothocad.databaseio.Datum;
import org.clothocad.tool.spectacles.Spectacles;
import org.clothocad.tool.spectacles.eugeneimportexport.DevicePartWrapper;
import org.clothocad.tool.spectacles.ui.scenes.WorkspaceScene;
import org.clothocad.tool.spectacles.ui.scenes.SpectaclesFactory;
import org.clothocore.api.data.Part;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Format;
import org.clothocore.api.data.Feature;
//import org.openide.util.ImageUtilities;

/**
 * PropertiesDialog displays editable part properties.
 * @author Rich
 * @author Joanna
 */
public class PropertiesDialog extends javax.swing.JDialog {

    /**
     * Creates a new PropertiesDialog.
     */
    public PropertiesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        _wsFrame = (WorkspaceFrame) parent;
    }

    public String getPartObjectKeyword() {
        return _partObjectKeyword;
    }

    /**
     * Returns this dialog's parent frame.
     * @return the parent orkspaceFrame.
     */
    public WorkspaceFrame getWorkspaceFrame() {
        return _wsFrame;
    }

    /**
     * Given a wrapped part or device, shows this dialog.
     * Also sets this dialog's wrapper and WorkspaceScene.
     * @param dpw the wrapper.
     */
    public void showDialog(DevicePartWrapper dpw, WorkspaceScene wsScene) {
        _dpw = dpw;
        _wsScene = wsScene;
        setLocationRelativeTo(_wsFrame);
        setVisible(true);
    }

    /**
     * Sets this dialog's current DevicePartWrapper.
     * @param dpw the DevicePartWrapper.
     */
    public void setDevicePartWrapper(DevicePartWrapper dpw) {
        _dpw = dpw;
    }

    /**
     * Sets this dialog's WorkspaceScene.
     * @param wsScene the WorkspaceScene.
     */
    public void setWorkspaceScene(WorkspaceScene wsScene) {
        _wsScene = wsScene;
    }

    @Override
    public void setVisible(boolean visible) {
        if (!visible) {
            super.setVisible(visible);
        } else if (_wsScene != null && _dpw != null && _dpw.isPart()) {
            // load in properties here
            HashMap<String, Primitive> properties = _dpw.getPartPropertyValues();

            nameTextField.setEditable(true);
            if (properties.containsKey("Feature Name")) {
                nameTextField.setText((String) properties.get("Feature Name").getValue());
            }
            
            sequenceTextArea.setEditable(true);
            if (properties.containsKey("Feature Sequence")) {
                sequenceTextArea.setText((String) properties.get("Feature Sequence").getValue());
                //sequenceTextArea.setCaretPosition(0);
            }

            partsNameField.setEditable(true);
            partsNameField.setText(_dpw.getName());
            if (properties.containsKey("Part Name")){
                partsNameField.setText((String)properties.get("Part Name").getValue());
            }

            partsDescriptionField.setEditable(true);
            if (properties.containsKey("Part Description")){
                partsDescriptionField.setText((String)properties.get("Part Description").getValue());
            }

            partsFormatField.setEditable(true);
            if (properties.containsKey("Part Format")){
                partsFormatField.setText((String)properties.get("Part Format").getValue());
            }

            partsSequenceArea.setEditable(true);
            if (properties.containsKey("Part Sequence")){
                partsSequenceArea.setText((String)properties.get("Part Sequence").getValue());
            }

            comppartNameField.setEditable(true);
            if (properties.containsKey("Composite Part Name")){
                comppartNameField.setText((String)properties.get("Composite Part Name").getValue());
            }

            
            comppartLefty.setEditable(true);
            /*if (properties.containsKey("Lefty Part")){
                comppartLefty.setText((String)properties.get("Lefty Part").getValue());
            }*/

            comppartRighty.setEditable(true);
            /*if (properties.containsKey("Righty Part")){
                comppartRighty.setText((String)properties.get("Righty Part").getValue());
            }
             *
             */

            if (!Spectacles.isRunningSolo()) { // and if database connection exists??
                if (_partObjectKeyword == null) { // set the default keywords if they are not set
                    _partObjectKeyword = "biobrick";
                    _secondaryObjectKeyword = "family";
                    _typeFieldKeyword = "name";
                    _displayByKeyword = "name";
                }
                //partObjectKeywordComboBox.setEnabled(true);
                //populatePartObjectKeywordComboBox();
                // the rest of the combo boxes will populate in response to listeners on the partObjectKeywordComboBox
                //termToMatchTextField.setEditable(true);
                //termToMatchTextField.setText(_dpw.getType());
            }
            super.setVisible(visible);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        sequenceLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        sequenceScrollPane = new javax.swing.JScrollPane();
        sequenceTextArea = new javax.swing.JTextArea();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        partsDescriptionField = new javax.swing.JTextField();
        partsFormatField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        partsSequenceArea = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        comppartNameField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        comppartLefty = new javax.swing.JTextField();
        partsNameField = new javax.swing.JTextField();
        comppartRighty = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.title")); // NOI18N
        setBounds(new java.awt.Rectangle(0, 22, 550, 650));
        setName("PropertiesForm"); // NOI18N
        setPreferredSize(new java.awt.Dimension(550, 650));
        setSize(getPreferredSize());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        nameLabel.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.nameLabel.text_1")); // NOI18N
        nameLabel.setName("nameLabel"); // NOI18N

        sequenceLabel.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.sequenceLabel.text_1")); // NOI18N
        sequenceLabel.setName("sequenceLabel"); // NOI18N

        nameTextField.setEditable(false);
        nameTextField.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.nameTextField.text")); // NOI18N
        nameTextField.setName("nameTextField"); // NOI18N
        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });

        sequenceScrollPane.setName("sequenceScrollPane"); // NOI18N

        sequenceTextArea.setColumns(20);
        sequenceTextArea.setEditable(false);
        sequenceTextArea.setLineWrap(true);
        sequenceTextArea.setRows(5);
        sequenceTextArea.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.sequenceTextArea.text")); // NOI18N
        sequenceTextArea.setName("sequenceTextArea"); // NOI18N
        sequenceScrollPane.setViewportView(sequenceTextArea);

        saveButton.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.saveButton.text")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.jLabel1.text_1")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.partsNameLabel.text")); // NOI18N
        jLabel2.setName("partsNameLabel"); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.Parts.Label.text")); // NOI18N
        jLabel3.setName("Parts.Label"); // NOI18N

        jLabel6.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.Parts.Sequence.text")); // NOI18N
        jLabel6.setName("Parts.Sequence"); // NOI18N

        jLabel7.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        partsDescriptionField.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.partsDescriptionField.text")); // NOI18N
        partsDescriptionField.setName("partsDescriptionField"); // NOI18N
        partsDescriptionField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partsDescriptionFieldActionPerformed(evt);
            }
        });

        partsFormatField.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.partsFormatField.text")); // NOI18N
        partsFormatField.setName("partsFormatField"); // NOI18N
        partsFormatField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partsFormatFieldActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        partsSequenceArea.setColumns(20);
        partsSequenceArea.setRows(5);
        partsSequenceArea.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.partsSequenceArea.text")); // NOI18N
        partsSequenceArea.setName("partsSequenceArea"); // NOI18N
        jScrollPane1.setViewportView(partsSequenceArea);

        jLabel8.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.Parts.Type.text")); // NOI18N
        jLabel8.setName("Parts.Type"); // NOI18N

        jSeparator3.setName("jSeparator3"); // NOI18N

        jLabel9.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        comppartNameField.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.comppartNameField.text")); // NOI18N
        comppartNameField.setName("comppartNameField"); // NOI18N

        jLabel5.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        comppartLefty.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.comppartLefty.text")); // NOI18N
        comppartLefty.setName("comppartLefty"); // NOI18N
        comppartLefty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comppartLeftyActionPerformed(evt);
            }
        });

        partsNameField.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.partsNameField.text")); // NOI18N
        partsNameField.setName("partsNameField"); // NOI18N
        partsNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partsNameFieldActionPerformed(evt);
            }
        });

        comppartRighty.setText(org.openide.util.NbBundle.getMessage(PropertiesDialog.class, "PropertiesDialog.comppartRighty.text")); // NOI18N
        comppartRighty.setName("comppartRighty"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nameLabel)
                                    .addComponent(sequenceLabel)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel6))
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sequenceScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                    .addComponent(partsFormatField, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                    .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                    .addComponent(partsNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                    .addComponent(partsDescriptionField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addGap(52, 52, 52)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(comppartLefty, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(comppartRighty)
                                            .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 155, Short.MAX_VALUE)))
                                    .addComponent(comppartNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))))
                        .addGap(1213, 1213, 1213)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, saveButton});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {comppartLefty, comppartRighty});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sequenceScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jLabel1)
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(partsNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(sequenceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(partsDescriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(partsFormatField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comppartNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comppartRighty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comppartLefty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saveButton)
                            .addComponent(cancelButton))))
                .addGap(41, 41, 41))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        _dpw = null;
        _wsScene = null;

        //Set default dialogue texts as blank
        nameTextField.setEditable(false);
        nameTextField.setText("");
        sequenceTextArea.setEditable(false);
        sequenceTextArea.setText("");
        partsNameField.setEditable(false);
        partsNameField.setText("");
        partsFormatField.setEditable(false);
        partsFormatField.setText("");
        partsDescriptionField.setEditable(false);
        partsDescriptionField.setText("");
        partsSequenceArea.setEditable(false);
        partsSequenceArea.setText("");
        comppartNameField.setEditable(false);
        comppartNameField.setText("");
        comppartLefty.setEditable(false);
        comppartLefty.setText("");
        comppartRighty.setEditable(false);
        comppartRighty.setText("");

        /*partObjectKeywordComboBox.setEnabled(false);
        partObjectKeywordComboBox.removeAllItems();
        secondaryObjectKeywordComboBox.setEnabled(false);
        secondaryObjectKeywordComboBox.removeAllItems();
        typeFieldKeywordComboBox.setEnabled(false);
        typeFieldKeywordComboBox.removeAllItems();
        termToMatchTextField.setEditable(false);
        termToMatchTextField.setText("");
        displayByKeywordComboBox.setEnabled(false);
        displayByKeywordComboBox.removeAllItems();
        getImplementationsButton.setEnabled(false);
        implementationComboBox.setEnabled(false);
        implementationComboBox.removeAllItems();
        getSequenceButton.setEnabled(false);*/
    }//GEN-LAST:event_formWindowClosed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed

        //Renaming the part in the workspace
        String pname = partsNameField.getText();
        String fname = nameTextField.getText();
        if (pname.isEmpty() == false) {
            _wsFrame.renamePart(_dpw, partsNameField.getText());
        } else if (pname.isEmpty() == true && fname.isEmpty() == false) {
            _wsFrame.renamePart(_dpw, nameTextField.getText());
        } else {
            _wsFrame.renamePart(_dpw, "(" + _dpw.getName() + ")");
        }

        HashMap<String, Primitive> properties = _dpw.getPartPropertyValues();

        //Part GUI entry fields
        Primitive partsName = new Primitive("Part Name", "txt");
        partsName.txt = partsNameField.getText();
        properties.put("Part Name", partsName);

        Primitive partsDesc = new Primitive("Part Description", "txt");
        partsDesc.txt = partsDescriptionField.getText();
        properties.put("Part Description", partsDesc);

        Primitive partsSequence = new Primitive("Part Sequence", "txt");
        partsSequence.txt = partsSequenceArea.getText();
        properties.put("Part Sequence", partsSequence);

        //Feature entry fields

        Primitive sequencePrimitive = new Primitive("Feature Sequence", "txt");
        sequencePrimitive.txt = sequenceTextArea.getText();
        properties.put("Feature Sequence", sequencePrimitive);

        Primitive namePrimitive = new Primitive("Feature Name", "txt");
        namePrimitive.txt = nameTextField.getText();
        properties.put("Feature Name", namePrimitive);

        //Composite Part entry fields

        Primitive comppartPrimitive = new Primitive("Composite Part Name", "txt");
        comppartPrimitive.txt = comppartNameField.getText();
        properties.put("Composite Part Name", comppartPrimitive);

        Primitive cpartLeftyPrimitive = new Primitive("Lefty Part", "txt");
        cpartLeftyPrimitive.txt = comppartLefty.getText();
        properties.put("Lefty Part", cpartLeftyPrimitive);

        Primitive cpartRightyPrimitive = new Primitive("Righty Part", "txt");
        cpartRightyPrimitive.txt = comppartRighty.getText();
        properties.put("Righty Part", cpartRightyPrimitive);

        //Create Part using Clotho Core method//
        String name = partsNameField.getText();
        String description = partsDescriptionField.getText();
        String sequence = partsSequenceArea.getText();
        Format form = Format.retrieveByName("freeform");
        if (name.isEmpty() == false) {
        _part = org.clothocore.api.data.Part.generateBasic(name, description, sequence, form, Collector.getCurrentUser());
        //_part.saveDefault();
        }

        //Create Feature using Clotho Core method
        String featname = nameTextField.getText();
        String featseq = sequenceTextArea.getText();
        if (featname.isEmpty() == false) {
        _feature = org.clothocore.api.data.Feature.generateFeature(featname, featseq, Collector.getCurrentUser(), false);
        //_feature.saveDefault();
        }

        //*** Change icons based upon degree of part information ***//

        String imfirst = _dpw.getImagePath();
        int impathlength = imfirst.length();

        if (imfirst.substring(impathlength - 8).equalsIgnoreCase("part.png") == true) {
            String imtype = imfirst.substring(51, (imfirst.length() - 8));
            if (sequence.isEmpty() == false){
                //If there is a part sequence
                _wsScene.hideContents();
                _dpw.setImagePath("src/org/clothocad/tool/spectacles/partsimages/VBOL." + imtype + "part.png");
                _wsScene.showContents();
            } else if (sequence.isEmpty() == true && featseq.isEmpty() == false) {
                //If there is a feature sequences, but no part sequence
                _wsScene.hideContents();
                _dpw.setImagePath("src/org/clothocad/tool/spectacles/partsimages/VBOL." + imtype + "feat.png");
                _wsScene.showContents();
            } else {
                //If there is not part or feature sequence
                _wsScene.hideContents();
                _dpw.setImagePath("src/org/clothocad/tool/spectacles/partsimages/VBOL." + imtype + ".png");
                _wsScene.showContents();
            }
        } else if (imfirst.substring(impathlength - 8).equalsIgnoreCase("feat.png") == true) {
            String imtype = imfirst.substring(51, (imfirst.length() - 8));
            if (sequence.isEmpty() == false){
                //If there is a part sequence
                _wsScene.hideContents();
                _dpw.setImagePath("src/org/clothocad/tool/spectacles/partsimages/VBOL." + imtype + "part.png");
                _wsScene.showContents();
            } else if (sequence.isEmpty() == true && featseq.isEmpty() == false) {
                //If there is a feature sequences, but no part sequence
                _wsScene.hideContents();
                _dpw.setImagePath("src/org/clothocad/tool/spectacles/partsimages/VBOL." + imtype + "feat.png");
                _wsScene.showContents();
            } else {
                //If there is not part or feature sequence
                _wsScene.hideContents();
                _dpw.setImagePath("src/org/clothocad/tool/spectacles/partsimages/VBOL." + imtype + ".png");
                _wsScene.showContents();
            }
        } else {
            String imtype = imfirst.substring(51, (imfirst.length() - 4));
            if (sequence.isEmpty() == false){
                //If there is a part sequence
                _wsScene.hideContents();
                _dpw.setImagePath("src/org/clothocad/tool/spectacles/partsimages/VBOL." + imtype + "part.png");
                _wsScene.showContents();
            } else if (sequence.isEmpty() == true && featseq.isEmpty() == false) {
                //If there is a feature sequences, but no part sequence
                _wsScene.hideContents();
                _dpw.setImagePath("src/org/clothocad/tool/spectacles/partsimages/VBOL." + imtype + "feat.png");
                _wsScene.showContents();
            } else {
                //If there is not part or feature sequence
                _wsScene.hideContents();
                _dpw.setImagePath("src/org/clothocad/tool/spectacles/partsimages/VBOL." + imtype + ".png");
                _wsScene.showContents();
            }
        }

        //Create Composite Part using Clotho Core method

        /*
        String cpartname = comppartNameField.getText();
        Part lefty = Part.retrieveByName(comppartLefty.getText());
        Part righty = Part.retrieveByName(comppartRighty.getText());
        Format cpartform = Format.retrieveByName("freeform");
        
        ArrayList<Part> composition = new ArrayList<Part>();
        composition.add(lefty);
        composition.add(righty);
        _compPart = Part.generateComposite(composition, null, form, Collector.getCurrentUser(), name, "");
        _compPart.saveDefault(); */

        //Saving operations
        //_wsFrame.updateAssociatedWidgets(_dpw.getName());

        // store keywords for next setVisible()
        //_partObjectKeyword = (String) partObjectKeywordComboBox.getSelectedItem();
        //_secondaryObjectKeyword = (String) secondaryObjectKeywordComboBox.getSelectedItem();
        //_typeFieldKeyword = (String) typeFieldKeywordComboBox.getSelectedItem();
        //_displayByKeyword = (String) displayByKeywordComboBox.getSelectedItem();

        // assuming that only part properties will be displayed right now
        _wsFrame.displayProperties();
        _wsScene.validate();
        _wsFrame.setModified(true);
        dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextFieldActionPerformed

    private void partsDescriptionFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partsDescriptionFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partsDescriptionFieldActionPerformed

    private void partsNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partsNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partsNameFieldActionPerformed

    private void comppartLeftyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comppartLeftyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comppartLeftyActionPerformed

    private void partsFormatFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partsFormatFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partsFormatFieldActionPerformed

    private void getImplementations() {
        //implementationComboBox.setEnabled(true);
        //implementationComboBox.removeAllItems();
        //populateImplementationComboBox();
        //getSequenceButton.setEnabled(true);
    }
    private void populatePartObjectKeywordComboBox() {
        /*Part pa = Part.retrieveByName(partsNameField.getText());
        ArrayList<String> objNames = p.Fields.NAME;
        int index = -1;
        for (String kywd : objNames) {
            System.out.println("__" + kywd + "__");
            partObjectKeywordComboBox.addItem(kywd);
            if (kywd.equals(_partObjectKeyword)) {
                index = partObjectKeywordComboBox.getItemCount() - 1;
                System.out.println("part object keyword matched, index " + index);
            }
        }
        partObjectKeywordComboBox.setSelectedItem(_partObjectKeyword);  // set default keyword
        partObjectKeywordComboBox.setSelectedIndex(index); */
    }

    private void populateSecondaryObjectKeywordComboBox() {
        //secondaryObjectKeywordComboBox.addItem(_noneKeyword);
        //int index = -1;
        //if (_noneKeyword.equals(_secondaryObjectKeyword)) {
        //    index = 0;
        //}
        /*
        String[] objNames = Spectacles.getDataCore().getAdjacentDatumNames((String) partObjectKeywordComboBox.getSelectedItem());
        for (String kywd : objNames) {
            secondaryObjectKeywordComboBox.addItem(kywd);
            if (kywd.equals(_secondaryObjectKeyword)) {
                index = secondaryObjectKeywordComboBox.getItemCount() - 1;
            }
        }
         * */
         
        //secondaryObjectKeywordComboBox.setSelectedItem(_noneKeyword);  // set default keyword
        //secondaryObjectKeywordComboBox.setSelectedIndex(index);
    }

    private void populateTypeFieldKeywordComboBox(String objKeyword) {
        /*
        String[] fieldNames = Spectacles.getDataCore().getLocalFields(objKeyword);
        int index = -1;
        for (String kywd : fieldNames) {
            //System.out.println("__" + kywd + "__");
            typeFieldKeywordComboBox.addItem(kywd);
            if (kywd.equals(_typeFieldKeyword)) {
                index = typeFieldKeywordComboBox.getItemCount() - 1;
                //System.out.println("type field keyword matched, index " + index);
            }
        }
         *
         */
        //typeFieldKeywordComboBox.setSelectedItem(_typeFieldKeyword);  // set default keyword
        //typeFieldKeywordComboBox.setSelectedIndex(index);
    }

    private void populateDisplayByKeywordComboBox() {
        /*
        String objKeyword = (String) partObjectKeywordComboBox.getSelectedItem();
        String[] fieldNames = Spectacles.getDataCore().getLocalFields(objKeyword);
        int index = -1;
        for (String kywd : fieldNames) {
            displayByKeywordComboBox.addItem(kywd);
            if (kywd.equals(_displayByKeyword)) {
                index = displayByKeywordComboBox.getItemCount() - 1;
                //System.out.println("display by keyword matched, index " + index);
            }
        }
         *
         */
        //displayByKeywordComboBox.setSelectedItem(_displayByKeyword);  // set default keyword*/
        //displayByKeywordComboBox.setSelectedIndex(index);

    }

    private void populateImplementationComboBox() {
        /*DevicePartWrapper mainWrapper = _wsFrame.getManager().getWrappedPart(_dpw.getName());
        mainWrapper.implementations = 0;
        String partObjKywd = (String) partObjectKeywordComboBox.getSelectedItem();
        String secondaryObjKywd = (String) secondaryObjectKeywordComboBox.getSelectedItem();
        String typeFieldKywd = (String) typeFieldKeywordComboBox.getSelectedItem();
        String matchTerm = termToMatchTextField.getText();
        String dispByKywd = (String) displayByKeywordComboBox.getSelectedItem();
        String queryStr;
        if (secondaryObjKywd.equals(_noneKeyword)) {
            queryStr = "from " + partObjKywd + " where " + typeFieldKywd + " like '%" + matchTerm + "%'";
        } else {
            queryStr = "from " + partObjKywd + " where " + secondaryObjKywd + "." + typeFieldKywd + " like '%" + matchTerm + "%'";
        }
        //Iterator<Datum> iter = Spectacles.getDataCore().findUsingHQL(queryStr);
        //while (iter.hasNext()) {
            //try {
         //       implementationComboBox.addItem(iter.next().getFieldAsString(dispByKywd));
          //      mainWrapper.implementations += 1;
            /*} catch (DataFormatException ex) {
                Exceptions.printStackTrace(ex);
            } catch (NoSuchFieldException ex) {
                Exceptions.printStackTrace(ex);
            }*/
        //}
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PropertiesDialog dialog = new PropertiesDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private Feature _feature = null;
    private Part _part = null;
    private Part _compPart = null;
    private DevicePartWrapper _dpw;
    private WorkspaceFrame _wsFrame;
    private WorkspaceScene _wsScene;

    private String _partObjectKeyword;
    private String _secondaryObjectKeyword;
    private String _typeFieldKeyword;
    private String _displayByKeyword;

    private final String _noneKeyword = "none";

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField comppartLefty;
    private javax.swing.JTextField comppartNameField;
    private javax.swing.JTextField comppartRighty;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField partsDescriptionField;
    private javax.swing.JTextField partsFormatField;
    private javax.swing.JTextField partsNameField;
    private javax.swing.JTextArea partsSequenceArea;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel sequenceLabel;
    private javax.swing.JScrollPane sequenceScrollPane;
    private javax.swing.JTextArea sequenceTextArea;
    // End of variables declaration//GEN-END:variables

}
