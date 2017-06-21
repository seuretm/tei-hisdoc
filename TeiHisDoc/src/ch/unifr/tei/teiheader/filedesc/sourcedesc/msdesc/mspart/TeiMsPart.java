/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart;

import ch.unifr.tei.facsimile.surfacegrp.TeiSurfaceGrpPart;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.TeiMsDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.mscontents.TeiMsContents;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.msidentifier.TeiMsIdentifier;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.TeiPhysDesc;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiFacsReferrer;
import ch.unifr.tei.utils.postponed.FacsSetter;
import org.jdom2.Element;

/**
 * @author ms
 */
public class TeiMsPart extends TeiElement implements TeiFacsReferrer<TeiSurfaceGrpPart> {
    private TeiMsIdentifier msIdentifier = null;
    private TeiMsContents msContents = null;
    private TeiPhysDesc physDesc = null;
    private TeiSurfaceGrpPart facs = null;

    public TeiMsPart(TeiElement parent) {
        super(parent);
    }

    private TeiMsPart(TeiMsDesc parent, Element el) {
        this(parent);

        msIdentifier = TeiMsIdentifier.load(this, consumeChild(el, "msIdentifier", TeiNS, true));

        Element e = consumeChild(el, "msContents", TeiNS, false);
        if (e != null) {
            msContents = TeiMsContents.load(this, e);
        } else {
            msContents = new TeiMsContents(this);
        }

        e = consumeChild(el, "physDesc", TeiNS, false);
        if (e != null) {
            physDesc = TeiPhysDesc.load(this, e);
        } else {
            physDesc = new TeiPhysDesc(this);
        }
        
        String facsAttr = consumeAttributeStr(el, "facs", NoNS, false);
        if (facsAttr!=null) {
            // TODO : 1 Mathias load the facs after the other elements
            postpone(
                    new FacsSetter<>(this, facsAttr)
            );
        }

        consume(el);
    }

    public static TeiMsPart load(TeiMsDesc parent, Element el) {
        return new TeiMsPart(parent, el);
    }

    @Override
    public String getElementName() {
        return "msPart";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.addContent(msIdentifier.toXML());

        if (msContents != null) {
            el.addContent(msContents.toXML());
        }
        if (physDesc != null) {
            el.addContent(physDesc.toXML());
        }
        
        if (facs!=null) {
            addAttribute(el, "facs", "#"+facs.getId(), NoNS);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        int n = ((TeiMsDesc)parent).getIndex(this) + 1;
        setId("msPart-" + n);
    }

    @Override
    public void setFacs(TeiSurfaceGrpPart f) {
        facs = f;
    }

    @Override
    public TeiSurfaceGrpPart getFacs() {
        return facs;
    }

}
