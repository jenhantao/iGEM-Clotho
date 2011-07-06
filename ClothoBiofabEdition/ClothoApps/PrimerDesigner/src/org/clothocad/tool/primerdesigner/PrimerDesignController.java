/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.primerdesigner;

import java.util.HashMap;
import java.util.HashSet;
import org.clothocore.api.data.Annotation;
import org.clothocore.api.data.NucSeq;

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
        _view = d;
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
        if (_sequence==null) {
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
            toReturn=null;
        }
        return toReturn;
    }
    private String _sequence;
    private DesignFrame _view;
}
