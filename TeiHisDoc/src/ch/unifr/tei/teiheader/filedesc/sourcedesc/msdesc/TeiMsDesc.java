/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.TeiSourceDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.additional.TeiAdditional;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.history.TeiHistory;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.TeiMsPart;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.mscontents.TeiMsContents;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.msidentifier.TeiMsIdentifier;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.TeiPhysDesc;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiMsDesc extends TeiElement {
    private TeiMsIdentifier msIdentifier = new TeiMsIdentifier(this);
    private TeiMsContents msContents = null;
    private TeiPhysDesc physDesc = new TeiPhysDesc(this);
    private TeiAdditional additional = new TeiAdditional(this);
    private List<TeiMsPart> msParts = new LinkedList<>();
    private TeiHistory history = null;

    public TeiMsDesc(TeiElement parent) {
        super(parent);
    }

    private TeiMsDesc(TeiSourceDesc parent, Element el) {
        this(parent);

        
        Element e = consumeChild(el, "msIdentifier", TeiNS, false);
        if (e != null) {
            msIdentifier = TeiMsIdentifier.load(this, e);
        } else {
            msIdentifier = new TeiMsIdentifier(this);
        }

        e = consumeChild(el, "msContents", TeiNS, false);
        if (e != null) {
            msContents = TeiMsContents.load(this, e);
        } else {
            msContents = new TeiMsContents(this);
        }

        e = consumeChild(el, "physDesc", TeiNS, true);
        physDesc = TeiPhysDesc.load(this, e);

        e = consumeChild(el, "additional", TeiNS, false);
        if (e != null) {
            additional = TeiAdditional.load(this, e);
        } else {
            additional = new TeiAdditional(this);
        }

        e = consumeChild(el, "history", TeiNS, false);
        if (e != null) {
            history = TeiHistory.load(this, e);
        } else {
            history = new TeiHistory(this);
        }

        for (Element m : el.getChildren("msPart", TeiNS)) {
            msParts.add(TeiMsPart.load(this, m));
        }
        el.removeChildren("msPart", TeiNS);

        consume(el);
    }

    public static TeiMsDesc load(TeiSourceDesc parent, Element el) {
        return new TeiMsDesc(parent, el);
    }

    @Override
    public String getElementName() {
        return "msDesc";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        addContent(el, msIdentifier);
        addContent(el, physDesc);
        addContent(el, history);
        addContent(el, additional);

        for (TeiElement e : msParts) {
            el.addContent(e.toXML());
        }

        addContent(el, msContents);
        return el;
    }

    @Override
    public void updateChildrenIDs() {
        msIdentifier.updateChildrenIDs();
        physDesc.updateChildrenIDs();
        additional.updateChildrenIDs();
        history.updateChildrenIDs();
        if (msContents != null) {
            msContents.updateChildrenIDs();
        }
        for (TeiElement e : msParts) {
            e.setId(e.getRandomId());
        }
        for (TeiElement e : msParts) {
            e.generateDefaultId();
            e.updateChildrenIDs();
        }
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    // Accessors

    /**
     * @return the phys desc element
     */
    public TeiPhysDesc getPhysDesc() {
        return physDesc;
    }

    public int getIndex(TeiMsPart p) {
        return msParts.indexOf(p);
    }

}
