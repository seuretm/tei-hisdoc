/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.editionstmt.respstmt;

import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiMethodName extends TeiElement {
    private TeiDate date = null;
    private TeiName name = null;
    private String role = null;

    public TeiMethodName(TeiElement parent) {
        super(parent);
    }

    public TeiMethodName(TeiElement parent, String name, String role) {
        this(parent);
        this.name = new TeiName(this);
        this.name.setContent(name);
        this.role = role;
        date = new TeiDate(this);
    }

    private TeiMethodName(TeiRespStmt parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "date", TeiNS, true);
        date = TeiDate.load(this, e);

        e = consumeChild(el, "name", TeiNS, true);
        name = TeiName.load(this, e);

        role = consumeAttributeStr(el, "role", NoNS, false);

        consume(el);
    }

    public static TeiMethodName load(TeiRespStmt parent, Element el) {
        return new TeiMethodName(parent, el);
    }

    @Override
    public String getElementName() {
        return "name";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.addContent(date.toXML());
        el.addContent(name.toXML());

        if (role != null) {
            el.setAttribute("role", role, NoNS);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
