/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp.surface.zone;

import ch.unifr.tei.text.body.div.div.ab.TeiC;
import ch.unifr.tei.utils.TeiArea;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiCharZone extends TeiZone<TeiWordZone, TeiCharZone> {
    private TeiWordZone parent;
    
    protected TeiC transcription = null;

    public TeiCharZone(TeiWordZone parent, TeiArea area) {
        super(parent, area);
        this.parent = parent;
    }

    private TeiCharZone(TeiWordZone parent, Element el) {
        super(parent, el);
        this.parent = parent;
    }

    public static TeiCharZone load(TeiWordZone parent, Element el) {
        return new TeiCharZone(parent, el);
    }

    public TeiWordZone getParent() {
        return parent;
    }
    
    public void initializeTranscription() {
        if (parent.getTranscription()==null) {
            parent.initializeTranscription();
        }
        transcription = new TeiC(parent.getTranscription());
        transcription.setFacs(this);
    }
    
    public void setTranscription(TeiC c) {
        if (c==transcription) {
            return;
        }
        if (transcription!=null) {
            transcription.setFacs(null);
        }
        transcription = c;
        if (transcription!=null && transcription.getFacs()!=this) {
            transcription.setFacs(this);
        }
    }
    
    
    public TeiC getTranscription() {
        return transcription;
    }
    
    @Override
    public void notifyDeletion() {
        super.notifyDeletion();
        if (transcription==null) {
            return;
        }
        transcription.setFacs(null);
    }

    @Override
    protected void loadSubZones(Element el) {
        if (!el.getChildren("zone", TeiNS).isEmpty()) {
            throw new Error("cannot have subzones in " + getPath());
        }
    }

    @Override
    public void generateDefaultId() {
        String pid = parent.getId();
        if (pid == null) {
            pid = "";
        }
        int n = parent.getIndex(this) + 1;
        setId(pid + "c" + n);
    }

    @Override
    public int getIndex(TeiCharZone child) {
        throw new Error("TeiCharZones do not have children");
    }
}
