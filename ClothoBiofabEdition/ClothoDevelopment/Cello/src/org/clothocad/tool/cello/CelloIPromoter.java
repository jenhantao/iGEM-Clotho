/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.cello;

import org.clothocore.api.data.Feature;

/**
 *
 * @author Douglas Densmore
 */
public class CelloIPromoter extends CelloPrimitive {


    public CelloIPromoter()
    {
        _type = CelloPrimitiveType.IPromoter;
    }

    public CelloIPromoter(String s, int i)
    {
        _id = i;
        _sequence = s;
        _type = CelloPrimitiveType.IPromoter;
    }

    public CelloIPromoter(CelloIPromoter cip)
    {
        _id = cip._id;
        _sequence = cip._sequence;
        _type = cip._type;
        _feature = cip._feature;
    }

    public CelloIPromoter Copy()
    {
        return new CelloIPromoter(this);
    }
    
    public String getSequence(){
            return _sequence;
    }

    public int getId(){
        return _id;
    }

    public void setId(int i)
    {
        _id = i;
    }
    public void setFeature(Feature f)
    {
        _feature = f;
    }

    public Feature getFeature ()
    {
        return this._feature;
    }
    @Override
    public String print()
    {
        return "IPomoter(" + _id + "," + _sequence + ")\n";
    }

    //private String _sequence;
    //private int _id;
}
