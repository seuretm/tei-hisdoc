/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional.admininfo.recordhist.source;

import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author ms
 */
public class TeiRef extends TeiElement {
    private String target = null;

    public TeiRef(TeiElement parent) {
        super(parent);
    }

    private TeiRef(TeiSource parent, Element el) {
        this(parent);

        target = consumeAttributeStr(el, "target", NoNS, true);

        consume(el);
    }

    public static TeiRef load(TeiSource parent, Element el) {
        if (el==null) {
            return null;
        }
        return new TeiRef(parent, el);
    }

    @Override
    public String getElementName() {
        return "ref";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.setAttribute("target", target, NoNS);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
