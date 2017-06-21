/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp.surface.zone;

import ch.unifr.tei.facsimile.surfacegrp.surface.TeiSurface;
import ch.unifr.tei.utils.TeiArea;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiDecorationZone extends TeiZone<TeiSurface, TeiDecorationZone> {

    public TeiDecorationZone(TeiSurface parent, TeiArea area) {
        super(parent, area);
        setType(TeiZoneType.DECORATION);
    }

    private TeiDecorationZone(TeiSurface parent, Element el) {
        super(parent, el);
        setType(TeiZoneType.DECORATION);
    }

    public static TeiDecorationZone load(TeiSurface parent, Element el) {
        return new TeiDecorationZone(parent, el);
    }

    @Override
    protected void loadSubZones(Element el) {
        // No subzone allowed
    }

    @Override
    public void generateDefaultId() {
        String pid = parent.getId();
        if (pid == null) {
            pid = "";
        }
        int n = ((TeiSurface) parent).getIndex(this) + 1;
        setId(pid + "D" + n);
    }

    @Override
    public int getIndex(TeiDecorationZone child) {
        throw new Error("decoration zone cannot have children");
    }

    @Override
    public void updateParentSize() {
        //nothing done as this is the super-zone.
    }


}
