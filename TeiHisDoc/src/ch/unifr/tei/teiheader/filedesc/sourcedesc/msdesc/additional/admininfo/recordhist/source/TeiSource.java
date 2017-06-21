/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional.admininfo.recordhist.source;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional.admininfo.recordhist.TeiRecordHist;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author ms
 */
public class TeiSource extends TeiElement {
    private TeiRef ref = null;

    public TeiSource(TeiElement parent) {
        super(parent);
    }

    private TeiSource(TeiRecordHist parent, Element el) {
        this(parent);

        ref = TeiRef.load(this, consumeChild(el, "ref", TeiNS, false));

        consume(el);
    }

    public static TeiSource load(TeiRecordHist parent, Element el) {
        return new TeiSource(parent, el);
    }

    @Override
    public String getElementName() {
        return "source";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        if (ref!=null) {
            el.addContent(ref.toXML());
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
