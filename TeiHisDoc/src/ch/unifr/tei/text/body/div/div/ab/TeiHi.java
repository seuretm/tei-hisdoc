/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.text.body.div.div.ab;

import org.jdom2.Element;

/**
 *
 * @author Mathias Seuret
 */
public class TeiHi extends TeiWSeg {
    
    private String rend;
    
    public TeiHi(TeiW parent) {
        super(parent);
    }

    TeiHi(TeiWPartContainer parent, Element e) {
        super(parent, e);
    }
    
    @Override
    protected void consume(Element el) {
        rend = this.consumeAttributeStr(el, "rend", NoNS, false);
        super.consume(el);
    }
    
    @Override
    protected Element getExportElement() {
        Element e = super.getExportElement();
        
        addAttribute(e, "rend", rend, NoNS);
        
        return e;
    }
    
    @Override
    protected String getElementName() {
        return "hi";
    }
}
