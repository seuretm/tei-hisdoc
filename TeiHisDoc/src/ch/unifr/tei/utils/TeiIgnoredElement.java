/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.utils;

import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiIgnoredElement extends TeiElement {
    private Element element;

    public TeiIgnoredElement(TeiElement parent, Element e) {
        super(parent);
        element = e.clone().detach();
    }

    @Override
    public String getElementName() {
        return element.getName();
    }

    @Override
    public Element toXML() {
        return element.clone().detach();
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
