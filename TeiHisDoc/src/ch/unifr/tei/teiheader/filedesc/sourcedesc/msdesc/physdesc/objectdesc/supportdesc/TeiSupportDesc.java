/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.TeiObjectDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc.extent.TeiExtent;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc.support.TeiSupport;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiSupportDesc extends TeiElement {
    private TeiSupport support = null;
    private TeiExtent extent = new TeiExtent(this);
    private TeiFoliation foliation = null;
    private String material = null;

    public TeiSupportDesc(TeiElement parent) {
        super(parent);
    }

    private TeiSupportDesc(TeiObjectDesc parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "support", TeiNS, false);
        if (e != null) {
            support = TeiSupport.load(this, e);
//        } else {
//            support = new TeiSupport(this);
        }

        e = consumeChild(el, "extent", TeiNS, true);
        extent = TeiExtent.load(this, e);

        e = consumeChild(el, "foliation", TeiNS, false);
        if (e != null) {
            foliation = TeiFoliation.load(this, e);
        } else {
            foliation = new TeiFoliation(this);
        }

        for (Element i : el.getChildren("collation", TeiNS)) {
            addIgnoredElement(i);
        }
        el.removeChildren("collation", TeiNS);

        for (Element i : el.getChildren("condition", NoNS)) {
            addIgnoredElement(i);
        }
        el.removeChildren("condition", TeiNS);

        material = consumeAttributeStr(el, "material", NoNS, false);

        consume(el);
    }

    public static TeiSupportDesc load(TeiObjectDesc parent, Element el) {
        return new TeiSupportDesc(parent, el);
    }

    @Override
    public String getElementName() {
        return "supportDesc";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        addContent(el, support);
        addContent(el, extent);
        addContent(el, foliation);
        addAttribute(el, "material", material, NoNS);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
