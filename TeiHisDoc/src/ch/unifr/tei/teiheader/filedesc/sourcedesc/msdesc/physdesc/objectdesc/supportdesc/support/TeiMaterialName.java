/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.objectdesc.supportdesc.support;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiMaterialName extends TeiTextElement {
    public TeiMaterialName(TeiElement parent) {
        super(parent);
    }

    private TeiMaterialName(TeiMaterial parent, Element el) {
        this(parent);

        setContent(el.getText());

        consume(el);
    }

    public static TeiMaterialName load(TeiMaterial parent, Element el) {
        return new TeiMaterialName(parent, el);
    }

    @Override
    public String getElementName() {
        return "name";
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
