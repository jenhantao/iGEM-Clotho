package org.clothocore.widget.fabdash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Jenhan Tao
 */
public class FileMenuPopulator extends AbstractAction implements Presenter.Menu {

    @Override
    public JMenuItem getMenuPresenter() {
        JMenu m = new JMenu("New") {
        };
        JMenuItem partsItem = new JMenuItem("Part");
        partsItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("I added a part!");
            }
        });
        m.add(partsItem);

        JMenuItem oligoItem = new JMenuItem("Oligo");
        oligoItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("I added a oligo!");
            }
        });
        m.add(oligoItem);

        JMenuItem vectorItem = new JMenuItem("Vector");
        vectorItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("I added a vector!");
            }
        });
        m.add(vectorItem);

        JMenuItem plasmidItem = new JMenuItem("Plasmid");
        plasmidItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("I added a plasmid!");
            }
        });
        m.add(plasmidItem);

        JMenuItem featureItem = new JMenuItem("Feature");
        featureItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("I added a feature!");
            }
        });
        m.add(featureItem);

        JMenuItem sampleItem = new JMenuItem("Sample");
        sampleItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("I added a sample!");
            }
        });
        m.add(sampleItem);

        JMenuItem collectionItem = new JMenuItem("Collection");
        collectionItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("I added a collection!");
            }
        });
        m.add(collectionItem);
        return m;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
