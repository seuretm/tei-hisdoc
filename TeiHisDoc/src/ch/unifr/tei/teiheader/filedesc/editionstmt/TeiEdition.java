/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.editionstmt;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiEdition extends TeiTextElement {

    public TeiEdition(TeiElement parent) {
        super(parent);
        setContent("Electronic version in TEI-HisDoc");
    }

    private TeiEdition(TeiEditionStmt parent, Element el) {
        this(parent);

        setContent(el.getText());

        consume(el);
    }

    public static TeiEdition load(TeiEditionStmt parent, Element el) {
        return new TeiEdition(parent, el);
    }

    @Override
    public String getElementName() {
        return "edition";
    }

    @Override
    public void generateDefaultId() {
        // nothing to do
    }

}
