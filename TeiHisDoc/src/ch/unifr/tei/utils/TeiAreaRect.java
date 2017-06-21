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
public class TeiAreaRect extends TeiArea {
    private Rectangle r;

    public TeiAreaRect(Rectangle r) {
        this.r = new Rectangle();
        this.r.x = r.x;
        this.r.y = r.y;
        this.r.width = r.width;
        this.r.height = r.height;
    }

    public TeiAreaRect(int ulx, int uly, int lrx, int lry) {
        r = new Rectangle(ulx, uly, lrx - ulx, lry - uly);
    }

    @Override
    public boolean contains(Point2D p) {
        return r.contains(p);
    }

    @Override
    public void addAttributesTo(Element el) {
        el.setAttribute("ulx", String.valueOf(r.x), TeiElement.NoNS);
        el.setAttribute("uly", String.valueOf(r.y), TeiElement.NoNS);
        el.setAttribute("lrx", String.valueOf(r.x + r.width), TeiElement.NoNS);
        el.setAttribute("lry", String.valueOf(r.y + r.height), TeiElement.NoNS);
    }

    @Override
    public Shape getShape() {
        return r;
    }
}
