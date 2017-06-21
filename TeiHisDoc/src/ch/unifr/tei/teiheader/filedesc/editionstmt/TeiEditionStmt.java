/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.editionstmt;

import ch.unifr.tei.teiheader.filedesc.TeiFileDesc;
import ch.unifr.tei.teiheader.filedesc.editionstmt.respstmt.TeiRespStmt;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiEditionStmt extends TeiElement {
    private TeiEdition edition = new TeiEdition(this);
    private List<TeiRespStmt> respStmts = new LinkedList<>();

    public TeiEditionStmt(TeiElement parent) {
        super(parent);
    }

    private TeiEditionStmt(TeiFileDesc parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "edition", TeiNS, true);
        edition = TeiEdition.load(this, e);

        for (Element rs : el.getChildren("respStmt", TeiNS)) {
            respStmts.add(TeiRespStmt.load(this, rs));
        }
        el.removeChildren("respStmt", TeiNS);
        if (respStmts.isEmpty()) {
            throw new Error("at least one <respStmt> is required in " + getPath());
        }

        consume(el);
    }

    public static TeiEditionStmt load(TeiFileDesc parent, Element el) {
        return new TeiEditionStmt(parent, el);
    }

    @Override
    public String getElementName() {
        return "editionStmt";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.addContent(edition.toXML());

        for (TeiRespStmt e : respStmts) {
            el.addContent(e.toXML());
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    public int indexOf(TeiRespStmt child) {
        return respStmts.indexOf(child);
    }

    @Override
    public void updateChildrenIDs() {
        for (TeiElement e : respStmts) {
            e.setId(e.getRandomId());
        }
        for (TeiElement e : respStmts) {
            e.generateDefaultId();
            e.updateChildrenIDs();
        }
        edition.updateChildrenIDs();
    }

    /*
        ACCESSORS
    */
    public TeiRespStmt addRespStmt(TeiRespStmt r) {
        respStmts.add(r);
        r.setParent(this);
        return r;
    }

    /**
     * Returns the resp statement with the given id
     *
     * @param id id of the resp statement to fetch
     * @return either a resp statement, or null if it was not found
     */
    public TeiRespStmt getRespStatement(String id) {
        for (TeiRespStmt r : respStmts) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    /**
     * @return the edition
     */
    public String getEdition() {
        return edition.getContent();
    }

    /**
     * Sets the edition
     *
     * @param edition new edition
     */
    public void setEdition(String edition) {
        this.edition.setContent(edition);
    }

    public List<TeiRespStmt> getRespStmts() {
        return Collections.unmodifiableList(respStmts);
    }
}
