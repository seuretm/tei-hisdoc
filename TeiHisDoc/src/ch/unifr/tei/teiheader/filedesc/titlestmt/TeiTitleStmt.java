/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.titlestmt;

import ch.unifr.tei.teiheader.filedesc.TeiFileDesc;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiTitleStmt extends TeiElement {
    private TeiTitle title = new TeiTitle(this);

    public TeiTitleStmt(TeiElement parent) {
        super(parent);
    }

    private TeiTitleStmt(TeiFileDesc parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "title", TeiNS, true);
        title = TeiTitle.load(this, e);

        consume(el);
    }

    public static TeiTitleStmt load(TeiFileDesc parent, Element el) {
        return new TeiTitleStmt(parent, el);
    }

    @Override
    public String getElementName() {
        return "titleStmt";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.addContent(title.toXML());

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }
    
    
    /*
        ACCESSORS
    */

    /**
     * @return the content of the title tag, i.e., the title of the manuscript
     */
    public String getTitle() {
        return title.getContent();
    }

    /**
     * Sets the title of the manuscript.
     *
     * @param s new title
     */
    public void setTitle(String s) {
        title.setContent(s);
    }
}
