/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc.extent;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc.TeiSupportDesc;
import ch.unifr.tei.utils.TeiDimensions;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiMeasure;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiExtent extends TeiElement {
    private List<TeiMeasure> measures = new LinkedList<>();
    private TeiDimensions dimensions = null;

    public TeiExtent(TeiElement parent) {
        super(parent);
    }

    private TeiExtent(TeiSupportDesc parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("measure", TeiNS)) {
            measures.add(TeiMeasure.load(this, e));
        }
        el.removeChildren("measure", TeiNS);

        Element e = consumeChild(el, "dimensions", TeiNS, false);
        if (e != null) {
            dimensions = TeiDimensions.load(this, e);
//        } else {
//            dimensions = new TeiDimensions(this, "");
        }

        consume(el);
    }

    public static TeiExtent load(TeiSupportDesc parent, Element el) {
        return new TeiExtent(parent, el);
    }

    @Override
    public String getElementName() {
        return "extent";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : measures) {
            addContent(el, e);
        }
        addContent(el, dimensions);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
