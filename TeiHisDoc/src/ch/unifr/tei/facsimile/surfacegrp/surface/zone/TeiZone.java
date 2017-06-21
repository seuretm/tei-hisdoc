/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp.surface.zone;

import ch.unifr.tei.facsimile.surfacegrp.surface.TeiSurface;
import ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.handdesc.handnote.TeiHandNote;
import ch.unifr.tei.utils.*;
import org.jdom2.Attribute;
import org.jdom2.Element;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
//import java.util.Collections;
//import java.util.Arrays;
//import org.apache.commons.lang.ArrayUtils;

/**
 * @param <P> parent zone type
 * @param <C> child zone type - if there are no children, use the class itself
 * @author Mathias Seuret
 */
public abstract class TeiZone<P extends TeiElement, C extends TeiZone> extends TeiElement implements TeiZoneContainer<C> {

    protected List<C> zones = new LinkedList<>();
    protected TeiArea area;
    protected String label;
    protected TeiZoneType type = TeiZoneType.UNKNOWN;
    private TeiHandNote handNote = null;

    protected TeiZone(P parent, TeiArea area) {
        super(parent);
        this.area = area;
    }

    protected TeiZone(P parent, Element el) {
        super(parent);
        boolean hasPoints = el.getAttribute("points", NoNS) != null;
        boolean hasUlx = el.getAttribute("ulx", NoNS) != null;
        boolean hasUly = el.getAttribute("uly", NoNS) != null;
        boolean hasLrx = el.getAttribute("lrx", NoNS) != null;
        boolean hasLry = el.getAttribute("lry", NoNS) != null;

        loadSubZones(el);

        if (hasPoints && !hasUlx && !hasUly && !hasLrx && !hasLry) {
            //System.out.println("I am in 1");
            String[] pts = el.getAttributeValue("points", NoNS).trim().split(" ");

            if (pts.length < 3) {
                throw new Error("there must be at least three points in " + parent.getPath() + "/zone");
            }

            Polygon poly = new Polygon();
            for (int i = 0; i < pts.length; i++) {
                int x, y;
                String[] pt = pts[i].split(",");
                if (pt.length != 2) {
                    throw new Error("illegal number of coordinates in point " + (i + 1) + "of " + parent.getPath() + "/zone");
                }
                try {
                    x = Integer.parseInt(pt[0]);
                } catch (Exception e) {
                    throw new Error("non-integer coordinate x of point " + (i + 1) + "of " + parent.getPath() + "/zone");
                }
                try {
                    y = Integer.parseInt(pt[1]);
                } catch (Exception e) {
                    throw new Error("non-integer coordinate x of point " + (i + 1) + "of " + parent.getPath() + "/zone");
                }
                poly.addPoint(x, y);
            }
            area = new TeiAreaPoly(poly);
            el.removeAttribute("points", NoNS);
        } else if (!hasPoints && hasUlx && hasUly && hasLrx && hasLry) {
            //System.out.println("I am in 2");
            int x1 = Integer.parseInt(el.getAttributeValue("ulx", NoNS));
            int y1 = Integer.parseInt(el.getAttributeValue("uly", NoNS));
            int x2 = Integer.parseInt(el.getAttributeValue("lrx", NoNS));
            int y2 = Integer.parseInt(el.getAttributeValue("lry", NoNS));
            area = new TeiAreaRect(
                    x1,
                    y1,
                    x2,
                    y2
            );
            el.removeAttribute("ulx", NoNS);
            el.removeAttribute("uly", NoNS);
            el.removeAttribute("lrx", NoNS);
            el.removeAttribute("lry", NoNS);

        } else {
            fitChildrenBounds();
        }

        label = consumeAttributeStr(el, "n", NoNS, false);
        type = TeiZoneType.get(consumeAttributeStr(el, "type", NoNS, false));
        if (el.getAttribute("written") != null) {
            String hnID = el.getAttributeValue("written");
            if (hnID.length() > 0 && hnID.charAt(0) == '#') {
                hnID = hnID.substring(1);
            }
            handNote = getRoot().getHeader().getFileDesc().getSourceDesc().getMsDesc().getPhysDesc().getHandDesc().getHandNote(hnID);
            el.removeAttribute("written", NoNS);
        }

        consume(el);
    }

