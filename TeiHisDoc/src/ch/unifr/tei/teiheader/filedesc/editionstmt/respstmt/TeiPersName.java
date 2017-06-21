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
public class TeiPersName extends TeiElement {
    private TeiDate date = new TeiDate(this);
    private TeiEmail email = null;
    private TeiSurname surname = null;
    private TeiName name = new TeiName(this);
    private String role = null;

    public TeiPersName(TeiElement parent) {
        super(parent);
    }

    public TeiPersName(TeiRespStmt parent, String email, String surname, String name, String role) {
        this(parent);
        this.email = new TeiEmail(parent);
        this.email.setContent(email);
        this.surname = new TeiSurname(parent);
        this.surname.setContent(surname);
        this.name = new TeiName(parent);
        this.name.setContent(name);
        this.role = role;


    }

    private TeiPersName(TeiRespStmt parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "date", TeiNS, true);
        date = TeiDate.load(this, e);

        e = consumeChild(el, "email", TeiNS, false);
        if (e != null) {
            email = TeiEmail.load(this, e);
        } else {
            email = new TeiEmail(this);
        }

        e = consumeChild(el, "surname", TeiNS, false);
        if (e != null) {
            surname = TeiSurname.load(this, e);
        } else {
            surname = new TeiSurname(this);
        }

        e = consumeChild(el, "name", TeiNS, true);
        name = TeiName.load(this, e);

        role = consumeAttributeStr(el, "role", NoNS, false);

        consume(el);
    }

    public static TeiPersName load(TeiRespStmt parent, Element el) {
        return new TeiPersName(parent, el);
    }

    @Override
    public String getElementName() {
        return "persName";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.addContent(date.toXML());

        if (email != null) {
            el.addContent(email.toXML());
        }

        if (surname != null) {
            el.addContent(surname.toXML());
        }

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

    /**
     * Sets the modification date.
     * Note that when creating a TeiPersName the current date is set by
     * default.
     *
     * @param d new date to set
     */
    public void setDate(String d) {
        date.setDate(d);
    }


}
