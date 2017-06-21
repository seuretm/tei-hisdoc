/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.text.body.div.div.ab;

import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 *
 * @author Mathias Seuret
 */
public abstract class TeiWPartContainer extends TeiElement {
    public TeiWPartContainer(TeiElement parent) {
        super(parent);
    }
    
    public abstract boolean remove(TeiWPart wp);
    
    public abstract void add(TeiC p);
    
    public abstract void add(TeiWSeg p);
    
    public abstract void add(TeiG g);
    
    public abstract void add(TeiHi h);
    
    public abstract void add(TeiLb teiLb);
    
    public abstract int countParts();
    
    public void createAndAddPart(Element e) {
        switch (e.getName()) {
            case "c":
                add(new TeiC(this, e));
                break;
            case "g":
                add(new TeiG(this, e));
                break;
            case "lb":
                add(new TeiLb(this, e));
                break;
            case "seg":
                add(new TeiWSeg(this, e));
                break;
            case "hi":
                add(new TeiHi(this, e));
                break;
            default:
                throw new Error("unexpected <"+e.getName()+"> in a <"+getElementName()+">");
        }
    }
}
