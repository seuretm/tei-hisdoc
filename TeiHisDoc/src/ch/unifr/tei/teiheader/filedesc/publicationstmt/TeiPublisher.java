/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.publicationstmt;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiPublisher extends TeiTextElement {
    public TeiPublisher(TeiElement parent) {
        super(parent);
    }

    private TeiPublisher(TeiPublicationStmt parent, Element el) {
        this(parent);

        setContent(el.getText());

        consume(el);
    }

    public static TeiPublisher load(TeiPublicationStmt parent, Element el) {
        return new TeiPublisher(parent, el);
    }
    
    @Override
    public void generateDefaultId() {
        int n = ((TeiPublicationStmt)parent).getIndex(this) + 1;
        setId("publisher-" + n);
    }

    @Override
    public String getElementName() {
        return "publisher";
    }

}
