/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.msidentifier;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiIdno extends TeiTextElement {
    private String label = null;

    public TeiIdno(TeiElement parent) {
        super(parent);
    }

    private TeiIdno(TeiIdnoContainer parent, Element el) {
        this((TeiElement) parent);

        setContent(el.getText());

        label = consumeAttributeStr(el, "n", NoNS, false);

        consume(el);
    }

    public static TeiIdno load(TeiIdnoContainer parent, Element el) {
        return new TeiIdno(parent, el);
    }

    @Override
    public String getElementName() {
        return "idno";
    }

    @Override
    public Element toXML() {
        Element el = super.toXML();

        addAttribute(el, "n", label, NoNS);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
