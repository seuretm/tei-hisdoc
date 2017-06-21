/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.history.origin;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiOrigPlace extends TeiTextElement {
    private String origPlace; //Fotini

    public TeiOrigPlace(TeiElement parent) {
        super(parent);
    }

    private TeiOrigPlace(TeiOrigin parent, Element el) {
        this(parent);
        origPlace = el.getTextTrim(); //fotini
        consume(el);
    }

    public static TeiOrigPlace load(TeiOrigin parent, Element el) {
        return new TeiOrigPlace(parent, el);
    }

    @Override
    public String getElementName() {
        return "origPlace";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.setText(origPlace);//fotini

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
