/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.msidentifier.altidentifier;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.msidentifier.TeiIdno;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.msidentifier.TeiIdnoContainer;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.msidentifier.TeiMsIdentifier;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiAltIdentifier extends TeiElement implements TeiIdnoContainer {
    private List<TeiIdno> idnos = new LinkedList<>();
    private String type = null;

    public TeiAltIdentifier(TeiElement parent) {
        super(parent);
    }

    private TeiAltIdentifier(TeiMsIdentifier parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("idno", TeiNS)) {
            idnos.add(TeiIdno.load(this, e));
        }
        el.removeChildren("idno", TeiNS);

        type = consumeAttributeStr(el, "type", NoNS, true);

        consume(el);
    }

    public static TeiAltIdentifier load(TeiMsIdentifier parent, Element el) {
        return new TeiAltIdentifier(parent, el);
    }

    @Override
    public String getElementName() {
        return "altIdentifier";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : idnos) {
            addContent(el, e);
        }
        addAttribute(el, "type", type, NoNS);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
