/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.css.svg;

import org.apache.batik.css.PropertyMap;
import org.apache.batik.css.value.AbstractIdentifierFactory;
import org.apache.batik.css.value.ImmutableString;
import org.apache.batik.css.value.ImmutableValue;
import org.w3c.css.sac.Parser;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class provides a factory for the 'text-anchor' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class TextAnchorFactory extends AbstractIdentifierFactory {
    /**
     * The 'start' keyword.
     */
    public final static String START = "start";

    /**
     * The 'start' keyword.
     */
    public final static ImmutableValue START_VALUE =
	new ImmutableString(CSSPrimitiveValue.CSS_IDENT, START);

    /**
     * The 'middle' keyword.
     */
    public final static String MIDDLE = "middle";

    /**
     * The 'middle' keyword.
     */
    public final static ImmutableValue MIDDLE_VALUE =
	new ImmutableString(CSSPrimitiveValue.CSS_IDENT, MIDDLE);

    /**
     * The 'end' keyword.
     */
    public final static String END = "end";

    /**
     * The 'end' keyword.
     */
    public final static ImmutableValue END_VALUE =
	new ImmutableString(CSSPrimitiveValue.CSS_IDENT, END);

    /**
     * The identifier values.
     */
    protected final static PropertyMap values = new PropertyMap();
    static {
	values.put(START,  START_VALUE);
	values.put(MIDDLE, MIDDLE_VALUE);
	values.put(END,    END_VALUE);
    }

    /**
     * Creates a new TextAnchorFactory object.
     */
    public TextAnchorFactory(Parser p) {
	super(p);
    }

     /**
     * Returns the name of the property handled.
     */
    public String getPropertyName() {
	return "text-anchor";
    }
    
    /**
     * Returns the property map that contains the possible values.
     */
    protected PropertyMap getIdentifiers() {
	return values;
    }
}
