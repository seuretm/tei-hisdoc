/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.handdesc.handnote;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiPlaceName extends TeiTextElement {

    public TeiPlaceName(TeiElement parent) {
        super(parent);
    }

    public TeiPlaceName(TeiHandNote parent, Element el) {
        super(parent);
        setContent(el.getText());

        consume(el);
    }

    @Override
    public String getElementName() {
        return "placeName";
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
