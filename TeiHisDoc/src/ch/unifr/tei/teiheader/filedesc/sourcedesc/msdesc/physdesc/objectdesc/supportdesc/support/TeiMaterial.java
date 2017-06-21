/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc.support;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiLocus;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiMaterial extends TeiElement {
    private String material;
    private TeiLocus locus = null;
    private TeiMaterialName name = null;

    public TeiMaterial(TeiElement parent) {
        super(parent);
    }

    private TeiMaterial(TeiElement parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "locus", TeiNS, false);
        if (e != null) {
            locus = TeiLocus.load(this, e);
        } else {
            locus = new TeiLocus(this);
        }

        e = consumeChild(el, "name", TeiNS, false);
        if (e != null) {
            name = TeiMaterialName.load(this, e);
        } else {
            name = new TeiMaterialName(this);
        }
        material = el.getTextTrim(); //fotini
        consume(el);
    }

    public static TeiMaterial load(TeiElement parent, Element el) {
        return new TeiMaterial(parent, el);
    }

    @Override
    public String getElementName() {
        return "material";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        addContent(el, locus);
        addContent(el, name);

        el.setText(material);//fotini

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
