/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.cello;

import java.util.HashMap;

/**
 *
 * @author rozaghamari
 */
public class CelloRGenePromoterTable {

    public CelloRGenePromoterTable()
    {
        _table = new HashMap<CelloRPromoter, CelloGene>(); //key = Rpromoter id and Value = Gene id
    }

    public void add( CelloRPromoter crpK, CelloGene cgV)
    {
        _table.put( crpK, cgV);
    }

    public CelloGene getGene(CelloRPromoter crpK)
    {
        return  _table.get(crpK);
    }
    public  HashMap<CelloRPromoter, CelloGene> getTable ()
    {
        return this._table;
    }

    private HashMap<CelloRPromoter, CelloGene> _table;

}
