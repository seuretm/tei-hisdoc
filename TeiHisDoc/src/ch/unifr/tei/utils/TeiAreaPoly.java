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
public class TeiAreaPoly extends TeiArea {
    private Polygon p;

    public TeiAreaPoly(Polygon poly) {
        p = new Polygon(
                poly.xpoints.clone(),
                poly.ypoints.clone(),
                poly.npoints
        );
    }

    @Override
    public boolean contains(Point2D p) {
        return this.p.contains(p);
    }

    @Override
    public void addAttributesTo(Element el) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < p.npoints; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(String.valueOf(p.xpoints[i]));
            sb.append(',');
            sb.append(String.valueOf(p.ypoints[i]));
        }
        el.setAttribute("points", sb.toString(), TeiElement.NoNS);
    }

    @Override
    public Shape getShape() {
        return p;
    }

}
