/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader;

import ch.unifr.tei.TeiHisDoc;
import ch.unifr.tei.teiheader.filedesc.TeiFileDesc;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiHeader extends TeiElement {

    private TeiFileDesc fileDesc = new TeiFileDesc(this);

    public TeiHeader(TeiElement parent) {
        super(parent);
    }

    private TeiHeader(TeiHisDoc parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "fileDesc", TeiNS, true);
        fileDesc = TeiFileDesc.load(this, e);

        consume(el);
    }

    public static TeiHeader load(TeiHisDoc parent, Element el) {
        return new TeiHeader(parent, el);
    }

    @Override
    public String getElementName() {
        return "teiHeader";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.addContent(fileDesc.toXML());

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    @Override
    public void updateChildrenIDs() {
        fileDesc.updateChildrenIDs();
    }


    /*
        ACCESSORS
    */
    public TeiFileDesc getFileDesc() {
        return fileDesc;
    }
}
