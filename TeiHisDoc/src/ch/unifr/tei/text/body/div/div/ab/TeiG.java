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
public class TeiG extends TeiC {
    public TeiG(TeiWPartContainer parent) {
        super(parent);
    }
    
    public TeiG(TeiWPartContainer parent, String c) {
        super(parent, c);
    }
    
    TeiG(TeiWPartContainer parent, Element e) {
        super(parent, e);
    }
    
    @Override
    protected String getElementName() {
        return "g";
    }
    
}
