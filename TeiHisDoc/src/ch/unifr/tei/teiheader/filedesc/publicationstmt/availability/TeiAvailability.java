/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.publicationstmt.availability;

import ch.unifr.tei.teiheader.filedesc.publicationstmt.TeiPublicationStmt;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiAvailability extends TeiElement {
    private String status = null;
    private String label = null;
    private TeiLicence licence = null;

    public TeiAvailability(TeiElement parent) {
        super(parent);
        status = "restricted";
        label = "cc-by";
        licence = new TeiLicence(this);
    }

    private TeiAvailability(TeiPublicationStmt parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "licence", TeiNS, true);
        licence = TeiLicence.load(this, e);

        status = consumeAttributeStr(el, "status", NoNS, true);
        label = consumeAttributeStr(el, "n", NoNS, true);

        consume(el);
    }

    public static TeiAvailability load(TeiPublicationStmt parent, Element el) {
        return new TeiAvailability(parent, el);
    }

    @Override
    public String getElementName() {
        return "availability";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.addContent(licence.toXML());

        el.setAttribute("status", status, NoNS);
        el.setAttribute("n", label, NoNS);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }
}
