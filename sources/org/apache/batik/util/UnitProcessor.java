/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.util;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;
import java.util.MissingResourceException;
import org.apache.batik.i18n.Localizable;
import org.apache.batik.i18n.LocalizableSupport;
import org.apache.batik.parser.AWTTransformProducer;
import org.apache.batik.parser.LengthHandler;
import org.apache.batik.parser.LengthParser;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.ParserFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGLength;
import org.w3c.dom.svg.SVGSVGElement;
import org.w3c.dom.svg.SVGTransformable;

/**
 * This class contains utility methods for processing the SVG and CSS
 * units.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public abstract class UnitProcessor {
    /**
     * This constant represents horizontal lengths.
     */
    public final static short HORIZONTAL_LENGTH = 2;

    /**
     * This constant represents vertical lengths.
     */
    public final static short VERTICAL_LENGTH = 1;

    /**
     * This constant represents other lengths.
     */
    public final static short OTHER_LENGTH = 0;

    /**
     * The error messages bundle class name.
     */
    protected final static String RESOURCES =
        "org.apache.batik.util.resources.Messages";

    /**
     * The localizable support for the error messages.
     */
    protected static LocalizableSupport localizableSupport =
        new LocalizableSupport(RESOURCES);

    /**
     * This class does not need to be instantiated.
     */
    protected UnitProcessor() {
    }

    /**
     * Implements {@link org.apache.batik.i18n.Localizable#setLocale(Locale)}.
     */
    public static void setLocale(Locale l) {
        localizableSupport.setLocale(l);
    }

    /**
     * Implements {@link org.apache.batik.i18n.Localizable#getLocale()}.
     */
    public static Locale getLocale() {
        return localizableSupport.getLocale();
    }

    /**
     * Implements {@link
     * org.apache.batik.i18n.Localizable#formatMessage(String,Object[])}.
     */
    public static String formatMessage(String key, Object[] args)
        throws MissingResourceException {
        return localizableSupport.formatMessage(key, args);
    }

    /**
     * Converts a SVG length value to screen pixels.
     * @param t the unit type like specified in the CSSPrimitiveType interface.
     * @param v the length value.
     * @param e the element.
     * @param d HORIZONTAL_LENGTH, VERTICAL_LENGTH or OTHER_LENGTH.
     * @param c The context.
     * @exception RuntimeException If an invalid unit type is specified.
     */
    public static float cssToUserSpace(short t, float v, SVGElement e, short d,
                                       Context c) throws RuntimeException {
        if (t == CSSPrimitiveValue.CSS_NUMBER) {
            return v;
        }
        float f = c.getPixelToMM();
        switch (t) {
        case CSSPrimitiveValue.CSS_PX:
            return pixelsToNumber(v, e, d, c);
        case CSSPrimitiveValue.CSS_MM:
            return pixelsToNumber(v / f, e, d, c);
        case CSSPrimitiveValue.CSS_CM:
            return pixelsToNumber(v * 10 / f, e, d, c);
        case CSSPrimitiveValue.CSS_IN:
            return pixelsToNumber(v * 25.4f / f, e, d, c);
        case CSSPrimitiveValue.CSS_PT:
            return pixelsToNumber(v * 25.4f / (72 * f), e, d, c);
        case CSSPrimitiveValue.CSS_PC:
            return pixelsToNumber(v * 25.4f / (6 * f), e, d, c);
        case CSSPrimitiveValue.CSS_EMS:
            return pixelsToNumber(emsToPixels(v, e, d, c), e, d, c);
        case CSSPrimitiveValue.CSS_EXS:
            return pixelsToNumber(exsToPixels(v, e, d, c), e, d, c);
        case CSSPrimitiveValue.CSS_PERCENTAGE:
            return pixelsToNumber(percentagesToPixels(v, e, d, c), e, d, c);
        default:
            throw new RuntimeException
                (formatMessage("invalid.css.unit",
                               new Object[] { new Integer(t) }));
        }
    }

    /**
     * Converts a SVG length value to screen pixels.
     * @param t the unit type like specified in the SVGLength interface.
     * @param v the length value.
     * @param e the element.
     * @param d HORIZONTAL_LENGTH, VERTICAL_LENGTH or OTHER_LENGTH.
     * @param c The context.
     * @exception RuntimeException If an invalid unit type is specified.
     */
    public static float svgToUserSpace(short t, float v, SVGElement e, short d,
                                       Context c) throws RuntimeException {
        if (t == SVGLength.SVG_LENGTHTYPE_NUMBER) {
            return v;
        }
        float f = c.getPixelToMM();
        switch (t) {
        case SVGLength.SVG_LENGTHTYPE_PX:
            return pixelsToNumber(v, e, d, c);
        case SVGLength.SVG_LENGTHTYPE_MM:
            return pixelsToNumber(v / f, e, d, c);
        case SVGLength.SVG_LENGTHTYPE_CM:
            return pixelsToNumber(v * 10 / f, e, d, c);
        case SVGLength.SVG_LENGTHTYPE_IN:
            return pixelsToNumber(v * 25.4f / f, e, d, c);
        case SVGLength.SVG_LENGTHTYPE_PT:
            return pixelsToNumber(v * 25.4f / (72 * f), e, d, c);
        case SVGLength.SVG_LENGTHTYPE_PC:
            return pixelsToNumber(v * 25.4f / (6 * f), e, d, c);
        case SVGLength.SVG_LENGTHTYPE_EMS:
            return pixelsToNumber(emsToPixels(v, e, d, c), e, d, c);
        case SVGLength.SVG_LENGTHTYPE_EXS:
            return pixelsToNumber(exsToPixels(v, e, d, c), e, d, c);
        case SVGLength.SVG_LENGTHTYPE_PERCENTAGE:
            return pixelsToNumber(percentagesToPixels(v, e, d, c), e, d, c);
        default:
            throw new RuntimeException
                (formatMessage("invalid.svg.unit",
                               new Object[] { new Integer(t) }));
        }
    }

    /**
     * Converts a pixels units to user space units.
     * @param v the length value.
     * @param e the element.
     * @param d HORIZONTAL_LENGTH, VERTICAL_LENGTH or OTHER_LENGTH.
     * @param c The context.
     * @exception RuntimeException If an invalid unit type is specified.
     */
    protected static float pixelsToNumber(float v, SVGElement e, short d,
                                          Context c) {
        // Compute the current transformation matrix (CTM).
        AffineTransform ctm = null;
        for (Node n = e; n != null; n = n.getParentNode()) {
            if (n instanceof SVGTransformable) {
                if (ctm == null) {
                    ctm = new AffineTransform();
                }
                String s = ((Element)n).getAttributeNS(null, "transform");
                Reader r = new StringReader(s);
                ParserFactory pf = c.getParserFactory();
                AffineTransform at;
                at = AWTTransformProducer.createAffineTransform(r, pf);
                ctm.preConcatenate(at);
            } else if (n instanceof SVGSVGElement) {
                break;
            }
        }
        if (ctm == null) {
            return v;
        }
        
        try {
            ctm = ctm.createInverse();
        } catch (NoninvertibleTransformException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        Point2D pt1 = new Point2D.Float();
        Point2D pt2;
        if (d == OTHER_LENGTH) {
            SVGSVGElement svg = e.getOwnerSVGElement();
            if (svg == null) {
                return v;
            }
            // !!! Use a parser for SVGLength
            SVGLength len = svg.getWidth().getBaseVal();
            double dx = svgToUserSpace(len.getUnitType(),
                                       len.getValueInSpecifiedUnits(),
                                       svg,
                                       HORIZONTAL_LENGTH,
                                       c);
            len = svg.getHeight().getBaseVal();
            double dy = svgToUserSpace(len.getUnitType(),
                                       len.getValueInSpecifiedUnits(),
                                       svg,
                                       VERTICAL_LENGTH,
                                       c);
            double vppx = Math.sqrt(dx * dx + dy * dy);
            pt2 = new Point2D.Float((float)dx, (float)dy);
            pt1 = ctm.transform(pt1, pt1);
            pt2 = ctm.transform(pt2, pt2);
            dx = pt2.getX() - pt1.getX();
            dy = pt2.getY() - pt1.getY();
            double vpduser = Math.sqrt(dx * dx + dy * dy);
            return (float)(v * vpduser / vppx);
        } else {
            pt2 = (d == HORIZONTAL_LENGTH)
                ? new Point2D.Float(v, 0)
                : new Point2D.Float(0, v);
            pt1 = ctm.transform(pt1, pt1);
            pt2 = ctm.transform(pt2, pt2);
            double dx = pt2.getX() - pt1.getX();
            double dy = pt2.getY() - pt1.getY();
            int sgn = (v < 0) ? -1 : 1;
            return (float)(sgn * Math.sqrt(dx * dx + dy * dy));
        }
    }

    /**
     * Converts ems units to pixels units.
     * @param v the length value.
     * @param e the element.
     * @param d HORIZONTAL_LENGTH, VERTICAL_LENGTH or OTHER_LENGTH.
     * @param c The context.
     * @exception RuntimeException If an invalid unit type is specified.
     */
    protected static float emsToPixels(float v, SVGElement e, short d,
                                       Context c) {
        if (e == null) {
            throw new RuntimeException
                (formatMessage("element.needed", null));
        }
        CSSPrimitiveValue val = c.getFontSize(e);
        short type = val.getPrimitiveType();
        return v * cssToUserSpace(type,
                                  val.getFloatValue(type),
                                  // !!! DOM.getParentElement(e)
                                  (SVGElement)e.getParentNode(),
                                  d,
                                  c);
    }

    /**
     * Converts exs units to pixels units.
     * @param v the length value.
     * @param e the element.
     * @param d HORIZONTAL_LENGTH, VERTICAL_LENGTH or OTHER_LENGTH.
     * @param c The context.
     * @exception RuntimeException If an invalid unit type is specified.
     */
    protected static float exsToPixels(float v, SVGElement e, short d,
                                       Context c) {
        if (e == null) {
            throw new RuntimeException
                (formatMessage("element.needed", null));
        }
        CSSPrimitiveValue val = c.getFontSize(e);
        short type = val.getPrimitiveType();
        float fs = cssToUserSpace(type,
                                  val.getFloatValue(type),
                                  // !!! DOM.getParentElement(e)
                                  (SVGElement)e.getParentNode(),
                                  d,
                                  c);
        float xh = c.getXHeight(e);
        return v * xh * fs;
    }

    /**
     * Converts percentages units to pixels units.
     * @param v the length value.
     * @param e the element.
     * @param d HORIZONTAL_LENGTH, VERTICAL_LENGTH or OTHER_LENGTH.
     * @param c The context.
     * @exception RuntimeException If an invalid unit type is specified.
     */
    protected static float percentagesToPixels(float v, SVGElement e, short d,
                                               Context c) {
        if (e == null) {
            throw new RuntimeException
                (formatMessage("element.needed", null));
        }
        SVGSVGElement svg = e.getOwnerSVGElement();
        if (svg == null) {
            throw new RuntimeException
                (formatMessage("svg.element.needed", null));
        }
        // !!! Use a parser for SVGLength
        if (d == HORIZONTAL_LENGTH) {
            SVGLength len = svg.getWidth().getBaseVal();
            float w = svgToUserSpace(len.getUnitType(),
                                     len.getValueInSpecifiedUnits(),
                                     svg,
                                     d,
                                     c);
            return w * v / 100;
        } else if (d == VERTICAL_LENGTH) {
            SVGLength len = svg.getHeight().getBaseVal();
            float h = svgToUserSpace(len.getUnitType(),
                                     len.getValueInSpecifiedUnits(),
                                     svg,
                                     d,
                                     c);
            return h * v / 100;
        } else {
            SVGLength len = svg.getWidth().getBaseVal();
            double w = svgToUserSpace(len.getUnitType(),
                                      len.getValueInSpecifiedUnits(),
                                      svg,
                                      HORIZONTAL_LENGTH,
                                      c);
            len = svg.getHeight().getBaseVal();
            double h = svgToUserSpace(len.getUnitType(),
                                      len.getValueInSpecifiedUnits(),
                                      svg,
                                      VERTICAL_LENGTH,
                                      c);
            double vpp = Math.sqrt(w * w + h * h) / Math.sqrt(2);
            return (float)(vpp * v / 100);
        }
    }

    /**
     * An utility method to implement Context.getFontSize(SVGElement e).
     */
    public static CSSPrimitiveValue getFontSize(SVGElement e,
                                                CSSStyleDeclaration d) {
        return(CSSPrimitiveValue)d.getPropertyCSSValue
            (CSSConstants.FONT_SIZE_PROPERTY);
    }

    /**
     * Converts a SVG length value to screen pixels.
     * @param value the length value
     * @param e the element.
     * @param d HORIZONTAL_LENGTH, VERTICAL_LENGTH or OTHER_LENGTH.
     * @param c The context.
     * @exception RuntimeException If an invalid unit type is specified.
     */
    public static float svgToUserSpace(String value, SVGElement e, short d,
                                       Context c) {
        if (value.length() == 0) {
            return 0;
        }
        LengthParser p = c.getParserFactory().createLengthParser();
        UnitResolver ur = new UnitResolver();
        p.setLengthHandler(ur);
        p.parse(new StringReader(value));
        return svgToUserSpace(ur.unit, ur.value, e, d, c);
    }

    /**
     * A simple class that can convert units.
     */
    public static class UnitResolver implements LengthHandler {

        /** The length value. */
        public float value;
        /** The length type. */
        public short unit = SVGLength.SVG_LENGTHTYPE_NUMBER;

        /**
         * Implements {@link LengthHandler#startLength()}.
         */
        public void startLength() throws ParseException {
        }

        /**
         * Implements {@link LengthHandler#lengthValue(float)}.
         */
        public void lengthValue(float v) throws ParseException {
            this.value = v;
        }

        /**
         * Implements {@link LengthHandler#em()}.
         */
        public void em() throws ParseException {
            this.unit = SVGLength.SVG_LENGTHTYPE_EMS;
        }

        /**
         * Implements {@link LengthHandler#ex()}.
         */
        public void ex() throws ParseException {
            this.unit = SVGLength.SVG_LENGTHTYPE_EXS;
        }

        /**
         * Implements {@link LengthHandler#in()}.
         */
        public void in() throws ParseException {
            this.unit = SVGLength.SVG_LENGTHTYPE_IN;
        }

        /**
         * Implements {@link LengthHandler#cm()}.
         */
        public void cm() throws ParseException {
            this.unit = SVGLength.SVG_LENGTHTYPE_CM;
        }

        /**
         * Implements {@link LengthHandler#mm()}.
         */
        public void mm() throws ParseException {
            this.unit = SVGLength.SVG_LENGTHTYPE_MM;
        }

        /**
         * Implements {@link LengthHandler#pc()}.
         */
        public void pc() throws ParseException {
            this.unit = SVGLength.SVG_LENGTHTYPE_PC;
        }

        /**
         * Implements {@link LengthHandler#pt()}.
         */
        public void pt() throws ParseException {
            this.unit = SVGLength.SVG_LENGTHTYPE_PT;
        }

        /**
         * Implements {@link LengthHandler#px()}.
         */
        public void px() throws ParseException {
            this.unit = SVGLength.SVG_LENGTHTYPE_PX;
        }

        /**
         * Implements {@link LengthHandler#percentage()}.
         */
        public void percentage() throws ParseException {
            this.unit = SVGLength.SVG_LENGTHTYPE_PERCENTAGE;
        }

        /**
         * Implements {@link LengthHandler#endLength()}.
         */
        public void endLength() throws ParseException {
        }
    }


    /**
     * Holds the informations needed to compute the units.
     */
    public interface Context {
        /**
         * Returns the pixel to mm factor.
         */
        float getPixelToMM();

        /**
         * Returns the parser factory.
         */
        ParserFactory getParserFactory();

        /**
         * Returns the font-size medium value in pt.
         */
        float getMediumFontSize();

        /**
         * Returns the font-size value.
         */
        CSSPrimitiveValue getFontSize(SVGElement e);

        /**
         * Returns the x-height value.
         */
        float getXHeight(SVGElement e);
    }
}
