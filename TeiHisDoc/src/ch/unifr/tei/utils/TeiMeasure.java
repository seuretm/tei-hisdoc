/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.utils;

import org.jdom2.Element;

/**
 * @author ms
 */
public abstract class TeiMeasure extends TeiElement {

    private String type;

    public TeiMeasure(TeiElement parent, String type) {
        super(parent);
        if (type == null) {
            throw new Error("attribute type not specified in " + getPath());
        }
        this.type = type;
    }

    public static TeiMeasure load(TeiElement parent, Element el) {
        if (el.getAttribute("type", NoNS) == null) {
            throw new Error("<measure> does not specify its type in " + parent.getPath());
        }

        boolean containsN = el.getAttribute("n", NoNS) != null;
        boolean containsWH = el.getChild("width", TeiNS) != null && el.getChild("height", TeiNS) != null;

        if (containsN && !containsWH) {
            return TeiCountMeasure.load(parent, el);
        }

        if (!containsN && containsWH) {
            return TeiAreaMeasure.load(parent, el);
        }

        throw new Error("invalid <measure> content in " + parent.getPath());
    }

    public String getType() {
        return type;
    }

    @Override
    public String getElementName() {
        return "measure";
    }
}
