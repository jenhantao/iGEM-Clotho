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
import org.clothocore.api.core.Collector;

/**
 * NoteFactoidXref generated by hbm2java
 */
public class NoteFactoidXref  implements java.io.Serializable {


     private NoteFactoidXrefId id;
     private FactoidTable factoidTable;
     private PersonTable personTable;
     private NoteTable noteTable;
     private Date dateCreated;
     private Date lastModified;

    public NoteFactoidXref() {
    }

	
    public NoteFactoidXref(NoteFactoidXrefId id, FactoidTable factoidTable, NoteTable noteTable) {
        this.id = id;
        this.factoidTable = factoidTable;
        this.noteTable = noteTable;

        //JCA added this:
        this.personTable = new PersonTable(Collector.getCurrentUser().getUUID());
        this.dateCreated = new Date();
        this.lastModified = new Date();
    }
    public NoteFactoidXref(NoteFactoidXrefId id, FactoidTable factoidTable, PersonTable personTable, NoteTable noteTable, Date dateCreated, Date lastModified) {
       this.id = id;
       this.factoidTable = factoidTable;
       this.personTable = personTable;
       this.noteTable = noteTable;
       this.dateCreated = dateCreated;
       this.lastModified = lastModified;
    }
   
    public NoteFactoidXrefId getId() {
        return this.id;
    }
    
    public void setId(NoteFactoidXrefId id) {
        this.id = id;
    }
    public FactoidTable getFactoidTable() {
        return this.factoidTable;
    }
    
    public void setFactoidTable(FactoidTable factoidTable) {
        this.factoidTable = factoidTable;
    }
    public PersonTable getPersonTable() {
        return this.personTable;
    }
    
    public void setPersonTable(PersonTable personTable) {
        this.personTable = personTable;
    }
    public NoteTable getNoteTable() {
        return this.noteTable;
    }
    
    public void setNoteTable(NoteTable noteTable) {
        this.noteTable = noteTable;
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

