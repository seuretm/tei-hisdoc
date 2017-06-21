/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.utils;

import org.jdom2.Element;

/**
 * @author ms
 */
public class TeiAreaMeasure extends TeiMeasure {
    // to xml ok
    private float width = 0.0f;

    // to xml ok
    private float height = 0.0f;

    // to xml ok
    private String hUnit = "cm";

    // to xml ok
    private String vUnit = "cm";

    // to xml ok
    // type


    public TeiAreaMeasure(TeiElement parent, String type) {
        super(parent, type);
    }

    private TeiAreaMeasure(TeiElement parent, Element el) {
        this(parent, el.getAttributeValue("type", NoNS));
        el.removeAttribute("type", NoNS);

        Element ew = el.getChild("width", TeiNS);
        if ((hUnit = ew.getAttributeValue("unit", NoNS)) == null) {
            throw new Error("no unit attribute defined in " + getPath() + "/width");
        }
        Element eh = el.getChild("width", TeiNS);
        if ((vUnit = eh.getAttributeValue("unit", NoNS)) == null) {
            throw new Error("no unit attribute defined in " + getPath() + "/height");
        }

        try {
            width = Float.parseFloat(ew.getText());
        } catch (Exception e) {
            throw new Error("non-numeric value (" + ew.getText() + ") given to " + getPath() + "/width");
        }
        try {
            height = Float.parseFloat(eh.getText());
        } catch (Exception e) {
            throw new Error("non-numeric value (" + eh.getText() + ") given to " + getPath() + "/height");
        }

        el.removeChild("width", TeiNS);
        el.removeChild("height", TeiNS);

        // For now
        el.removeChild("locus", TeiNS);

        consume(el);
    }

    public static TeiAreaMeasure load(TeiElement parent, Element el) {
        return new TeiAreaMeasure(parent, el);
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.setAttribute("type", getType(), NoNS);

        Element w = new Element("width", TeiNS);
        w.setAttribute("unit", vUnit, NoNS);
        w.setAttribute("n", String.valueOf(width), NoNS);

        Element h = new Element("height", TeiNS);
        h.setAttribute("unit", hUnit, NoNS);
        h.setAttribute("n", String.valueOf(height), NoNS);

        el.addContent(w);
        el.addContent(h);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
