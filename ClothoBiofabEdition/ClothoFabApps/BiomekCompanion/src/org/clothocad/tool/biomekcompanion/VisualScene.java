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

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Collection;
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.data.ObjLink;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.Plate;
import org.clothocore.api.dnd.ObjBaseObserver;
import org.clothocore.api.dnd.RefreshEvent;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author jcanderson_Home
 */
public class VisualScene extends GraphScene {

    public VisualScene() {
        Scene scene = this;

        layer = new LayerWidget (scene);
        scene.addChild(layer);

        
        scene.getActions().addAction (ActionFactory.createZoomAction ());
        scene.getActions().addAction (ActionFactory.createPanAction ());
        WidgetAction hoverAction = ActionFactory.createHoverAction (new MyHoverProvider ());
        WidgetAction popupMenuAction = ActionFactory.createPopupMenuAction ((PopupMenuProvider) new DeckPosPopup ());
        scene.getActions().addAction (hoverAction);

        //Put in the P1 through P8 positions
        Image img = ImageUtilities.loadImage("org/clothocad/tool/biomekcompanion/box.png", true);
        for(int col=0; col<4; col++) {
            for(int row =0; row <2; row++) {
                int index = 2*(col)+row+1;
                String pos = "P" + index;
                DeckPosition widget = new DeckPosition(pos);
                widget.setImage(img);
                widget.setPreferredLocation (new Point (200*col+10, 150*row+10));
                widget.setLabel(pos);
                layer.addChild (widget);
                widget.getActions().addAction(ActionFactory.createAcceptAction(new DropListener()));
                widget.getActions().addAction (popupMenuAction);
            }
        }

        //Put in the transient Collection for gathering plates and a listener
        _coll = new Collection();
        _obo = new ObjBaseObserver() {
            @Override
            public void update(ObjBase obj, RefreshEvent evt) {
                System.out.println("_coll had a refresh event");
                if(!evt.isCondition(RefreshEvent.Condition.COLLECTION_ADD)) {
                    System.out.println("refresh event not the right type");
                    return;
                }
                if(!obj.getType().equals(ObjType.COLLECTION)) {
                    System.out.println("refresh event not on a collection");
                    return;
                }

                //Remove all the plate widgets for the Collection
                for(PlateWidget pw : availablePlates) {
                    layer.removeChild(pw);
                }
                availablePlates.clear();

                Collection acoll = (Collection) obj;
                ArrayList<Plate> plates = (ArrayList<Plate>) acoll.getAll(ObjType.PLATE);
                for(Plate aplate : plates) {
                    PlateWidget pw = new PlateWidget(aplate);
                    pw.setPreferredLocation (nextAvailable);
                    layer.addChild(pw);
                    availablePlates.add(pw);

                    //Update the next available position
                    nextAvailable.x+=200;
                    nextAvailable.y+=100;
                }
                VisualScene.this.validate();
                VisualScene.this.repaint();
            }
        };
        _coll.isObservedBy(_obo);

        _coll.addObject(Collector.getPlate("409db53b40d1484d856353318111f533"));
    }

    private class DropListener implements AcceptProvider {
        @Override
        public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
            System.out.println("isacceptable called");
            return ConnectorState.ACCEPT;
        }

        @Override
        public void accept(Widget widget, Point point, Transferable t) {
            try {
                PlateWidget pw = (PlateWidget) widget;
                System.out.println(pw.name);
            }catch(Exception err) {
                System.out.println("It's not a platewidget");
            }
        }
    }
    private class DeckPosition extends IconNodeWidget {
        //VARIABLES//
        String position;
        Point locationOnScreen;
        DeckObject obj;

        public DeckPosition(String pos) {
            super(VisualScene.this);
            position = pos;
        }

        public void addDeckObject(DeckObject o) {
            if(obj==null) {
                obj = o;
                //Repaint things
            }
        }
    }

    private abstract class DeckObject extends IconNodeWidget {
        //VARIABLES//
        String name;

        public DeckObject(String n) {
            super(VisualScene.this);
            name = n;
        }
        public String getName() {
            return name;
        }
    }

    private class PlateWidget extends DeckObject {
        //VARIABLES//
        Plate plate;
        public PlateWidget(Plate aplate) {
            super(aplate.getName());
            plate = aplate;
            Image img = ImageUtilities.loadImage("org/clothocad/tool/biomekcompanion/plate.png", true);
            setImage(img);
            getActions().addAction (ActionFactory.createMoveAction ());
            getActions().addAction(ActionFactory.createAcceptAction(new DropListener()));
            setLabel(aplate.getName());
        }
    }

    private class MyHoverProvider implements TwoStateHoverProvider {

        @Override
        public void unsetHovering(Widget widget) {
            if (widget != null) {
                widget.setBackground (Color.WHITE);
                widget.setForeground (Color.BLACK);
            }
        }

        @Override
        public void setHovering(Widget widget) {
            if (widget != null) {
                widget.setBackground (new Color (52, 124, 150));
                widget.setForeground (Color.WHITE);
            }
        }

    }

    private class DeckPosPopup implements PopupMenuProvider {
        //VARIABLES//

        @Override
        public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
            JPopupMenu menu = new JPopupMenu ();
            final DeckPosition dp = (DeckPosition) widget;
            if(dp.obj==null) {
                JMenuItem addPlateItem = new JMenuItem("Add Plate");
                addPlateItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            //Throw up a dialog and get user to select the collection stored as 'chosen'
                            ArrayList<ObjLink> allColl = Collector.getAllLinksOf(ObjType.PLATE);
                            if(allColl.isEmpty()) {
                                return;
                            }
                            Object[] allNames = allColl.toArray();
                            ObjLink link = (ObjLink) JOptionPane.showInputDialog(null, "Choose plate", "Plate",
                                JOptionPane.INFORMATION_MESSAGE, null, allNames, allNames[0]);
                            if(link!=null) {
                                Plate chosen = Collector.getPlate(link.uuid);
                                System.out.println("adding plate " + chosen.getName());
                                PlateWidget pw = new PlateWidget(chosen);
                                dp.addChild(pw);
                                dp.obj = pw;
                                validate();
                                repaint();
                            }
                        } catch(Exception err) {
                            err.printStackTrace();
                        }
                    }
                });
                menu.add(addPlateItem);
            } else {
                JMenuItem removeItem = new JMenuItem("Remove Object");
                removeItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                    }
                });
                menu.add(removeItem);
            }
            return menu;
        }

    }

    @Override
    protected Widget attachNodeWidget(Object node) {
        return null;
    }

    @Override
    protected Widget attachEdgeWidget(Object edge) {
        return null;
    }

    @Override
    protected void attachEdgeSourceAnchor(Object edge, Object oldSourceNode, Object newSourceNode) {

    }

    @Override
    protected void attachEdgeTargetAnchor(Object edge, Object oldTargetNode, Object newTargetNode) {

    }

    public Collection getCollection() {
        return _coll;
    }

    //VARIABLES

    private Collection _coll;   //Bundle of plates dropped into the Collection
    private ObjBaseObserver _obo;  //Listener for changes to plate list
    private LayerWidget layer;
    private Point nextAvailable = new Point(10,500);
    private ArrayList<PlateWidget> availablePlates = new ArrayList<PlateWidget>();
}