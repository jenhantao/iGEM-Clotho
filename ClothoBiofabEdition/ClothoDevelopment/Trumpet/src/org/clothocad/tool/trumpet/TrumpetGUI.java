/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TrumpetGUI.java
 *
 * Created on Jul 13, 2011, 11:35:43 AM
 */
package org.clothocad.tool.trumpet;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Format;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.Part;


/**
 *
 * @author Craig LaBoda
 *
 */
public class TrumpetGUI extends javax.swing.JFrame {

    /** Creates new form TrumpetGUI */
    public TrumpetGUI() {

        // initialize the components
        initComponents();

        // connect controller to GUI
        _controller = new TrumpetController(this);
        // initialize the number of tabs to 1 since the simulation tool exists
        _numberOfTabs = 1;
        _numberOfResults = 0;
        _totalResults = 0;
        _isModelSet = false;
        _mousePlugin = new MyMousePlugin<InvEdge,String>(this);

        // group the algorithm radio buttons
        groupRadioButtons();

        // update the combo box for the formats
        initFormats();

//\\//\\//\\//\\//\\//\\//\\//\\ HARD CODED //\\//\\//\\//\\//\\//\\//\\//\\//\\

        // set the link sort button to true for now
        jLinkSortButton.setSelected(true);


        
        // hard coding the addition of parts to the parts list
        addPartsAndInvertases(5,5);

        this._controller.setCurrentModel(new LinkSort(this._controller.getNumberOfParts(),true));

        this.updatePermutationsList();
      
    }





    /**
     * Initializes the format combo box.  Called by the GUIs constructor.
     */
    private void initFormats()
    {
        // get all formats now
        ArrayList<Format> formats = Collector.getAll(ObjType.FORMAT);

        // populate the format combo box with these
        jFormatComboBox.setModel(new DefaultComboBoxModel(formats.toArray()));

        // set the current format
        _currentFormat = (Format) jFormatComboBox.getSelectedItem();
    }



     /**
     * This is a hard coded method for placing n parts into the working parts list.
     * @param n - number of parts available for permuting
     */
    public void addPartsAndInvertases(int parts, int invs)
    {
        // set the format to the same format of the parts we are going to add
        jFormatComboBox.setSelectedIndex(5);

        // get all part objects
        ArrayList<Part> test = Collector.getAll(ObjType.PART);

        int numAdded = 0;
        int i = 0;

        // append n parts to the working parts list
        while ((numAdded<parts) && (i<test.size()))
        {

            if (test.get(i).getFormat().getName().equals("RFC10"))
            {
                _controller.appendElement(jPartsList, ("P"+((numAdded++)+1)+": "+test.get(i).toString()));
                // update number of parts
                _controller.incrementNumberOfParts();
            }
            i++;
        }

        numAdded = 0;
        while ((numAdded<invs) && (i<test.size()))
        {

            if (test.get(i).getFormat().getName().equals("RFC10"))
            {
                _controller.appendElement(jInvertaseList, (test.get(i).toString()));

                numAdded++;
            }
            i++;
        }


    }



