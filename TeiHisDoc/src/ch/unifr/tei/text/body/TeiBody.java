/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.text.body;

import ch.unifr.tei.text.TeiText;
import ch.unifr.tei.text.body.div.TeiDiv;
import ch.unifr.tei.text.body.div.TeiDivContainer;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiElementContainer;
import org.jdom2.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiBody extends TeiElement implements TeiDivContainer, TeiElementContainer<TeiDiv> {
    private List<TeiDiv> divs = new LinkedList<>();

    public TeiBody(TeiElement parent) {
        super(parent);
    }

    private TeiBody(TeiText parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("div", TeiNS)) {
            divs.add(TeiDiv.load(this, e));
        }
        el.removeChildren("div", TeiNS);

        consume(el);
    }

    public static TeiBody load(TeiText parent, Element el) {
        if (el == null) {
            return null;
        }
        return new TeiBody(parent, el);
    }

    @Override
    public String getElementName() {
        return "body";
    }
    
    public boolean remove(TeiDiv div) {
        div.notifyDeletion();
        return divs.remove(div);
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : divs) {
            addContent(el, e);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    @Override
    public int getIndex(TeiDiv child) {
        return divs.indexOf(child);
    }

    public List<TeiDiv> getDivs() {
        return Collections.unmodifiableList(divs);
    }
    
    public TeiDiv addDiv() {
        TeiDiv d = new TeiDiv(this);
        divs.add(d);
        return d;
    }

}
