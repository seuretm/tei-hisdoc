/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp;

import ch.unifr.tei.facsimile.surfacegrp.surface.SurfaceType;
import ch.unifr.tei.facsimile.surfacegrp.surface.TeiSurface;
import ch.unifr.tei.facsimile.surfacegrp.surface.TeiSurfaceContainer;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author ms
 */
public class TeiSurfaceGrpFolio extends TeiSurfaceGrp implements TeiSurfaceContainer, Iterable<TeiSurface> {
    /**
     * Surface group part parent.
     */
    private TeiSurfaceGrpPart parent;
    // Accessors ok
    /**
     * List of the pages - usually two of them.
     */
    private List<TeiSurface> surfaces = new ArrayList<>();
    // No accessor needed

    /**
     * Constructs a new folio belonging to the given part.
     *
     * @param parent the parent TeiElement
     */
    public TeiSurfaceGrpFolio(TeiSurfaceGrpPart parent) {
        super(parent);
        this.parent = parent;
    }

    /**
     * Constructor from XML element
     *
     * @param parent part
     * @param el     XML element
     */
    private TeiSurfaceGrpFolio(TeiSurfaceGrpPart parent, Element el) {
        this(parent);

        for (Element e : el.getChildren("surface", TeiNS)) {
            surfaces.add(TeiSurface.load(this, e));
        }
        el.removeChildren("surface", TeiNS);

        consume(el);
    }

    /**
     * Loads a folio from an XML element.
     *
     * @param parent part
     * @param el     XML element
     * @return the new folio
     */
    public static TeiSurfaceGrpFolio load(TeiSurfaceGrpPart parent, Element el) {
        return new TeiSurfaceGrpFolio(parent, el);
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : surfaces) {
            addContent(el, e);
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
        String pid = parent.getId();
        if (pid == null) {
            pid = "";
        }
        int n = parent.getIndex(this) + 1;
        setId(pid + "L" + n);
    }

    @Override
    public int getIndex(TeiSurface child) {
        return surfaces.indexOf(child);
    }

    @Override
    public void updateChildrenIDs() {
        for (TeiElement e : surfaces) {
            e.setId(e.getRandomId());
        }
        for (TeiElement e : surfaces) {
            e.generateDefaultId();
            e.updateChildrenIDs();
        }
    }
    
    
    /*
        ACCESSORS
    */

    /**
     * Adds a page (side) to the folio. Although it probably makes little
     * sense, it'd be possible to add more than two.
     *
     * @return a ref to the new page
     */
    public TeiSurface addPage() {
        TeiSurface surf = new TeiSurface(this, SurfaceType.PAGE);

        surfaces.add(surf);
        surf.generateDefaultId();
        surf.setType(SurfaceType.PAGE);


        return surf;
    }
    
    /**
     * @param page removes the given page
     */
    public void removePage(TeiSurface page) {
        surfaces.remove(page);
        page.notifyDeletion();
        
    }

    /**
     * @return an iterator on the pages
     */
    @Override
    public Iterator<TeiSurface> iterator() {
        return Collections.unmodifiableList(surfaces).iterator();
    }

    /**
     * @return the list of pages
     */
    public List<TeiSurface> getPages() {
        return Collections.unmodifiableList(surfaces);
    }

    /**
     * @param n page number
     * @return the page number n
     */
    public TeiSurface getPage(int n) {
        return surfaces.get(n);
    }
}
