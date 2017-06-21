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
public abstract class TeiAbPartContainer extends TeiElement {

    public TeiAbPartContainer(TeiElement parent) {
        super(parent);
    }
    
    public abstract boolean remove(TeiAbPart part);
    
    public abstract void add(TeiC p);
    
    public abstract void add(TeiW p);
    
    public abstract void add(TeiPc p);
    
    public abstract void add(TeiLb p);
    
    public abstract void add(TeiAbSeg p);
    
    public abstract void add(TeiWSeg p);
    
    public abstract void add(TeiAdd p);
    
    public void createAndAddPart(Element e) {
        switch (e.getName()) {
            case "c":
                add(new TeiC(this, e));
                break;
            case "w":
                add(new TeiW(this, e));
                break;
            case "pc":
                add(new TeiPc(this, e));
                break;
            case "lb":
                add(new TeiLb(this, e));
                break;
            case "seg":
                add(new TeiAbSeg(this, e));
                break;
            case "add":
                add(new TeiAdd(this, e));
                break;
            default:
                throw new Error("unexpected <"+e.getName()+"> in a <"+getElementName()+">");
        }
    }
}
