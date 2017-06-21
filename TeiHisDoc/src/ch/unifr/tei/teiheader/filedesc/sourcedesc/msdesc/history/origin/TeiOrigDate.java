/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.history.origin;

import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiOrigDate extends TeiElement {
    private String datingMethod = null;
    private String notBefore = null;
    private String notAfter = null;

    public TeiOrigDate(TeiElement parent) {
        super(parent);
    }

    private TeiOrigDate(TeiOrigin parent, Element el) {
        this(parent);

        datingMethod = consumeAttributeStr(el, "datingMethod", NoNS, false);
        notBefore = consumeAttributeStr(el, "notBefore", NoNS, false);
        notAfter = consumeAttributeStr(el, "notAfter", NoNS, false);

        consume(el);
    }

    public static TeiOrigDate load(TeiOrigin parent, Element el) {
        return new TeiOrigDate(parent, el);
    }

    @Override
    public String getElementName() {
        return "origDate";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        if (datingMethod != null) {
            el.setAttribute("datingMethod", datingMethod, NoNS);
        }
        if (notBefore != null) {
            el.setAttribute("notBefore", notBefore, NoNS);
        }
        if (notAfter != null) {
            el.setAttribute("notAfter", notAfter, NoNS);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
