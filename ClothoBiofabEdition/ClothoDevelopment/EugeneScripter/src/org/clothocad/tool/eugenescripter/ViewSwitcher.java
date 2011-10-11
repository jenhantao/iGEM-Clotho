/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.eugenescripter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.openide.windows.TopComponent;

/**
 *
 * @author jenhan
 */
public class ViewSwitcher {

    private boolean _isTC;
    private TopComponent _tcView;
    private JFrame _frameview;

    public ViewSwitcher(eugeneFrame wf) {
        _frameview = wf;
        _isTC = true;



        final JComponent guiContentPane = (JComponent) _frameview.getContentPane();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                _tcView = new TopComponent();
                _tcView.setLayout(new BorderLayout());
                JScrollPane sp = new JScrollPane(guiContentPane);
                _tcView.add(sp, BorderLayout.CENTER);
                _tcView.setName("Eugene Scriptor");
                _tcView.open();
                _tcView.requestActive();




            }
        });
        _frameview.dispose();
        wf.dispose();
        _isTC = true;

    }

    public void switchViews() {
        if (_isTC) {
            _frameview = new JFrame();
            Component[] components = _tcView.getComponents();
            _frameview.setContentPane((Container) components[0]);
            _frameview.pack();
            _isTC = false;
            _tcView.close();

            _frameview.setVisible(true);

        } else {
            final JComponent guiContentPane = (JComponent) _frameview.getContentPane();
//            final JMenuBar menu = _frameview.getJMenuBar();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    _tcView = new TopComponent();
                    _tcView.setLayout(new BorderLayout());
                    JScrollPane sp = new JScrollPane(guiContentPane);
//                    _tcView.add(menu, BorderLayout.NORTH);
                    _tcView.add(sp, BorderLayout.CENTER);
                    _tcView.setName("Eugene Scriptor");
                    _tcView.open();
                    _tcView.requestActive();

                }
            });
            _frameview.dispose();
            _isTC = true;
        }
    }

    public void close() {
        if (_tcView != null) {
            if (_isTC) {
                _tcView.close();
            }
        }
    }
}
