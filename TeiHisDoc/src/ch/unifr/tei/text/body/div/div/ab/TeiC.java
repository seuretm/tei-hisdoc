/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.text.body.div.div.ab;

import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiCharZone;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 *
 * @author Mathias Seuret
 */
public class TeiC extends TeiElement implements TeiAbPart, TeiWPart {
    
    private String c;
    private TeiCharZone facs = null;

    public TeiC(TeiAbPartContainer parent) {
        super((TeiElement)parent);
    }
    
    public TeiC(TeiAbPartContainer parent, String c) {
        this(parent);
        this.c = c;
    }
    
    public TeiC(TeiWPartContainer parent) {
        super((TeiElement)parent);
    }
    
    public TeiC(TeiWPartContainer parent, String c) {
        this(parent);
        this.c = c;
    }

    TeiC(TeiAbPartContainer parent, Element e) {
        super(parent);
        loadFromElement(e);
    }
    
    TeiC(TeiWPartContainer parent, Element e) {
        super(parent);
        loadFromElement(e);
    }
    
    protected void loadFromElement(Element e) {
        c = e.getText();
        //TODO: add in the documentation that a <c/> or a <c></c> is considered as a space
        if (c==null || c.length()==0) {
            c = " ";
        }
        
        String facsStr = consumeAttributeStr(e, "facs", NoNS, false);
        TeiElement facsEl = getElement(facsStr);
        if (facsEl instanceof TeiCharZone) {
            setFacs((TeiCharZone)facsEl);
        } else if (facsEl!=null) {
            System.err.println(
                    "warning, facs=\""+facsStr+"\" does not correspond to a character zone. "
                    +"The facs attribute is removed."
            );
        }
        
        consume(e);
        
    }
    
    public TeiAbPartContainer getParent() {
        return (TeiAbPartContainer) parent;
    }
    
    /**
     * Facs setter
     * @param zone tei word zone
     */
    public void setFacs(TeiCharZone zone) {
        facs = zone;
        if (zone!=null) {
            zone.setTranscription(this);
        }
    }
    
    /**
     * @return the tei word zone associated to this word
     */
    public TeiCharZone getFacs() {
        return facs;
    }
    
    @Override
    public void notifyDeletion() {
        super.notifyDeletion();
        if (facs!=null) {
            facs.setTranscription(null);
        }
    }
    
    public void setText(String s) {
        c = s;
    }
    
    public String getText() {
        return c;
    }

    @Override
    protected String getElementName() {
        return "c";
    }

    @Override
    public Element toXML() {
        Element e = getExportElement();
        
        e.setText(c);
        
        if (facs!=null) {
            addAttribute(e, "facs", "#"+facs.getId(), NoNS);
        }
        
        return e;
    }
    
    @Override
    public String toString() {
        return c;
    }

    @Override
    public String getTagSoup() {
        if (c.length()==1) {
            return c;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(getElementName()).append(">");
        sb.append(c);
        sb.append("</").append(getElementName()).append(">");
        return sb.toString();
    }
    
}
