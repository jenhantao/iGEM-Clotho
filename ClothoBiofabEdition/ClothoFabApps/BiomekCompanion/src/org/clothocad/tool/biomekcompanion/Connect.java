/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.biomekcompanion;

import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.Plate;
import org.clothocore.api.plugin.ClothoTool;
import org.clothocore.api.plugin.ClothoViewer;

/**
 *
 * @author jcanderson
 */
public class Connect implements ClothoTool {

    @Override
    public void launch(ObjBase o) {
        if(!o.getType().equals(ObjType.PLATE)) {
            return;
        }
        final Plate aplate = (Plate) o;

        launch();

    }

    @Override
    public void close() {
    }

    @Override
    public void init() {
    }

    @Override
    public void launch() {
        System.out.println("Launched called for biomek companion");
//        new CherryPicker().setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                DeckTopComponent ptc = new DeckTopComponent();
                ptc.setName("Biomek 3000 Deck");
                ptc.open();
                ptc.requestActive();
            }
        });
    }

}
