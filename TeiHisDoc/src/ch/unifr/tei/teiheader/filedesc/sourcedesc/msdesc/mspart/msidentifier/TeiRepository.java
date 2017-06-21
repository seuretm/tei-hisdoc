/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.msidentifier;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiRepository extends TeiTextElement {
    public TeiRepository(TeiElement parent) {
        super(parent);
    }

    private TeiRepository(TeiMsIdentifier parent, Element el) {
        this(parent);

        setContent(el.getText());

        consume(el);
    }

    public static TeiRepository load(TeiMsIdentifier parent, Element el) {
        return new TeiRepository(parent, el);
    }

    @Override
    public String getElementName() {
        return "repository";
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }
}
