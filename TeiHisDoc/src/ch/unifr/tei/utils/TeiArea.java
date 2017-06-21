/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.utils;

import org.jdom2.Element;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author Mathias Seuret
 */
public abstract class TeiArea {
    public abstract boolean contains(Point2D p);

    public abstract void addAttributesTo(Element el);

    public abstract Shape getShape();
}
