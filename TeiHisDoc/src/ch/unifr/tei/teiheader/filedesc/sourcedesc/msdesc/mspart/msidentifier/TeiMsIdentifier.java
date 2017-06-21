/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.msidentifier;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.msidentifier.altidentifier.TeiAltIdentifier;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiMsIdentifier extends TeiElement implements TeiIdnoContainer {
    private TeiSettlement settlement = new TeiSettlement(this);
    private TeiRepository repository = new TeiRepository(this);
    private TeiIdno idno = new TeiIdno(this);
    private TeiAltIdentifier altIdentifier = null;

    public TeiMsIdentifier(TeiElement parent) {
        super(parent);
    }

    private TeiMsIdentifier(TeiElement parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "settlement", TeiNS, true);
        settlement = TeiSettlement.load(this, e);

        e = consumeChild(el, "repository", TeiNS, true);
        repository = TeiRepository.load(this, e);

        e = consumeChild(el, "idno", TeiNS, true);
        idno = TeiIdno.load(this, e);

        e = consumeChild(el, "altIdentifier", TeiNS, false);
        if (e != null) {
            altIdentifier = TeiAltIdentifier.load(this, e);
//        } else {
//            altIdentifier = new TeiAltIdentifier(this);
        }

        consume(el);
    }

    public static TeiMsIdentifier load(TeiElement parent, Element el) {
        return new TeiMsIdentifier(parent, el);
    }

    @Override
    public String getElementName() {
        return "msIdentifier";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        addContent(el, settlement);
        addContent(el, repository);
        addContent(el, idno);
        addContent(el, altIdentifier);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
