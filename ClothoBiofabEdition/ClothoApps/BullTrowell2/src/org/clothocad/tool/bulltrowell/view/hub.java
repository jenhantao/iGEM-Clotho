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
 * hub.java
 *
 * Created on Apr 2, 2010, 8:14:43 PM
 */
package org.clothocad.tool.bulltrowell.view;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;
import org.clothocad.tool.bulltrowell.connect;
import org.clothocad.tool.bulltrowell.interpreters.addComposite;
import org.clothocad.tool.bulltrowell.interpreters.addFeature;
import org.clothocad.tool.bulltrowell.interpreters.addOligos;
import org.clothocad.tool.bulltrowell.interpreters.addPart;
import org.clothocad.tool.bulltrowell.interpreters.addPlasmid;
import org.clothocad.tool.bulltrowell.interpreters.addPlate;
import org.clothocad.tool.bulltrowell.interpreters.addSample;
import org.clothocad.tool.bulltrowell.interpreters.addVector;
import org.clothocore.util.basic.ImageSource;
import org.clothocore.util.misc.BareBonesBrowserLaunch;

/**
 *
 * @author J. Christopher Anderson
 */
public class hub extends javax.swing.JFrame {

    /** Creates new form hub */
    public hub() {
        super("Bull Trowell");
        setIconImage(ImageSource.getTinyLogo());
        initComponents();
        if (!connect.playBullSound) {
            bullSounds.setVisible(false);
        }
        setVisible(true);
        _prefs = Preferences.userNodeForPackage(hub.class);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addParts = new javax.swing.JButton();
        addVectorsButton = new javax.swing.JButton();
        addOligosButton = new javax.swing.JButton();
        bullSounds = new javax.swing.JButton();
        addPlasmidButton = new javax.swing.JButton();
        compositeButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        addFeatures = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();
        numRowField = new javax.swing.JTextField();
        addSampleButton = new javax.swing.JButton();
        addPlateButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        addParts.setText("Basic Parts");
        addParts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPartsActionPerformed(evt);
            }
        });

        addVectorsButton.setText("Vectors");
        addVectorsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addVectorsButtonActionPerformed(evt);
            }
        });

        addOligosButton.setText("Oligos");
        addOligosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOligosButtonActionPerformed(evt);
            }
        });

        bullSounds.setText("Disable Bull Sounds");
        bullSounds.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bullSoundsActionPerformed(evt);
            }
        });

        addPlasmidButton.setText("Plasmids");
        addPlasmidButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPlasmidButtonActionPerformed(evt);
            }
        });

        compositeButton.setText("Composite Parts");
        compositeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compositeButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Quick Add");

        addFeatures.setText("Features");
        addFeatures.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFeaturesActionPerformed(evt);
            }
        });

        helpButton.setText("Help");
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButtonActionPerformed(evt);
            }
        });

        numRowField.setText("50");

        addSampleButton.setText("Sample");
        addSampleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSampleButtonActionPerformed(evt);
            }
        });

        addPlateButton.setText("Plate");
        addPlateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPlateButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addOligosButton)
                            .addComponent(addFeatures))
                        .addContainerGap(128, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addParts)
                            .addComponent(addVectorsButton)
                            .addComponent(addPlasmidButton)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addGap(13, 13, 13)
                                    .addComponent(numRowField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(compositeButton, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(helpButton)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addSampleButton)
                        .addContainerGap(139, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addPlateButton)
                        .addContainerGap(154, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bullSounds, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(numRowField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(helpButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addParts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addVectorsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compositeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addPlasmidButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addOligosButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addFeatures)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addSampleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addPlateButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bullSounds)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addPartsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPartsActionPerformed
        calcNumRows();
        new addPart();
    }//GEN-LAST:event_addPartsActionPerformed

    private void addVectorsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addVectorsButtonActionPerformed
        calcNumRows();
        new addVector();
    }//GEN-LAST:event_addVectorsButtonActionPerformed

    private void addOligosButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOligosButtonActionPerformed
        calcNumRows();
        new addOligos();
    }//GEN-LAST:event_addOligosButtonActionPerformed

    private void bullSoundsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bullSoundsActionPerformed
        connect._prefs.put("playBullSound", "false");
        bullSounds.setVisible(false);
        connect.playBullSound = false;
    }//GEN-LAST:event_bullSoundsActionPerformed

    private void addPlasmidButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPlasmidButtonActionPerformed
        calcNumRows();
        new addPlasmid();
    }//GEN-LAST:event_addPlasmidButtonActionPerformed

    private void compositeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compositeButtonActionPerformed
        calcNumRows();
        new addComposite();
    }//GEN-LAST:event_compositeButtonActionPerformed

    private void addFeaturesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFeaturesActionPerformed
        calcNumRows();
        new addFeature();
    }//GEN-LAST:event_addFeaturesActionPerformed

    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
        try {
            URL url = new URL("http://wiki.bu.edu/ece-clotho/index.php/Bull_Trowell");
            BareBonesBrowserLaunch.openURL(url.toString());
        } catch (MalformedURLException ex) {
        }
    }//GEN-LAST:event_helpButtonActionPerformed

    private void addSampleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSampleButtonActionPerformed
        calcNumRows();
        new addSample();
    }//GEN-LAST:event_addSampleButtonActionPerformed

    private void addPlateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPlateButtonActionPerformed
        calcNumRows();
        new addPlate();
    }//GEN-LAST:event_addPlateButtonActionPerformed

    private void calcNumRows() {
        try {
            String sval = numRowField.getText().trim();
            int rows = Integer.parseInt(sval);
            numrows = rows;
        } catch (Exception e) {
            numrows = 50;
        }
        numRowField.setText(Integer.toString(numrows));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new hub();
            }
        });
    }

    public static void putPreference(String tag, String value) {
        _prefs.put(tag, value);
    }

    public static String getPreference(String tag) {
        return _prefs.get(tag, "-1");
    }
    /**-----------------
    variables
    -----------------*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addFeatures;
    private javax.swing.JButton addOligosButton;
    private javax.swing.JButton addParts;
    private javax.swing.JButton addPlasmidButton;
    private javax.swing.JButton addPlateButton;
    private javax.swing.JButton addSampleButton;
    private javax.swing.JButton addVectorsButton;
    private javax.swing.JButton bullSounds;
    private javax.swing.JButton compositeButton;
    private javax.swing.JButton helpButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField numRowField;
    // End of variables declaration//GEN-END:variables
    private static Preferences _prefs;
    public static int numrows = 50;
}
