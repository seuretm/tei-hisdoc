/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.text.body.div.table;

import ch.unifr.tei.text.body.div.div.TeiDivDiv;
import ch.unifr.tei.text.body.div.div.TeiDivDivContainer;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiCell extends TeiDivDiv {
    private String role = null;
    
    public TeiCell(TeiDivDivContainer parent, Element el) {
        super(parent, el);
        role = consumeAttributeStr(el, "role", NoNS, false);
    }

    public String getRole() {
        return role;
    }
    
    @Override
    public String getElementName() {
        return "cell";
    }

}
