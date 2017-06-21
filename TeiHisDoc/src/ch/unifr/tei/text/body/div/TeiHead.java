/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.text.body.div;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiHead extends TeiTextElement {
    public TeiHead(TeiElement parent) {
        super(parent);
    }

    private TeiHead(TeiDiv parent, Element el) {
        this(parent);

        consume(el);
    }

    public static TeiHead load(TeiDiv parent, Element el) {
        return new TeiHead(parent, el);
    }

    @Override
    public String getElementName() {
        return "head";
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
