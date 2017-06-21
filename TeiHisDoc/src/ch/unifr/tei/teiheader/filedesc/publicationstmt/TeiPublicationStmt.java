/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.publicationstmt;

import ch.unifr.tei.teiheader.filedesc.TeiFileDesc;
import ch.unifr.tei.teiheader.filedesc.publicationstmt.availability.TeiAvailability;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiPublicationStmt extends TeiElement {
    private List<TeiPublisher> publishers = new LinkedList<>();
    private TeiAvailability availability;

    public TeiPublicationStmt(TeiElement parent) {
        super(parent);

        availability = new TeiAvailability(this);

        publishers.add(new TeiPublisher(parent));
        publishers.add(new TeiPublisher(parent));
        publishers.get(0).setContent("e-codices - Virtual Manuscript Library of Switzerland");
        publishers.get(1).setContent("Document, Image and Voice Analysis (DIVA) Group, University of Fribourg, Switzerland");
    }

    private TeiPublicationStmt(TeiFileDesc parent, Element el) {
        this(parent);

        publishers.clear();
        for (Element e : el.getChildren("publisher", TeiNS)) {
            publishers.add(TeiPublisher.load(this, e));
        }
        el.removeChildren("publisher", TeiNS);
        if (publishers.isEmpty()) {
            throw new Error("at least one <publisher> required in" + getPath());
        }

        Element e = consumeChild(el, "availability", TeiNS, true);
        availability = TeiAvailability.load(this, e);

        consume(el);
    }

    public static TeiPublicationStmt load(TeiFileDesc parent, Element el) {
        return new TeiPublicationStmt(parent, el);
    }

    @Override
    public String getElementName() {
        return "publicationStmt";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : publishers) {
            addContent(el, e);
        }
        addContent(el, availability);

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    @Override
    public void updateChildrenIDs() {
        for (TeiElement e : publishers) {
            e.setId(e.getRandomId());
        }
        for (TeiElement e : publishers) {
            e.generateDefaultId();
            e.updateChildrenIDs();
        }
    }

    int getIndex(TeiPublisher p) {
        return publishers.indexOf(p);
    }

}
