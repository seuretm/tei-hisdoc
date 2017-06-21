/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp;

import ch.unifr.tei.facsimile.TeiFacsimile;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiElementContainer;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ms
 */
public class TeiSurfaceGrpPart extends TeiSurfaceGrp implements TeiSurfaceGrpContainer, TeiElementContainer<TeiSurfaceGrpFolio> {
    /**
     * List of folios contained in the page.
     */
    private List<TeiSurfaceGrpFolio> folios = new ArrayList<>();
    // Accessors OK

    private TeiFacsimile parent;
    // Accessors not needed

    public TeiSurfaceGrpPart(TeiFacsimile parent) {
        super(parent);
        this.parent = parent;
    }

    private TeiSurfaceGrpPart(TeiFacsimile parent, Element el) {
        this(parent);
        this.parent = parent;

        for (Element e : el.getChildren("surfaceGrp", TeiNS)) {
            folios.add(TeiSurfaceGrpFolio.load(this, e));
        }
        el.removeChildren("surfaceGrp", TeiNS);

        consume(el);
    }

    public static TeiSurfaceGrpPart load(TeiFacsimile parent, Element el) {
        if (!el.getChildren("surface", TeiNS).isEmpty()) {
            throw new Error(
                    "cannot have <surface> inside the <surfaceGrp> corresponding "
                            + "to manuscript parts."
            );
        }
        return new TeiSurfaceGrpPart(parent, el);
    }

    @Override
    public void generateDefaultId() {
        String pid = parent.getId();
        if (pid == null) {
            pid = "";
        }
        int n = parent.getIndex(this) + 1;
        setId(pid + "P" + n);
    }

    @Override
    public int getIndex(TeiSurfaceGrpFolio child) {
        return folios.indexOf(child);
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement e : folios) {
            addContent(el, e);
        }

        return el;
    }

    @Override
    public void updateChildrenIDs() {
        for (TeiElement e : folios) {
            e.setId(e.getRandomId());
        }
        for (TeiElement e : folios) {
            e.generateDefaultId();
            e.updateChildrenIDs();
        }
    }
    
   /*
        ACCESSORS
    */

    /**
     * @return the list of all folios
     */
    public List<TeiSurfaceGrpFolio> getFolios() {
        return folios;
    }

    /**
     * @param n folio number
     * @return the folio number n
     */
    public TeiSurfaceGrpFolio getFolio(int n) {
        return folios.get(n);
    }

    /**
     * Creates an empty folio and adds it at the end of this part.
     *
     * @return the newly created folio
     */
    public TeiSurfaceGrpFolio addFolio() {
        return addFolio(folios.size());
    }

    /**
     * Creates an empty folio and places it at the indicated position in this
     * part.
     *
     * @param position insertion position of the folio
     * @return the newly created folio
     */
    public TeiSurfaceGrpFolio addFolio(int position) {
        TeiSurfaceGrpFolio t = new TeiSurfaceGrpFolio(this);

        folios.add(position, t);
        t.generateDefaultId();

        return t;
    }

    /**
     * @return the number of folios in this part
     */
    public int nbFolios() {
        return folios.size();
    }

    /**
     * Removes the indicated folio.
     *
     * @param position of the folio
     */
    public void removeFolio(int position) {
        folios.get(position).notifyDeletion();
        folios.remove(position);
    }
    
    public void removeFolio(TeiSurfaceGrpFolio folio) {
        folios.remove(folio);
        folio.notifyDeletion();
    }
}
