/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp.surface.zone;

import ch.unifr.tei.TeiHisDoc;
import org.apache.log4j.Logger;

/**
 * @author Mathias Seuret
 */
public enum TeiZoneType {
    MAIN_TEXT("main-text"),
    COMMENT_TEXT("comment-text"),
    DECORATION("decoration"),
    UNKNOWN("unknown");

    private static final Logger logger = Logger.getLogger(TeiZoneType.class);

    public final String name;

    TeiZoneType(String n) {
        name = n;
    }

    /**
     * @param name of a zone type
     * @return the type or throws an error if type not found
     */
    public static TeiZoneType get(String name) {
        for (TeiZoneType t : TeiZoneType.values()) {
            if (t.name.equalsIgnoreCase(name)) {
                return t;
            }
        }
        if (name==null) {
            return UNKNOWN;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Trying to load an invalid zone type: ");
        sb.append(name);
        sb.append("\nAllowed zone types are, for TeiHisDoc version ");
        sb.append(TeiHisDoc.VERSION);
        sb.append(":\n");
        for (TeiZoneType t : TeiZoneType.values()) {
            sb.append("  - ");
            sb.append(t.name);
            sb.append("\n");
        }
        sb.append("\nWarning: type will be loaded and saved, but the tool cannot handle it.");
        logger.warn(sb);
        return UNKNOWN;
    }
}
