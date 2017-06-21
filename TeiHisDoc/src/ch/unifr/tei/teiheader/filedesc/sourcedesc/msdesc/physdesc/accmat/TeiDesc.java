/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.accmat;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc.support.TeiMaterial;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiLocus;
import org.jdom2.Element;

/**
 * @author ms
 */
public class TeiDesc extends TeiElement {

    private TeiLocus locus = null;

    private TeiMaterial material = null;

    public TeiDesc(TeiElement parent) {
        super(parent);
    }

    private TeiDesc(TeiAccMat parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "locus", TeiNS, false);
        if (e != null) {
            locus = TeiLocus.load(this, e);
        } else {
            locus = new TeiLocus(this);
        }

        e = consumeChild(el, "material", TeiNS, false);
        if (e != null) {
            material = TeiMaterial.load(this, e);
        } else {
            material = new TeiMaterial(this);
        }

        consume(el);
    }

    public static TeiDesc load(TeiAccMat parent, Element el) {
        return new TeiDesc(parent, el);
    }

    @Override
    public String getElementName() {
        return "desc";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        addContent(el, locus);
        addContent(el, material);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
