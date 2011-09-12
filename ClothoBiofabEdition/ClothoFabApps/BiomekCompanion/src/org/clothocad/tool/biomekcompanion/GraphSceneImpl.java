/*
 * 
Copyright (c) 2010 The Regents of the University of California.
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

package org.clothocad.tool.biomekcompanion;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;



/**

 *

 * <at> author varbalak

 */

public class GraphSceneImpl extends GraphScene<GraphSceneImpl.Node, String> {

    private GraphScene _instanceScene;
    private Widget top;

    public GraphSceneImpl() {
        super();
        _instanceScene = this;
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);
        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {

            public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
                return ConnectorState.ACCEPT;
            }

            public void accept(Widget widget, Point point, Transferable transferable) {
                Object o = null;
                try {
                    o = transferable.getTransferData(DataFlavor.stringFlavor);
                } catch (Exception ex) {
                }

                GraphSceneImpl.Node node = new GraphSceneImpl.Node(widget);
                node.setComponentName(o == null ? "" : (String) o);
                if (node.getComponentName().equals("JPanel")) {
                    Widget w = GraphSceneImpl.this.addNode(node);
                    w.setPreferredLocation(widget.convertLocalToScene(point));
                }
            }
        }));

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());

    }

    private LayerWidget mainLayer;

    @Override
    protected Widget attachNodeWidget(GraphSceneImpl.Node node) {
        if (node.getComponentName().equals("JPanel")) {
            JPanel panel = new JPanel();
            top = new ComponentWidget(this, panel);
            top.setBorder(BorderFactory.createResizeBorder(8));
            top.setPreferredSize(new Dimension(300, 300));
            top.setLayout(LayoutFactory.createAbsoluteLayout());
            top.getActions().addAction(ActionFactory.createSelectAction(new EatEventSelectProvider()));
            top.getActions().addAction(ActionFactory.createResizeAction());
            top.getActions().addAction(ActionFactory.createMoveAction());
            top.getActions().addAction(ActionFactory.createPopupMenuAction(new SceneMainMenu(this, top)));
            top.getActions().addAction(createObjectHoverAction());

            mainLayer.addChild(top);
            top.getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {
                public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
                    return ConnectorState.ACCEPT;
                }

                public void accept(final Widget widget, Point point, Transferable transferable) {
                    Object o = null;
                    try {
                        o = transferable.getTransferData(DataFlavor.stringFlavor);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (UnsupportedFlavorException ex) {
                        ex.printStackTrace();
                    }

                    GraphSceneImpl.Node node = new GraphSceneImpl.Node(widget);
                    node.setComponentName(o == null ? "" : (String) o);
                    if (node.getComponentName().equals("JButton")) {
                        Widget w = GraphSceneImpl.this.addNode(node);
                        w.setPreferredLocation(widget.convertLocalToScene(point));
                    }
                }
            }));
            return top;

        } else if (node.getComponentName().equals("JButton")) {
            JButton btn = new JButton();
            final Widget cw = new ComponentWidget(_instanceScene, btn);
            cw.setBorder(BorderFactory.createResizeBorder(5));
            cw.setPreferredSize(new Dimension(100, 21));
            cw.getActions().addAction(ActionFactory.createSelectAction(new EatEventSelectProvider()));
            cw.getActions().addAction(ActionFactory.createResizeAction());
            cw.getActions().addAction(ActionFactory.createMoveAction());
            cw.getActions().addAction(createObjectHoverAction());
            top.addChild(cw);
            return cw;
        }
        return null;
    }

    protected Widget attachEdgeWidget(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void attachEdgeSourceAnchor(String arg0, GraphSceneImpl.Node arg1, GraphSceneImpl.Node arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void attachEdgeTargetAnchor(String arg0, GraphSceneImpl.Node arg1, GraphSceneImpl.Node arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    class SceneMainMenu implements PopupMenuProvider, ActionListener {
        private static final String REFRESH_LAYOUT = "refreshLayout"; // NOI18N
        private GraphScene scene;
        private JPopupMenu menu;
        private Point point;
        private int nodeCount = 3;
        private Widget panel;

        public SceneMainMenu(GraphScene scene) {
            this.scene = scene;
            menu = new JPopupMenu("Scene Menu");
            JMenuItem item;
            item = new JMenuItem("Refresh Layout");
            item.setActionCommand(REFRESH_LAYOUT);
            item.addActionListener(this);
            menu.add(item);
        }

        private SceneMainMenu(GraphSceneImpl aThis, Widget componentWidget) {
            this(aThis);
            this.panel = componentWidget;
        }

        public JPopupMenu getPopupMenu(Widget widget, Point point) {
            this.point = point;
            return menu;
        }

        public void actionPerformed(ActionEvent e) {
            if (REFRESH_LAYOUT.equals(e.getActionCommand())) {
                panel.setPreferredSize(null);
                panel.setPreferredSize(new Dimension(400, 400));
                panel.revalidate();
                scene.revalidate();
            }
        }
    }

    private class EatEventSelectProvider implements SelectProvider {

        public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return false;
        }

        public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return true;
        }

        public void select(Widget widget, Point localLocation, boolean invertSelection) {
            System.out.println();
        }
    }



    class Node {
        private Widget parent;
        private String componentName;

        public Node(Widget parent) {
            this.parent = parent;
        }

        public Widget getWidget() {
            return parent;
        }

        public void setComponentName(String name) {
            componentName = name;
        }

        public String getComponentName() {
            return componentName;
        }
    }
}