/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.text.body.div.div;

import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiTextSegZone;
import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiTextZone;
import ch.unifr.tei.text.body.div.TeiHead;
import ch.unifr.tei.text.body.div.div.ab.TeiAb;
import ch.unifr.tei.text.body.div.div.ab.TeiAbContainer;
import ch.unifr.tei.text.body.div.div.ab.TeiL;
import ch.unifr.tei.text.body.div.div.ab.TeiNote;
import ch.unifr.tei.text.body.div.table.TeiTable;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiElementContainer;
import org.jdom2.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Simistir
 */
public class TeiDivDiv extends TeiElement implements TeiAbContainer {
    private static final Logger logger = Logger.getLogger(TeiElement.class);
    private List<TeiHead> heads = new LinkedList<>();
    // TODO Mathias: what it divs for? It is never updated…
    private List<TeiDivDiv> divs = new LinkedList<>();
    private List<TeiAb> abs = new LinkedList<>();
    
    //TODO 3 : split TeiDivDiv into two classes, one for TeiAb/TeiL, and one for TeiNote
    private List<TeiNote> notes = new LinkedList<>();
    
    private List<TeiTable> tables = new LinkedList<>();
    private TeiDivDivContainer parent;
    private String part;
    private String sameAs;
    
    protected TeiTextZone facs = null;

    public TeiDivDiv(TeiElement parent) {
        super(parent);
    }

    public TeiDivDiv(TeiDivDivContainer parent, Element el) {
        this((TeiElement) parent);
        this.parent = parent;


        /*for (Element e : el.getChildren("div", TeiNS)) {
            divs.add(TeiDivDiv.load(this, e));
        }
        el.removeChildren("div", TeiNS);*/
        
        for (Element e : el.getChildren()) {
            switch (e.getName()) {
                case "ab":
                    abs.add(new TeiAb(this, e));
                    break;
                case "l":
                    abs.add(new TeiL(this, e));
                    break;
                case "note":
                    notes.add(new TeiNote(this, e));
                    break;
            }
        }
        el.removeChildren("ab", TeiNS);
        el.removeChildren("l", TeiNS);
        el.removeChildren("note", TeiNS);

        String facsStr = consumeAttributeStr(el, "facs", NoNS, false);
        TeiElement facsEl = getElement(facsStr);
        if (facsEl instanceof TeiTextZone) {
            setFacs((TeiTextZone)facsEl);
        } else if (facsEl!=null) {
            System.err.println(
                    "warning, facs=\""+facsStr+"\" does not correspond to a text zone. "
                    +"The facs attribute is removed."
            );
        }

        consume(el);
    }
    
    public TeiDivDivContainer getParent() {
        return parent;
    }
    
    public boolean remove(TeiAb ab) {
        ab.notifyDeletion();
        return abs.remove(ab);
    }
    
    public void setFacs(TeiTextZone zone) {
        facs = zone;
        if (facs!=null) {
            facs.setTranscription(this);
        }
    }
    
    public TeiTextZone getFacs() {
        return facs;
    }

    public static TeiDivDiv load(TeiDivDivContainer parent, Element el) {
        return new TeiDivDiv(parent, el);
    }

    @Override
    public String getElementName() {
        return "div";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : divs) {
            addContent(el, e);
        }
        for (TeiElement e : abs) {
            addContent(el, e);
        }
//        for (TeiElement e : fabs) {
//            addContent(el, e);
//        }

        if (facs!=null) {
            addAttribute(el, "facs", "#"+facs.getId(), NoNS);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        String pid = ((TeiElement) parent).getId();
        if (pid == null) {
            pid = "";
        }
        // TODO Mathias: shouldn't TeiDivContainer implement TeiElementContainer? (Manu)
        int n = ((TeiElementContainer<TeiDivDiv>) parent).getIndex(this) + 1;
        setId(pid + "b" + n);
    }
    
    public List<TeiAb> getAbs() {
        return Collections.unmodifiableList(abs);
        // return
    }
    
    /**
     * Replaces an Ab by another one, removing the previous one.
     * @param previous Ab to replace
     * @param replacement new Ab
     * @return true in case of success
     */
    @Override
    public boolean replaceAb(TeiAb previous, TeiAb replacement) {
        for (int i=0; i<abs.size(); i++) {
            TeiAb ab = abs.get(i);
            if (ab==previous) {
                ab.notifyDeletion();
                abs.set(i, replacement);
                return true;
            }
        }
        return false;
    }

    
    
    /**
     * @param byID ab id
     * @return the ab of id byID
     */
    public TeiAb getAb(String byID) {
        for (TeiAb ab : abs) {
            if (ab.getId().equals(byID)) {
                return ab;
            }
        }
        throw new Error("cannot find an ab with ID \"" + byID + "\"");
    }
    
    public TeiAb addAb() {
        TeiAb child = new TeiAb(this);
        abs.add(child);
        return child;
    }
    
    public TeiAb setAb(String byFacs) {
        for (TeiAb ab : abs) {
            if (ab.getFacs().equals(byFacs)) {
                abs.add(ab);
            }
        }
        throw new Error("cannot find a ab with Facs \"" + byFacs + "\"");
    }

    /**
     * Creates an empty ab
     *
     * @return the newly created handNote
     */
    public TeiAb addAb(TeiAb t) {
        //TeiAb t = new TeiAb(this);
        abs.add(abs.size(), t);
        //t.generateDefaultId();

        return t;
    }

    /**
     * Removes the indicated handNote.
     *
     * @param id of the handNote
     */
    public void removeAb(TeiTextSegZone zone) {
        for (TeiAb ab : abs) {
            if (ab.getFacs().equals(zone)) {
                ab.notifyDeletion();
                abs.remove(ab);
                logger.debug("Removing <Ab> ...");
                return;
            }
        }
//        throw new Error("cannot find an ab with Facs \"" + zone + "\"");
    }

}
