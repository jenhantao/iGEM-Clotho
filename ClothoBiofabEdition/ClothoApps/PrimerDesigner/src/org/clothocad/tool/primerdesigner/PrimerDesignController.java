/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.primerdesigner;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.clothocore.api.data.Annotation;
import org.clothocore.api.data.Feature;
import org.clothocore.api.data.NucSeq;
import org.openide.windows.TopComponent;

/**
 *
 * @author Jenhan Tao
 * Performs primer design calculations
 */
public class PrimerDesignController {

    /**
     * constructor links controller to the view
     * @param s
     */
    public PrimerDesignController(DesignFrame d) {
        isTC = false;
        _frameView = d;
        _ns = new NucSeq("");
    }

    public String getSequence() {
        return _sequence;
    }

    /**
     * 
     * Primers of length +/-3 of target length are generated
     * @param seq
     * @param tm
     * @param length
     * @param insert1
     * @param insert2
     * @param spacer1
     * @param spacer2
     */
    public void generatePrimers(String seq, Double tm, int length, String insert1, String insert2, String spacer1, String spacer2) {
//        System.out.println("Sequence: " + seq);
//        System.out.println("Target tm: " + tm);
//        System.out.println("Target length: " + length);
//        System.out.println("Inserts: " + insert1 + ", " + insert2);
//        System.out.println("Spacers: " + spacer1 + ", " + spacer2);
        ArrayList<String> fwdSequences = new ArrayList<String>();
        ArrayList<String> revSequences = new ArrayList<String>();
        if (spacer1 == null) {
            spacer1 = "";
        }
        if (spacer2 == null) {
            spacer2 = "";
        }
        if (seq.length() < 60) {
            String[] yesNoOpt = {"Yes", "No"};
            if (javax.swing.JOptionPane.showOptionDialog(new JFrame(), "Template is less than 60 basepairs in length.\nProceed anyways?", "Primer Designer: Warning", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, yesNoOpt, yesNoOpt[1]) == 1) {
                return;
            }
            if (seq.length() < 20) {
                JOptionPane.showMessageDialog(new JFrame(), "Sequence is too short to generate primers", "Primer Designer: Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        if (length < 15) {
            String[] yesNoOpt = {"Yes", "No"};
            if (javax.swing.JOptionPane.showOptionDialog(new JFrame(), "Preferred primer length may be too short to allow for specific annealing.\nProceed anyways?", "Primer Designer: Warning", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, yesNoOpt, yesNoOpt[1]) == 1) {
                return;
            }
            if (length < 4) {
                length = 4; //min target length allowed
            }
        }
        if (spacer1.length() > 5 || spacer2.length() > 5) {
            String[] yesNoOpt = {"Yes", "No"};
            if (javax.swing.JOptionPane.showOptionDialog(new JFrame(), "Spacers seem to be large.\nProceed anyways?", "Primer Designer: Warning", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, yesNoOpt, yesNoOpt[1]) == 1) {
                return;
            }
        }
        String revPrimer = "";
        String fwdPrimer = "";
        //generates primer sequences with length +/-3 of target length excluding the insert length
        for (int i = length - 3; i <= length + 3; i++) {
            fwdPrimer = this.complementSequence(seq.substring(0, i));
            if (!insert1.equalsIgnoreCase("none")) {
                Feature afeat = Feature.retrieveByName(insert1);
                fwdPrimer = this.complementSequence(afeat.getSeq().toString()) + fwdPrimer;
            }
            fwdPrimer = spacer1 + fwdPrimer;
            fwdSequences.add(fwdPrimer);
//            _ns.changeSeq(seq.substring(seq.length() - i));
//            revPrimer = _ns.revComp();
            revPrimer = this.flip(seq.substring(seq.length() - i));
            if (!insert2.equalsIgnoreCase("none")) {
                Feature afeat = Feature.retrieveByName(insert2);
//                revPrimer = afeat.getSeq().revComp() + revPrimer;
                revPrimer = this.flip(afeat.getSeq().toString()) + revPrimer;
            }
            revPrimer = spacer2 + revPrimer;
            revSequences.add(revPrimer);
            System.out.println("fwd: " + fwdPrimer);
            System.out.println("rev: " + revPrimer);
        }
        PrimerResultFrame prf = new PrimerResultFrame(this, fwdSequences, revSequences);
        prf.pack();
        prf.setVisible(true);
    }

    /**
     * Complents input string
     * @param s
     * @return
     */
    public String complementSequence(String s) {
        if (s != null) {
            _ns.changeSeq(s);
            String seq = _ns.revComp();
            String temp = "";
            for (int j = seq.length() - 1; j > -1; j--) {
                temp = temp + seq.charAt(j);
            }
            return temp;
        }
        return null;
    }

    /**
     * flips input string
     * @param s
     * @return
     */
    public String flip(String s) {
        if (s != null) {
            String temp = "";
            for (int j = s.length() - 1; j > -1; j--) {
                temp = temp + s.charAt(j);
            }
            return temp;
        }
        return null;
    }

    /**
     * Checks sequence for restriction sites.
     * @param seq sequences being scanned
     * @return a hashmap containing restriction sites indices, key is start, value is end
     */
    public HashMap<Integer, Integer> checkForRESites() {
        if (_sequence == null) {
            return null;
        }
        NucSeq ns = new NucSeq(_sequence);
        HashMap<Integer, Integer> toReturn = new HashMap<Integer, Integer>();
        HashSet<Annotation> hs = ns.getAnnotations();

        for (Annotation an : hs) {
            if (an.getFeature().getSearchTags().contains("restriction enzyme")) {
                toReturn.put(an.getStart(), an.getEnd());
            }
        }
        if (toReturn.isEmpty()) {
            toReturn = null;
        }
        return toReturn;
    }

    public Double calcMeltingTemp(String seq) {
        _ns.changeSeq(seq);
        Double toReturn = _ns.meltingTemp();
        return toReturn;
    }

    public Double calcGCContent(String seq) {
        _ns.changeSeq(seq);
        Double toReturn = _ns.gcContent()[1];
        return toReturn;
    }

    /**
     * Calculates delta g value for input string, representing a dna sequence, using nearest neighbor method
     * Delta g calculations taken from "A unified view of polymer, dumbbell, and oligonucleotide DNA nearest-neighbor "
     * @param s
     * @return
     */
    public Double calcDeltaG(String s) {
        Double toReturn = 0.0;
        if (s != null) {
            s = s.toUpperCase();
            if (s.startsWith("C") || s.startsWith("G")) {
                toReturn = toReturn + 0.98;
            } else {
                toReturn = toReturn + 1.03;
            }
            if (s.endsWith("C") || s.endsWith("G")) {
                toReturn = toReturn + 0.98;
            } else {
                toReturn = toReturn + 1.03;
            }
            for (int i = 0; i < s.length() - 1; i++) {
                String token = s.substring(i, i + 2);
                if (token.equals("AA") || token.equals("TT")) {
                    toReturn = toReturn - 1.00;
                } else if (token.equals("AT")) {
                    toReturn = toReturn - 0.88;
                } else if (token.equals("TA")) {
                    toReturn = toReturn - -0.58;
                } else if (token.equals("CA") || token.equals("AC")) {
                    toReturn = toReturn - 1.45;
                } else if (token.equals("GT") || token.equals("TG")) {
                    toReturn = toReturn - 1.44;
                } else if (token.equals("CT") || token.equals("TC")) {
                    toReturn = toReturn - 1.28;
                } else if (token.equals("GA") || token.equals("AG")) {
                    toReturn = toReturn - 1.30;
                } else if (token.equals("CG")) {
                    toReturn = toReturn - 2.17;
                } else if (token.equals("GC")) {
                    toReturn = toReturn - 2.24;
                } else if (token.equals("GG") || token.equals("CC")) {
                    toReturn = toReturn - 1.84;
                }
            }
            return toReturn;
        }

        return 0.00;
    }
    public void checkForDimers(PrimerResultFrame rpf) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void switchViews() {
        if (isTC) {
            Component[] components = _tcView.getComponents();
//            for (int i = 0; i < components.length; i++) {
//                System.out.println(components[i]);
//            }
            _frameView = new JFrame();
            _frameView.setContentPane((Container) components[1]);
            _frameView.setJMenuBar((JMenuBar) components[0]);
            _frameView.pack();
            _frameView.setVisible(true);
            isTC = false;
            _tcView.close();
        } else {
            final JComponent guiContentPane = (JComponent) _frameView.getContentPane();
//            JRootPane guiRootPane = _frameView.getRootPane();
            final JMenuBar menu = _frameView.getJMenuBar();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    _tcView = new TopComponent();
                    _tcView.setLayout(new BorderLayout());
                    JScrollPane sp = new JScrollPane(guiContentPane);
                    _tcView.add(menu, BorderLayout.NORTH);
                    _tcView.add(sp, BorderLayout.CENTER);
                    _tcView.setName("Primer Designer");
                    _tcView.open();
                    _tcView.requestActive();

                }
            });
            _frameView.dispose();
            isTC = true;
        }

    }

    void close() {
        if (isTC) {
            _tcView.close();
        } else {
            _frameView.dispose();
        }
    }
    private String _sequence;
    private JFrame _frameView;
    private boolean isTC;
    private TopComponent _tcView;
    private NucSeq _ns;


}
