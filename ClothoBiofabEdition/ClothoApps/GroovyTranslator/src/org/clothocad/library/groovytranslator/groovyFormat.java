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

package org.clothocad.library.groovytranslator;

import org.clothocore.api.data.Format;


/**

 * This exists to relay static methods of the objBase class
 * into an instance so that groovy can call an instance of this
 * class and then use it to access the static methods of the objBase
 * class
 * 
 * @author J. Christopher Anderson
 */
public class groovyFormat {

    public Format generateFormat(String pluginId, String name, String shortDescription, String longDescription, String pluginDownloadLink, String pluginHelpLink) {
        return Format.generateFormat(pluginId, name, shortDescription,  pluginDownloadLink, pluginHelpLink);
    }

    public Format retrieveByName( String name ) {
        return Format.retrieveByName(name);
    }
}
