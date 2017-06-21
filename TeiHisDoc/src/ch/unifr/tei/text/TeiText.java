/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.text;

import ch.unifr.tei.TeiHisDoc;
import ch.unifr.tei.text.body.TeiBody;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 * @author ms
 */
public class TeiText extends TeiElement {

    private TeiBody body = new TeiBody(this);

    public TeiText(TeiElement parent) {
        super(parent);
    }

    private TeiText(TeiHisDoc parent, Element el) {
        this(parent);

        addIgnoredElement(consumeChild(el, "front", TeiNS, false));

        body = TeiBody.load(this, consumeChild(el, "body", TeiNS, false));

        consume(el);
    }

    public static TeiText load(TeiHisDoc parent, Element el) {
        return new TeiText(parent, el);
    }

    @Override
    public String getElementName() {
        return "text";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        addContent(el, body);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    public TeiBody getBody() {
        return body;
    }

}
