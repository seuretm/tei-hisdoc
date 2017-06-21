/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.accmat.TeiAccMat;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.handdesc.TeiHandDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.TeiObjectDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.scriptdesc.TeiScriptDesc;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiPhysDesc extends TeiElement {
    private TeiObjectDesc objectDesc = new TeiObjectDesc(this);
    private TeiHandDesc handDesc = new TeiHandDesc(this);
    private TeiScriptDesc scriptDesc = new TeiScriptDesc(this);
    private TeiAccMat accMat = null;

    public TeiPhysDesc(TeiElement parent) {
        super(parent);
    }

    private TeiPhysDesc(TeiElement parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "objectDesc", TeiNS, true);
        objectDesc = TeiObjectDesc.load(this, e);

        e = consumeChild(el, "handDesc", TeiNS, false);
        if (e != null) {
            handDesc = TeiHandDesc.load(this, e);
//        } else {
//            handDesc = new TeiHandDesc(this);
        }

        e = consumeChild(el, "accMat", TeiNS, false);
        if (e != null) {
            accMat = TeiAccMat.load(this, e);
//        } else {
//            accMat = new TeiAccMat(this);
        }

        e = consumeChild(el, "scriptDesc", TeiNS, false);
        if (e != null) {
            scriptDesc = TeiScriptDesc.load(this, e);
//        } else {
//            scriptDesc = new TeiScriptDesc(this);
        }

        for (Element i : el.getChildren("musicNotation", TeiNS)) {
            addIgnoredElement(i);
        }
        el.removeChildren("musicNotation", TeiNS);

        for (Element i : el.getChildren("decoDesc", TeiNS)) {
            addIgnoredElement(i);
        }
        el.removeChildren("decoDesc", TeiNS);


        consume(el);
    }

    public static TeiPhysDesc load(TeiElement parent, Element el) {
        return new TeiPhysDesc(parent, el);
    }

    @Override
    public String getElementName() {
        return "physDesc";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        addContent(el, objectDesc);
        addContent(el, handDesc);
        addContent(el, scriptDesc);
        addContent(el, accMat);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    // ACCESSORS

    /**
     * @return the hand desc
     */
    public TeiHandDesc getHandDesc() {
        return handDesc;
    }

}
