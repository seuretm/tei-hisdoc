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
public class TeiLb extends TeiElement implements TeiAbPart, TeiWPart {

    public TeiLb(TeiAbPartContainer parent) {
        super((TeiElement)parent);
    }
    
    public TeiLb(TeiWPartContainer parent) {
        super((TeiElement)parent);
    }

    TeiLb(TeiAbPartContainer parent, Element e) {
        super(parent);
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    TeiLb(TeiWPartContainer parent, Element e) {
        super(parent);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getElementName() {
        return "lb";
    }

    @Override
    public Element toXML() {
        return getExportElement();
    }

    @Override
    public String getTagSoup() {
        return "<lb/>";
    }
    
}
