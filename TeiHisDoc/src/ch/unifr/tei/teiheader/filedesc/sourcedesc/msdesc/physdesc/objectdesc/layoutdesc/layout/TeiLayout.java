/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.layoutdesc.layout;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.layoutdesc.TeiLayoutDesc;
import ch.unifr.tei.utils.TeiDimensions;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiLocus;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiLayout extends TeiElement {
    private TeiLocus locus = null;

    private int columns = 0;
    private List<Integer> writtenLines = new ArrayList<>();

    private TeiDimensions dimensions = null;

    public TeiLayout(TeiElement parent) {
        super(parent);
    }

    private TeiLayout(TeiLayoutDesc parent, Element el) {
        this(parent);

        columns = consumeAttributeInt(el, "columns", NoNS, true);
        String ls = consumeAttributeStr(el, "writtenLines", NoNS, true);
        for (String s : ls.split(" ")) {
            writtenLines.add(Integer.parseInt(s));
        }

        Element e = consumeChild(el, "locus", TeiNS, false);
        if (e != null) {
            locus = TeiLocus.load(this, e);
        } else {
            locus = new TeiLocus(this);
        }

        e = consumeChild(el, "dimensions", TeiNS, true);
        dimensions = TeiDimensions.load(this, e);

        for (Element i : el.getChildren("measure", TeiNS)) {
            addIgnoredElement(i);
        }
        el.removeChildren("measure", TeiNS);

        consume(el);
    }

    public static TeiLayout load(TeiLayoutDesc parent, Element el) {
        return new TeiLayout(parent, el);
    }

    @Override
    public String getElementName() {
        return "layout";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        addAttribute(el, "columns", String.valueOf(columns), NoNS);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < writtenLines.size(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(writtenLines.get(i).toString());
        }
        addAttribute(el, "writtenLines", sb.toString(), NoNS);

        addContent(el, locus);
        addContent(el, dimensions);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
