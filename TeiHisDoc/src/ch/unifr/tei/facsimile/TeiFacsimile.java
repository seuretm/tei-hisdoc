/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile;

import ch.unifr.tei.TeiHisDoc;
import ch.unifr.tei.facsimile.surfacegrp.TeiSurfaceGrp;
import ch.unifr.tei.facsimile.surfacegrp.TeiSurfaceGrpContainer;
import ch.unifr.tei.facsimile.surfacegrp.TeiSurfaceGrpPart;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiElementContainer;
import org.jdom2.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiFacsimile extends TeiElement implements TeiSurfaceGrpContainer, TeiElementContainer<TeiSurfaceGrpPart> {
    /**
     * List of parts of the manuscript. There should be at least one.
     */
    private List<TeiSurfaceGrpPart> surfaceGrps = new LinkedList<>();
    // Accessors implemented

    /**
     * Creates a facsimile.
     *
     * @param parent TeiHisDoc element
     */
    public TeiFacsimile(TeiElement parent) {
        super(parent);
    }

    /**
     * Constructing a TeiFacsimile from an XML element
     *
     * @param parent TeiHisDoc parent
     * @param el     XML element
     */
    private TeiFacsimile(TeiHisDoc parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("surfaceGrp", TeiNS)) {
            surfaceGrps.add(TeiSurfaceGrpPart.load(this, e));
        }
        el.removeChildren("surfaceGrp", TeiNS);
        if (surfaceGrps.isEmpty()) {
            throw new Error("at least one <surfaceGrp> is required in " + getPath());
        }

        consume(el);
    }

    /**
     * Loads a TeiFacSimile from an XML element.
     *
     * @param parent TeiHisDoc object
     * @param el     XML element
     * @return the new facsimile
     */
    public static TeiFacsimile load(TeiHisDoc parent, Element el) {
        return new TeiFacsimile(parent, el);
    }

    @Override
    protected String getElementName() {
        return "facsimile";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiSurfaceGrp g : surfaceGrps) {
            el.addContent(g.toXML());
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    @Override
    public int getIndex(TeiSurfaceGrpPart child) {
        return surfaceGrps.indexOf(child);
    }

    @Override
    public void updateChildrenIDs() {
        for (TeiSurfaceGrp sg : surfaceGrps) {
            sg.setId(sg.getRandomId());
        }
        for (TeiSurfaceGrp sg : surfaceGrps) {
            sg.generateDefaultId();
            sg.updateChildrenIDs();
        }
    }
    
    /*
        Accessors
    */

    /**
     * Returns a list of the parts (chapters?) of the manuscripts. The list
     * contains surface groups, each group corresponding to a folio and having
     * two surfaces at most (pages).
     *
     * @return an array list
     */
    public List<TeiSurfaceGrpPart> getParts() {
        return Collections.unmodifiableList(surfaceGrps);
    }

    /**
     * Returns a part of the facsimile.
     *
     * @param num part number
     * @return the num-th part
     */
    public TeiSurfaceGrpPart getPart(int num) {
        return surfaceGrps.get(num);
    }

    /**
     * Adds a part.
     *
     * @return the newly created part
     */
    public TeiSurfaceGrpPart addPart() {
        TeiSurfaceGrpPart s = new TeiSurfaceGrpPart(this);
        surfaceGrps.add(s);
        s.generateDefaultId();
        return s;
    }
}
