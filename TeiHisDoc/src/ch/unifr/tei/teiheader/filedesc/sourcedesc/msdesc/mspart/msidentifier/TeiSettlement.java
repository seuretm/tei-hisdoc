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
public class TeiSettlement extends TeiTextElement {
    public TeiSettlement(TeiElement parent) {
        super(parent);
    }

    private TeiSettlement(TeiMsIdentifier parent, Element el) {
        this(parent);

        setContent(el.getText());

        consume(el);
    }

    public static TeiSettlement load(TeiMsIdentifier parent, Element el) {
        return new TeiSettlement(parent, el);
    }

    @Override
    public String getElementName() {
        return "settlement";
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
