package org.clothocad.tool.trumpet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.lang.String;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Format;
import org.clothocore.api.data.Part;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.PlainDocument;
import javax.swing.text.StyledDocument;
import org.clothocore.api.data.Collection;

/**
 *
 * @author Craig LaBoda
 *
 */
public class ResultsPanel extends JPanel{

    public ResultsPanel(InvertSim invObj,ArrayList<String> partsSet, ArrayList<String>
            desiredPerms, MyMousePlugin mousePlugin, Format partFormat, boolean table, boolean tree)
    {

        // the invertSim object that represents this result
        _invObj = invObj;
        // partsSet does not inlcude P1, P2, etc. at the beginning of each part name
        _partsSet = partsSet;
        // invComboBoxes will hold all of the necessary combo boxes for the invertases
        _invComboBoxes = new ArrayList<JComboBox>(0);
        // we need to remember the format for this construct
        _partFormat = partFormat;

        _constructKey="";
        
        _levels = 0;
        _totalPermutations = 0;
        _currentTabIndex = 0;

        // give the results panel a border layout
        setLayout(new BorderLayout());


        // create the key pane which holds the key table
        _keyPane = createKeyPane(desiredPerms);
        // create the tree panel
        _treePanel = createTreePanel(mousePlugin, desiredPerms);

        // create the save and export panel
        _savePanel = createSavePanel();
        _savePanel.setVisible(true);


//
//
//
//        JPanel keyPane = createKeyPane(desiredPerms);
//        _keyPanel = new JPanel();
//        _keyPanel.setLayout(new BorderLayout());
//        _keyPanel.add(keyPane,BorderLayout.CENTER);
//
//        JPanel treePanel = createTreePanel(desiredPerms);
//        _treePanel = new JPanel();
//        _treePanel.setLayout(new BorderLayout());
//        _treePanel.add(treePanel,BorderLayout.CENTER);
//
//        JPanel savePanel = createSavePanel();
//        _savePanel = new JPanel();
//        _savePanel.setLayout(new BorderLayout());
//        _savePanel.add(savePanel,BorderLayout.CENTER);


        // create the design info
        _designPane = createDesignPane(/*desiredPerms*/);
        _designPane.setVisible(true);

        // create the quickSummary
        _quickSummary = createQuickSummary();
        _quickSummary.setVisible(true);


        // create a tabbed pane to store everything the three panels above
        _resultsTabbedPane = new JTabbedPane();


//            _resultsTabbedPane.addChangeListener(new ChangeListener()
//            {
//                public void stateChanged(ChangeEvent e) {
//                    int index = _resultsTabbedPane.getSelectedIndex();
//                    if (_currentTabIndex == 1)
//                    {
//                        _resultsTabbedPane.requestFocus();
//                    }
//                    _currentTabIndex = index;
//                System.out.println("Tab=" + _resultsTabbedPane.getSelectedIndex());
//            }
//            });



        // add the design info
        _resultsTabbedPane.add("Design Summary",_designPane);
        // add the key table and key tree to the the results tabbed pane
        if (table)
            _resultsTabbedPane.addTab("Invertase Key Table",_keyPane);
        if (tree)
            _resultsTabbedPane.addTab("Invertase Tree",_treePanel);
        _resultsTabbedPane.addTab("Save and Export",_savePanel);


        // then add the results tabbed pane and the design info to the results tab
        add(_quickSummary,BorderLayout.NORTH);
        add(_resultsTabbedPane,BorderLayout.CENTER);

        _isKeyTabVisible = table;
        _isTreeTabVisible = tree;
    }