    /**
     * Creates a new results tab.  Stores this tab so that the information
     * can be referenced later through the controller's resultList.  This version
     * of generateResults() is called whenever a result is generated from an existing
     * invertase tree.
     */
    public void generateResults(String parentNode)
    {
        setStatusBar("Generating Results");

        // get the index of the result panel
        int resultIndex = jWorkAreaPane.getSelectedIndex()-1;
        if (jSimulateDesignsMenuItem.isSelected())
            resultIndex = resultIndex-1;

        // get result tab
        ResultsPanel oldPanel = this._controller.getResultPanel(resultIndex);

        // get the original parts list
        ArrayList<String> partsSet = oldPanel.getPartsSet();

        // get the original format
        Format form = oldPanel.getFormat();

        KeyTree oldTree = oldPanel.getTree();

        if (oldTree.getPath(parentNode).equals(""))
        {
               //custom title, warning icon
                JOptionPane.showMessageDialog(new JFrame(),
                    "Creating a construct from this node will\n"
                    + " create the current construct again.",
                    "Cannot Create Construct",
                    JOptionPane.WARNING_MESSAGE);
        }

        else
        {

//        // get all children nodes = desired permutations
//        Collection<String> allNodes = new ArrayList<String>(0);
//        oldTree.getSubNodes(parentNode, allNodes);
//        allNodes.remove(parentNode);

//        Object[] desiredPermArray = allNodes.toArray();
//        ArrayList<String> desiredPerms = new ArrayList<String>(0);
//        for (int i=0; i<desiredPermArray.length; i++)
//        {
//            desiredPerms.add((String)desiredPermArray[i]);
//        }

        // we need to get a sim model that represets the new construct
        // first get the old sim model
        InvertSim oldModel = oldPanel.getInvertSim();
//        InvertSim newModel = null;
        InvertSim newModel = oldModel.cloneFresh();

        
        String[][] treeTable = oldTree.getSubTreeTable(parentNode);



        String newConstructKey = oldTree.getPath(parentNode);
        String oldConstructKey = oldPanel.getConstructKey();
//        if (!(oldConstructKey.equals("")))
//            constructKey = oldConstructKey.concat(constructKey);

        // we need the key which gets us to the parentNode
//        String constructKey = newModel.generateKey(parentNode);
//        String constructKey = "";
//        for (int i=0; i<testKey.size(); i++)
//            constructKey = constructKey.concat(testKey.get(i));

        // now apply this key to give us the correct construct
//        String[] key = constructKey.split(" ");
//        for (int i=0; i<key.length; i++)
//        {
//            if (newModel.isPossible(key[i]))
//                newModel.invert();
//        }


//            // create a results panel
//            ResultsPanel result = new ResultsPanel(newModel,partsSet,desiredPerms,
//                    _mousePlugin,form,jKeyTableMenuItem.isSelected(), jKeyTreesMenuItem.isSelected());

            ResultsPanel result = new ResultsPanel(newModel,partsSet,treeTable, oldTree.getDesiredPerms(), newConstructKey,
                    oldConstructKey,_mousePlugin,form,jKeyTableMenuItem.isSelected(), jKeyTreesMenuItem.isSelected());

            // add the result to the working list of results
            _controller.addResultPanel(result);

            // update the results invertase combo boxes
            updateInvertaseCombos(result);

            // update the work area with this new tab
            jWorkAreaPane.addTab("Result "+(++_totalResults),result);
            // account for the new tab
            _numberOfTabs++;
            _numberOfResults++;
            jWorkAreaPane.setTabComponentAt(_numberOfTabs, new ButtonTabComponent(this));

            // set the view to the newly added tab
            jWorkAreaPane.setSelectedIndex(_numberOfTabs);
        }

        setStatusBar("Finished Generating Results");

    }


    public boolean isSimulateDesigns()
    {
        return this.jSimulateDesignsMenuItem.isSelected();
    }



    /**
     * Creates a new results tab.  Stores this tab so that the information
     * can be referenced later through the controller's resultList.  This version
     * of generateResults() is called whenever a result is generated from the main
     * permutation selection screen of Trumpet.
     */
    public void generateResults()
    {
        // first check whether there are any desired permutations
        if (!(_controller.isListEmpty(jDesiredPermList)))
        {
            setStatusBar("Generating Results");

            // get the desired permutations
            ArrayList<String> desiredPerms = _controller.getListElements(jDesiredPermList);

            ArrayList<String> partsSet = _controller.getListElements(jPartsList);
            // remove the P1 stuff
            for (int i=0; i<partsSet.size(); i++)
            {
                int deletionIndex = partsSet.get(i).indexOf(": ")+2;
                partsSet.set(i, partsSet.get(i).substring(deletionIndex,partsSet.get(i).length()));
            }

            // get the format of the parts being used
            Format form = (Format) jFormatComboBox.getSelectedItem();

            // create a results panel
            ResultsPanel result = new ResultsPanel(_controller.getCurrentModel().cloneFresh(),partsSet,desiredPerms,
                    _mousePlugin,form,jKeyTableMenuItem.isSelected(), jKeyTreesMenuItem.isSelected());

            // add the result to the working list of results
            _controller.addResultPanel(result);

            // update the results invertase combo boxes
            updateInvertaseCombos(result);

            // update the work area with this new tab
            jWorkAreaPane.addTab("Result "+(++_totalResults),result);
            // account for the new tab
            _numberOfTabs++;
            _numberOfResults++;
            jWorkAreaPane.setTabComponentAt(_numberOfTabs, new ButtonTabComponent(this));
            
            // set the view to the newly added tab
            jWorkAreaPane.setSelectedIndex(_numberOfTabs);

            setStatusBar("Finished Generating Results");
        }
    }



