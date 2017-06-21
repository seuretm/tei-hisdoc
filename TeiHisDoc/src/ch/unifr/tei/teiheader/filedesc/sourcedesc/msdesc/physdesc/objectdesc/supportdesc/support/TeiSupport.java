/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc.support;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc.TeiSupportDesc;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiSupport extends TeiElement {
    private List<TeiMaterial> materials = new LinkedList<>();
    private List<TeiWatermark> watermarks = new LinkedList<>();

    public TeiSupport(TeiElement parent) {
        super(parent);
    }

    private TeiSupport(TeiSupportDesc parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("material", TeiNS)) {
            materials.add(TeiMaterial.load(this, e));
        }
        el.removeChildren("material", TeiNS);
        if (materials.isEmpty()) {
            throw new Error("at least one <material> expected in " + getPath());
        }

        for (Element w : el.getChildren("watermark", TeiNS)) {
            watermarks.add(TeiWatermark.load(this, w));
        }
        el.removeChildren("watermark", TeiNS);

        consume(el);
    }

    public static TeiSupport load(TeiSupportDesc parent, Element el) {
        return new TeiSupport(parent, el);
    }

    @Override
    public String getElementName() {
        return "support";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : materials) {
            addContent(el, e);
        }
        for (TeiElement e : watermarks) {
            addContent(el, e);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
