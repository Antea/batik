/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.bridge;

import org.w3c.dom.Element;
import org.apache.batik.gvt.ShapePainter;

/**
 * Factory class for vending <tt>ShapePainter</tt> objects.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @version $Id$
 */
public interface ShapePainterBridge extends Bridge {

    /**
     * Creates a <tt>ShapePainter</tt> using the specified context and element.
     * @param ctx the context to use
     * @param element the Element with the 'stroke', 'fill',
     * 'fill-opacity'... attributes
     */
    ShapePainter createShapePainter(BridgeContext ctx, Element element);

    /**
     * Updates an Element coresponding to the specified BridgeMutationEvent.
     * @param evt the event that describes the modification to perform
     */
    void update(BridgeMutationEvent evt);

}
