/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.text.body.div.div.ab;

import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiTextSegZone;
import ch.unifr.tei.text.body.div.div.TagSoupSource;
import ch.unifr.tei.utils.TeiElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Text;

/**
 *
 * @author Mathias Seuret
 */
public class TeiAb extends TeiAbPartContainer implements TagSoupSource {
    protected List<TeiAbPart> parts = new ArrayList<>();
    
    protected TeiTextSegZone facs = null;
    
    public TeiAb(TeiAbContainer parent, Element el) {
        super((TeiElement)parent);
        
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
    
    public TeiAb(TeiAbContainer parent) {
        super((TeiElement)parent);
    }
    
    public TeiAbContainer getParent() {
        return (TeiAbContainer)parent;
    }
    
    public boolean remove(TeiAbPart abp) {
        ((TeiElement)abp).notifyDeletion();
        return parts.remove(abp);
    }
    
    public void setFacs(TeiTextSegZone zone) {
        facs = zone;
        if (facs!=null) {
            facs.setTranscription(this);
        }
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
    
    public TeiTextSegZone getFacs() {
        return facs;
    }
    
    public void add(TeiAbSeg p) {
        parts.add(p);
    }
    
    @Override
    public void add(TeiC p) {
        parts.add(p);
    }
    
    @Override
    public void add(TeiW p) {
        parts.add(p);
    }
    
    @Override
    public void add(TeiPc p) {
        parts.add(p);
    }
    
    @Override
    public void add(TeiLb p) {
        parts.add(p);
    }
    
    @Override
    public void add(TeiAdd p) {
        parts.add(p);
    }
    
    @Override
    public void add(TeiWSeg p) {
        throw new Error("cannot add such a segment in a teiAb");
    }
    
    public void add(TeiAbPart p) {
        parts.add(p);
    }
    
    public List<TeiAbPart> getParts() {
        return Collections.unmodifiableList(parts);
    }
    
    public void clearContent() {
        while (!parts.isEmpty()) {
            TeiAbPart p = parts.get(0);
            ((TeiElement)p).notifyDeletion();
            parts.remove(p);
        }
    }

    @Override
    protected String getElementName() {
        return "ab";
    }

    @Override
    public Element toXML() {
        Element e = this.getExportElement();
        
        for (TeiAbPart p : parts) {
            e.addContent(p.toXML());
        }
        
        if (facs!=null) {
            addAttribute(e, "facs", "#"+facs.getId(), NoNS);
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

    public TeiW addW() {
        TeiW child = new TeiW(this);
        parts.add(child);
        return child;
    }
}
