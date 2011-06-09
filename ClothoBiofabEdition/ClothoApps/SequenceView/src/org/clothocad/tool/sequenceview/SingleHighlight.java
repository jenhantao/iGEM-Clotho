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

package org.clothocad.tool.sequenceview;

import java.util.HashMap;

/** 
 * Thy humble attempt to clean up ArrayList _highlightDataIndividual from Sequenceview.
 * @author Nade Sritanyaratana
 */
public class SingleHighlight implements Cloneable {
    public SingleHighlight() {
        _highlightData = new HashMap();
        _highlightData.put("name", null);
        _highlightData.put("endSearch", null);
        _highlightData.put("startSearch", null);
        _highlightData.put("highlightHelper", null);
        _highlightData.put("pattern", null);
        _highlightData.put("sender", null);
        _addIndex = 0;
    }
    
    @Override
    public SingleHighlight clone() {
        SingleHighlight clone = new SingleHighlight();
        clone.add(this.get("name"));
        clone.add(this.get("endSearch"));
        clone.add(this.get("startSearch"));
        clone.add(this.get("highlightHelper"));
        clone.add(this.get("pattern"));
        clone.add(this.get("sender"));
        return clone;
    }
    
    public void add(Object value) {
        if (_addIndex==0) {
            _highlightData.put("name", value);
        }
        else if (_addIndex==1) {
            _highlightData.put("endSearch", value);
        }
        else if (_addIndex==2) {
            _highlightData.put("startSearch", value);
        }
        else if (_addIndex==3) {
            _highlightData.put("highlightHelper", value);
        }
        else if (_addIndex==4) {
            _highlightData.put("pattern", value);
        }
        else if (_addIndex==5) {
            _highlightData.put("sender", value);
        }
        _addIndex++;
    }
    
    public Object get(int index) {
        Object returnable = null;
        if (index==0) {
            returnable = _highlightData.get("name");
        }
        else if (index == 1) {
            returnable = _highlightData.get("endSearch");
        }
        else if (index == 2) {
            returnable = _highlightData.get("startSearch");
        }
        else if (index == 3) {
            returnable = _highlightData.get("highlightHelper");
        }
        else if (index == 4) {
            returnable = _highlightData.get("pattern");
        }
        else if (index == 5) {
            returnable = _highlightData.get("sender");
        }
        return returnable;
    }
    
    public void set(int index, Object value) {
        if (index==0) {
            _highlightData.put("name", value);
        }
        else if (index==1) {
            _highlightData.put("endSearch", value);
        }
        else if (index==2) {
            _highlightData.put("startSearch", value);
        }
        else if (index==3) {
            _highlightData.put("highlightHelper", value);
        }
        else if (index==4) {
            _highlightData.put("pattern", value);
        }
        else if (index==5) {
            _highlightData.put("sender", value);
        }
    }
    
    public Object get(String key) {
        return _highlightData.get(key);
    }
    
    public void set(String key, Object value) {
        _highlightData.put(key,value);
    }
    
    public int size() {
        return _highlightData.size();
    }
    
    private HashMap<String,Object> _highlightData;
    private int _addIndex;
}
