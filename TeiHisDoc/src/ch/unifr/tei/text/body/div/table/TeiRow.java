/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.text.body.div.table;

import ch.unifr.tei.text.body.div.div.TeiDivDivContainer;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiRow extends TeiElement implements TeiDivDivContainer {
    protected List<TeiCell> cells = new ArrayList<>();

    protected String role;
    protected int cols = -1;

    public TeiRow(TeiElement parent) {
        super(parent);
    }

    private TeiRow(TeiTable parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("cell", TeiNS)) {
            cells.add(new TeiCell(this, e));
        }
        el.removeChildren("cell", TeiNS);

        role = consumeAttributeStr(el, "role", NoNS, false);
        cols = consumeAttributeInt(el, "cols", NoNS, false);

        consume(el);
    }

    public static TeiRow load(TeiTable parent, Element el) {
        return new TeiRow(parent, el);
    }

    @Override
    public String getElementName() {
        return "row";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiCell c : cells) {
            addContent(el, (TeiElement) c);
        }

        addAttribute(el, "role", role, NoNS);
        if (cols != -1) {
            addAttribute(el, "cols", String.valueOf(cols), NoNS);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }
}
