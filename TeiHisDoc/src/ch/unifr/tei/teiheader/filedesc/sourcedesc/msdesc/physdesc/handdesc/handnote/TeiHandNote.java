/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */


package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.handdesc.handnote;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.handdesc.TeiHandDesc;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiLocus;
import org.jdom2.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * TeiHandNote contain the locus written by a specific scribe in a specific script
 *
 * @author Mathias Seuret - mathias.seuret@unifr.ch
 * @author Manuel Bouillon - manuel.bouillon@unifr.ch
 */
public class TeiHandNote extends TeiElement {
    private String scribe = null;
    private String script = null;
    private TeiPlaceName placeName = null;
    private List<TeiPersName> persNames = new LinkedList<>();
    private List<TeiLocus> locuses = new LinkedList<>();

    public TeiHandNote(TeiElement parent) {
        super(parent);
    }

    private TeiHandNote(TeiHandDesc parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("locus", TeiNS)) {
            locuses.add(TeiLocus.load(this, e));
        }
        el.removeChildren("locus", TeiNS);

        for (Element e : el.getChildren("persName", TeiNS)) {
            persNames.add(TeiPersName.load(this, e));
        }
        el.removeChildren("persName", TeiNS);

        Element e = consumeChild(el, "placeName", TeiNS, false);
        if (e != null) {
            placeName = new TeiPlaceName(this, e);
        } else {
            placeName = new TeiPlaceName(this);
        }

        scribe = consumeAttributeStr(el, "scribe", NoNS, true);
        script = consumeAttributeStr(el, "script", NoNS, true);

        consume(el);
    }

    public static TeiHandNote load(TeiHandDesc parent, Element el) {
        return new TeiHandNote(parent, el);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getElementName() {
        return "handNote";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : locuses) {
            addContent(el, e);
        }
        for (TeiElement e : persNames) {
            addContent(el, e);
        }
        addContent(el, placeName);
        addAttribute(el, "scribe", scribe, NoNS);
        addAttribute(el, "script", script, NoNS);

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
//    private String scribe = null;
//    private String script = null;
//    private TeiPlaceName placeName = null;
//    private List<TeiPersName> persNames = new LinkedList();
//    private List<TeiLocus> locuses = new LinkedList();

    /**
     * Getter for property 'scribe'.
     *
     * @return Value for property 'scribe'.
     */
    public String getScribe() {
        return scribe;
    }

    /**
     * Setter for property 'scribe'.
     *
     * @param scribe Value to set for property 'scribe'.
     */
    public void setScribe(String scribe) {
        this.scribe = scribe;
    }

    /**
     * Getter for property 'script'.
     *
     * @return Value for property 'script'.
     */
    public String getScript() {
        return script;
    }

    /**
     * Setter for property 'script'.
     *
     * @param script Value to set for property 'script'.
     */
    public void setScript(String script) {
        this.script = script;
    }

    /**
     * Getter for property 'placeName'.
     *
     * @return Value for property 'placeName'.
     */
    public TeiPlaceName getPlaceName() {
        return placeName;
    }

    /**
     * Setter for property 'placeName'.
     *
     * @param placeName Value to set for property 'placeName'.
     */
    public void setPlaceName(TeiPlaceName placeName) {
        this.placeName = placeName;
    }

    /**
     * Getter for property 'persNames'.
     *
     * @return Value for property 'persNames'.
     */
    public List<TeiPersName> getPersNames() {
        return Collections.unmodifiableList(persNames);
    }

    /**
     * Get persName by ID
     *
     * @param id the persName ID
     * @return the persName of ID id
     */
    public TeiPersName getPersName(String id) {
        for (TeiPersName pn : persNames) {
            if (pn.getId().equals(id)) {
                return pn;
            }
        }
        throw new Error("cannot find a persName with ID \"" + id + "\"");
    }

    /**
     * Creates an empty persName and adds it at the end the list
     *
     * @return the newly created persName
     */
    public TeiPersName addPersName() {
        TeiPersName pn = new TeiPersName(this);

        persNames.add(persNames.size(), pn);
        pn.generateDefaultId();

        return pn;
    }

    /**
     * Removes the indicated persName.
     *
     * @param id of the persName
     */
    public void removePersName(String id) {
        for (TeiPersName pn : persNames) {
            if (pn.getId().equals(id)) {
                persNames.remove(pn);
                return;
            }
        }
        throw new Error("cannot find a persName with ID \"" + id + "\"");
    }

    /**
     * Get locus list
     *
     * @return the list of all locuses
     */
    public List<TeiLocus> getLocuses() {
        return Collections.unmodifiableList(locuses);
    }

    /**
     * Get locus by ID
     *
     * @param id the locus ID
     * @return the locus of ID id
     */
    public TeiLocus getLocus(String id) {
        for (TeiLocus l : locuses) {
            if (l.getId().equals(id)) {
                return l;
            }
        }
        throw new Error("cannot find a locus with ID \"" + id + "\"");
    }

    /**
     * Creates an empty locus and adds it at the end the list
     *
     * @return the newly created locus
     */
    public TeiLocus addLocus() {
        TeiLocus t = new TeiLocus(this);

        locuses.add(locuses.size(), t);
        t.generateDefaultId();

        return t;
    }

    /**
     * Removes the indicated locus.
     *
     * @param id of the locus
     */
    public void removeLocus(String id) {
        for (TeiLocus l : locuses) {
            if (l.getId().equals(id)) {
                locuses.remove(l);
                return;
            }
        }
        throw new Error("cannot find a locus with ID \"" + id + "\"");
    }

    /**
     * Get the number of locuses
     *
     * @return the number of locuses in this part
     */
    public int nbLocuses() {
        return locuses.size();
    }

}
