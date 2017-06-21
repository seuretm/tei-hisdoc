/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.utils;

import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiLocus extends TeiElement {

    private int from = 0;
    private int to = 0;

    public TeiLocus(TeiElement parent) {
        super(parent);
    }

    private TeiLocus(TeiElement parent, Element el) {
        this(parent);

        from = consumeAttributeInt(el, "from", NoNS, true);
        to = consumeAttributeInt(el, "to", NoNS, true);


    }

    public static TeiLocus load(TeiElement parent, Element el) {
        return new TeiLocus(parent, el);
    }

    @Override
    public String getElementName() {
        return "locus";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.setAttribute("from", String.valueOf(from), NoNS);
        el.setAttribute("to", String.valueOf(to), NoNS);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    /* ********* ACCESSORS ********** */
//    private int from = 0;
//    private int to = 0;

    /**
     * Getter for property 'from'.
     *
     * @return Value for property 'from'.
     */
    public int getFrom() {
        return from;
    }

    /**
     * Setter for property 'from'.
     *
     * @param from Value to set for property 'from'.
     */
    public void setFrom(int from) {
        this.from = from;
    }

    /**
     * Getter for property 'to'.
     *
     * @return Value for property 'to'.
     */
    public int getTo() {
        return to;
    }

    /**
     * Setter for property 'to'.
     *
     * @param to Value to set for property 'to'.
     */
    public void setTo(int to) {
        this.to = to;
    }

}
