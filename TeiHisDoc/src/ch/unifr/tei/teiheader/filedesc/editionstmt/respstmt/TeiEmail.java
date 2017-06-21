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
public class TeiEmail extends TeiTextElement {
    public TeiEmail(TeiElement parent) {
        super(parent);
    }

    private TeiEmail(TeiPersName parent, Element el) {
        this(parent);

        setContent(el.getText());

        consume(el);
    }

    public static TeiEmail load(TeiPersName parent, Element el) {
        return new TeiEmail(parent, el);
    }

    @Override
    public String getElementName() {
        return "email";
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }
}
