/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.utils;

import org.jdom2.Element;

/**
 * @author ms
 */
public class TeiCountMeasure extends TeiMeasure {
    // to xml ok
    private int count = 0;

    // to xml ok
    // type

    public TeiCountMeasure(TeiElement parent, String type) {
        super(parent, type);
    }

    private TeiCountMeasure(TeiElement parent, Element el) {
        this(parent, el.getAttributeValue("type", NoNS));
        el.removeAttribute("type", NoNS);

        try {
            count = Integer.parseInt(el.getAttributeValue("n", NoNS));
        } catch (Exception e) {
            throw new Error("non-numeric value (" + el.getAttributeValue("n", NoNS) + ")given to n in " + getPath());
        }
        el.removeAttribute("n", NoNS);

        // For now
        el.removeChild("locus", TeiNS);

        consume(el);
    }

    public static TeiCountMeasure load(TeiElement parent, Element el) {
        return new TeiCountMeasure(parent, el);
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.setAttribute("type", getType(), NoNS);
        el.setAttribute("n", String.valueOf(count), NoNS);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
