/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.svg.SVGDescElement;

/**
 * This class implements {@link org.w3c.dom.svg.SVGDescElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class SVGOMDescElement
    extends    SVGDescriptiveElement
    implements SVGDescElement {
    /**
     * Creates a new SVGOMDescElement object.
     */
    public SVGOMDescElement() {
    }

    /**
     * Creates a new SVGOMDescElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMDescElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);

    }

    /**
     * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getLocalName()}.
     */
    public String getLocalName() {
        return "desc";
    }
}