    public ResultsPanel(InvertSim invObj,ArrayList<String> partsSet, String[][] treeTable, ArrayList<String>desiredPerms,
            String newConstructKey, String oldConstructKey, MyMousePlugin mousePlugin, Format partFormat, boolean table, boolean tree)
    {

        // the invertSim object that represents this result
        _invObj = invObj;
        // partsSet does not inlcude P1, P2, etc. at the beginning of each part name
        _partsSet = partsSet;
        // invComboBoxes will hold all of the necessary combo boxes for the invertases
        _invComboBoxes = new ArrayList<JComboBox>(0);
        // we need to remember the format for this construct
        _partFormat = partFormat;

        _constructKey = oldConstructKey.concat(newConstructKey);

        _levels = 0;
        _totalPermutations = 0;
        _currentTabIndex = 0;

        // give the results panel a border layout
        setLayout(new BorderLayout());


        // create the key pane which holds the key table
        _keyPane = createKeyPane(treeTable,newConstructKey,oldConstructKey);
        // create the tree panel
        _treePanel = createTreePanel(mousePlugin, desiredPerms);

        // create the save and export panel
        _savePanel = createSavePanel();
        _savePanel.setVisible(true);
//
//
//
//        JPanel keyPane = createKeyPane(desiredPerms);
//        _keyPanel = new JPanel();
//        _keyPanel.setLayout(new BorderLayout());
//        _keyPanel.add(keyPane,BorderLayout.CENTER);
//
//        JPanel treePanel = createTreePanel(desiredPerms);
//        _treePanel = new JPanel();
//        _treePanel.setLayout(new BorderLayout());
//        _treePanel.add(treePanel,BorderLayout.CENTER);
//
//        JPanel savePanel = createSavePanel();
//        _savePanel = new JPanel();
//        _savePanel.setLayout(new BorderLayout());
//        _savePanel.add(savePanel,BorderLayout.CENTER);


        // create the design info
        _designPane = createDesignPane(/*desiredPerms*/);
        _designPane.setVisible(true);

        // create the quickSummary
        _quickSummary = createQuickSummary();
        _quickSummary.setVisible(true);


        // create a tabbed pane to store everything the three panels above
        _resultsTabbedPane = new JTabbedPane();


//            _resultsTabbedPane.addChangeListener(new ChangeListener()
//            {
//                public void stateChanged(ChangeEvent e) {
//                    int index = _resultsTabbedPane.getSelectedIndex();
//                    if (_currentTabIndex == 1)
//                    {
//                        _resultsTabbedPane.requestFocus();
//                    }
//                    _currentTabIndex = index;
//                System.out.println("Tab=" + _resultsTabbedPane.getSelectedIndex());
//            }
//            });



        // add the design info
        _resultsTabbedPane.add("Design Summary",_designPane);
        // add the key table and key tree to the the results tabbed pane
        if (table)
            _resultsTabbedPane.addTab("Invertase Key Table",_keyPane);
        if (tree)
            _resultsTabbedPane.addTab("Invertase Tree",_treePanel);
        _resultsTabbedPane.addTab("Save and Export",_savePanel);


        // then add the results tabbed pane and the design info to the results tab
        add(_quickSummary,BorderLayout.NORTH);
        add(_resultsTabbedPane,BorderLayout.CENTER);

        _isKeyTabVisible = table;
        _isTreeTabVisible = tree;
    }






    private JPanel createSavePanel()
    {
        
        JPanel namePanel = createNamePanel();

        JScrollPane topAssignPanel = createInvAssignPanel();

        // delivered to the user
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());

        // place each one of these panels inside the outer panel
        savePanel.add(namePanel, BorderLayout.NORTH);
        savePanel.add(topAssignPanel, BorderLayout.CENTER);

