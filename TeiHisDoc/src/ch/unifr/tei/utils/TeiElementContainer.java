/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.utils;

/**
 * @author Mathias Seuret
 */
public interface TeiElementContainer<T extends TeiElement> {
    int getIndex(T child);
}
