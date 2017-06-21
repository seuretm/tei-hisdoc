/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.utils;

import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiDimensions extends TeiMeasure {
    private float width;
    private float height;
    private String hUnit;
    private String vUnit;

    public TeiDimensions(TeiElement parent, String type) {
        super(parent, type);
    }

    private TeiDimensions(TeiElement parent, Element el) {
        this(parent, el.getAttributeValue("type", NoNS));
        el.removeAttribute("type", NoNS);

        Element ew = el.getChild("width", TeiNS);
        if ((hUnit = ew.getAttributeValue("unit", NoNS)) == null) {
            throw new Error("no unit attribute defined in " + getPath() + "/width");
        }
        //Fotini
        Element eh = el.getChild("height", TeiNS);
        if ((vUnit = eh.getAttributeValue("unit", NoNS)) == null) {
            throw new Error("no unit attribute defined in " + getPath() + "/height");
        }

        try {
            width = Float.parseFloat(ew.getTextTrim());
        } catch (Exception e) {
            throw new Error("non-numeric value (\"" + ew.getText() + "\") given to " + getPath() + "/width");
        }
        try {
            //Fotini
            height = Float.parseFloat(eh.getTextTrim());
        } catch (Exception e) {
            throw new Error("non-numeric value given to " + getPath() + "/height");
        }

        el.removeChild("width", TeiNS);
        el.removeChild("height", TeiNS);

        // For now
        el.removeChild("locus", TeiNS);

        consume(el);
    }

    public static TeiDimensions load(TeiElement parent, Element el) {
        return new TeiDimensions(parent, el);
    }

    @Override
    public String getElementName() {
        return "dimensions";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.setAttribute("type", getType(), NoNS);

        Element w = new Element("width", TeiNS);
        w.setAttribute("unit", vUnit, NoNS);
        //Fotini
        //w.setAttribute("n", String.valueOf(width), NoNS);
        w.setText(String.valueOf(width));


        Element h = new Element("height", TeiNS);
        h.setAttribute("unit", hUnit, NoNS);
        //Fotini        
        //h.setAttribute("n", String.valueOf(height), NoNS);
        h.setText(String.valueOf(height));

        el.addContent(h);
        el.addContent(w);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