    public static TeiZone load(TeiSurface parent, Element el) {

        Attribute t = el.getAttribute("type", NoNS);
        if (null != t) {
            TeiZoneType type = TeiZoneType.get(t.getValue());
            if (type.equals(TeiZoneType.MAIN_TEXT)) {
                return TeiTextZone.load(parent, el);
            } else if (type.equals(TeiZoneType.COMMENT_TEXT)) {
                return TeiTextZone.load(parent, el);
            } else if (type.equals(TeiZoneType.DECORATION)) {
                return TeiDecorationZone.load(parent, el);
            } else {
                return TeiTextZone.load(parent, el);
            }
        }
        return TeiTextZone.load(parent, el);
    }

    /**
     * @param ints the values as an array of chars
     * @return the max value in the array of chars
     */
    private static int maxValue(int[] ints) {
        int max = ints[0];
        for (int anInt : ints) {
            if (anInt > max) {
                max = anInt;
            }
        }
        return max;
    }

    private static int minValue(int[] ints) {
        int min = ints[0];
        for (int anInt : ints) {
            if (anInt < min) {
                min = anInt;
            }
        }
        return min;
    }

    public void fitChildrenBounds() {
        // check if the direct children have coordinates
        // then get min and max
        // if they do not have coordinates we cannot do anything
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = 0;
        int maxY = 0;
        TeiArea area;
        for (TeiZone zone : zones) {
            TeiArea subArea = zone.getArea();
            if (subArea.getShape() != null) {
                Rectangle subBounds = subArea.getShape().getBounds();
                minX = subBounds.x < minX ? subBounds.x : minX;
                minY = subBounds.y < minY ? subBounds.y : minY;
                maxX = (subBounds.x + subBounds.width) > maxX ? subBounds.x + subBounds.width : maxX;
                maxY = (subBounds.y + subBounds.height) > maxY ? subBounds.y + subBounds.height : maxY;
            } else {
                System.err.println("Warning, children zone with no coordinates: " + zone.getId());
            }
        }
        if (maxX > 0) {
            area = new TeiAreaRect(minX, minY, maxX, maxY);
        } else {
            area = new TeiAreaUnlocalized();
        }
        setArea(area);
    }
    
    /*
        ACCESSORS
    */

    protected abstract void loadSubZones(Element el);

    @Override
    public String getElementName() {
        return "zone";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiElement z : zones) {
            addContent(el, z);
        }

        addAttribute(el, "n", label, NoNS);
        if (type != null && type != TeiZoneType.UNKNOWN) {
            addAttribute(el, "type", type.name, NoNS);
        }
        if (handNote != null) {
            addAttribute(el, "written", "#" + handNote.getId(), NoNS);
        }

        area.addAttributesTo(el);

        return el;
    }

    @Override
    public void updateChildrenIDs() {
        for (C e : zones) {
            e.setId(e.getRandomId());
        }
        for (C e : zones) {
            e.generateDefaultId();
            e.updateChildrenIDs();
        }
    }

    /*
        ACCESSORS
     */
    public boolean contains(Point2D pt) {
        return area.contains(pt);
    }

    /**
     * @return the area of the zones
     */
    public TeiArea getArea() {
        return area;
    }

    /**
     * Updates the area of the zone.
     *
     * @param a new area
     * @return the area
     */
    public TeiArea setArea(TeiArea a) {
        area = a;
        updateResp();
        return a;
    }

    /**
     * @return the handNode (null if none)
     */
    public TeiHandNote getHandNote() {
        return handNote;
    }

    /**
     * Updates the hand note.
     *
     * @param handNote new hand note
     * @return the hand note
     */
    public TeiHandNote setHandNote(TeiHandNote handNote) {
        this.handNote = handNote;
        return handNote;
    }

    /**
     * Remove the hand note.
     */
    public void removeHandNote() {
        this.handNote = null;
    }

    /**
     * @return the zone type
     */
    public TeiZoneType getType() {
        return type;
    }

    /**
     * Sets the type of the zone.
     *
     * @param type new type
     */
    public void setType(TeiZoneType type) {
        this.type = type;
    }

    public int getNumberOfChildZones() {
        return zones.size();
    }

    public void updateParentSize() {
        if (parent instanceof TeiZone) {
            TeiZone parentZone = (TeiZone) parent;
            Rectangle pr = parentZone.getArea().getShape().getBounds();
            Rectangle r = this.getArea().getShape().getBounds();
            if (!pr.contains(r)) {
                parentZone.fitChildrenBounds();
            }
            parentZone.updateParentSize();
        }
    }


}
