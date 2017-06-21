/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Simistir
 */
public class TeiCollation extends TeiTextElement {
    public TeiCollation(TeiElement parent) {
        super(parent);
    }


    static TeiCollation load(TeiElement parent, Element el) {
        TeiCollation collation = new TeiCollation(parent);
        collation.setContent(el.getText());
        return collation;
    }

    @Override
    public String getElementName() {
        return "collation";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();
        return el;
    }
}