/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.spectacles;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.clothocad.tool.spectacles.ui.frames.NotepadWindow;
import org.clothocad.tool.spectacles.ui.frames.WorkspaceFrame;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author jenhan
 */
public class ViewSwitcher {

    private boolean _isTC;
    private TopComponent _tcNotepad;
    private TopComponent _tcView;
    private JFrame _frameview;
    private JFrame _notepad;

    public ViewSwitcher(WorkspaceFrame wf) {
        _frameview = wf;
        _notepad = wf.getNotepad();
        _isTC = true;



        final JComponent guiContentPane = (JComponent) _frameview.getContentPane();
        final JMenuBar menu = _frameview.getJMenuBar();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                _tcView = new TopComponent() {

                    @Override
                    public boolean canClose() {
                        _tcNotepad.close();
                        return super.canClose();
                    }
                };
                _tcView.setLayout(new BorderLayout());
                JScrollPane sp = new JScrollPane(guiContentPane);
                _tcView.add(menu, BorderLayout.NORTH);
                _tcView.add(sp, BorderLayout.CENTER);
                _tcView.setName("Spectacles");
                _tcView.open();
                _tcView.requestActive();

                _tcNotepad = new TopComponent() {

                    @Override
                    public void open() {
                        Mode m = WindowManager.getDefault().findMode("output");
                        if (m != null) {
                            m.dockInto(this);
                        }
                        super.open();
                    }
                };
                _tcNotepad.setLayout(new BorderLayout());
                JScrollPane sp2 = new JScrollPane(_notepad.getContentPane());
                _tcNotepad.add(_notepad.getJMenuBar(), BorderLayout.NORTH);
                _tcNotepad.add(sp2, BorderLayout.CENTER);
                _tcNotepad.setName("Notepad");
                _tcNotepad.open();
                _tcNotepad.requestActive();


            }
        });
        _frameview.dispose();
        wf.dispose();
        _notepad.dispose();
        _isTC = true;

    }

    public void switchViews() {
        if (_isTC) {
            _frameview=new JFrame();
            _notepad= new JFrame();
            Component[] components = _tcView.getComponents();
            _frameview.setContentPane((Container) components[1]);
            _frameview.setJMenuBar((JMenuBar) components[0]);
            _frameview.pack();
            _isTC = false;
            _tcView.close();

            Component[] components2 = _tcNotepad.getComponents();
            _notepad.setContentPane((Container) components2[1]);
            _notepad.setJMenuBar((JMenuBar) components2[0]);
            _notepad.pack();
            _tcNotepad.close();
            _frameview.setVisible(true);

            _notepad.setVisible(true);

        } else {
            final JComponent guiContentPane = (JComponent) _frameview.getContentPane();
            final JMenuBar menu = _frameview.getJMenuBar();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    _tcView = new TopComponent() {

                        @Override
                        public boolean canClose() {
                            _tcNotepad.close();
                            return super.canClose();
                        }
                    };
                    _tcView.setLayout(new BorderLayout());
                    JScrollPane sp = new JScrollPane(guiContentPane);
                    _tcView.add(menu, BorderLayout.NORTH);
                    _tcView.add(sp, BorderLayout.CENTER);
                    _tcView.setName("Spectacles");
                    _tcView.open();
                    _tcView.requestActive();

                    _tcNotepad = new TopComponent() {

                        @Override
                        public void open() {
                            Mode m = WindowManager.getDefault().findMode("output");
                            if (m != null) {
                                m.dockInto(this);
                            }
                            super.open();
                        }
                    };
                    _tcNotepad.setLayout(new BorderLayout());
                    JScrollPane sp2 = new JScrollPane(_notepad.getContentPane());
                    _tcNotepad.add(_notepad.getJMenuBar(), BorderLayout.NORTH);
                    _tcNotepad.add(sp2, BorderLayout.CENTER);
                    _tcNotepad.setName("Notepad");
                    _tcNotepad.open();
                    _tcNotepad.requestActive();


                }
            });
            _frameview.dispose();
            _notepad.dispose();
            _isTC = true;
        }
    }
}
