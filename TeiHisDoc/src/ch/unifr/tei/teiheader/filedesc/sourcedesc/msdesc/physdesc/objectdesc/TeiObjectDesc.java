/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.TeiPhysDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.layoutdesc.TeiLayoutDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc.TeiSupportDesc;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiObjectDesc extends TeiElement {
    private TeiSupportDesc supportDesc = new TeiSupportDesc(this);
    private TeiLayoutDesc layoutDesc = null;
    private String form = null;

    public TeiObjectDesc(TeiElement parent) {
        super(parent);
    }

    private TeiObjectDesc(TeiPhysDesc parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "supportDesc", TeiNS, true);
        supportDesc = TeiSupportDesc.load(this, e);

        e = consumeChild(el, "layoutDesc", TeiNS, false);
        if (e != null) {
            layoutDesc = TeiLayoutDesc.load(this, e);
//        } else {
//            layoutDesc = new TeiLayoutDesc(this);
        }

        form = consumeAttributeStr(el, "form", NoNS, false);

        consume(el);
    }

    public static TeiObjectDesc load(TeiPhysDesc parent, Element el) {
        return new TeiObjectDesc(parent, el);
    }

    @Override
    public String getElementName() {
        return "objectDesc";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        addContent(el, supportDesc);
        addContent(el, layoutDesc);
        addAttribute(el, "form", form, NoNS);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
