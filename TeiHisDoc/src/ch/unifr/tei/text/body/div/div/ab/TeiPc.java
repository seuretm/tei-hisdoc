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
 * @author ms
 */
public class TeiPc extends TeiC {
    
    public TeiPc(TeiAbPartContainer parent) {
        super(parent);
    }
    
    public TeiPc(TeiAbPartContainer parent, String pChar) {
        super(parent, pChar);
    }
    
    public TeiPc(TeiAbPartContainer parent, Element el) {
        super(parent, el);
    }
    
    @Override
    protected String getElementName() {
        return "pc";
    }
}
