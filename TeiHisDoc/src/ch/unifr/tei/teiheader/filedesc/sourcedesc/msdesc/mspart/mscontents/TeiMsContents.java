/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.mscontents;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.mscontents.msitem.TeiMsItem;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiMsContents extends TeiElement {
    private List<TeiMsItem> msItems = new LinkedList<>();

    public TeiMsContents(TeiElement parent) {
        super(parent);
    }

    private TeiMsContents(TeiElement parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("msItem", TeiNS)) {
            msItems.add(TeiMsItem.load(this, e));
        }
        el.removeChildren("msItem", TeiNS);

        consume(el);
    }

    public static TeiMsContents load(TeiElement parent, Element el) {
        return new TeiMsContents(parent, el);
    }

    @Override
    public String getElementName() {
        return "msContents";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : msItems) {
            el.addContent(e.toXML());
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
