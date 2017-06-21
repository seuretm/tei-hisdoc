/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp.surface;

/**
 * Enumeration of the different kinds of surfaces - currently only pages.
 *
 * @author Mathias Seuret
 */
public enum SurfaceType {
    PAGE("page");

    private String name;

    SurfaceType(String name) {
        this.name = name;
    }

    /**
     * Uses TEI name convention instead of enum variable names.
     *
     * @param name to look for
     * @return the corresponding type
     * @throws IllegalArgumentException if the type does not exist
     */
    public static SurfaceType getType(String name) {
        for (SurfaceType t : values()) {
            if (t.name.equals(name)) {
                return t;
            }
        }
        throw new IllegalArgumentException(
                "Surface type '" + name + "' does not exist"
        );
    }

    /**
     * @return the TEI string corresponding to the type
     */
    public String getName() {
        return name;
    }
}
