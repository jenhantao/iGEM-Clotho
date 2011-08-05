/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.cello;

import org.clothocore.api.data.Feature;
import org.clothocore.api.data.Part;

/**
 *
 * @author Douglas Densmore
 */
public class CelloPrimitive {


    public String print()
    {
        return "CelloPrimitive\n";
    }

    public String printDNA()
    {
        if (_feature != null)
            return _feature.getName();
        return "N/A";
    }

    public CelloPrimitive Copy()
    {
        return null;
    }
    

    protected String _sequence;
    protected int _id;
    protected CelloPrimitiveType _type;
    protected Feature _feature;

    Part getPart() {
        if (this._feature!= null)
            return dbConnection.getPart(this);
        else return null;
    }
}
