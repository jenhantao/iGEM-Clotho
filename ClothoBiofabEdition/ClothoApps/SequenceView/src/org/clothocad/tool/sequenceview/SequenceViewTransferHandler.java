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

/* FIXME
import clothodata.ClothoPartsData;
import clothodata.ClothoTransferData;
import clothodata.ClothoTransferHandler;
import clothodata.PoBoLDataStructure;
import clothodata.PoBoLField;
import clothodata.PoBoLFieldEnum;
import clothodata.PoBoLObject;
import clothodata.PoBoLObjectEnum;
 * */
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.text.BadLocationException;
//commented
//import org.clothocad.core.ClothoTool;
//import org.clothocad.databaseio.Datum;

import org.clothocore.util.dialog.ClothoDialogBox;
//import org.clothocad.util.dragndrop.ClothoTransferData;
//import org.clothocad.util.dragndrop.ClothoTransferHandler;

/**
 * A class for enabling drag and drop features in the Sequence View Window
 * @author Matthew Johnson
 */
public class SequenceViewTransferHandler //extends ClothoTransferHandler //commented
{

    public SequenceViewTransferHandler (SequenceViewManager svm) {
        //commented
      //  super(svm);
    }
    
    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    //// 
    /*
    @Override
    public boolean canImport(TransferSupport support) {
        DataFlavor[] flavors = support.getTransferable().getTransferDataFlavors();
        
        // supports strings, plain text, files, and ClothoTransferData
        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(DataFlavor.stringFlavor) ||
                    flavors[i].equals(DataFlavor.getTextPlainUnicodeFlavor()) ||
                    flavors[i].equals(ClothoTransferData.clothoTransferDataFlavor) ||
                    flavors[i].equals(DataFlavor.javaFileListFlavor)) {
                return true;
            }
        }

        // returns false if no data flavor is matched
        return false;
    }
    
    @Override
    protected Transferable createTransferable(JComponent c) {
        JTextPane pane = (JTextPane) c;
        return new StringSelection(pane.getSelectedText());
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int action) {
        JTextPane pane = (JTextPane) c;
        if (action == MOVE) {
            pane.replaceSelection("");
        }
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }
    
    @Override
    public boolean importData (TransferHandler.TransferSupport info) {
        JTextPane seqViewPane = (JTextPane) info.getComponent();
        String text = null;
        String message = "";
        
        // For ClothoTransferData
        if (info.isDataFlavorSupported(ClothoTransferData.clothoTransferDataFlavor)) {
            try {
                //ClothoTransferData transfer = (ClothoTransferData) info.getTransferable();
                ArrayList data = (ArrayList) info.getTransferable().getTransferData(ClothoTransferData.clothoTransferDataFlavor);
                String sender = data.get(0).getClass().getName();
//                if (transfer.isPartData()) {
//                    ClothoPartsData partData = (ClothoPartsData) data.get(0);
//                    text = (String) partData.get_sequence();
//                    message = partData.get_partNickname() + " imported into the SequenceView";
//                }
                
                // Importing sequences from Enzyme and Feature Libraries
                if (sender.equals("tool.enzymelibrary.EnzymeLibraryTool") ||
                   sender.equals("tool.featureslibrary.FeatureLibraryTool")) {
                   text = (String) data.get(1);
                }
                
                // Importing sequences from the Plate Manager
                else if (sender.equals("clothoplates.ClothoPlateConnection")) {
                    /*
                    String label = (String) data.get(1);
                    //Matt - you can get the unique id with the following line of code....
                    String ui = (String) data.get(3);
                    PoBoLDataStructure pobolData = (PoBoLDataStructure) data.get(2);

                    String vectorSequence = "";
                    String insertSequence = "";
                    //FIXME
                    // Change to use "lookup" method in PoBoLDataStructure instead
                    // of the complicated code here.
                    
                    // Use Label on Sample Table to get DNA ID for the DNA Table
                    PoBoLObject sampleTable = pobolData.getObjectwithEnum(PoBoLObjectEnum.Samples);
                    //PoBoLField sampleLabelField = pobolData.getFieldwithEnum(PoBoLFieldEnum.Label, PoBoLObjectEnum.Samples);
                    PoBoLField sampleUIField = pobolData.getFieldwithEnum(PoBoLFieldEnum.UniqueIdentifier, PoBoLObjectEnum.Samples);
                    PoBoLField sampleDNAField = pobolData.getFieldwithEnum(PoBoLFieldEnum.DNARef, PoBoLObjectEnum.Samples);
                    //Hashtable data1 = pobolData.getObjectInfo(sampleTable.getDBTrans(), sampleLabelField.getDBTrans(), label);
                    Hashtable data1 = pobolData.getObjectInfo(sampleTable.getDBTrans(), sampleUIField.getDBTrans(), ui);
                    if (data1 != null) {
                        String dnaID = (String) data1.get(sampleDNAField.getStringName());
                        //System.out.println("DNA ID: " + dnaID);

                        // Use DNA ID on DNA Table to get the Vector ID for the Biobrick Table
                        PoBoLObject dnaTable = pobolData.getObjectwithEnum(PoBoLObjectEnum.DNA);
                        PoBoLField dnaUniqueIDField = pobolData.getFieldwithEnum(PoBoLFieldEnum.UniqueIdentifier, PoBoLObjectEnum.DNA);
                        PoBoLField dnaVectorField = pobolData.getFieldwithEnum(PoBoLFieldEnum.Vector, PoBoLObjectEnum.DNA);
                        Hashtable data2 = pobolData.getObjectInfo(dnaTable.getDBTrans(), dnaUniqueIDField.getDBTrans(), dnaID);
                        if (data2 != null) {
                            String vectorID = (String) data2.get(dnaVectorField.getStringName());
                            
                            //System.out.println("Vector ID: " + vectorID);
                            
                            // Use the Vector ID on the Biobrick Table to get the Sequence data
                            PoBoLObject biobrickTable = pobolData.getObjectwithEnum(PoBoLObjectEnum.BioBrick);
                            PoBoLField biobrickUniqueIDField = pobolData.getFieldwithEnum(PoBoLFieldEnum.UniqueIdentifier, PoBoLObjectEnum.BioBrick);
                            PoBoLField biobrickVectorField = pobolData.getFieldwithEnum(PoBoLFieldEnum.Sequence, PoBoLObjectEnum.BioBrick);   
                            Hashtable data3 = pobolData.getObjectInfo(biobrickTable.getDBTrans(), biobrickUniqueIDField.getDBTrans(), vectorID);
                            if (data3 != null) {
                                vectorSequence = (String) data3.get(biobrickVectorField.getStringName());
               
                                //text = vectorSequence;
                                //message = label + " imported to Sequence View";
                                message = "sample " + label + " with unique id " + ui + "\nVector imported Sequence View\n";
                            }
                            else {
                                System.out.println("ERROR: Data3 null!");
                            }
                        }

                        else {
                            System.out.println("ERROR: Data2 null!");
                        }

                        PoBoLField dnaInsertField = pobolData.getFieldwithEnum(PoBoLFieldEnum.Insert, PoBoLObjectEnum.DNA);
                        Hashtable data4 = pobolData.getObjectInfo(dnaTable.getDBTrans(), dnaUniqueIDField.getDBTrans(), dnaID);
                        if(data4 != null)
                        {
                           String insertID = (String) data2.get(dnaInsertField.getStringName());

                            //System.out.println("Vector ID: " + vectorID);

                            // Use the Vector ID on the Biobrick Table to get the Sequence data
                            PoBoLObject biobrickTable = pobolData.getObjectwithEnum(PoBoLObjectEnum.BioBrick);
                            PoBoLField biobrickUniqueIDField = pobolData.getFieldwithEnum(PoBoLFieldEnum.UniqueIdentifier, PoBoLObjectEnum.BioBrick);
                            PoBoLField biobrickVectorField = pobolData.getFieldwithEnum(PoBoLFieldEnum.Sequence, PoBoLObjectEnum.BioBrick);
                            Hashtable data5 = pobolData.getObjectInfo(biobrickTable.getDBTrans(), biobrickUniqueIDField.getDBTrans(), insertID);
                            if (data5 != null) {
                                insertSequence = (String) data5.get(biobrickVectorField.getStringName());

                                //text = vectorSequence;
                                //message = label + " imported to Sequence View";
                                message = message + "Insert imported Sequence View\n";
                            }
                            else {
                                System.out.println("ERROR: Data5 null!");
                            }
                        }

                        else {
                            System.out.println("ERROR: Data4 null!");
                        }
                        message = message + "(insert + vector)\n";
                        text = insertSequence + vectorSequence;
                    }
                    else {
                        System.out.println("ERROR: Data1 null!");
                    }//uncommented
                }
                
                
                // Importing sequences from the Part Manager
                else if (sender.equals("tool.partsmanager.ClothoPartsManagerTool")) {
                    
                    Datum partData = (Datum) data.get(1);
                    String level = (String) data.get(2);

                    String name = "";
                    if(level.equals("BioBrick"))
                    {
                        name = partData.getFieldAsString("name");
                        message = "BioBrick " + name + " imported into the SequeceView";
                    }
                    if(level.equals("DNA"))
                    {
                        String insert = partData.getFieldAsDatum("insert").getFieldAsString("name");
                        String vector = partData.getFieldAsDatum("vector").getFieldAsString("name");
                        name = insert + "-" + vector;
                        message = "DNA " + name + " imported into the SequeceView";
                    }
                    if(level.equals("Sample"))
                    {
                        name = partData.getFieldAsString("label");
                        message = "Sample " + name + " imported into the SequeceView";
                    }

                    if (!message.isEmpty()) {
                        ((SequenceViewManager) _clothoTool).getCurrentSV().getSequenceView().getOutputTextArea().setText(message);
                    }
                    
                    // Breaks out of the loop after the ClothoPartsData is
                    // loaded
                    ((SequenceViewManager) _clothoTool).getCurrentSV().loadPart(partData, level, name);
                    return true;
                }
                
                // If it fails to recognize the sender, it will simply assume
                // it has been handed String data
                else {
                    text = (String) data.get(1);
                }      
            }
            catch (UnsupportedFlavorException ex) {
                System.out.println("Error in SequenceViewTransferHandler: " + ex);
            }
            catch (IOException ex) {
                System.out.println("Error in SequenceViewTransferHandler: " + ex);
            }
            
            // Insertion into SequenceView
            if (text != null) {
                if (seqViewPane.getSelectionEnd() - seqViewPane.getSelectionStart() > 0) {
                           seqViewPane.replaceSelection(text);
                }
                else {
                    int pos = seqViewPane.getCaretPosition();
                    try {
                        seqViewPane.getDocument().insertString(pos, text, null);
                    }
                    catch (BadLocationException ex) {
                        System.out.println("Error in SequenceViewTransferHandler: " + ex);
                    }
                }
                
                if (!message.isEmpty()) {
                    ((SequenceView) getConnection()).getSequenceView().getOutputTextArea().setText(message);
                }
                
                return true;
            }
            else {
                return false;
            }
        }
        
        // For text-only data
        else if (info.isDataFlavorSupported(DataFlavor.stringFlavor) ||
                info.isDataFlavorSupported(DataFlavor.getTextPlainUnicodeFlavor())) {
            try {
                DataFlavor[] flavors = {DataFlavor.stringFlavor, DataFlavor.getTextPlainUnicodeFlavor()};
                text = (String) info.getTransferable().getTransferData(DataFlavor.selectBestTextFlavor(flavors));
                
                // Insertion into SequenceView
                if (text != null) {
                    if (seqViewPane.getSelectionEnd() - seqViewPane.getSelectionStart() > 0) {
                               seqViewPane.replaceSelection(text);
                    }
                    else {
                        int pos = seqViewPane.getCaretPosition();                  
                        try {
                            seqViewPane.getDocument().insertString(pos, text, null);
                        }
                        catch (BadLocationException ex) {
                            System.out.println("Error in SequenceViewTransferHandler: " + ex);
                        }
                    }
                    return true;
                }
            }
            catch (UnsupportedFlavorException ex) {
                System.out.println("Error in SequenceViewTransferHandler: " + ex);
            }
            catch (IOException ex) {
                System.out.println("Error in SequenceViewTransferHandler: " + ex);
            }
            
            return false;
        }
        
        // For loading files dragged into the Sequence View window
        else if (info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            try {
                ArrayList fileList = new ArrayList ((java.util.List) info.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                java.io.File f = new java.io.File(fileList.get(0).toString());
                
                // Sends the file to the Sequence View to be loaded
                SequenceViewManager svm = (SequenceViewManager) _clothoTool;
                svm.getCurrentSV().loadSequence(f);
                
                return true;
            }
            catch (UnsupportedFlavorException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            
            return false;
        }
        
        else {
            ClothoDialogBox db = new ClothoDialogBox("Error", "Data type is not supported");
            db.show_Dialog(javax.swing.JOptionPane.ERROR_MESSAGE);
            System.out.println("Incompatible DataFlavor passed to SequenceView");
            return false;
        }
    }
    */
}
