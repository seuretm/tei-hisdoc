/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.mscontents.msitem;

import ch.unifr.tei.text.body.div.div.TeiDivDiv;
import ch.unifr.tei.text.body.div.div.TeiDivDivContainer;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

/**
 *
 * @author Mathias Seuret
 */
public class TeiRubric extends TeiDivDiv {
    public TeiRubric(TeiElement parent) {
        super(parent);
    }

    public TeiRubric(TeiDivDivContainer parent, Element el) {
        super(parent, el);
    }
    
    @Override
    public String getElementName() {
        return "rubric";
    }
}
