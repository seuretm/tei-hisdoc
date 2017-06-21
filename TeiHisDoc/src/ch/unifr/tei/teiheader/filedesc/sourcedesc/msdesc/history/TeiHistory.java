/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.history;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.TeiMsDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.history.origin.TeiOrigin;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiHistory extends TeiElement {
    private TeiOrigin origin = null;

    public TeiHistory(TeiElement parent) {
        super(parent);
    }

    private TeiHistory(TeiMsDesc parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "origin", TeiNS, false);
        if (e != null) {
            origin = TeiOrigin.load(this, e);
        } else {
            origin = new TeiOrigin(this);
        }

        consume(el);
    }

    public static TeiHistory load(TeiMsDesc parent, Element el) {
        return new TeiHistory(parent, el);
    }

    @Override
    public String getElementName() {
        return "history";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        if (origin != null) {
            el.addContent(origin.toXML());
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