    /**
     * Updates the results panel's set of combo boxes.  Should only be called
     * when the results panel is first created to populate the combo boxes.
     * @param result
     */
    public void updateInvertaseCombos(ResultsPanel result)
    {
        setStatusBar("Updating Invertase Options");
        ArrayList<String> possibleInvs = _controller.getListElements(jInvertaseList);
        result.setInvComboBoxes(possibleInvs);        
        setStatusBar("Finished Updating Invertase Options");
    }



    /**
     * Updates every results panel's set of combo boxes.  This should be called
     * whenever the invertase list is changed.
     */
    public void updateAllInvertaseCombos()
    {
        setStatusBar("Updating Invertase Options");

        ArrayList<String> possibleInvs = _controller.getListElements(jInvertaseList);

        ResultsPanel currentPanel;

        for (int i=0; i<_numberOfResults; i++)
        {
            currentPanel = _controller.getResultPanel(i);
            currentPanel.setInvComboBoxes(possibleInvs);
            _controller.setResultPanel(i,currentPanel);
        }

        setStatusBar("Finished Updating Invertase Options");
    }



    /**
     * Checks what permutations are selected and adds them to the desired
     * permutation list if they do not already exist within that list
     */
    public void addDesiredPerms()
    {
        setStatusBar("Adding Desired Permutations");

        int[] selection = jPossiblePermList.getSelectedIndices();
        ArrayList<String> permsToAdd = new ArrayList<String>(0);

        ListModel possiblePermModel = jPossiblePermList.getModel();

        String currentElement = "";

        for (int i=0; i<selection.length; i++)
        {
            currentElement = (String)possiblePermModel.getElementAt(selection[i]);
            if (!(_controller.containsElement(jDesiredPermList, currentElement)))
                permsToAdd.add(currentElement);
        }
     
        _controller.appendElements(jDesiredPermList,permsToAdd);

        setStatusBar("Finished Adding Permutations");
    }


    
    /**
     * Updates the Possible Permutations list based on the parts list, the
     * algorithm chosen, and the combination checkbox
     *
     */
    public void updatePermutationsList()
    {
        setStatusBar("Updating Possible Permutation List");

        if (_controller.getCurrentModel()!=null)
        {
            _controller.clearList(jPossiblePermList);
            ArrayList<String> possiblePerms = _controller.getCurrentModel().getAllPartPermutations();
            possiblePerms.remove(0);
            _controller.appendElements(jPossiblePermList,possiblePerms);
        }

        setStatusBar("Possible Permutation List Updated");
    }



