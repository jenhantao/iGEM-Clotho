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

package org.clothocad.viewer.plasmideditortc;

import javax.swing.SwingUtilities;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.Plasmid;
import org.clothocore.api.plugin.ClothoViewer;


/**
 *
 * @author J. Christopher Anderson
 */
public class Connect implements ClothoViewer {

    @Override
    public void launch(ObjBase o) {
        if(!Collector.isConnected()) {
            return;
        }
        if(!o.getType().equals(ObjType.PLASMID)) {
            return;
        }
        final Plasmid aplasmid = (Plasmid) o;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                EditorTopComponent ntc = new EditorTopComponent();
                ntc.open();
                ntc.setPlasmid(aplasmid);
                ntc.requestActive();
                ntc.setName(aplasmid.getName());
            }
        });
    }

    @Override
    public void close() {
    }

    @Override
    public void init() {
    }

/*-----------------
     variables
 -----------------*/

}
