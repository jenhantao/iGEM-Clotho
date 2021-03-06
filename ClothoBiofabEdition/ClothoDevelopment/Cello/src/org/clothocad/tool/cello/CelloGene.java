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
public class CelloGene extends CelloPrimitive {


    public CelloGene()
    {
        _type= CelloPrimitiveType.Gene;

    }
    
    public CelloGene(String s, int i)
    {
        _id = i;
        _sequence = s;
        _type= CelloPrimitiveType.Gene;
    }

    public CelloGene(CelloGene g)
    {
        _id = g._id;
        _sequence = g._sequence;
        _type= g._type;
    }

    public CelloGene Copy ()
    {
        return new CelloGene(this);
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
        return "Gene(" + _id + "," + _sequence + ")\n";
    }

    //private String _sequence;
    //private int _id;


}
