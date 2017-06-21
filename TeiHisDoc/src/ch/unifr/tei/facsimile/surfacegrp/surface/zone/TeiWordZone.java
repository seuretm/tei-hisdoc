/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp.surface.zone;

import ch.unifr.tei.text.body.div.div.ab.TeiW;
import ch.unifr.tei.utils.TeiArea;
import org.jdom2.Element;

import java.util.Collections;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiWordZone extends TeiZone<TeiTextSegZone, TeiCharZone> {
    
    protected TeiW transcription = null;

    public TeiWordZone(TeiTextSegZone parent, TeiArea area) {
        super(parent, area);
    }

    private TeiWordZone(TeiTextSegZone parent, Element el) {
        super(parent, el);
    }

    public static TeiWordZone load(TeiTextSegZone parent, Element el) {
        return new TeiWordZone(parent, el);
    }

    @Override
    protected void loadSubZones(Element el) {
        for (Element e : el.getChildren("zone", TeiNS)) {
            zones.add(TeiCharZone.load(this, e));
        }
        el.removeChildren("zone", TeiNS);
    }

    public TeiTextSegZone getParent() {
        return (TeiTextSegZone) parent;
    }
    
    public void setTranscription(TeiW word) {
        if (transcription!=null) {
            transcription.setFacs(null);
        }
        transcription = word;
        if (transcription!=null && transcription.getFacs()!=this) {
            transcription.setFacs(this);
        }
    }
    
    public TeiW getTranscription() {
        return transcription;
    }
    
    @Override
    public void notifyDeletion() {
        super.notifyDeletion();
        for (TeiZone z : zones) {
            z.notifyDeletion();
        }
        if (transcription!=null) {
            transcription.setFacs(null);
            transcription.notifyDeletion();
        }
    }

    @Override
    public int getIndex(TeiCharZone child) {
        return zones.indexOf(child);
    }

    @Override
    public void generateDefaultId() {
        String pid = parent.getId();
        if (pid == null) {
            pid = "";
        }
        // TODO Mathias: shouldn't there be a TeiZone parent field in this class? (Manu)
        int n = ((TeiZone) parent).getIndex(this) + 1;
        setId(pid + "w" + n);
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
    public TeiCharZone addCharZone(TeiArea area) {
        TeiCharZone t = new TeiCharZone(this, area);
        zones.add(t);
        t.generateDefaultId();
        return t;
    }

    /**
     * @return the list of char zones
     */
    public List<TeiCharZone> getCharZones() {
        return Collections.unmodifiableList(zones);
    }

    public void removeCharZone(TeiCharZone z) {
        if (zones.contains(z)) {
            zones.remove(z);
            z.notifyDeletion();
            regenerateCharIDs();
        } else {
            throw new Error(
                    z.getPath()+" not inside of "+this.getPath()
            );
        }
    }

    private void regenerateCharIDs() {
        for (TeiCharZone t : zones) {
            t.generateDefaultId();
        }
    }

    public void initializeTranscription() {
        if (transcription!=null) {
            return;
        }
        System.out.println("Initializing TeiWordZone transcription");
        if (getParent().getTranscription()==null) {
            getParent().initializeTranscription();
        }
        transcription = getParent().getTranscription().addW();
        transcription.setFacs(this);
        System.out.println("TeiWordZone transcription initialized");
    }


}
