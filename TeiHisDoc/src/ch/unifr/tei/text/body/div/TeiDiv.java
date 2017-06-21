/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.text.body.div;

import ch.unifr.tei.facsimile.surfacegrp.surface.TeiSurface;
import ch.unifr.tei.text.body.div.div.TeiDivDiv;
import ch.unifr.tei.text.body.div.div.TeiDivDivContainer;
import ch.unifr.tei.text.body.div.table.TeiTable;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiElementContainer;
import org.jdom2.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiDiv extends TeiElement implements TeiDivDivContainer, TeiElementContainer<TeiDivDiv> {
    private TeiSurface facs;
    private List<TeiHead> heads = new LinkedList<>();
    // TODO Mathias: what it divs for? It is never updated…
    private List<TeiDiv> divs = new LinkedList<>();
    private List<TeiDivDiv> divdivs = new LinkedList<>();
    private List<TeiTable> tables = new LinkedList<>();
    private TeiDivContainer parent;
    private String part;
    @SuppressWarnings("FieldCanBeLocal")
    private String sameAs;

    public TeiDiv(TeiElement parent) {
        super(parent);
    }

    private TeiDiv(TeiDivContainer parent, Element el) {
        this((TeiElement) parent);
        this.parent = parent;

        for (Element e : el.getChildren("head", TeiNS)) {
            heads.add(TeiHead.load(this, e));
        }
        el.removeChildren("head", TeiNS);

        /*for (Element e : el.getChildren("div", TeiNS)) {
            divs.add(TeiDiv.load(this, e));
        }
        el.removeChildren("div", TeiNS);*/

        for (Element e : el.getChildren("div", TeiNS)) {
            divdivs.add(TeiDivDiv.load(this, e));
        }
        el.removeChildren("div", TeiNS);

        for (Element e : el.getChildren("table", TeiNS)) {
            tables.add(TeiTable.load(this, e));
        }
        el.removeChildren("table", TeiNS);

        for (Element e : el.getChildren("metamark", TeiNS)) {
            addIgnoredElement(e);
        }
        el.removeChildren("metamark", TeiNS);

        part = consumeAttributeStr(el, "part", NoNS, false);
        sameAs = consumeAttributeStr(el, "sameAs", NoNS, false);
        
        System.out.println("TeiDiv: loading the facs");
        String facsStr = consumeAttributeStr(el, "facs", NoNS, false);
        System.out.println("\tfacsStr: "+facsStr);
        
        TeiElement facsEl = getElement(facsStr);
        if (facsEl instanceof TeiSurface) {
            setFacs((TeiSurface)facsEl);
        } else if (facsEl!=null) {
            System.err.println(
                    "warning, facs=\""+facsStr+"\" does not correspond to a page surface. "
                    +"The facs attribute is removed."
            );
        }

        consume(el);
    }
    
    public void setFacs(TeiSurface page) {
        facs = page;
        if (facs!=null) {
            facs.setTranscription(this);
        }
    }
    
    public TeiSurface getFacs() {
        return facs;
    }

    public static TeiDiv load(TeiDivContainer parent, Element el) {
        return new TeiDiv(parent, el);
    }

    @Override
    public String getElementName() {
        return "div";
    }
    
    public TeiDivContainer getParent() {
        return parent;
    }
    
    public boolean remove(TeiDivDiv divdiv) {
        divdiv.notifyDeletion();
        return divdivs.remove(divdiv);
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : heads) {
            addContent(el, e);
        }
        for (TeiElement e : divs) {
            addContent(el, e);
        }
        for (TeiElement e : divdivs) {
            addContent(el, e);
        }
        
        for (TeiElement e : tables) {
            addContent(el, e);
        }
        
        if (facs!=null) {
            addAttribute(el, "facs", "#"+facs.getId(), NoNS);
        }


        addAttribute(el, "part", part, NoNS);
        addAttribute(el, "sameAs", part, NoNS);

        return el;
    }

    @Override
    public void generateDefaultId() {
        String pid = ((TeiElement) parent).getId();
        if (pid == null) {
            pid = "";
        }
        // TODO Mathias: shouldn't TeiDivContainer implement TeiElementContainer? (Manu)
        int n = ((TeiElementContainer) parent).getIndex(this) + 1;
        setId(pid + "b" + n);
    }

    public List<TeiDivDiv> getDivDivs() {
        return Collections.unmodifiableList(divdivs);
    }

    @Override
    public int getIndex(TeiDivDiv child) {
        return divdivs.indexOf(child);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public TeiDivDiv addDivDiv() {
        TeiDivDiv child = new TeiDivDiv(this);
        divdivs.add(child);
        return child;
    }
}
