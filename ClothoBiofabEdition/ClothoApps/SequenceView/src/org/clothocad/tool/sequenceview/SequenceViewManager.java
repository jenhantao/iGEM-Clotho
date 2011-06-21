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

package org.clothocad.tool.sequenceview;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
//import org.clothocad.core.ClothoCore;
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.plugin.ClothoTool;
//commented
//import org.clothocad.core.ClothoTool;
import org.clothocore.util.basic.UnknownKeywordException;
//        clothocad.util.UnknownKeywordException; //commented

//import org.clothocad.util.help.ClothoHelp;
//import org.clothocad.util.highlight.ClothoHighlightData;

//import org.java.plugin.PluginManager;
//import org.java.plugin.registry.Extension;
//import org.java.plugin.registry.ExtensionPoint;
//import org.java.plugin.registry.PluginDescriptor;
//import org.openide.util.Exceptions;

/**
 *
 * @author Douglas Densmore
 * @author Roza Ghamari
 */
public class SequenceViewManager implements ClothoTool {


    public SequenceViewManager()
    {
        /*_sequenceViewArray = new ArrayList<SequenceView>();
        _sequenceViewArray.add(new SequenceView("SequenceView", "SequenceView", this, 0, _plugIns));
        _currentSequenceViewIndex = 0;
        _sequenceViewArray.get(_currentSequenceViewIndex).setTitle("Clotho: Sequence View (Address: " + _currentSequenceViewIndex + ") New Sequence");
        _help = new ClothoHelp();*/

    }

    public static void main(String[] args) {
        SequenceViewManager svm = new SequenceViewManager();
        svm.init();
        svm.launch();
    }

/*public void activate(){

        try {
        PluginManager manager;
        PluginDescriptor desc;

            manager = ClothoCore.getCore().get_plugin_manager();
            desc = manager.getPlugin("sequenceview").getDescriptor();


        _plugIns = new HashMap<String, SequenceViewPlugInInterface>();
        ExtensionPoint analyzerExtPoint =
            manager.getRegistry().getExtensionPoint(
                    desc.getId(), "SequenceViewPlugInExtensionPoint");
        for (Object e: analyzerExtPoint.getConnectedExtensions()) {
            Extension ext = (Extension) e;
            String name = ext.getParameter("name").valueAsString();
            try{
                manager.activatePlugin(
                        ext.getDeclaringPluginDescriptor().getId());
                // Get plug-in class loader.
                ClassLoader classLoader = manager.getPluginClassLoader(
                        ext.getDeclaringPluginDescriptor());
                // Load Tool class.
                Class analyzerCls = classLoader.loadClass(
                        ext.getParameter("class").valueAsString());
                // Create Tool instance.
                SequenceViewPlugInInterface analyzer = (SequenceViewPlugInInterface) analyzerCls.newInstance();
                _plugIns.put(name, analyzer);
            }
            catch(Exception ex){
                System.out.println("ERROR MAKING PLUGIN: " + ex.getMessage());
            }

        }

        }catch(Exception e) { System.out.println("PLUGIN EXCEPTION: " + e.getMessage());}

    }
*/
    public void init() {
        //activate(); commented

        _sequenceViewArray = new ArrayList<SequenceView>();
        _sequenceViewArray.add(new SequenceView("SequenceView", "SequenceView", this, 0, _plugIns));
        _currentSequenceViewIndex = 0;
        _sequenceViewArray.get(_currentSequenceViewIndex).setTitle("Clotho: Sequence View (Address: " + _currentSequenceViewIndex + ") New Sequence");
       // _help = new ClothoHelp(); commented

        /*commented
        try {
            _help.addArticle("ClothoSequenceViewHelp.html", "Sequence View Help");
        } catch (FileNotFoundException ex) {
          //  Exceptions.printStackTrace(ex);
        }
         *
         */
    }

    public void launch() {
        //copy

        _sequenceViewArray = new ArrayList<SequenceView>();
        _sequenceViewArray.add(new SequenceView("SequenceView", "SequenceView", this, 0, _plugIns));
        _currentSequenceViewIndex = 0;
        _sequenceViewArray.get(_currentSequenceViewIndex).setTitle("Clotho: Sequence View (Address: " + _currentSequenceViewIndex + ") New Sequence");
       //
       //commented _sequenceViewArray.get(_currentSequenceViewIndex).load_preferences();
        _sequenceViewArray.get(_currentSequenceViewIndex).getSequenceView().setVisible(true);
        _sequenceViewArray.get(_currentSequenceViewIndex).getSequenceView().requestFocus();
    }

    public Object getData(String object, String field) throws UnknownKeywordException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendData(Object data, ClothoTool sender) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**Use by other tools to send data to Sequence View*/
    /*commented
    public void sendData(Object data, ClothoTool sender, int opcode) {

        if(opcode == SequenceViewEnums.enzymeHighlight.ordinal())
            _sequenceViewArray.get(_currentSequenceViewIndex).highlightFeaturesEnzymeAction((ClothoHighlightData[]) data);

        if(opcode == SequenceViewEnums.displayNewSequence.ordinal())
         {
            _sequenceViewArray.get(_currentSequenceViewIndex).setSequence((String) data);
         }

        if(opcode == SequenceViewEnums.featureHighlight.ordinal())
            _sequenceViewArray.get(_currentSequenceViewIndex).highlightFeaturesEnzymeAction((ClothoHighlightData[]) data);

    }
*/
/*commented
    public void openHelp()
    {
        _help.launch();
    }
 *
 */

    public void add(SequenceView sv) {
        _sequenceViewArray.add(sv);
    }

    public boolean getCircular() {
        return _sequenceViewArray.get(_currentSequenceViewIndex).getCircular();

    }

    public SequenceView getCurrentSV() {
        return _sequenceViewArray.get(_currentSequenceViewIndex);
    }

    public boolean getDegeneracy() {
        return _sequenceViewArray.get(_currentSequenceViewIndex).getDegeneracy();
    }

    public int getIndex() {
        return _currentSequenceViewIndex;
    }

    public boolean getMethylated() {
        return _sequenceViewArray.get(_currentSequenceViewIndex).getMethylated();
    }

    public String getSequence() {
        return _sequenceViewArray.get(_currentSequenceViewIndex).getSequence();

    }

    public ArrayList<SequenceView> getSequenceViewArray()
    {
        return _sequenceViewArray;
    }

    public SequenceView getSpecificSV(int i)
    {
        return _sequenceViewArray.get(i);
    }

    public void setMainSequenceView(SequenceView sv)
    {
        _currentSequenceViewIndex = sv.getIndex();
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////


    private ArrayList<SequenceView> _sequenceViewArray;
//    private ClothoHelp _help;
    private int _currentSequenceViewIndex;

    private HashMap<String, SequenceViewPlugInInterface> _plugIns;

    @Override
    public void launch(ObjBase o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
