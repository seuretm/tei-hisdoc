/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.text.body.div.div.ab;

import ch.unifr.tei.text.body.div.div.TeiDivDiv;
import org.jdom2.Element;

/**
 *
 * @author Mathias Seuret
 */
public class TeiNote extends TeiAb {
    public TeiNote(TeiDivDiv parent) {
        super(parent);
    }
    
    public TeiNote(TeiDivDiv parent, Element el) {
        super(parent, el);
    }
    
    @Override
    protected String getElementName() {
        return "note";
    }
}
