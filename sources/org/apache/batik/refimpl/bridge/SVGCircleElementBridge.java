/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.refimpl.bridge;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.util.UnitProcessor;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.css.CSSStyleDeclaration;

/**
 * A factory for the &lt;circle> SVG element.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class SVGCircleElementBridge extends SVGShapeElementBridge {

    /**
     * Returns a <tt>Ellipse2D.Float</tt>.
     */
    protected Shape createShape(BridgeContext ctx,
                                SVGElement svgElement,
                                CSSStyleDeclaration decl,
                                UnitProcessor.Context uctx) {
        String s = svgElement.getAttributeNS(null, ATTR_CX);
        float cx = UnitProcessor.svgToUserSpace(s,
                                                svgElement,
                                                UnitProcessor.HORIZONTAL_LENGTH,
                                                uctx);
        s = svgElement.getAttributeNS(null, ATTR_CY);
        float cy = UnitProcessor.svgToUserSpace(s,
                                                svgElement,
                                                UnitProcessor.VERTICAL_LENGTH,
                                                uctx);
        s = svgElement.getAttributeNS(null, ATTR_R);
        float r = UnitProcessor.svgToUserSpace(s,
                                               svgElement,
                                               UnitProcessor.OTHER_LENGTH,
                                               uctx);
        float x = cx - r;
        float y = cy - r;
        float w = r * 2;
        return new Ellipse2D.Float(x, y, w, w);
    }
}
