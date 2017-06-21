/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc;

import ch.unifr.tei.teiheader.filedesc.TeiFileDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.TeiMsDesc;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiSourceDesc extends TeiElement {
    private TeiMsDesc msDesc = new TeiMsDesc(this);

    public TeiSourceDesc(TeiElement parent) {
        super(parent);
    }

    private TeiSourceDesc(TeiFileDesc parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "msDesc", TeiNS, true);
        msDesc = TeiMsDesc.load(this, e);

        consume(el);
    }

    public static TeiSourceDesc load(TeiFileDesc parent, Element el) {
        return new TeiSourceDesc(parent, el);
    }

    @Override
    public String getElementName() {
        return "sourceDesc";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.addContent(msDesc.toXML());

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    @Override
    public void updateChildrenIDs() {
        msDesc.updateChildrenIDs();
    }

    // ACCESSORS
    public TeiMsDesc getMsDesc() {
        return msDesc;
    }
}
