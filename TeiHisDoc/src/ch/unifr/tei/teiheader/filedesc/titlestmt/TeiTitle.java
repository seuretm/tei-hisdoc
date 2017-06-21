/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.titlestmt;

import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiTitle extends TeiTextElement {

    public TeiTitle(TeiTitleStmt parent) {
        super(parent);
    }

    private TeiTitle(TeiTitleStmt parent, Element el) {
        this(parent);
        setContent(el.getText());
        consume(el);
    }

    public static TeiTitle load(TeiTitleStmt parent, Element el) {
        return new TeiTitle(parent, el);
    }

    @Override
    public String getElementName() {
        return "title";
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
