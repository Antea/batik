/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.css.sac;

import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class provides an implementation for the
 * {@link org.w3c.css.sac.CombinatorSelector} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */

public class CSSOMDirectAdjacentSelector extends AbstractCombinatorSelector {
    /**
     * Creates a new CSSOMDirectAdjacentSelector object.
     */
    public CSSOMDirectAdjacentSelector(Selector parent,
                                       SimpleSelector simple) {
	super(parent, simple);
    }

    /**
     * <b>SAC</b>: Implements {@link
     * org.w3c.css.sac.Selector#getSelectorType()}.
     */
    public short getSelectorType() {
	return SAC_DIRECT_ADJACENT_SELECTOR;
    }

    /**
     * Tests whether this selector matches the given element.
     */
    public boolean match(Element e, String pseudoE) {
	Node n = e;
        while ((n = n.getPreviousSibling()) != null &&
               n.getNodeType() != Node.ELEMENT_NODE);
	if (n != null) {
	    return ((ExtendedSelector)getParentSelector()).match((Element)n,
                                                                 null) &&
		   ((ExtendedSelector)getSimpleSelector()).match(e, pseudoE);
	}	
	return false;
    }

    /**
     * Returns a representation of the selector.
     */
    public String toString() {
	return getParentSelector() + " + " + getSimpleSelector();
    }
}
