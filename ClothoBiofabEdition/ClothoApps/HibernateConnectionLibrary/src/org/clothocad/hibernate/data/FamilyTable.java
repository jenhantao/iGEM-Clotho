/*
Copyright (c) 2009 The Regents of the University of California.
All rights reserved.
Permission is hereby granted, without written agreement and without
license or royalty fees, to use, copy, modify, and distribute this
software and its documentation for any purpose, provided that the above
copyright notice and the following two paragraphs appear in all copies
of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
ENHANCEMENTS, OR MODIFICATIONS..
 */
package org.clothocad.hibernate.data;
// Generated Jun 1, 2010 8:47:54 PM by Hibernate Tools 3.2.1.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.clothocore.api.data.Family;
import org.clothocore.api.data.Family.FamilyDatum;
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.data.ObjBase.ObjBaseDatum;
import org.clothocore.api.data.ObjType;
import org.clothocad.hibernate.hibernateDatum;
import org.clothocad.hibernate.HibernateConnection;

/**
 * FamilyTable generated by hbm2java
 */
public class FamilyTable implements java.io.Serializable, hibernateDatum {

    public static ObjType getType( Enum field ) {
        if ( field.getDeclaringClass() != Family.Fields.class ) {
            return null;
        }

        Family.Fields f = (Family.Fields) field;

        switch ( f ) {
            case AUTHOR:
                return ObjType.PERSON;
            case SUPER_FAMILY:
                return ObjType.FAMILY;
            case CHILD_FAMILIES:
                return ObjType.FAMILY;
            default:
                return null;
        }
    }

    public FamilyTable( Family f ) {
        this.idFamily = f.getUUID();
        this.name = f.getName();
        this.dateCreated = f.getDateCreated();
        this.lastModified = f.getLastModified();
        this.soterm = f.getSOTerm();
        if ( f.isExtendable() ) {
            this.isExtendable = 1;
        } else {
            this.isExtendable = 0;
        }

        this.personTable = new PersonTable( f.getAuthorUUID() );
        this.riskGroup = f.getRiskGroup();
        this.description = f.getDescription();
        this.level = f.getLevel();
        if(f.getSuperFamily()!=null) {
            this.familyTable = new FamilyTable( f.getSuperFamily().getUUID() );
        }

   //     this.familyData = f.getDataDescription();  //Temporarily disabled for clotho2.0

        if(!f.isInDatabase()) {
            needsSecondaryProcessing = true;
            return;
        }

        HashSet<String> existingLinks = new HashSet<String>();

        //Remove old Note:Family links
        String query = "from FamilyNoteXref where familyId='" + idFamily + "'";
        Iterator xrefs = HibernateConnection.connection.query( query );
        while ( xrefs.hasNext() ) {
            FamilyNoteXref cx = (FamilyNoteXref) xrefs.next();
            String uuid = cx.getId().getNoteId();
            existingLinks.add( uuid );
            if ( !f.getNoteLinks().contains( uuid ) ) {
                HibernateConnection.connection.deleteDatum( cx );
            }
        }

        //Save all the new Xrefs for notes
        for ( String uuid : f.getNoteLinks() ) {
            if ( !existingLinks.contains( uuid ) ) {
                FamilyNoteXrefId cxi = new FamilyNoteXrefId( idFamily, uuid );
                FamilyNoteXref cxr = new FamilyNoteXref( cxi, new NoteTable( uuid ), new FamilyTable( idFamily ) );
                familyNoteXrefs.add( cxr );
                HibernateConnection.connection.saveDatum( cxr );
            }
        }
    }

    @Override
    public void runSecondaryProcessing(ObjBase obj) {
        Family f = (Family) obj;
        //Save all the new Xrefs for notes
        for ( String uuid : f.getNoteLinks() ) {
            FamilyNoteXrefId cxi = new FamilyNoteXrefId( idFamily, uuid );
            FamilyNoteXref cxr = new FamilyNoteXref( cxi, new NoteTable( uuid ), new FamilyTable( idFamily ) );
            familyNoteXrefs.add( cxr );
            HibernateConnection.connection.saveDatum( cxr );
        }
        needsSecondaryProcessing = false;
    }

    @Override
    public boolean needsSecondaryProcessing() {
        return this.needsSecondaryProcessing;
    }

    @Override
    public ObjBase getObject() {
        Family.FamilyDatum d = (FamilyDatum) getObjBaseDatum();
        Family f = new Family( d );
        return f;
    }

    @Override
    public ObjBaseDatum getObjBaseDatum() {
        String authorUUID = null;
        if ( personTable != null ) {
            authorUUID = personTable.getUUID();
        }
        String famUUID = null;
        if ( familyTable != null ) {
            famUUID = familyTable.getUUID();
        }

        //find all the strain links
        HashSet<String> noteLinks = new HashSet<String>();
        Iterator<hibernateDatum> objs = this.familyNoteXrefs.iterator();
        while ( objs.hasNext() ) {
            FamilyNoteXref cx = (FamilyNoteXref) objs.next();
            String uuid = cx.getId().getNoteId();
            noteLinks.add( uuid );
        }

        boolean isextendable = false;
        if ( this.isExtendable > 0 ) {
            isextendable = true;
        }

        Family.FamilyDatum d = new Family.FamilyDatum();
        d.uuid = idFamily;
        d.name = name;
        d.dateCreated = dateCreated;
        d.lastModified = lastModified;
        d._SOterm = soterm;
        d._isExtendable = isextendable;
        d._superFamilyUUID = famUUID;
        d._riskGroup = riskGroup;
        d._authorUUID = authorUUID;
        d._description = description;
        d._dataDescription = familyData;
        d._level = level;
        d._noteLinks = noteLinks;
        return d;
    }


