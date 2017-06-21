/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.scriptdesc;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiLocus;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ms
 */
public class TeiScriptNote extends TeiElement {
    private List<TeiLocus> locuses = new LinkedList<>();
    private String script = null;

    public TeiScriptNote(TeiElement parent) {
        super(parent);
    }

    private TeiScriptNote(TeiScriptDesc parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("locus", TeiNS)) {
            locuses.add(TeiLocus.load(this, e));
        }
        el.removeChildren("locus", TeiNS);

        script = consumeAttributeStr(el, "script", NoNS, true);

        consume(el);
    }

    public static TeiScriptNote load(TeiScriptDesc parent, Element el) {
        return new TeiScriptNote(parent, el);
    }

    @Override
    public String getElementName() {
        return "scriptNote";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : locuses) {
            addContent(el, e);
        }
        addAttribute(el, "script", script, NoNS);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
