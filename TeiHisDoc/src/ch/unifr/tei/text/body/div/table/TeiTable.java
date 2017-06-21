/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.text.body.div.table;

import ch.unifr.tei.text.body.div.TeiDiv;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiTable extends TeiElement {
    protected List<TeiRow> rows = new ArrayList<>();

    public TeiTable(TeiElement parent) {
        super(parent);
    }

    private TeiTable(TeiDiv parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("row", TeiNS)) {
            rows.add(TeiRow.load(this, e));
        }
        el.removeChildren("row", TeiNS);

        consume(el);
    }

    public static TeiTable load(TeiDiv parent, Element el) {
        return new TeiTable(parent, el);
    }

    @Override
    public String getElementName() {
        return "table";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiRow r : rows) {
            addContent(el, r);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
