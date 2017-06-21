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
public class TeiSurname extends TeiTextElement {
    public TeiSurname(TeiElement parent) {
        super(parent);
    }

    private TeiSurname(TeiPersName parent, Element el) {
        this(parent);

        setContent(el.getText());

        consume(el);
    }

    public static TeiSurname load(TeiPersName parent, Element el) {
        return new TeiSurname(parent, el);
    }

    @Override
    public String getElementName() {
        return "surname";
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