    /**
     * Groups together all of the algorithm buttons
     */
    private void groupRadioButtons()
    {
        ButtonGroup algorithmsGroup = new ButtonGroup();

        algorithmsGroup.add(jLinkSortButton);
        algorithmsGroup.add(jPancakeButton);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPartsPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jDeletePartButton = new javax.swing.JButton();
        jCurrentPartSetLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPartsList = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jFormatComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        AvailableInvertasesLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jInvertaseList = new javax.swing.JList();
        jWorkAreaPane = new javax.swing.JTabbedPane();
        jPermutationPanel = new javax.swing.JPanel();
        jCreateDesignButton = new javax.swing.JButton();
        jAlgorithmsPanel = new javax.swing.JPanel();
        jLinkSortButton = new javax.swing.JRadioButton();
        jPancakeButton = new javax.swing.JRadioButton();
        jCombinationsCheckBox = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jButtonsPanel = new javax.swing.JPanel();
        jAddPermButton = new javax.swing.JButton();
        jDeletePermButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTrumpetLabel = new javax.swing.JLabel();
        jPossiblePermPanel = new javax.swing.JPanel();
        jPossiblePermsLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPossiblePermList = new javax.swing.JList();
        jDesiredPermPanel = new javax.swing.JPanel();
        jDesiredPermsLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jDesiredPermList = new javax.swing.JList();
        jSimulationPanel = new javax.swing.JPanel();
        jStatusLabel = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jfileMenu = new javax.swing.JMenu();
        jwindowsMenu = new javax.swing.JMenu();
        jSimulateDesignsMenuItem = new javax.swing.JCheckBoxMenuItem();
        jKeyTreesMenuItem = new javax.swing.JCheckBoxMenuItem();
        jKeyTableMenuItem = new javax.swing.JCheckBoxMenuItem();
        jViewsMenu = new javax.swing.JMenu();
        jswitchViewsMenuItem = new javax.swing.JMenuItem();
        jHelpMenu = new javax.swing.JMenu();
        jInstructionsMenuItem = new javax.swing.JMenuItem();
        jVersionMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.title")); // NOI18N

        jPartsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jPartsPanel.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 1, 14))); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jPanel1.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(0, 102, 255))); // NOI18N

        jDeletePartButton.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jDeletePartButton.text")); // NOI18N
        jDeletePartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDeletePartButtonActionPerformed(evt);
            }
        });

        jCurrentPartSetLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        jCurrentPartSetLabel.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jCurrentPartSetLabel.text")); // NOI18N

        DefaultListModel partsListModel = new DefaultListModel()
        {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }

        };
        jPartsList.setModel(partsListModel);
        jScrollPane1.setViewportView(jPartsList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jCurrentPartSetLabel)
                        .addGap(70, 70, 70))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jDeletePartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCurrentPartSetLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDeletePartButton)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jPanel4.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(0, 102, 255))); // NOI18N

        jFormatComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {}));
        jFormatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormatComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        AvailableInvertasesLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        AvailableInvertasesLabel.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.AvailableInvertasesLabel.text")); // NOI18N

        jInvertaseList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jInvertaseList);

        javax.swing.GroupLayout jPartsPanelLayout = new javax.swing.GroupLayout(jPartsPanel);
        jPartsPanel.setLayout(jPartsPanelLayout);
        jPartsPanelLayout.setHorizontalGroup(
            jPartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPartsPanelLayout.createSequentialGroup()
                .addGroup(jPartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPartsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPartsPanelLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPartsPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPartsPanelLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(AvailableInvertasesLabel)))
                .addContainerGap())
        );
        jPartsPanelLayout.setVerticalGroup(
            jPartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPartsPanelLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AvailableInvertasesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addContainerGap())
        );

        jWorkAreaPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jWorkAreaPane.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 1, 14))); // NOI18N
        jWorkAreaPane.setMaximumSize(new java.awt.Dimension(600, 453));

        jCreateDesignButton.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jCreateDesignButton.text")); // NOI18N
        jCreateDesignButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCreateDesignButtonActionPerformed(evt);
            }
        });

        jAlgorithmsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jAlgorithmsPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(51, 102, 255))); // NOI18N

        jLinkSortButton.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jLinkSortButton.text")); // NOI18N
        jLinkSortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLinkSortButtonActionPerformed(evt);
            }
        });

        jPancakeButton.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jPancakeButton.text")); // NOI18N
        jPancakeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPancakeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jAlgorithmsPanelLayout = new javax.swing.GroupLayout(jAlgorithmsPanel);
        jAlgorithmsPanel.setLayout(jAlgorithmsPanelLayout);
        jAlgorithmsPanelLayout.setHorizontalGroup(
            jAlgorithmsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jAlgorithmsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLinkSortButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPancakeButton)
                .addContainerGap(251, Short.MAX_VALUE))
        );
        jAlgorithmsPanelLayout.setVerticalGroup(
            jAlgorithmsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jAlgorithmsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jAlgorithmsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLinkSortButton)
                    .addComponent(jPancakeButton))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jCombinationsCheckBox.setSelected(true);
        jCombinationsCheckBox.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jCombinationsCheckBox.text")); // NOI18N
        jCombinationsCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCombinationsCheckBoxActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(0, 102, 255))); // NOI18N

        jAddPermButton.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jAddPermButton.text")); // NOI18N
        jAddPermButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAddPermButtonActionPerformed(evt);
            }
        });

        jDeletePermButton.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jDeletePermButton.text")); // NOI18N
        jDeletePermButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDeletePermButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jButtonsPanelLayout = new javax.swing.GroupLayout(jButtonsPanel);
        jButtonsPanel.setLayout(jButtonsPanelLayout);
        jButtonsPanelLayout.setHorizontalGroup(
            jButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jButtonsPanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jAddPermButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDeletePermButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jButtonsPanelLayout.setVerticalGroup(
            jButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jButtonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jAddPermButton)
                .addGap(18, 18, 18)
                .addComponent(jDeletePermButton)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        jTrumpetLabel.setFont(new java.awt.Font("Segoe Script", 1, 16));
        jTrumpetLabel.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jTrumpetLabel.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jTrumpetLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jTrumpetLabel)
                .addContainerGap(120, Short.MAX_VALUE))
        );

        jPossiblePermsLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        jPossiblePermsLabel.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jPossiblePermsLabel.text")); // NOI18N

        jPossiblePermList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jPossiblePermList);

        javax.swing.GroupLayout jPossiblePermPanelLayout = new javax.swing.GroupLayout(jPossiblePermPanel);
        jPossiblePermPanel.setLayout(jPossiblePermPanelLayout);
        jPossiblePermPanelLayout.setHorizontalGroup(
            jPossiblePermPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPossiblePermPanelLayout.createSequentialGroup()
                .addGroup(jPossiblePermPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPossiblePermPanelLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jPossiblePermsLabel))
                    .addGroup(jPossiblePermPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPossiblePermPanelLayout.setVerticalGroup(
            jPossiblePermPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPossiblePermPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPossiblePermsLabel)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDesiredPermsLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        jDesiredPermsLabel.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jDesiredPermsLabel.text")); // NOI18N

        jDesiredPermList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jDesiredPermList);

        javax.swing.GroupLayout jDesiredPermPanelLayout = new javax.swing.GroupLayout(jDesiredPermPanel);
        jDesiredPermPanel.setLayout(jDesiredPermPanelLayout);
        jDesiredPermPanelLayout.setHorizontalGroup(
            jDesiredPermPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesiredPermPanelLayout.createSequentialGroup()
                .addGroup(jDesiredPermPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jDesiredPermPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDesiredPermPanelLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jDesiredPermsLabel)))
                .addContainerGap())
        );
        jDesiredPermPanelLayout.setVerticalGroup(
            jDesiredPermPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesiredPermPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDesiredPermsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPossiblePermPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jDesiredPermPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPossiblePermPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDesiredPermPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPermutationPanelLayout = new javax.swing.GroupLayout(jPermutationPanel);
        jPermutationPanel.setLayout(jPermutationPanelLayout);
        jPermutationPanelLayout.setHorizontalGroup(
            jPermutationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPermutationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPermutationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPermutationPanelLayout.createSequentialGroup()
                        .addComponent(jAlgorithmsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPermutationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCreateDesignButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCombinationsCheckBox))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPermutationPanelLayout.setVerticalGroup(
            jPermutationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPermutationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPermutationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPermutationPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jCreateDesignButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCombinationsCheckBox))
                    .addGroup(jPermutationPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jAlgorithmsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );

        jWorkAreaPane.addTab(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jPermutationPanel.TabConstraints.tabTitle"), jPermutationPanel); // NOI18N

        javax.swing.GroupLayout jSimulationPanelLayout = new javax.swing.GroupLayout(jSimulationPanel);
        jSimulationPanel.setLayout(jSimulationPanelLayout);
        jSimulationPanelLayout.setHorizontalGroup(
            jSimulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 617, Short.MAX_VALUE)
        );
        jSimulationPanelLayout.setVerticalGroup(
            jSimulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );

        jWorkAreaPane.addTab(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jSimulationPanel.TabConstraints.tabTitle"), jSimulationPanel); // NOI18N

        jStatusLabel.setFont(new java.awt.Font("Arial", 0, 11));
        jStatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jStatusLabel.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jStatusLabel.text")); // NOI18N
        jStatusLabel.setMaximumSize(new java.awt.Dimension(51, 15));
        jStatusLabel.setMinimumSize(new java.awt.Dimension(51, 15));
        jStatusLabel.setPreferredSize(new java.awt.Dimension(51, 15));

        jfileMenu.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jfileMenu.text")); // NOI18N
        jMenuBar.add(jfileMenu);

        jwindowsMenu.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jwindowsMenu.text")); // NOI18N
        jwindowsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jwindowsMenuActionPerformed(evt);
            }
        });

        jSimulateDesignsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK));
        jSimulateDesignsMenuItem.setSelected(true);
        jSimulateDesignsMenuItem.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jSimulateDesignsMenuItem.text")); // NOI18N
        jSimulateDesignsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSimulateDesignsMenuItemActionPerformed(evt);
            }
        });
        jwindowsMenu.add(jSimulateDesignsMenuItem);

        jKeyTreesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.SHIFT_MASK));
        jKeyTreesMenuItem.setSelected(true);
        jKeyTreesMenuItem.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jKeyTreesMenuItem.text")); // NOI18N
        jKeyTreesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jKeyTreesMenuItemActionPerformed(evt);
            }
        });
        jwindowsMenu.add(jKeyTreesMenuItem);

        jKeyTableMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.SHIFT_MASK));
        jKeyTableMenuItem.setSelected(true);
        jKeyTableMenuItem.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jKeyTableMenuItem.text")); // NOI18N
        jKeyTableMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jKeyTableMenuItemActionPerformed(evt);
            }
        });
        jwindowsMenu.add(jKeyTableMenuItem);

        jMenuBar.add(jwindowsMenu);

        jViewsMenu.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jViewsMenu.text")); // NOI18N

        jswitchViewsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.SHIFT_MASK));
        jswitchViewsMenuItem.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jswitchViewsMenuItem.text")); // NOI18N
        jswitchViewsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jswitchViewsMenuItemActionPerformed(evt);
            }
        });
        jViewsMenu.add(jswitchViewsMenuItem);

        jMenuBar.add(jViewsMenu);

        jHelpMenu.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jHelpMenu.text")); // NOI18N

        jInstructionsMenuItem.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jInstructionsMenuItem.text")); // NOI18N
        jHelpMenu.add(jInstructionsMenuItem);

        jVersionMenuItem.setText(org.openide.util.NbBundle.getMessage(TrumpetGUI.class, "TrumpetGUI.jVersionMenuItem.text")); // NOI18N
        jVersionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jVersionMenuItemActionPerformed(evt);
            }
        });
        jHelpMenu.add(jVersionMenuItem);

        jMenuBar.add(jHelpMenu);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jStatusLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPartsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jWorkAreaPane, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPartsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jWorkAreaPane, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jswitchViewsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jswitchViewsMenuItemActionPerformed
        _controller.switchViews();
    }//GEN-LAST:event_jswitchViewsMenuItemActionPerformed

    private void jDeletePartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDeletePartButtonActionPerformed
        // when the delete button is pressed for the parts list

        int[] toBeDeleted = jPartsList.getSelectedIndices();

        // if the selection is not empty
        if (!(toBeDeleted.length==0))
        {
            _controller.removeElements(jPartsList,true);


            boolean combos = _controller.getCurrentModel().combos;
            int n = _controller.getCurrentModel().getNumberOfParts()-toBeDeleted.length;

            String alg = _controller.getCurrentModel().getAlgorithm();
            if (alg.equals("LinkSort"))
                _controller.setCurrentModel(new LinkSort(n,combos));
            else if (alg.equals("Pancake"))
                _controller.setCurrentModel(new Pancake(n,combos));


    //        _controller.setCurrentModel(_controller.getCurrentModel().cloneFresh());

            updatePermutationsList();
            _controller.clearList(jDesiredPermList);
        }

        
    }//GEN-LAST:event_jDeletePartButtonActionPerformed

    private void jDeletePermButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDeletePermButtonActionPerformed
        // pressing this button will delete a desired permutation from the list
        _controller.removeElements(jDesiredPermList, false);
    }//GEN-LAST:event_jDeletePermButtonActionPerformed

    private void jAddPermButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAddPermButtonActionPerformed
        // when this button is pressed, we want to add the selected elements to
        // the desired list of permutations
        addDesiredPerms();
    }//GEN-LAST:event_jAddPermButtonActionPerformed

    private void jCombinationsCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCombinationsCheckBoxActionPerformed
        // if the combinations check box is clicked
        // reset the current model

        if (_controller.isModelSet())
        {
            String modelType = _controller.getCurrentModel().getAlgorithm();

            if (modelType.equals("LinkSort"))
                _controller.setCurrentModel(new LinkSort(_controller.getNumberOfParts(),jCombinationsCheckBox.isSelected()));
            else if (modelType.equals("Pancake"))
                _controller.setCurrentModel(new Pancake(_controller.getNumberOfParts(), jCombinationsCheckBox.isSelected()));

            updatePermutationsList();
            if (!(jCombinationsCheckBox.isSelected()))
                _controller.clearList(jDesiredPermList);
        }
        
    }//GEN-LAST:event_jCombinationsCheckBoxActionPerformed

    private void jLinkSortButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLinkSortButtonActionPerformed
        // if the jLinkSort Button is pressed
        _controller.setCurrentModel(new LinkSort(_controller.getNumberOfParts(),jCombinationsCheckBox.isSelected()));

