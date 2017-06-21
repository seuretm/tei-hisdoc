/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.publicationstmt.availability;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiLicence extends TeiTextElement {
    private String target;

    public TeiLicence(TeiElement parent) {
        super(parent);
        setContent("Creative Commons Attribution 3.0 Unsupported (CC BY 3.0)");
        target = "http://creativecommons.org/licenses/by/3.0/";
    }

    private TeiLicence(TeiAvailability parent, Element el) {
        this(parent);

        setContent(el.getText());

        target = consumeAttributeStr(el, "target", NoNS, true);

        consume(el);
    }

    public static TeiLicence load(TeiAvailability parent, Element el) {
        return new TeiLicence(parent, el);
    }

    @Override
    public String getElementName() {
        return "licence";
    }

    @Override
    public Element toXML() {
        Element el = super.toXML();

        el.setAttribute("target", target);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
