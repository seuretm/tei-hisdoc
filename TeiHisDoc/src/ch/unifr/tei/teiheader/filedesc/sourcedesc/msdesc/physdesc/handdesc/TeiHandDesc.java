/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.handdesc;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.TeiPhysDesc;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.handdesc.handnote.TeiHandNote;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * TeiHandDesc contain description of the scribes and scripts
 *
 * @author Mathias Seuret - mathias.seuret@unifr.ch
 * @author Manuel Bouillon - manuel.bouillon@unifr.ch
 */
public class TeiHandDesc extends TeiElement {
    private List<TeiHandNote> handNotes = new LinkedList<>();


    public TeiHandDesc(TeiElement parent) {
        super(parent);
    }

    private TeiHandDesc(TeiPhysDesc parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("handNote", TeiNS)) {
            handNotes.add(TeiHandNote.load(this, e));
        }
        el.removeChildren("handNote", TeiNS);

        consume(el);
    }

    public static TeiHandDesc load(TeiPhysDesc parent, Element el) {
        return new TeiHandDesc(parent, el);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getElementName() {
        return "handDesc";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : handNotes) {
            addContent(el, e);
        }

        return el;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    /* ********* ACCESSORS ********** */
//    private List<TeiHandNote> handNotes = new LinkedList();

    /**
     * Get the hand note list
     *
     * @return hand note list
     */
    public List<TeiHandNote> getHandNotes() {
        return Collections.unmodifiableList(handNotes);
    }

    /**
     * @param byID handNote id
     * @return the handNote of id byID
     */
    public TeiHandNote getHandNote(String byID) {
        for (TeiHandNote hn : handNotes) {
            if (hn.getId().equals(byID)) {
                return hn;
            }
        }
        throw new Error("cannot find a hand note with ID \"" + byID + "\"");
    }

    /**
     * Creates an empty handNote
     *
     * @return the newly created handNote
     */
    public TeiHandNote addHandNote() {
        TeiHandNote t = new TeiHandNote(this);

        handNotes.add(handNotes.size(), t);
        t.generateDefaultId();

        return t;
    }

    /**
     * Removes the indicated handNote.
     *
     * @param id of the handNote
     */
    public void removeHandNote(String id) {
        for (TeiHandNote hn : handNotes) {
            if (hn.getId().equals(id)) {
                handNotes.remove(hn);
                return;
            }
        }
        throw new Error("cannot find a hand note with ID \"" + id + "\"");
    }

    /**
     * @return the number of handNote in this part
     */
    public int nbHandNotes() {
        return handNotes.size();
    }

}
