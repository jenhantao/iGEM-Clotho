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
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.data.ObjBase.ObjBaseDatum;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.WikiText;
import org.clothocore.api.data.WikiText.WikiTextDatum;
import org.clothocad.hibernate.hibernateDatum;

/**
 * WikitextTable generated by hbm2java
 */
public class WikitextTable implements java.io.Serializable, hibernateDatum {

    public WikitextTable( WikiText w ) {
        this.idWikitext = w.getUUID();
        this.name = w.getName();
        this.dateCreated = w.getDateCreated();
        this.lastModified = w.getLastModified();
        this.text = w.getAsWikiText();

//this has been silenced due to loss of attachments.  Without this,
//attachments never get deleted, they'll just accumulate
/*        HashSet<String> existingLinks = new  HashSet<String>();
        String query =  "from AttachmentTable where wikiTextId='" + idWikitext + "'" ;
        Iterator xrefs = HibernateConnection.connection.query(query);
        while (xrefs.hasNext()) {
            AttachmentTable cx = (AttachmentTable) xrefs.next();
            String uuid = cx.getIdAttachment();
            existingLinks.add(uuid);
            if(!w.getAttachmentLinks().contains(uuid)) {
                HibernateConnection.connection.deleteDatum(cx);
            }
        }
 */
    }

    @Override
    public void runSecondaryProcessing(ObjBase obj) {
    }

    @Override
    public boolean needsSecondaryProcessing() {
        return false;
    }

    @Override
    public ObjBase getObject() {
        WikiText.WikiTextDatum d = (WikiTextDatum) getObjBaseDatum();
        WikiText w = new WikiText( d );
        return w;
    }
     
    @Override
    public ObjBaseDatum getObjBaseDatum() {
        HashSet<String> attach = new HashSet<String>();
        Iterator<hibernateDatum> objs = this.attachmentTables.iterator();

        while ( objs.hasNext() ) {
            AttachmentTable at = (AttachmentTable) objs.next();
            String uuid = at.getIdAttachment();
            attach.add( uuid );
        }
        WikiText.WikiTextDatum d = new WikiText.WikiTextDatum();
        d.uuid = idWikitext;
        d.name = name;
        d.dateCreated = dateCreated;
        d.lastModified = lastModified;
        d._wikiText = text;
        d._attachments = attach;
        return d;
    }

    @Override
    public String getUUID() {
        return idWikitext;
    }

    public static String translate( Enum field ) {
        if ( field.getDeclaringClass() != WikiText.Fields.class ) {
            return null;
        }

        WikiText.Fields f = (WikiText.Fields) field;

        switch ( f ) {
            case NAME:
                return "name";
            case DATE_CREATED:
                return "dateCreated";
            case LAST_MODIFIED:
                return "lastModifed";
            case WIKI_TEXT:
                return "text";
            case SAMPLE_DATA:
                return "sampleDataTables";
            case ATTACHMENTS:
                return "attachmentTables";
            case NOTES:
                return "noteTables";
            case FACTOIDS:
                return "factoidTables";
            default:
                return null;
        }
    }

    public static ObjType getType( Enum field ) {
        if ( field.getDeclaringClass() != WikiText.Fields.class ) {
            return null;
        }

        WikiText.Fields f = (WikiText.Fields) field;

        switch ( f ) {
            case SAMPLE_DATA:
                return ObjType.SAMPLE_DATA;
            case ATTACHMENTS:
                return ObjType.ATTACHMENT;
            case NOTES:
                return ObjType.NOTE;
            case FACTOIDS:
                return ObjType.FACTOID;
            default:
                return null;
        }
    }
    /***** AUTO-GENERATED CODE *****/
    private String idWikitext;
    private String name;
    private String text;
    private Date dateCreated;
    private Date lastModified;
    private Set sampleDataTables = new HashSet( 0 );
    private Set attachmentTables = new HashSet( 0 );
    private Set noteTables = new HashSet( 0 );
    private Set factoidTables = new HashSet( 0 );

    public WikitextTable() {
    }

    public WikitextTable( String idWikitext ) {
        this.idWikitext = idWikitext;
    }

    public WikitextTable( String idWikitext, String name, String text, Date dateCreated, Date lastModified, Set sampleDataTables, Set attachmentTables, Set noteTables, Set factoidTables ) {
        this.idWikitext = idWikitext;
        this.name = name;
        this.text = text;
        this.dateCreated = dateCreated;
        this.lastModified = lastModified;
        this.sampleDataTables = sampleDataTables;
        this.attachmentTables = attachmentTables;
        this.noteTables = noteTables;
        this.factoidTables = factoidTables;
    }

    public String getIdWikitext() {
        return this.idWikitext;
    }

    public void setIdWikitext( String idWikitext ) {
        this.idWikitext = idWikitext;
    }

    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getText() {
        return this.text;
    }

    public void setText( String text ) {
        this.text = text;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated( Date dateCreated ) {
        this.dateCreated = dateCreated;
    }

    @Override
    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified( Date lastModified ) {
        this.lastModified = lastModified;
    }

    public Set getSampleDataTables() {
        return this.sampleDataTables;
    }

    public void setSampleDataTables( Set sampleDataTables ) {
        this.sampleDataTables = sampleDataTables;
    }

    public Set getAttachmentTables() {
        return this.attachmentTables;
    }

    public void setAttachmentTables( Set attachmentTables ) {
        this.attachmentTables = attachmentTables;
    }

    public Set getNoteTables() {
        return this.noteTables;
    }

    public void setNoteTables( Set noteTables ) {
        this.noteTables = noteTables;
    }

    public Set getFactoidTables() {
        return this.factoidTables;
    }

    public void setFactoidTables( Set factoidTables ) {
        this.factoidTables = factoidTables;
    }
}
