/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.mscontents.msitem;

import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.mspart.mscontents.TeiMsContents;
import ch.unifr.tei.text.body.div.div.TeiDivDivContainer;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiLocus;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiMsItem extends TeiElement implements TeiDivDivContainer {
    private TeiLocus locus = new TeiLocus(this);
    private TeiTitle title = new TeiTitle(this);
    private List<TeiRubric> rubrics = new LinkedList<>();
    private TeiIncipit incipit = null;
    private TeiExplicit explicit = null;
    private List<TeiFinalRubric> finalRubrics = new LinkedList<>();

    public TeiMsItem(TeiElement parent) {
        super(parent);
    }

    private TeiMsItem(TeiMsContents parent, Element el) {
        this(parent);

        Element e = consumeChild(el, "locus", TeiNS, false);
        if (e!=null) {
            locus = TeiLocus.load(this, e);
        }

        e = consumeChild(el, "title", TeiNS, false);
        if (e != null) {
            title = TeiTitle.load(this, e);
        } else {
            title = new TeiTitle(this);
        }

        for (Element r : el.getChildren("rubric", TeiNS)) {
            rubrics.add(
                    new TeiRubric(this, r)
            );
        }
        el.removeChildren("rubric", TeiNS);

        e = consumeChild(el, "incipit", TeiNS, false);
        if (e != null) {
            incipit = new TeiIncipit(this, e);
//        } else {
//            incipit = new TeiText(this);
        }

        e = consumeChild(el, "explicit", TeiNS, false);
        if (e != null) {
            explicit = new TeiExplicit(this, e);
//        } else {
//            explicit = new TeiText(this);
        }

        for (Element r : el.getChildren("finalRubric", TeiNS)) {
            finalRubrics.add(TeiFinalRubric.load(this, r));
        }
        el.removeChildren("finalRubric", TeiNS);

        consume(el);
    }

    public static TeiMsItem load(TeiMsContents parent, Element el) {
        return new TeiMsItem(parent, el);
    }

    @Override
    public String getElementName() {
        return "msItem";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        //addContent(el, locus);
        addContent(el, title);
        for (TeiElement e : rubrics) {
            addContent(el, e);
        }
        addContent(el, incipit);
        addContent(el, explicit);
        for (TeiElement e : finalRubrics) {
            addContent(el, e);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

}
