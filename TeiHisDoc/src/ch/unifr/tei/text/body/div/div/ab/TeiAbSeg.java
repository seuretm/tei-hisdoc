/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.text.body.div.div.ab;

import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiTextSegZone;
import ch.unifr.tei.utils.TeiElement;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Text;

/**
 *
 * @author Mathias Seuret
 */
public class TeiAbSeg extends TeiAbPartContainer implements TeiAbPart {

    protected List<TeiAbPart> parts = new ArrayList<>();
    
    protected TeiTextSegZone facs = null;
    
    public TeiAbSeg(TeiAbPartContainer parent) {
        super(parent);
    }

    TeiAbSeg(TeiAbPartContainer parent, Element el) {
        super(parent);
        
        for (ListIterator<Content> it=el.getContent().listIterator(); it.hasNext(); ) {
            Content c = it.next();
            if (! (c instanceof Element)) {
                if (((Text)c).getTextTrim().length()==0) {
                    continue;
                }
                throw new Error(
                        "Only tags allowed inside of <"+getElementName()+">"
                );
            }
            createAndAddPart((Element)c);
            it.remove();
        }
        
        String facsStr = consumeAttributeStr(el, "facs", NoNS, false);
        TeiElement facsEl = getElement(facsStr);
        if (facsEl instanceof TeiTextSegZone) {
            setFacs((TeiTextSegZone)facsEl);
        } else if (facsEl!=null) {
            System.err.println(
                    "warning, facs=\""+facsStr+"\" does not correspond to a text seg zone. "
                    +"The facs attribute is removed."
            );
        }
        
        consume(el);
    }
    
    public TeiAbPartContainer getParent() {
        return (TeiAbPartContainer) parent;
    }
    
    public boolean remove(TeiAbPart abp) {
        ((TeiElement)abp).notifyDeletion();
        return parts.remove(abp);
    }
    
    public void setFacs(TeiTextSegZone zone) {
        facs = zone; // TODO: check if the TeiTextSegZone can have an AbSeg as facs
    }
    
    public TeiTextSegZone getFacs() {
        return facs;
    }
    
    @Override
    public void notifyDeletion() {
        super.notifyDeletion();
        if (facs!=null) {
            facs.setTranscription(null);
        }
        for (TeiAbPart p : parts) {
            ((TeiElement)p).notifyDeletion();
        }
    }
    
    private void addPart(TeiAbPart part) {
        if (part instanceof TeiAbSeg) {
            throw new Error("Cannot have a <seg> inside another <seg>");
        }
        parts.add(part);
    }
    
    public void addSeg(TeiAbSeg p) {
        addPart(p);
    }

    @Override
    public void add(TeiC p) {
        addPart(p);
    }

    @Override
    public void add(TeiW p) {
        addPart(p);
    }

    @Override
    public void add(TeiPc p) {
        addPart(p);
    }

    @Override
    public void add(TeiLb p) {
        addPart(p);
    }
    
    @Override
    public void add(TeiAbSeg p) {
        throw new Error("cannot add a <seg> inside of another <seg>");
    }
    
    @Override
    public void add(TeiAdd p) {
        throw new Error("cannot add an <add> inside of a <seg>");
    }

    @Override
    public void add(TeiWSeg p) {
        throw new Error("cannot add such a <seg> here");
    }

    
    @Override
    protected String getElementName() {
        return "seg";
    }
    
    @Override
    public Element toXML() {
        Element e = getExportElement();
        
        for (TeiAbPart p : parts) {
            e.addContent(p.toXML());
        }
        
        return e;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TeiAbPart p : parts) {
            sb.append(p.toString());
        }
        return sb.toString();
    }

    @Override
    public String getTagSoup() {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(getElementName()).append(">");
        for (TeiAbPart p : parts) {
            sb.append(p.getTagSoup());
        }
        sb.append("</").append(getElementName()).append(">");
        return sb.toString();
    }
    
    
}
