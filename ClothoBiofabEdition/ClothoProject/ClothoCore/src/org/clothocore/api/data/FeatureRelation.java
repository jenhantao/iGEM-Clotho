/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocore.api.data;

/**
 *
 * @author sbhatia
 */

import org.clothocore.api.core.Collector;
import org.clothocore.api.plugin.ClothoConnection;
import java.util.Date;
//import org.clothocore.api.dnd.RefreshEvent.Condition;
/**
 * A FeatureRelation encodes a relationship between two Features. The relationship is binary,
 * and has a type.
 */
public class FeatureRelation extends ObjBase {

    public FeatureRelation ( FeatureRelationDatum d ) {
        super( d );
        _featRelDatum = d;
    }

    public FeatureRelation( String name, String f1Uuid1, String f2Uuid2, String relType) {
        super( );
        _featRelDatum = new FeatureRelationDatum();
        _datum = _featRelDatum;
        
        
        
        _featRelDatum._f1Uuid = f1Uuid1;
        _featRelDatum._f2Uuid = f2Uuid2;
        _featRelDatum._relType = relType;

        _datum.uuid = _uuid;
        _datum.name = name;
        _datum.dateCreated = new Date();
        _datum.lastModified = new Date();
        _datum._isChanged = true;

        System.out.println("Saving FR named " + name);
    }


    /**
     * Recursively save all child elements and then call ObjBase to save itself.
     */
/*
    @Override
    public boolean save( ClothoConnection conn ) {
        System.out.println( "============ Starting FeatureRelation save" );

        return super.save( conn );
    }
*/
    @Override
    public ObjType getType() {
        return ObjType.FEATURERELATION;
    }

    public Feature getFeature1() {
        return Collector.getFeature( _featRelDatum._f1Uuid );
    }

    public Feature getFeature2() {
        return Collector.getFeature( _featRelDatum._f2Uuid );
    }

    public String getF1Uuid() {
        return  _featRelDatum._f1Uuid;
    }

    public String getF2Uuid() {
        return  _featRelDatum._f2Uuid;
    }

    public String getRelType() {
        return _featRelDatum._relType;
    }

    public String getName() {
        return _featRelDatum.name;
    }


    /* What does this do? */
    @Override
    public boolean addObject( ObjBase dropObject ) {
        switch ( dropObject.getType() ) {
            default:
                return false;
        }
    }

    @Override
    public boolean isChanged() {
        return _featRelDatum._isChanged;
    }
/*********************************************/
    private FeatureRelationDatum _featRelDatum;

    private void setChanged() {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public static class FeatureRelationDatum extends ObjBase.ObjBaseDatum {


        public String _f1Uuid;
        public String _f2Uuid;
        public String _relType;


        @Override
        public ObjType getType() {
            return ObjType.FEATURERELATION;
        }

    }

    /******* FIELDS *******/
    public enum Fields {
        NAME,
        FEATURE1,
        FEATURE2,
        RELTYPE,
        DATE_CREATED,
        LAST_MODIFIED
    }
}
