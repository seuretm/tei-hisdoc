/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.layoutdesc;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.TeiObjectDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.layoutdesc.layout.TeiLayout;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiLayoutDesc extends TeiElement {
    private List<TeiLayout> layouts = new LinkedList<>();

    public TeiLayoutDesc(TeiElement parent) {
        super(parent);
    }

    private TeiLayoutDesc(TeiObjectDesc parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("layout", TeiNS)) {
            layouts.add(TeiLayout.load(this, e));
        }
        el.removeChildren("layout", TeiNS);
        if (layouts.isEmpty()) {
            throw new Error("at least one <layout> is required at " + getPath());
        }

        consume(el);
    }

    public static TeiLayoutDesc load(TeiObjectDesc parent, Element el) {
        return new TeiLayoutDesc(parent, el);
    }

    @Override
    public String getElementName() {
        return "layoutDesc";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : layouts) {
            addContent(el, e);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
