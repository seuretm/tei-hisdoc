/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.accmat;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.TeiPhysDesc;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ms
 */
public class TeiAccMat extends TeiElement {
    private List<TeiDesc> descs = new LinkedList<>();

    public TeiAccMat(TeiElement parent) {
        super(parent);
    }

    private TeiAccMat(TeiPhysDesc parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("desc", TeiNS)) {
            descs.add(TeiDesc.load(this, e));
        }
        el.removeChildren("desc", TeiNS);
        if (descs.isEmpty()) {
            throw new Error("at least one <desc> required in " + getPath());
        }

        consume(el);
    }

    public static TeiAccMat load(TeiPhysDesc parent, Element el) {
        return new TeiAccMat(parent, el);
    }

    @Override
    public String getElementName() {
        return "accMat";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : descs) {
            addContent(el, e);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
