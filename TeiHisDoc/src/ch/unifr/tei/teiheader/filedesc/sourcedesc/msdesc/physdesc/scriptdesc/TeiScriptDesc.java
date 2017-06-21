/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.scriptdesc;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.TeiPhysDesc;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ms
 */
public class TeiScriptDesc extends TeiElement {
    private List<TeiScriptNote> scriptNotes = new LinkedList<>();

    public TeiScriptDesc(TeiElement parent) {
        super(parent);
    }

    private TeiScriptDesc(TeiPhysDesc parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("scriptNote", TeiNS)) {
            scriptNotes.add(TeiScriptNote.load(this, e));
        }
        el.removeChildren("scriptNote", TeiNS);

        consume(el);
    }

    public static TeiScriptDesc load(TeiPhysDesc parent, Element el) {
        return new TeiScriptDesc(parent, el);
    }

    @Override
    public String getElementName() {
        return "scriptDesc";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : scriptNotes) {
            addContent(el, e);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }
}
