/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc.support;

import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiWatermark extends TeiElement {
    public TeiWatermark(TeiElement parent) {
        super(parent);
    }

    private TeiWatermark(TeiSupport parent, Element el) {
        this(parent);

        consume(el);
    }

    public static TeiWatermark load(TeiSupport parent, Element el) {
        return new TeiWatermark(parent, el);
    }

    @Override
    public String getElementName() {
        return "watermark";
    }

    @Override
    public Element toXML() {
        return getExportElement();
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
