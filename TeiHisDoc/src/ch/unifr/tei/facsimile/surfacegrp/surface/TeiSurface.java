/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp.surface;

import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiDecorationZone;
import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiTextZone;
import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiZone;
import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiZoneType;
import ch.unifr.tei.text.body.div.TeiDiv;
import ch.unifr.tei.utils.TeiArea;
import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiSurface extends TeiElement implements TeiSurfaceContainer {
    /**
     * List of graphics corresponding to the page.
     */
    private List<TeiGraphic> graphics = new LinkedList<>();
    // Accessor ok

    /**
     * Text zones on the page.
     */
    private List<TeiTextZone> zones = new ArrayList<>();
    // Accessors OK

    /**
     * Decoration zones on the page.
     */
    private List<TeiDecorationZone> decorationZones = new ArrayList<>();

    /**
     * Additional surfaces on the pages (e.g., stickers).
     */
    private List<TeiSurface> surfaces = new ArrayList<>();
    
    private TeiDiv transcription = null;

    /**
     * Folio.
     */
    private TeiSurfaceContainer parent;

    private String attachment = null;
    private SurfaceType type = null;

    private String pageNum = null;

    public TeiSurface(TeiElement parent) {
        super(parent);
        this.parent = (TeiSurfaceContainer) parent;
    }

    public TeiSurface(TeiSurfaceContainer parent, SurfaceType type) {
        this((TeiElement) parent);
        this.type = type;
    }

    private TeiSurface(TeiSurfaceContainer parent, Element el) {
        this((TeiElement) parent);

        attachment = consumeAttributeStr(el, "attachment", NoNS, false);
        type = SurfaceType.getType(consumeAttributeStr(el, "type", NoNS, false));

        for (Element e : el.getChildren("surface", TeiNS)) {
            surfaces.add(TeiSurface.load(this, e));
        }
        el.removeChildren("surface", TeiNS);

        for (Element e : el.getChildren("graphic", TeiNS)) {
            graphics.add(TeiGraphic.load(this, e));
        }
        el.removeChildren("graphic", TeiNS);

        for (Element e : el.getChildren("zone", TeiNS)) {
            TeiZone zone = TeiZone.load(this, e);
            if (zone.getType().equals(TeiZoneType.DECORATION)) {
                decorationZones.add((TeiDecorationZone) zone);
            } else {
                zones.add((TeiTextZone) zone);
            }
        }
        el.removeChildren("zone", TeiNS);

        for (Element e : el.getChildren("damageSpan", TeiNS)) {
            addIgnoredElement(e);
        }
        el.removeChildren("damageSpan", TeiNS);

        if (type != null && type == SurfaceType.PAGE) {
            // pages have page numbers
            pageNum = consumeAttributeStr(el, "n", NoNS, false);
        }

        for (Element e : el.getChildren("listTranspose", TeiNS)) {
            addIgnoredElement(e);
        }
        el.removeChildren("listTranspose", TeiNS);
        
        consume(el);
    }

    public static TeiSurface load(TeiSurfaceContainer parent, Element el) {
        return new TeiSurface(parent, el);
    }
    
    @Override
    public void notifyDeletion() {
        super.notifyDeletion();
        
        for (TeiGraphic g : graphics) {
            g.notifyDeletion();
        }
        for (TeiZone z : decorationZones) {
            z.notifyDeletion();
        }
        for (TeiZone z : zones) {
            z.notifyDeletion();
        }
        for (TeiSurface s : surfaces) {
            s.notifyDeletion();
        }
        
        graphics.clear();
        decorationZones.clear();
        zones.clear();
        surfaces.clear();
    }

    @Override
    public String getElementName() {
        return "surface";
    }
    
    public void setTranscription(TeiDiv div) {
        if (div==transcription) {
            return;
        }
        if (transcription!=null) {
            transcription.setFacs(null);
        }
        transcription = div;
        if (transcription!=null && transcription.getFacs()!=this) {
            transcription.setFacs(this);
        }
    }
    
    public TeiDiv getTranscription() {
        return transcription;
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        for (TeiGraphic g : graphics) {
            el.addContent(g.toXML());
        }
        for (TeiZone z : decorationZones) {
            el.addContent(z.toXML());
        }
        for (TeiZone z : zones) {
            el.addContent(z.toXML());
        }
        for (TeiSurface s : surfaces) {
            el.addContent(s.toXML());
        }

        if (attachment != null) {
            el.setAttribute("attachment", attachment, NoNS);
        }
        if (type != null && type.getName() != null) {
            el.setAttribute("type", type.getName(), NoNS);
        }
        if (type != null && type == SurfaceType.PAGE && pageNum != null) {
            el.setAttribute("n", pageNum, NoNS);
        }


        return el;
    }

    @Override
    public void generateDefaultId() {
        if (type == SurfaceType.PAGE) {
            String pid = ((TeiElement) parent).getId();
            if (pid == null) {
                pid = "";
            }
            int n = parent.getIndex(this) + 1;
            setId(pid + "p" + n);
        }
    }

    @Override
    public int getIndex(TeiSurface child) {
        return surfaces.indexOf(child);
    }

    public int getIndex(TeiTextZone child) {
        return zones.indexOf(child);
    }

    public int getIndex(TeiDecorationZone child) {
        return decorationZones.indexOf(child);
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
        for (TeiElement e : graphics) {
            e.setId(e.getRandomId());
        }
        for (TeiElement e : graphics) {
            e.generateDefaultId();
            e.updateChildrenIDs();
        }
        for (TeiTextZone z : zones) {
            z.setId(z.getRandomId());
        }
        for (TeiTextZone z : zones) {
            z.generateDefaultId();
            z.updateChildrenIDs();
        }
        for (TeiElement e : decorationZones) {
            e.setId(e.getRandomId());
        }
        for (TeiElement e : decorationZones) {
            e.generateDefaultId();
            e.updateChildrenIDs();
        }
    }
    
    /*
        ACCESSORS
    */

    /**
     * @return the list of all graphics from this surface
     */
    public List<TeiGraphic> getGraphics() {
        return Collections.unmodifiableList(graphics);
    }

    /**
     * Adds a graphic for the surface.
     *
     * @param url  file location
     * @param desc description of this graphic
     * @return a ref to the newly created graphic
     */
    public TeiGraphic addGraphic(String url, String desc) {
        TeiGraphic g = new TeiGraphic(this, url, desc);

        graphics.add(g);

        return g;
    }

    /**
     * Removes a graphics.
     *
     * @param g graphics to remove
     * @return true in case of success
     */
    public boolean removeGraphics(TeiGraphic g) {
        g.notifyDeletion();
        return graphics.remove(g);
    }

    /**
     * Removes a graphics.
     *
     * @param index of the graphic to remove
     */
    public void removeGraphics(int index) {
        graphics.get(index).notifyDeletion();
        removeGraphics(graphics.get(index));
    }

    /**
     * Adds a new text zone in the page.
     *
     * @param area containing the zone
     * @return the newly created text zone
     */
    public TeiTextZone addTextZone(TeiArea area) {
        TeiTextZone t = new TeiTextZone(this, area);
        zones.add(t);
        t.generateDefaultId();
        return t;
    }

    public TeiDecorationZone addDecorationZone(TeiArea area) {
        TeiDecorationZone d = new TeiDecorationZone(this, area);
        decorationZones.add(d);
        d.generateDefaultId();
        return d;
    }

    /**
     * Removes a zone.
     *
     * @param zone the zone to remove
     */
    public void removeTextZone(TeiTextZone zone) {
        zones.remove(zone);
        zone.notifyDeletion();
    }

    /**
     * Removes a zone.
     *
     * @param zone the zone to remove
     */
    public void removeDecorationZone(TeiDecorationZone zone) {
        decorationZones.remove(zone);
        zone.notifyDeletion();
    }

    /**
     * Removes a zone.
     *
     * @param zoneIndex index of the zone to remove
     */
    public void removeTextZone(int zoneIndex) {
        zones.get(zoneIndex).notifyDeletion();
        zones.remove(zoneIndex);
    }

    /**
     * Removes a decoration zone.
     *
     * @param zoneIndex index of the zone to remove
     */
    public void removeDecorationZone(int zoneIndex) {
        decorationZones.get(zoneIndex).notifyDeletion();
        decorationZones.remove(zoneIndex);
    }

    /**
     * @return the list of text zones
     */
    public List<TeiTextZone> getTextZones() {
        return Collections.unmodifiableList(zones);
    }

    /**
     * @return the list of decoration zones
     */
    public List<TeiDecorationZone> getDecorationZones() {
        return Collections.unmodifiableList(decorationZones);
    }

    /**
     * @return the type of the surface
     */
    public SurfaceType getType() {
        return type;
    }

    /**
     * Selects the type of the surface.
     *
     * @param t new type
     */
    public void setType(SurfaceType t) {
        type = t;
    }

    /**
     * Getter for property 'pageNum'.
     *
     * @return Value for property 'pageNum'.
     */
    public String getPageNum() {
        return pageNum;
    }

    /**
     * Sets the page number. It is recommended to use digits.
     *
     * @param newNum new page number
     */
    public void setPageNum(String newNum) {
        this.pageNum = newNum;
    }

    int getIndex(TeiGraphic g) {
        return graphics.indexOf(g);
    }

    /**
     * Creates the needed tag in TEI/text/body.
     */
    public void initializeTranscription() {
        if (transcription!=null) {
            return;
        }
        System.out.println("Initializing TeiSurface transcription");
        transcription = getRoot().getText().getBody().addDiv();
        transcription.setFacs(this);
        
        if (!getRoot().getText().getBody().getDivs().contains(transcription)) {
            throw new Error("No div in the body!");
        }
        
        System.out.println("TeiSurface transcription initialized");
    }
}
