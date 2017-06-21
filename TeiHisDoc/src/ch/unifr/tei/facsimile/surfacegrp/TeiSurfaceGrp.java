/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp;

import ch.unifr.tei.utils.TeiElement;

/**
 * @author ms
 */
public abstract class TeiSurfaceGrp extends TeiElement {
    /**
     * Constructs a TeiSurfaceGrp
     *
     * @param parent the parent TeiElement
     */
    public TeiSurfaceGrp(TeiSurfaceGrpContainer parent) {
        super((TeiElement) parent);
    }


    @Override
    protected String getElementName() {
        return "surfaceGrp";
    }
}
