/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.utils;

import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public abstract class TeiTextElement extends TeiElement {
    private String content;

    public TeiTextElement(TeiElement parent) {
        super(parent);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String c) {
        content = c;
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        if (content != null) {
            el.setText(content);
        }

        return el;
    }

}
