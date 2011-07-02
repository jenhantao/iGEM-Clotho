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

/**
 * SampleDataXref generated by hbm2java
 */
public class SampleDataXref  implements java.io.Serializable {


     private SampleDataXrefId id;
     private SampleTable sampleTable;
     private SampleDataTable sampleDataTable;
     private Date dateCreated;
     private Date lastModified;

    public SampleDataXref() {
    }

	
    public SampleDataXref(SampleDataXrefId id, SampleTable sampleTable, SampleDataTable sampleDataTable) {
        this.id = id;
        this.sampleTable = sampleTable;
        this.sampleDataTable = sampleDataTable;

        //JCA added this
        this.dateCreated = new Date();
        this.lastModified = new Date();
    }
    public SampleDataXref(SampleDataXrefId id, SampleTable sampleTable, SampleDataTable sampleDataTable, Date dateCreated, Date lastModified) {
       this.id = id;
       this.sampleTable = sampleTable;
       this.sampleDataTable = sampleDataTable;
       this.dateCreated = dateCreated;
       this.lastModified = lastModified;
    }
   
    public SampleDataXrefId getId() {
        return this.id;
    }
    
    public void setId(SampleDataXrefId id) {
        this.id = id;
    }
    public SampleTable getSampleTable() {
        return this.sampleTable;
    }
    
    public void setSampleTable(SampleTable sampleTable) {
        this.sampleTable = sampleTable;
    }
    public SampleDataTable getSampleDataTable() {
        return this.sampleDataTable;
    }
    
    public void setSampleDataTable(SampleDataTable sampleDataTable) {
        this.sampleDataTable = sampleDataTable;
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


