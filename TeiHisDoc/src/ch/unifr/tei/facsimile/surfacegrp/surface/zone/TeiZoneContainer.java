/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp.surface.zone;

/**
 * @author ms
 */
public interface TeiZoneContainer<Z extends TeiZone> {
    int getIndex(Z child);
}
