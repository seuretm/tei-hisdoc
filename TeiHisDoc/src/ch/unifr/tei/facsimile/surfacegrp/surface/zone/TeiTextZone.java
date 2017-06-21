/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */
package ch.unifr.tei.facsimile.surfacegrp.surface.zone;

import ch.unifr.tei.facsimile.surfacegrp.surface.TeiSurface;
import ch.unifr.tei.text.body.div.TeiDiv;
import ch.unifr.tei.text.body.div.div.TeiDivDiv;
import ch.unifr.tei.utils.TeiArea;
import org.jdom2.Element;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiTextZone extends TeiZone<TeiSurface, TeiTextSegZone> implements Iterable<TeiTextSegZone> {

    protected TeiDivDiv transcription = null;

    public TeiTextZone(TeiSurface parent, TeiArea area) {
        super(parent, area);
    }

    private TeiTextZone(TeiSurface parent, Element el) {
        super(parent, el);
    }

    public static TeiTextZone load(TeiSurface parent, Element el) {
        return new TeiTextZone(parent, el);
    }

    @Override
    protected void loadSubZones(Element el) {
        for (Element e : el.getChildren("zone", TeiNS)) {
            zones.add(TeiTextSegZone.load(this, e));
        }
        el.removeChildren("zone", TeiNS);
    }

    public void setTranscription(TeiDivDiv div) {
        if (div == transcription) {
            return;
        }
        if (transcription != null) {
            transcription.setFacs(null);
        }
        transcription = div;
        if (transcription != null && transcription.getFacs() != this) {
            transcription.setFacs(this);
        }
    }

    public TeiDivDiv getTranscription() {
        return transcription;
    }

    @Override
    public void notifyDeletion() {
        super.notifyDeletion();
        for (TeiZone z : zones) {
            z.notifyDeletion();
        }
        if (transcription != null) {
            transcription.setFacs(null);
            transcription.notifyDeletion();
        }
    }

    @Override
    public void generateDefaultId() {
        String pid = parent.getId();
        if (pid == null) {
            pid = "";
        }
        int n = ((TeiSurface) parent).getIndex(this) + 1;
        setId(pid + "t" + n);
    }

    @Override
    public int getIndex(TeiTextSegZone child) {
        return zones.indexOf(child);
    }

    /*
        ACCESSORS
     */
    /**
     * Adds a new text zone in the page.
     *
     * @param area containing the zone
     * @return the newly created text zone
     */
    public TeiTextSegZone addTextSegZone(TeiArea area) {
        TeiTextSegZone t = new TeiTextSegZone(this, area);
        zones.add(t);
        t.generateDefaultId();
        return t;
    }

    /**
     * Removes the text segments.
     */
    public void removeTextSegZones() {
        while (zones.size() > 0) {
            removeTextSegZone(zones.get(0));
        }
    }

    public void removeTextSegZone(TeiTextSegZone z) {
        if (zones.contains(z)) {
            zones.remove(z);
            unregisterId(z.getId());
            regenerateTextSegIDs();
        }
    }

    public List<TeiTextSegZone> getTextSegZones() {
        return Collections.unmodifiableList(zones);
    }

    public TeiSurface getParent() {
        return (TeiSurface) parent;
    }

    /**
     * Warning, this method has to make a copy of the list, therefore it is not
     * the fastest one.
     *
     * @return an iterator
     */
    @Override
    public Iterator<TeiTextSegZone> iterator() {
        return getTextSegZones().iterator();
    }

    private void regenerateTextSegIDs() {
        for (TeiTextSegZone t : zones) {
            t.generateDefaultId();
        }
    }

    @Override
    public void updateParentSize() {
        //nothing done as this is the super-zone.
    }

    public void initializeTranscription() {
        if (transcription != null) {
            return;
        }
        System.out.println("Initializing TeiTextZone transcription");
        if (getParent().getTranscription() == null) {
            getParent().initializeTranscription();
        } else {
            System.out.println("TeiTextZone's parent: " + getParent().getPath() + ": -> " + getParent().getTranscription().getPath());
        }
        transcription = getParent().getTranscription().addDivDiv();
        transcription.setFacs(this);
        System.out.println("TeiTextZone transcription initialized");
    }

    public void doSortChildZonesByYPosition() {
        List<TeiTextSegZone> newZones = new LinkedList<>();
        while(!zones.isEmpty()){
            int minY = Integer.MAX_VALUE;
            TeiTextSegZone tMin=null;
            for (TeiTextSegZone t : zones) {
                if (t.getArea().getShape().getBounds().y < minY) {
                    tMin=t;
                    minY=t.getArea().getShape().getBounds().y;
                }
            }
            newZones.add(tMin);
            zones.remove(tMin);
        }
        zones=newZones;
    }

}
