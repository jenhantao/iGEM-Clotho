package org.clothocad.hibernate.data;
// Generated Jul 26, 2010 11:53:27 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * StrainNoteXref generated by hbm2java
 */
public class StrainNoteXref  implements java.io.Serializable {


     private StrainNoteXrefId id;
     private NoteTable noteTable;
     private StrainTable strainTable;
     private Date dateCreated;
     private Date lastModified;

    public StrainNoteXref() {
    }

	
    public StrainNoteXref(StrainNoteXrefId id, NoteTable noteTable, StrainTable strainTable) {
        this.id = id;
        this.noteTable = noteTable;
        this.strainTable = strainTable;

        //JCA added this:
       this.dateCreated = new Date();
       this.lastModified = new Date();
    }
    public StrainNoteXref(StrainNoteXrefId id, NoteTable noteTable, StrainTable strainTable, Date dateCreated, Date lastModified) {
       this.id = id;
       this.noteTable = noteTable;
       this.strainTable = strainTable;
       this.dateCreated = dateCreated;
       this.lastModified = lastModified;
    }
   
    public StrainNoteXrefId getId() {
        return this.id;
    }
    
    public void setId(StrainNoteXrefId id) {
        this.id = id;
    }
    public NoteTable getNoteTable() {
        return this.noteTable;
    }
    
    public void setNoteTable(NoteTable noteTable) {
        this.noteTable = noteTable;
    }
    public StrainTable getStrainTable() {
        return this.strainTable;
    }
    
    public void setStrainTable(StrainTable strainTable) {
        this.strainTable = strainTable;
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




}


