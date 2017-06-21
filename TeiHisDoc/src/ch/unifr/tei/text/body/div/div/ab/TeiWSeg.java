/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.text.body.div.div.ab;

import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiWordZone;
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
public class TeiWSeg extends TeiWPartContainer implements TeiWPart {
    
    private List<TeiWPart> parts = new ArrayList<>();
    
    private TeiWordZone facs = null;
    
    public TeiWSeg(TeiW parent) {
        super(parent);
    }

    TeiWSeg(TeiWPartContainer parent, Element e) {
        this((TeiW)parent);
        loadFromElement(e);
    }
    
    protected void loadFromElement(Element el) {
        // Loading the children
        for (ListIterator<Content> it=el.getContent().listIterator(); it.hasNext(); ) {
            Content c = it.next();
            if (c instanceof Text) {
                if (((Text) c).getTextTrim().length()>0) {
                    throw new Error("<seg> cannot contain free text ; you wrote \""+((Text) c).getText()+"\"");
                } else {
                    continue;
                }
                //TODO 3 : add partial parsing using TranscriptionMananger
            }
            this.createAndAddPart((Element)c);
            it.remove();
        }
        
        String facsStr = consumeAttributeStr(el, "facs", NoNS, false);
        TeiElement facsEl = getElement(facsStr);
        if (facsEl instanceof TeiWordZone) {
            setFacs((TeiWordZone)facsEl);
        } else if (facsEl!=null) {
            System.err.println(
                    "warning, facs=\""+facsStr+"\" does not correspond to a word-level zone. "
                    +"The facs attribute is removed."
            );
        }
        
        consume(el);
    }
    
    public TeiW getParent() {
        return (TeiW) parent;
    }
    
    public boolean remove(TeiWPart wp) {
        ((TeiElement)wp).notifyDeletion();
        return parts.remove(wp);
    }
    
    public void setFacs(TeiWordZone zone) {
        facs = zone;
    }
    
    public TeiWordZone getFacs() {
        return facs;
    }
    
    @Override
    public void notifyDeletion() {
        super.notifyDeletion();
        if (facs!=null) {
            facs.setTranscription(null);
        }
        for (TeiWPart p : parts) {
            ((TeiElement)p).notifyDeletion();
        }
    }
    
    @Override
    public void add(TeiC c) {
        parts.add(c);
    }
    
    @Override
    public void add(TeiHi h) {
        parts.add(h);
    }

    @Override
    public void add(TeiG g) {
        parts.add(g);
    }

    @Override
    public void add(TeiLb teiLb) {
        parts.add(teiLb);
    }

    @Override
    public void add(TeiWSeg p) {
        throw new Error("<seg> cannot be nested");
    }
    
    @Override
    public int countParts() {
        return parts.size();
    }

    @Override
    protected String getElementName() {
        return "seg";
    }

    @Override
    public Element toXML() {
        Element e = getExportElement();
        
        for (TeiWPart p : parts) {
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
        for (TeiWPart p : parts) {
            sb.append(p.toString());
        }
        return sb.toString();
    }

    @Override
    public String getTagSoup() {
        StringBuilder content = new StringBuilder();
        for (TeiWPart p : parts) {
            content.append(p.getTagSoup());
        }
        String cstr = content.toString();
        if (cstr.contains("<")) {
            return "<seg>"+cstr+"</seg>";
        }
        return cstr;
    }
}
