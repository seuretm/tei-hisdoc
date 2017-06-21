/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc;

import ch.unifr.tei.teiheader.TeiHeader;
import ch.unifr.tei.teiheader.filedesc.editionstmt.TeiEditionStmt;
import ch.unifr.tei.teiheader.filedesc.publicationstmt.TeiPublicationStmt;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.TeiSourceDesc;
import ch.unifr.tei.teiheader.filedesc.titlestmt.TeiTitleStmt;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiFileDesc extends TeiElement {

    private TeiTitleStmt titleStmt = new TeiTitleStmt(this);
    private TeiEditionStmt editionStmt = new TeiEditionStmt(this);
    private TeiPublicationStmt publicationStmt = new TeiPublicationStmt(this);
    private TeiSourceDesc sourceDesc = new TeiSourceDesc(this);

    public TeiFileDesc(TeiElement parent) {
        super(parent);
    }

    private TeiFileDesc(TeiHeader parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "titleStmt", TeiNS, true);
        titleStmt = TeiTitleStmt.load(this, e);

        e = consumeChild(el, "editionStmt", TeiNS, true);
        editionStmt = TeiEditionStmt.load(this, e);

        e = consumeChild(el, "publicationStmt", TeiNS, true);
        publicationStmt = TeiPublicationStmt.load(this, e);

        e = consumeChild(el, "sourceDesc", TeiNS, true);
        sourceDesc = TeiSourceDesc.load(this, e);

        consume(el);
    }

    public static TeiFileDesc load(TeiHeader parent, Element el) {
        return new TeiFileDesc(parent, el);
    }

    @Override
    public String getElementName() {
        return "fileDesc";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.addContent(titleStmt.toXML());
        el.addContent(editionStmt.toXML());
        el.addContent(getPublicationStmt().toXML());
        el.addContent(getSourceDesc().toXML());

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    @Override
    public void updateChildrenIDs() {
        titleStmt.updateChildrenIDs();
        editionStmt.updateChildrenIDs();
        publicationStmt.updateChildrenIDs();
        sourceDesc.updateChildrenIDs();
    }
    
    /*
        ACCESSORS
    */

    /**
     * @return the edition statement
     */
    public TeiEditionStmt getEditionStmt() {
        return editionStmt;
    }

    /**
     * @return the title statement
     */
    public TeiTitleStmt getTitleStmt() {
        return titleStmt;
    }

    /**
     * @return the publication statement
     */
    public TeiPublicationStmt getPublicationStmt() {
        return publicationStmt;
    }

    /**
     * @return the source description
     */
    public TeiSourceDesc getSourceDesc() {
        return sourceDesc;
    }
}
