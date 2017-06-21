/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.history.origin;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.history.TeiHistory;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiOrigin extends TeiElement {
    private TeiOrigDate originDate = null;
    private TeiOrigPlace originPlace = null;

    public TeiOrigin(TeiElement parent) {
        super(parent);
    }

    private TeiOrigin(TeiHistory parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "origDate", TeiNS, false);
        if (e != null) {
            originDate = TeiOrigDate.load(this, e);
        } else {
            originDate = new TeiOrigDate(this);
        }

        e = consumeChild(el, "origPlace", TeiNS, false);
        if (e != null) {
            originPlace = TeiOrigPlace.load(this, e);
        } else {
            originPlace = new TeiOrigPlace(this);
        }

        consume(el);
    }

    public static TeiOrigin load(TeiHistory parent, Element el) {
        return new TeiOrigin(parent, el);
    }

    @Override
    public String getElementName() {
        return "origin";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        if (originDate != null) {
            el.addContent(originDate.toXML());
        }

        if (originPlace != null) {
            el.addContent(originPlace.toXML());
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
