/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.primerdesigner;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.clothocore.api.data.Annotation;
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
    }

    public String getSequence() {
        return _sequence;
    }

    public void generatePrimers(String seq, Double tm, int length, String insert1, String insert2, String spacer1, String spacer2) {
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
                    _tcView.setName("Sequence View");
                    _tcView.open();
                    _tcView.requestActive();

                }
            });
            _frameView.dispose();
            isTC = true;
        }

    }
    private String _sequence;
    private JFrame _frameView;
    private boolean isTC;
    private TopComponent _tcView;
}
