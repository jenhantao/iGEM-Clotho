/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.trumpet;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.ObjType;
import org.openide.windows.TopComponent;

/**
 *
 * @author Craig LaBoda
 *
 */
public class TrumpetController {


    public TrumpetController(JFrame frame){
        _isTC = true;
            final JComponent guiContentPane = (JComponent) frame.getContentPane();

            final JMenuBar menu = frame.getRootPane().getJMenuBar();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    _tcView = new TopComponent();
                    _tcView.setLayout(new BorderLayout());
                    JScrollPane sp = new JScrollPane(guiContentPane);
                    _tcView.add(menu, BorderLayout.NORTH);
                    _tcView.add(sp, BorderLayout.CENTER);
                    _tcView.setName("Trumpet");
                    _tcView.open();
                    _tcView.requestActive();

                }
            });
            frame.dispose();

            _numberOfParts = 0;
            _resultsList = new ArrayList<ResultsPanel>(0);
            _isModelSet = false;
    }


     public void switchViews() {
        if (_isTC)
        {
            Component[] components = _tcView.getComponents();
            _frameView = new JFrame();
            _frameView.setContentPane((Container) components[1]);
            _frameView.setJMenuBar((JMenuBar) components[0]);
            _frameView.pack();
            _frameView.setVisible(true);
            _frameView.setTitle("Trumpet");
            _isTC = false;
            _tcView.close();

        } 
        else
        {
            final JComponent guiContentPane = (JComponent) _frameView.getContentPane();

            final JMenuBar menu = _frameView.getJMenuBar();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    _tcView = new TopComponent();
                    _tcView.setLayout(new BorderLayout());
                    JScrollPane sp = new JScrollPane(guiContentPane);
                    _tcView.add(menu, BorderLayout.NORTH);
                    _tcView.add(sp, BorderLayout.CENTER);
                    _tcView.setName("Trumpet");
                    _tcView.open();
                    _tcView.requestActive();

                }
            });
            _frameView.dispose();
            _isTC = true;
        }
    }


    public void toggleTrees()
    {
        for (int i=0; i<_resultsList.size(); i++)
        {
            _resultsList.get(i).toggleTreeTab();
        }
    }

    public void toggleKeys()
    {
        for (int i=0; i<_resultsList.size(); i++)
        {
            _resultsList.get(i).toggleKeyTab();
        }
    }



    /**
     * Clears any JList.
     * @param list - the list being cleared
     */
    public void clearList(JList list)
    {
        DefaultListModel emptyList = new DefaultListModel();
        list.setModel(emptyList);
    }



    /**
     * Checks any JList for a particular object
     * @param list - list being checked
     * @param element - element you wish to check for
     * @return
     */
    public boolean containsElement(JList list, Object element)
    {
        boolean isContained = false;

        ListModel currentModel = list.getModel();

        for (int i=0; i<currentModel.getSize(); i++)
        {
            if (((String)currentModel.getElementAt(i)).equals((String)element))
                isContained = true;
        }

        return isContained;
    }



    /**
     * Returns all objects in a list as a string ArrayList
     * @param list - list being examined
     * @return
     */
    public ArrayList<String> getListElements(JList list)
    {
        ArrayList<String> allElements = new ArrayList<String>(0);
        
        ListModel currentModel = list.getModel();
        
        for (int i=0; i<currentModel.getSize(); i++)
        {
            allElements.add((String)currentModel.getElementAt(i));
        }
        
        return allElements;
    }



    /**
     * Determines whether a list is empty
     * @param list - list being examined
     * @return
     */
    public boolean isListEmpty(JList list)
    {
        ListModel currentModel = list.getModel();

        if (currentModel.getSize()==0)
            return true;
        else
            return false;
    }



    /**
     * Appends a set of elements to the given jList.
     * @param list - list which is being modified
     * @param elements - ArrayList of elements being appended
     */
    public void appendElements(JList list, ArrayList<String> elements)
    {
        // get the current model
        ListModel currentModel = list.getModel();

        // create a new model
        DefaultListModel newModel = new DefaultListModel();

        // loop through and append the old model's elements to the new model
        for (int i=0; i<currentModel.getSize();i++)
            newModel.addElement(currentModel.getElementAt(i));

        // now add the new elements
        for (int i=0; i<elements.size(); i++)
        {
            newModel.addElement(elements.get(i));
        }

        list.setModel(newModel);
    }



    /**
     * Updates the controller's model field.
     * @param invObj - type of invertSim object being used
     */
    public void setCurrentModel(InvertSim invObj)
    {
        if (!(_isModelSet))
            _isModelSet = true;
        _currentModel = invObj;
    }



    /**
     * Returns the current InvertSim model that Trumpet is using.
     * @return
     */
    public InvertSim getCurrentModel()
    {
        return _currentModel;
    }


    /**
     * Append only a single element to a list
     * @param list - list which is being modified
     * @param element - element that is being appended
     */
    public void appendElement(JList list, String element)
    {
        // get the current model
        ListModel currentModel = list.getModel();

        // create a new model
        DefaultListModel newModel = new DefaultListModel();

        // loop through and append the old model's elements to the new model
        for (int i=0; i<currentModel.getSize();i++)
            newModel.addElement(currentModel.getElementAt(i));

        // append the new element
        newModel.addElement(element);

        // set the new model
        list.setModel(newModel);
    }
    


     /**
     * Removes a particular set of elements from a list.
     * @param list - list which is being modified
     * @param indices - indices of the list which need to be removed
     */
    public void removeElements(JList list, boolean reorder)
    {
        int[] indices = list.getSelectedIndices();

        // get the current model
        ListModel currentModel = list.getModel();

        // create a new model
        DefaultListModel newModel = new DefaultListModel();

        // keep track of the current indice index
        int indiceCounter = 0;

        // keeps track of whether to check indices anymore
        boolean done = false;


        if (indices.length!=0)
        {
            // loop through and append the old model's elements to the new model
            for (int i=0; i<currentModel.getSize();i++)
            {
                // if all of the elements have not been removed yet
                if (!done)
                {
                    if (i != indices[indiceCounter])
                    {
                        // add the element
                        newModel.addElement(currentModel.getElementAt(i));
                    }
                    else
                    {
                        indiceCounter++;
                        if (indiceCounter == indices.length)
                            done = true;
                    }
                }
                // if all of the elments have been removed
                else
                {
                    // add the element
                    newModel.addElement(currentModel.getElementAt(i));
                }
            }

            // now that the model is correct
            //clear the old list
            clearList(list);
            list.setModel(newModel);

            // if we are dealing with parts, then we need to reorder the parts
            if (reorder)
            {
                // reindex the parts list
                renumberParts(list);
                
                // we also need to increment the parts
                _numberOfParts = newModel.getSize();

                // update the model based on
                setCurrentModel(this._currentModel.cloneFresh());
            }
        }
    }




    /**
     * Renumber the list of parts based on the currentModel
     * @param list - list which is being renumbered, should only be the parts list
     */
    public void renumberParts(JList list)
    {
        // get the current list model
        ListModel currentModel = list.getModel();

        // create a new model
        DefaultListModel nextModel = new DefaultListModel();

        // create a temporary string for holding elements
        String currentElement = "";

        // deletionIndex is used to determine the index of the substrings
        int deletionIndex = -1;

        // loop through the model and make the appropriate adjustments
        for (int i=0; i<currentModel.getSize(); i++)
        {
            // get the entire element
            currentElement = ((String)currentModel.getElementAt(i));

            // LinkSort object have a specific part numbering defined by
            // their createDesign method
            deletionIndex = currentElement.indexOf(": ")+2;
            currentElement = "P"+(i+1)+": "+currentElement.substring(deletionIndex,currentElement.length());
            nextModel.addElement(currentElement);
            
        }

        // set the new model
        list.setModel(nextModel);
    }



    /**
     * Get the number of parts in the parts list
     * @return - number of parts
     */
    public int getNumberOfParts()
    {
        return _numberOfParts;
    }


    public void updateCurrentModel()
    {
//        String modelType = _currentModel.getAlgorithm();
//        Boolean combos = _currentModel.isCombos();
//
//        if (modelType.equals("LinkSort"))
//        {
//            setCurrentModel(new LinkSort(_numberOfParts,combos));
//        }
//        else if (modelType.equals("Pancake"))
//        {
//            setCurrentModel(new Pancake(_numberOfParts,combos));
//        }
        setCurrentModel(_currentModel.cloneFresh());
    }


    /**
     * Allows the TrumpetGUI to add a part.
     */
    public void incrementNumberOfParts()
    {
        _numberOfParts++;
    }


    public void setNumberOfParts(int n)
    {
        _numberOfParts = n;
    }


    /**
     * Allows the TrumpetGUI to decrement the number of parts
     */
    public void decrementNumberOfParts()
    {
        _numberOfParts--;
    }

    public void addResultPanel(ResultsPanel result)
    {
        _resultsList.add(result);
    }


    public ResultsPanel getResultPanel(int index)
    {
        return _resultsList.get(index);
    }

    public void setResultPanel(int index, ResultsPanel results)
    {
        _resultsList.set(index, results);
    }


    public boolean isModelSet()
    {
        return _isModelSet;
    }


    public void removeResult(int index)
    {
        _resultsList.remove(index);
    }

    private boolean _isModelSet;
    private int _numberOfParts;
    private InvertSim _currentModel;
    private ArrayList<ResultsPanel> _resultsList;

    private TopComponent _tcView;
    private JFrame _frameView;
    private boolean _isTC;
        // determines whether we are in the top component view or the frame view
}