        // return the outer panel
        return savePanel;
    }



    private JScrollPane createInvAssignPanel()
    {
        // Invertase Assignment Panel
        // inner panel
        JPanel innerAssignPanel = new JPanel();
        // get the invertases in the design
        _designInvs = _invObj.getInvertases();
        // give it a grid layout
        innerAssignPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = .5;
        c.fill = GridBagConstraints.CENTER;
        innerAssignPanel.add(new JLabel("Invertases"),c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = .5;
        c.fill = GridBagConstraints.CENTER;
        c.ipady = 10;
        innerAssignPanel.add(new JLabel("Choices"),c);
        c.ipady = 0;

        // loop through and create each label and each drop down box
        for (int i=0; i<_designInvs.size(); i++)
        {
            c.gridx = 0;
            c.gridy = i+1;
            c.weightx = .5;
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(0,0,0,0);
            innerAssignPanel.add(new JLabel(_designInvs.get(i)+":   "),c);
            c.gridx = 1;
            c.gridy = i+1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = .2;
            c.insets = new Insets(2,0,2,10);
            JComboBox invBox = new JComboBox();
            _invComboBoxes.add(invBox);
            innerAssignPanel.add(invBox,c);
        }

        c.gridx = 0;
        c.gridy = _designInvs.size()+1;
        c.anchor = GridBagConstraints.SOUTH;
        c.fill = GridBagConstraints.NONE;
        c.weighty = 1;
        c.weightx = 0;
        c.ipadx = 0;
        c.ipady = 10;
        c.gridwidth = 2;
        c.insets = new Insets(15,0,15,0);
        _saveButton = new JButton("Save Design to Database");
        innerAssignPanel.add(_saveButton,c);


        // assign an action listener to each save button
        _saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _saveButtonActionPerformed(evt);
            }
        });


        // stick the inner panel in a scroll panel
        JScrollPane assignPane = new JScrollPane(innerAssignPanel);
        assignPane.setBorder(new TitledBorder("Assign Invertases"));

        return assignPane;
    }




    private JPanel createNamePanel()
    {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new GridBagLayout());
        namePanel.setBorder(new TitledBorder("Name Your Design"));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;

        namePanel.add(new JLabel("Part Name:    "),c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        c.insets = new Insets(5,0,5,0);
        _nameField = new JTextField();
        namePanel.add(_nameField,c);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.insets = new Insets(0,0,5,0);
        c.anchor = GridBagConstraints.NORTHEAST;
        namePanel.add(new JLabel("Short Description:    "),c);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = .5;
        c.insets = new Insets(0,0,5,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        _descriptionTextArea = new JTextArea();
        //_descriptionTextArea.setPreferredSize(new Dimension(75,50));
        _descriptionTextArea.setWrapStyleWord(true);
        JScrollPane descriptionPane = new JScrollPane(_descriptionTextArea);
        descriptionPane.setBorder(new LineBorder(Color.GRAY));
        descriptionPane.setPreferredSize(new Dimension(75,50));
        namePanel.add(descriptionPane,c);

        return namePanel;
    }



    /**
     * Returns the tree panel with the current
     * @param desiredPerms
     * @return
     */
    private JPanel createTreePanel(MyMousePlugin mousePlugin, ArrayList<String> desiredPerms)
    {
            // create the tree panel
            JPanel jTreePanel = new JPanel();
            // add the new key tree
            jTreePanel.setLayout(new BorderLayout());
            _keyTree = new KeyTree(_invObj,keyArray, desiredPerms, mousePlugin);
            jTreePanel.add(_keyTree, BorderLayout.CENTER);

            _totalPermutations = _keyTree._numberOfNodes;

            return jTreePanel;
    }



    /**
     * Generates the design info for the created construct.
     * @return
     */
    private ColorPane createDesignPane(/*ArrayList<String> desiredPerms*/)
    {


        ColorPane designInfo = new ColorPane();

        StyledDocument doc = designInfo.getStyledDocument();
        doc.putProperty(PlainDocument.tabSizeAttribute,2);

        designInfo.setStyledDocument(doc);

        designInfo.appendHeader(Color.black,"",6,false,false);
        designInfo.appendHeader(Color.black, "CONSTRUCT SUMMARRY", 14,true, false);
        designInfo.appendLabel(myBlue,"\tAlgorithm:\t\t",12);
        designInfo.append(myOrange,_invObj.getAlgorithm()+"\n",12);
        designInfo.appendLabel(myBlue,"\tTotal Invertases:\t",12);
        designInfo.append(myOrange, Integer.toString(_designInvs.size())+"\n",12);
        designInfo.appendLabel(myBlue, "\tString Design:\t",12);
        designInfo.append(myOrange, _invObj.designString()+"\n\n",12);


        designInfo.appendHeader(Color.black, "PERMUTATION SUMMARY", 14,true, false);
        designInfo.appendLabel(myBlue,"\tNumber of Levels:\t",12);
        designInfo.append(myOrange,Integer.toString(_levels)+"\n",12);
        designInfo.appendLabel(myBlue, "\tDesired Permutations:\t",12);
        designInfo.append(myOrange,Integer.toString(keyArray.length/*desiredPerms.size()*/)+"\n",12);
        designInfo.appendLabel(myBlue,"\tTotal Permutations Hit:\t",12);
        designInfo.append(myOrange,Integer.toString(this._totalPermutations)+"\n",12);
        designInfo.appendLabel(myBlue, "\tStarting Permutation:\t",12);
        designInfo.append(myOrange, this.getPartString()+"\n\n",12);
        

        designInfo.appendHeader(Color.black, "PARTS SUMMARY",14, true, false);
        designInfo.appendLabel(myBlue, "\tFormat:\t",12);
        designInfo.append(myOrange,this._partFormat.toString()+"\n",12);

        // remind the user what each part represents
        for (int i=1; i<=_partsSet.size(); i++)
        {
            designInfo.appendLabel(myBlue, "\tP"+i+":\t",12);
            designInfo.append(myOrange,_partsSet.get(i-1)+"\n",12);
        }




        designInfo.setVisible(true);


        return designInfo;
    }



    /**
     * Handles the creation of the Key Table pane.
     * @param desiredPerms
     * @return
     */
    private JScrollPane createKeyPane(ArrayList<String> desiredPerms)
    {
            // generate the keyTable
            keyArray = _invObj.tailorDesign(desiredPerms);

            String[] parsedKey;
            for (int i=0; i<keyArray.length; i++)
            {
                parsedKey = keyArray[i][1].split(" ");
                if (parsedKey.length>_levels)
                    _levels = parsedKey.length;
            }

            // create the column headers
            String col[] = {"Desired Part Permutations","Invertase Key"};

            // create the actual JTable that houses the keys
            JTable jKeyTable = new JTable(keyArray, col);

            // change the header color of the table
            jKeyTable.getTableHeader().setBackground(new Color(0,145,255));

            Font body = new Font("Tahoma", Font.PLAIN, 12);
            jKeyTable.setFont(body);
            Font header = new Font("Tahoma", Font.BOLD, 14);
            jKeyTable.getTableHeader().setFont(header);
            jKeyTable.getTableHeader().setForeground(Color.white);


            // put this key table into a scroll pane
            JScrollPane jKeyPane = new JScrollPane(jKeyTable);


            return jKeyPane;
    }




    /**
     * Handles the creation of the Key Table pane.
     * @param desiredPerms
     * @return
     */
    private JScrollPane createKeyPane(String[][] treeTable, String newConstructKey, String oldConstructKey)
    {
            // generate the keyTable
//            keyArray = _invObj.tailorDesign(desiredPerms);
            keyArray = treeTable;

            String combinedKey = oldConstructKey.concat(newConstructKey);

            // apply the construct key
            String[] combinedKeyArray = combinedKey.split(" ");
            for (int i=0; i<combinedKeyArray.length; i++)
            {
                if(this._invObj.isPossible(combinedKeyArray[i]))
                    this._invObj.invert();
            }

            String[] newKeyArray = newConstructKey.split(" ");

            // now remove the construct key from each line of the table
            for (int i=1; i<keyArray.length; i++)
            {
                String[] key = keyArray[i][1].split(" ");
                String editedKey = "";
                for (int j=0; j<newKeyArray.length; j++)
                    key[j] = null;
                for (int j=0; j<key.length; j++)
                {
                    if (key[j]!=null)
                        editedKey = editedKey.concat(key[j]+" ");
                }
                // finally replace the original with the edited
                keyArray[i][1] = editedKey;
            }


            // eliminate unnecessary invertases
            this._invObj.removeInvertases(keyArray);


            // determine the largest level
            String[] parsedKey;
            for (int i=0; i<keyArray.length; i++)
            {
                parsedKey = keyArray[i][1].split(" ");
                if (parsedKey.length>_levels)
                    _levels = parsedKey.length;
            }

            // create the column headers
            String col[] = {"Desired Part Permutations","Invertase Key"};

            // create the actual JTable that houses the keys
            JTable jKeyTable = new JTable(keyArray, col);

            // change the header color of the table
            jKeyTable.getTableHeader().setBackground(new Color(0,145,255));

            Font body = new Font("Tahoma", Font.PLAIN, 12);
            jKeyTable.setFont(body);
            Font header = new Font("Tahoma", Font.BOLD, 14);
            jKeyTable.getTableHeader().setFont(header);
            jKeyTable.getTableHeader().setForeground(Color.white);


            // put this key table into a scroll pane
            JScrollPane jKeyPane = new JScrollPane(jKeyTable);


            return jKeyPane;
    }



    private JPanel createQuickSummary()
    {
        JPanel quickSum = new JPanel();

        quickSum.setLayout(new GridLayout(2,3,0,5));

        GridBagConstraints c = new GridBagConstraints();
        
        JLabel title = new JLabel("QUICK SUMMARY", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 12));
        
        quickSum.add(new JLabel(""));
        quickSum.add(title);
        quickSum.add(new JLabel(""));
//        c.gridx = 0;
//        c.gridy = 0;
//        c.insets = new Insets(5,0,0,5);
//        c.anchor = GridBagConstraints.CENTER;
//        c.gridwidth = 3;
//        quickSum.add(title,c);

        JLabel modelType = new JLabel("Algorithm:  "+this._invObj.getAlgorithm(), JLabel.CENTER);
        modelType.setForeground(myBlue);
        modelType.setFont(new Font("Tahoma", Font.BOLD, 11));
        JLabel perms = new JLabel("Total Permutations:  "+Integer.toString(this._totalPermutations), JLabel.CENTER);
        perms.setForeground(myBlue);
        perms.setFont(new Font("Tahoma", Font.BOLD, 11));
        JLabel invs = new JLabel("Invertases Required:  "+Integer.toString(this._designInvs.size()), JLabel.CENTER);
        invs.setForeground(myBlue);
        invs.setFont(new Font("Tahoma", Font.BOLD, 11));

        quickSum.add(modelType);
        quickSum.add(perms);
        quickSum.add(invs);



//        c.gridx = 0;
//        c.gridy = 1;
//        c.weightx = .5;
//        c.gridwidth = 1;
//        c.anchor = GridBagConstraints.CENTER;
//
//        quickSum.add(modelType,c);
//
//        c.gridx = 1;
//        c.gridy = 1;
//        c.weightx = .5;
//        c.anchor = GridBagConstraints.CENTER;
//
//        quickSum.add(perms,c);
//
//        c.gridx = 2;
//        c.gridy = 1;
//        c.weightx = .5;
//        c.anchor = GridBagConstraints.CENTER;
//
//        quickSum.add(invs,c);

//        JTextArea permSum = new JTextArea();
//
//        DefaultHighlighter hilit = new DefaultHighlighter();
//        DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(myBlue);
//        permSum.setHighlighter(hilit);
//        int[][] highlightSites = new int[3][2];
//
//
//        permSum.append("Algorithm:  ");
//        highlightSites[0][0] = permSum.getCaretPosition();
//        permSum.append(this._invObj.getAlgorithm());
//        highlightSites[0][1] = permSum.getCaretPosition();
//        permSum.append("    |    Hits ");
//        highlightSites[1][0] = permSum.getCaretPosition();
//        permSum.append(Integer.toString(this._totalPermutations));
//        highlightSites[1][1] = permSum.getCaretPosition();
//        permSum.append(" permutations using ");
//        highlightSites[2][0] = permSum.getCaretPosition();
//        permSum.append(Integer.toString(this._designInvs.size()));
//        highlightSites[2][1] = permSum.getCaretPosition();
//        permSum.append(" invertases.");
//
//
//        try
//        {
//            for (int i=0; i<highlightSites.length; i++)
//            {
//                hilit.addHighlight(highlightSites[i][0],highlightSites[i][1], painter);
//            }
//
//        }
//        catch (BadLocationException e)
//        {
//            e.printStackTrace();
//        }
//
//        quickSum.add(permSum,BorderLayout.NORTH);
//
//
//
        return quickSum;
    }