//        if (!_isModelSet)
//        {
//            updatePermutationsList();
//            _isModelSet=true;
//        }
}//GEN-LAST:event_jLinkSortButtonActionPerformed

    private void jCreateDesignButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCreateDesignButtonActionPerformed
        // when the create design button is pressed, generate the results for the user

        generateResults();
    }//GEN-LAST:event_jCreateDesignButtonActionPerformed

    private void jwindowsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jwindowsMenuActionPerformed

}//GEN-LAST:event_jwindowsMenuActionPerformed

    private void jSimulateDesignsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSimulateDesignsMenuItemActionPerformed
        // TODO add your handling code here:

        if (jSimulateDesignsMenuItem.isSelected())
        {
            jWorkAreaPane.insertTab("Simulate Designs",null,jSimulationPanel,"Simulate your designs!",1);
            _numberOfTabs++;
        }
        else
        {
            jWorkAreaPane.removeTabAt(1);
            _numberOfTabs--;
        }
    }//GEN-LAST:event_jSimulateDesignsMenuItemActionPerformed

    private void jKeyTreesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jKeyTreesMenuItemActionPerformed
        // when this button is pressed, all of the key trees are toggled on or off

        _controller.toggleTrees();
    }//GEN-LAST:event_jKeyTreesMenuItemActionPerformed

    private void jKeyTableMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jKeyTableMenuItemActionPerformed
        // when this button is pressed, all of the key tables are toggled on or off

        _controller.toggleKeys();
    }//GEN-LAST:event_jKeyTableMenuItemActionPerformed

    private void jFormatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormatComboBoxActionPerformed
        // when a format is chosen, the parts list must be cleared since
        // only one format is acceptable
        // also, a dialog box is generated warning the user

        setStatusBar("Changing Formats");

        Format nextForm = (Format) jFormatComboBox.getSelectedItem();

        if (!(_currentFormat.toString().equals(nextForm.toString())))
        {
            Object[] options = {"Yes, Change Format",
                    "Cancel"};
            int n = JOptionPane.showOptionDialog(new JFrame(),
                "Choosing a new format will reset your current part set.\n"
                +"Are you sure you would like to continue?",
                "Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title

            if (n==JOptionPane.YES_OPTION)
            {
                // set the format
               _currentFormat = nextForm;
               // clear the parts list
               _controller.clearList(jPartsList);
               // set the number of parts to 0
               _controller.setNumberOfParts(0);
               // update the number of permutations
               updatePermutationsList();
               // reset the desired perm list now that the number of parts has changed
               _controller.clearList(jDesiredPermList);
               setStatusBar("Current Format Changed");

            }
            else if (n==JOptionPane.NO_OPTION)
            {
                // reset the format
                jFormatComboBox.setSelectedItem(_currentFormat);

                setStatusBar("Current Format Remains Unchanged");

            }
        }

    }//GEN-LAST:event_jFormatComboBoxActionPerformed

    private void jPancakeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPancakeButtonActionPerformed
        // if the pancake button is pressed
