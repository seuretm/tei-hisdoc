/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.mscontents.msitem;

import ch.unifr.tei.text.body.div.div.TeiDivDiv;
import ch.unifr.tei.text.body.div.div.TeiDivDivContainer;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiFinalRubric extends TeiDivDiv {

    public TeiFinalRubric(TeiDivDivContainer parent) {
        super((TeiElement)parent);
    }

    private TeiFinalRubric(TeiMsItem parent, Element el) {
        super(parent, el);
    }

    public static TeiFinalRubric load(TeiMsItem parent, Element el) {
        return new TeiFinalRubric(parent, el);
    }

    @Override
    public String getElementName() {
        return "finalRubric";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();
        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }
}
