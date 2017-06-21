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
public class TeiW extends TeiWPartContainer implements TeiAbPart {

    List<TeiWPart> parts = new ArrayList<>();
    TeiWordZone facs = null;
    
    public TeiW(TeiAbPartContainer parent, ArrayList<Integer> codePoints) {
        this(parent);
        for (int cp : codePoints) {
            parts.add(
                    new TeiC(
                            this,
                            new String(Character.toChars(cp))
                    )
            );
        }
    }
    
    public TeiW(TeiAbPartContainer parent) {
        super((TeiElement)parent);
    }

    TeiW(TeiAbPartContainer parent, Element el) {
        super(parent);
        
        // Loading the children
        for (ListIterator<Content> it=el.getContent().listIterator(); it.hasNext(); ) {
            Content c = it.next();
            if (c instanceof Text) {
                if (((Text) c).getTextTrim().length()>0) {
                    throw new Error("<w> cannot contain free text ; you wrote \""+((Text) c).getText()+"\"");
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
    
    public boolean remove(TeiWPart wp) {
        ((TeiElement)wp).notifyDeletion();
        return parts.remove(wp);
    }
    
    /**
     * Facs setter
     * @param zone tei word zone
     */
    public void setFacs(TeiWordZone zone) {
        facs = zone;
        if (facs!=null) {
            facs.setTranscription(this);
        }
    }
    
    /**
     * @return the tei word zone associated to this word
     */
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
    public void add(TeiWSeg s) {
        parts.add(s);
    }
    
    @Override
    public void add(TeiHi h) {
        parts.add(h);
    }
    
    @Override
    public void add(TeiC c) {
        parts.add(c);
    }
    
    @Override
    public void add(TeiG g) {
        add(g);
    }
    
    @Override
    public void add(TeiLb teiLb) {
        parts.add(teiLb);
    }

    @Override
    protected String getElementName() {
        return "w";
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

    public int countParts() {
        return parts.size();
    }

    @Override
    public String getTagSoup() {
        StringBuilder content = new StringBuilder();
        for (TeiWPart p : parts) {
            content.append(p.getTagSoup());
        }
        String cstr = content.toString();
        if (cstr.indexOf("<")!=-1) {
            return "<w>"+cstr+"</w>";
        }
        return cstr;
    }
    
}
