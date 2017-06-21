/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.TeiMsDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional.admininfo.TeiAdminInfo;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author ms
 */
public class TeiAdditional extends TeiElement {
    private TeiAdminInfo adminInfo = null;

    public TeiAdditional(TeiElement parent) {
        super(parent);
    }

    private TeiAdditional(TeiMsDesc parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "adminInfo", TeiNS, false);
        if (e != null) {
            adminInfo = TeiAdminInfo.load(this, e);
        } else {
            adminInfo = new TeiAdminInfo(this);
        }

        consume(el);
    }

    public static TeiAdditional load(TeiMsDesc parent, Element el) {
        return new TeiAdditional(parent, el);
    }

    @Override
    public String getElementName() {
        return "additional";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        addContent(el, adminInfo);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateChildrenIDs() {
        this.adminInfo.updateChildrenIDs();
    }

}