    @Override
    public String getUUID() {
        return idFamily;
    }

    public static String translate( Enum field ) {
        if ( field.getDeclaringClass() != Family.Fields.class ) {
            return null;
        }

        Family.Fields f = (Family.Fields) field;

        switch ( f ) {
            case NAME:
                return "name";
            case DATE_CREATED:
                return "dateCreated";
            case LAST_MODIFIED:
                return "lastModified";
            case DESCRIPTION:
                return "description";
            case LEVEL:
                return "level";
            case RISK_GROUP:
                return "riskGroup";
            case SOTERM:
                return "soterm";
            case SCHEMA:
                return "familyData";
            case IS_EXTENDABLE:
                return "isExtendable";

            case SUPER_FAMILY:
                return "familyTable";
            case CHILD_FAMILIES:
                return "familyTables";
            case AUTHOR:
                return "personTable";

            default:
                return null;
        }
    }

    private boolean needsSecondaryProcessing = false;
    /***** AUTO-GENERATED CODE *****/
     private String idFamily;
     private FamilyTable familyTable;
     private PersonTable personTable;
     private String soterm;
     private Integer level;
     private Byte isExtendable;
     private String name;
     private Short riskGroup;
     private String description;
     private Date dateCreated;
     private Date lastModified;
     private String familyData;
     private Set traitFamilyXrefs = new HashSet(0);
     private Set traitTables = new HashSet(0);
     private Set familyTables = new HashSet(0);
     private Set featureFamilyXrefs = new HashSet(0);
     private Set familyNoteXrefs = new HashSet(0);

    public FamilyTable() {
    }


    public FamilyTable(String idFamily) {
        this.idFamily = idFamily;
    }
    public FamilyTable(String idFamily, FamilyTable familyTable, PersonTable personTable, String soterm, Integer level, Byte isExtendable, String name, Short riskGroup, String description, Date dateCreated, Date lastModified, String familyData, Set traitFamilyXrefs, Set traitTables, Set familyTables, Set featureFamilyXrefs, Set familyNoteXrefs) {
       this.idFamily = idFamily;
       this.familyTable = familyTable;
       this.personTable = personTable;
       this.soterm = soterm;
       this.level = level;
       this.isExtendable = isExtendable;
       this.name = name;
       this.riskGroup = riskGroup;
       this.description = description;
       this.dateCreated = dateCreated;
       this.lastModified = lastModified;
       this.familyData = familyData;
       this.traitFamilyXrefs = traitFamilyXrefs;
       this.traitTables = traitTables;
       this.familyTables = familyTables;
       this.featureFamilyXrefs = featureFamilyXrefs;
       this.familyNoteXrefs = familyNoteXrefs;
    }

    public String getIdFamily() {
        return this.idFamily;
    }

    public void setIdFamily(String idFamily) {
        this.idFamily = idFamily;
    }
    public FamilyTable getFamilyTable() {
        return this.familyTable;
    }

    public void setFamilyTable(FamilyTable familyTable) {
        this.familyTable = familyTable;
    }
    public PersonTable getPersonTable() {
        return this.personTable;
    }

    public void setPersonTable(PersonTable personTable) {
        this.personTable = personTable;
    }
    public String getSoterm() {
        return this.soterm;
    }

    public void setSoterm(String soterm) {
        this.soterm = soterm;
    }
    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    public Byte getIsExtendable() {
        return this.isExtendable;
    }

    public void setIsExtendable(Byte isExtendable) {
        this.isExtendable = isExtendable;
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Short getRiskGroup() {
        return this.riskGroup;
    }

    public void setRiskGroup(Short riskGroup) {
        this.riskGroup = riskGroup;
    }
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    public String getFamilyData() {
        return this.familyData;
    }

    public void setFamilyData(String familyData) {
        this.familyData = familyData;
    }
    public Set getTraitFamilyXrefs() {
        return this.traitFamilyXrefs;
    }

    public void setTraitFamilyXrefs(Set traitFamilyXrefs) {
        this.traitFamilyXrefs = traitFamilyXrefs;
    }
    public Set getTraitTables() {
        return this.traitTables;
    }

    public void setTraitTables(Set traitTables) {
        this.traitTables = traitTables;
    }
    public Set getFamilyTables() {
        return this.familyTables;
    }

    public void setFamilyTables(Set familyTables) {
        this.familyTables = familyTables;
    }
    public Set getFeatureFamilyXrefs() {
        return this.featureFamilyXrefs;
    }

    public void setFeatureFamilyXrefs(Set featureFamilyXrefs) {
        this.featureFamilyXrefs = featureFamilyXrefs;
    }
    public Set getFamilyNoteXrefs() {
        return this.familyNoteXrefs;
    }

    public void setFamilyNoteXrefs(Set familyNoteXrefs) {
        this.familyNoteXrefs = familyNoteXrefs;
    }
}