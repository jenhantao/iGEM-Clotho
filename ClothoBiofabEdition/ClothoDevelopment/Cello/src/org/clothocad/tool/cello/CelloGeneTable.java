/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.cello;

import java.util.ArrayList;

/**
 *
 * @author Douglas Densmore
 */
public class CelloGeneTable {

    
    public CelloGeneTable()
    {
        _table = new ArrayList<CelloGene>();
    }

    public void add(CelloGene cg)
    {
        int index = _table.size();
        cg.setId(index);
        _table.add(index, cg);
    }

    public int Size ()
    {
        return _table.size();
    }

    public CelloGene get(CelloGene g)
    {
        return _table.get(_table.indexOf(g));
    }

    public CelloGene get(int i)
    {
        return _table.get(i);
    }


    public ArrayList<CelloGene> getTable(){
         return _table;
    }
    private ArrayList<CelloGene> _table;
}
