package org.clothocad.tool.trumpet;

import java.util.ArrayList;


/**
 *
 * @author Craig LaBoda
 *
 */
public class InvEdgeSet {

    public InvEdgeSet()
    {
        _set = new ArrayList<String>(0);
        _edgeSet = new ArrayList<InvEdge>(0);
    }

    public void addEdge(InvEdge edge)
    {
        _set.add(edge.toString());
        _edgeSet.add(edge);
    }

    public boolean doesEdgeExist(String source, String target, String invNum)
    {
        if (_set.contains(source+" "+target))
            return true;
        else
            return false;
    }

    public int getSize()
    {
        return _set.size();
    }

    public InvEdge getEdge(String source, String target)
    {
        int index = _set.indexOf(source+" "+target);
        if (index!=-1)
            return _edgeSet.get(index);
        else
            return null;
    }

    private ArrayList<String> _set = new ArrayList<String>(0);
    private ArrayList<InvEdge> _edgeSet = new ArrayList<InvEdge>(0);

}
