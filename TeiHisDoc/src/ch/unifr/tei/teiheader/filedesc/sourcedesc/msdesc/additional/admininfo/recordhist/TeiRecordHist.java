/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional.admininfo.recordhist;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional.admininfo.TeiAdminInfo;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional.admininfo.recordhist.source.TeiSource;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author ms
 */
public class TeiRecordHist extends TeiElement {
    private TeiSource source = new TeiSource(this);

    public TeiRecordHist(TeiElement parent) {
        super(parent);
    }

    private TeiRecordHist(TeiAdminInfo parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "source", TeiNS, true);
        source = TeiSource.load(this, e);

        consume(el);
    }

    public static TeiRecordHist load(TeiAdminInfo parent, Element el) {
        return new TeiRecordHist(parent, el);
    }

    @Override
    public String getElementName() {
        return "recordHist";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        if (source!=null) {
            el.addContent(source.toXML());
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
