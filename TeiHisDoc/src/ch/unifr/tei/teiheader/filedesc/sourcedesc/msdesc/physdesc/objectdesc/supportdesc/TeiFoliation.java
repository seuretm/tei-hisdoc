/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiLocus;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiFoliation extends TeiElement {
    private List<TeiLocus> locuses = new LinkedList<>();

    public TeiFoliation(TeiElement parent) {
        super(parent);
    }

    private TeiFoliation(TeiSupportDesc parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("locus", TeiNS)) {
            locuses.add(TeiLocus.load(this, e));
        }
        el.removeChildren("locus", TeiNS);

        consume(el);
    }

    public static TeiFoliation load(TeiSupportDesc parent, Element el) {
        return new TeiFoliation(parent, el);
    }

    @Override
    public String getElementName() {
        return "foliation";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : locuses) {
            addContent(el, e);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