//    public void highlightText(JTextArea area, String text)
//    {
//
//        DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);
//        Highlighter hilit = area.getHighlighter();
//
//        int startPos = area.getCaretPosition();
//        area.append(text);
//        int endPos = area.getCaretPosition();
//
//        try
//        {
//            hilit.addHighlight(startPos, endPos, painter);
//        }
//        catch (BadLocationException e)
//        {
//            e.printStackTrace();
//        }
//
//        area.repaint();
//
//    }



    /**
     * Allows the GUI to update the invertase combo boxes whenever the invertase
     * list is changed.
     * @param invertases
     */
    public void setInvComboBoxes(ArrayList<String> invertases)
    {
        for (int i=0; i<_invComboBoxes.size(); i++)
        {
            _invComboBoxes.get(i).setModel(new DefaultComboBoxModel(invertases.toArray()));
        }
    }


    /**
     * Toggles the key table tab on and off for this result.
     */
    public void toggleKeyTab()
    {
        if (_isKeyTabVisible)
        {
            this._resultsTabbedPane.remove(_keyPane);
            _isKeyTabVisible = false;
        }
        else
        {
            this._resultsTabbedPane.insertTab("Invertase Key Table", null, _keyPane, TOOL_TIP_TEXT_KEY,1);
            _isKeyTabVisible = true;
        }
    }


    /**
     * Toggles the key tree tab on and off for this result.
     */
    public void toggleTreeTab()
    {
        if (_isTreeTabVisible)
        {
            this._resultsTabbedPane.remove(_treePanel);
            _isTreeTabVisible = false;
        }
        else
        {
            if (this._isKeyTabVisible)
                this._resultsTabbedPane.insertTab("Invertase Tree", null, _treePanel, TOOL_TIP_TEXT_KEY, 2);
            else
                this._resultsTabbedPane.insertTab("Invertase Tree", null, _treePanel, TOOL_TIP_TEXT_KEY, 1);
            _isTreeTabVisible = true;
        }

    }


    /**
     * When the user decides to save their construct, this action listener
     * composes the composite part and lets the user know if there are any
     * issues regarding their invertase assignment, name, or description.
     * @param evt
     */
     public void _saveButtonActionPerformed(java.awt.event.ActionEvent evt)
     {
        // when the create design button is pressed, generate the results for the user

        ArrayList<String> invertaseCheck = new ArrayList<String>(0);
        int option = -1;


        for (int i=0; i<this._invComboBoxes.size(); i++)
        {
            String currentInvertase = (String) _invComboBoxes.get(i).getSelectedItem();
            if (!(invertaseCheck.contains(currentInvertase)))
                invertaseCheck.add(currentInvertase);
            else
            {
                // pop up the dialog warning the user
                Object[] options = {"Yes, Save", "Cancel"};
                option = JOptionPane.showOptionDialog(new JFrame(),
                "You have assigned one of the invertases twice.\n"
                +"Are you sure you wish to save this construct?",
                "Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title

                // and break
                break;
            }
        }


        if (option==JOptionPane.YES_OPTION || option==-1)
        {





        // get the name and description of the new construct
        final String designName = _nameField.getText();
        final String description = _descriptionTextArea.getText();

        // create the array list of all parts
        final ArrayList<Part> construct = new ArrayList<Part>(0);
        
        // the part that will be appended to the construct
        Part partToAdd;
        Part invertaseToAdd;

        // the current part from the design array that is being examined
        String currentPart = "";
        String invName = "";
        String partName = "";

        // keeps track of the number associated with either a part or an invertase
        int partNum = -1;

        // keeps track of the index of the comboBox referenced for invertase assignment
        int comboIndex = -1;

        // go through design string and assign the invertases
            for (int i=0; i<_invObj.designArray.length; i++)
            {
                // get the part to be examined
                currentPart = _invObj.designArray[i];

                // if the current part is an invertase
                if (currentPart.startsWith("I"))
                {
                    if (currentPart.endsWith("'"))
                    {
                        currentPart = currentPart.substring(0,currentPart.length()-1);
                    }

                    // now that we know the invertase, get the index of it's combo box
                    comboIndex = _designInvs.indexOf(currentPart);
                    // with this index, get the current option in that combo box
                    invName = (String)_invComboBoxes.get(comboIndex).getSelectedItem();

                    // look up that invertase
                    invertaseToAdd = Part.retrieveByName(invName);
                   
                    // now add that part to the construct
                    construct.add(invertaseToAdd);
                }

                // if the current part is an actual part
                else if (currentPart.startsWith("P"))
                {
                    if (currentPart.endsWith("'"))
                    {
                        partNum = Integer.parseInt(currentPart.substring(1,currentPart.length()-1));
                    }
                    else
                    {
                        partNum = Integer.parseInt(currentPart.substring(1,currentPart.length()));
                    }

                    // now that we have the part number, we can directly get the part name
                    partName = _partsSet.get(partNum-1);
                    // look up that part
                    partToAdd = Part.retrieveByName(partName);
                    // add it to the construct
                    construct.add(partToAdd);
                }
             }

            // if the user has not specified a name, don't save the part
            if (designName.equals(""))
            {
                //default title and icon
                JOptionPane.showMessageDialog(new JFrame(),
                "You must specify a name for the design!");
            }

            // if the user has not specified a descriptions, don't save the part
            else if (description.equals(""))
            {
                //default title and icon
                JOptionPane.showMessageDialog(new JFrame(),
                "You must specify a description for the design!");
            }

            // if the user has specified everything correctly, save the part
            // as a background process
            else
            {
                // save the part in a background process
                new SwingWorker() {
                protected Object doInBackground() throws Exception {
                    //Insert todo code                  
                    
                    // with the construct put together, it's time to save it to the database
                    Part design = Part.generateComposite(construct, null, _partFormat, Collector.getCurrentUser(), designName, description);
                    // save the part
                    design.saveDefault();

                    Collection partsCollection = Collector.getCurrentUser().getHerCollection();
                    partsCollection.addObject(design);
                    partsCollection.saveDefault();

                    //custom title, no icon
                    JOptionPane.showMessageDialog(new JFrame(),
                        "Your construct has been successfully saved!",
                        "Construct Saved",
                        JOptionPane.PLAIN_MESSAGE);
                    
                    return null;
                }
                }.execute();
          }
        }
    }


    /**
     * Returns the InvertSim object associated with these results.
     * @return
     */
    public InvertSim getInvertSim()
    {
        return _invObj;
    }

    public String getPartString()
    {
//        String partString = "";
//        for (int i=0; i<_partsSet.size(); i++)
//        {
//            if (i!=0)
//                partString = partString.concat("  |  ");
//            partString = partString.concat(_partsSet.get(i));
//        }
//
//        return partString;


        String partConfig = this._invObj.partsString();
        String[] partArray = partConfig.split(" ");
        String partString = "";
        int partIndex = 0;

        for (int i=0; i<partArray.length; i++)
        {
            if (i!=0)
                partString = partString.concat("  |  ");

            partIndex = _invObj.currentPerm[i]-1;

            partString = partString.concat(_partsSet.get(partIndex));

            if (partArray[i].endsWith("'"))
                partString = partString.concat("'");
        }

        return partString;
    }



    public ArrayList<String> getPartsSet()
    {
        return _partsSet;
    }

    public KeyTree getTree()
    {
        return _keyTree;
    }

    public Format getFormat()
    {
        return _partFormat;
    }

    public String getConstructKey()
    {
        return _constructKey;
    }
    
    //\\//\\//\\//\\//\\//\\ ------ FIELDS ------ //\\//\\//\\//\\//\\//\\//\\//

    private InvertSim _invObj;
        // the invertase object which these results represent
    private ArrayList<String> _partsSet;
        // the parts that were used to create this construct, each string is the name of the part
    private JScrollPane _keyPane;
        // the scroll pane which holds the key table (first tab)
    private boolean _isKeyTabVisible;
        // determines whether the key pane is visible
    private ColorPane _designPane;
        // the color pane which describes the design to the user, at the top of the result tab
    public JPanel _treePanel;
        // panel which contains the invertase tree (second tab)
    private boolean _isTreeTabVisible;
        // determines whether the invertase tree is visible
    private JPanel _savePanel;
        // panel which allows the user to save the construct (thrid tab)
    private JTabbedPane _resultsTabbedPane;
        // tabbed pane which holds all of the result tabs for this construct
    private ArrayList<JComboBox> _invComboBoxes;
        // holds all of the invertase combo boxes within the save panel
    private JButton _saveButton;
        // button which allows the user to save the construct (MAY NOT NEED TO BE A FIELD)
    private JTextField _nameField;
        // field which allows the user to specify the name of their construct
    private JTextArea _descriptionTextArea;
        // field which allows the user to specify a description of their construct
    private ArrayList<String> _designInvs;
        // the list of invertases that are necessary for the design
    private Format _partFormat;
        // format of all the parts made up in the design
    private int _levels;
    private int _totalPermutations;
    private JPanel _quickSummary;
    public KeyTree _keyTree;
    public int _currentTabIndex;
    public String[][] keyArray;
    public String _constructKey;

    private static final Color myBlue = new Color(0,145,255);
    private static final Color myOrange = new Color(255,120,0);

}