//        updatePermutationsList();
        _controller.setCurrentModel(new Pancake(_controller.getNumberOfParts(),jCombinationsCheckBox.isSelected()));

//        if (!_isModelSet)
//        {
//            updatePermutationsList();
//            _isModelSet=true;
//        }
    }//GEN-LAST:event_jPancakeButtonActionPerformed

    private void jVersionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jVersionMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jVersionMenuItemActionPerformed



    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TrumpetGUI().setVisible(true);
            }
        });
    }


    /**
     * Allows the ButtonTabComponent class (which handles the result tabs)
     * access to the parent pane.
     * @return
     */
    public JTabbedPane getWorkAreaPane()
    {
        return jWorkAreaPane;
    }


    /**
     * Allows the controller and the ButtonTabComponent to decrement the number
     * of results.  This allows us to close result tabs correctly.
     */
    public void decrementResults(int index)
    {
        _numberOfResults--;
        _numberOfTabs--;

        _controller.removeResult(index);
    }


    /**
     * Allows methods in this class to update the status bar very quickly.
     * @param update
     */
    private void setStatusBar(String update)
    {
        jStatusLabel.setText(update);
    }


    /**
     * Allows the controller and other classes to set the status bar.
     * @return
     */
    public JLabel getStatusBar()
    {
        return jStatusLabel;
    }



    private TrumpetController _controller;
    private boolean _isModelSet;
    private int _numberOfTabs;
    private int _numberOfResults;
    private Format _currentFormat;
    private int _totalResults;
    private MyMousePlugin _mousePlugin;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AvailableInvertasesLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jAddPermButton;
    private javax.swing.JPanel jAlgorithmsPanel;
    private javax.swing.JPanel jButtonsPanel;
    private javax.swing.JCheckBox jCombinationsCheckBox;
    private javax.swing.JButton jCreateDesignButton;
    private javax.swing.JLabel jCurrentPartSetLabel;
    private javax.swing.JButton jDeletePartButton;
    private javax.swing.JButton jDeletePermButton;
    private javax.swing.JList jDesiredPermList;
    private javax.swing.JPanel jDesiredPermPanel;
    private javax.swing.JLabel jDesiredPermsLabel;
    private javax.swing.JComboBox jFormatComboBox;
    private javax.swing.JMenu jHelpMenu;
    private javax.swing.JMenuItem jInstructionsMenuItem;
    private javax.swing.JList jInvertaseList;
    private javax.swing.JCheckBoxMenuItem jKeyTableMenuItem;
    private javax.swing.JCheckBoxMenuItem jKeyTreesMenuItem;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JRadioButton jLinkSortButton;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JRadioButton jPancakeButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JList jPartsList;
    private javax.swing.JPanel jPartsPanel;
    private javax.swing.JPanel jPermutationPanel;
    private javax.swing.JList jPossiblePermList;
    private javax.swing.JPanel jPossiblePermPanel;
    private javax.swing.JLabel jPossiblePermsLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JCheckBoxMenuItem jSimulateDesignsMenuItem;
    private javax.swing.JPanel jSimulationPanel;
    private javax.swing.JLabel jStatusLabel;
    private javax.swing.JLabel jTrumpetLabel;
    private javax.swing.JMenuItem jVersionMenuItem;
    private javax.swing.JMenu jViewsMenu;
    private javax.swing.JTabbedPane jWorkAreaPane;
    private javax.swing.JMenu jfileMenu;
    private javax.swing.JMenuItem jswitchViewsMenuItem;
    private javax.swing.JMenu jwindowsMenu;
    // End of variables declaration//GEN-END:variables
}
