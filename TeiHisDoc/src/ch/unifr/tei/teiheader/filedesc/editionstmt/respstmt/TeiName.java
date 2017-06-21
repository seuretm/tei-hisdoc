/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.editionstmt.respstmt;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiName extends TeiTextElement {
    public TeiName(TeiElement parent) {
        super(parent);
    }

    private TeiName(TeiElement parent, Element el) {
        this(parent);

        setContent(el.getText());

        consume(el);
    }

    public static TeiName load(TeiElement parent, Element el) {
        return new TeiName(parent, el);
    }

    @Override
    public String getElementName() {
        return "name";
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
