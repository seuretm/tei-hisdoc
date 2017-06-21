/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional.admininfo;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional.TeiAdditional;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional.admininfo.recordhist.TeiRecordHist;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author ms
 */
public class TeiAdminInfo extends TeiElement {

    private TeiRecordHist recordHist = new TeiRecordHist(this);

    public TeiAdminInfo(TeiElement parent) {
        super(parent);
    }

    private TeiAdminInfo(TeiAdditional parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "recordHist", TeiNS, true);
        recordHist = TeiRecordHist.load(this, e);

        consume(el);
    }

    public static TeiAdminInfo load(TeiAdditional parent, Element el) {
        return new TeiAdminInfo(parent, el);
    }

    @Override
    public String getElementName() {
        return "adminInfo";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.addContent(recordHist.toXML());

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
