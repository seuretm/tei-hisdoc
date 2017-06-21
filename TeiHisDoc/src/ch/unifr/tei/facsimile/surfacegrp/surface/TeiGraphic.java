/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.facsimile.surfacegrp.surface;

import ch.unifr.tei.utils.TeiElement;
import org.jdom2.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Image of a page.
 *
 * @author Mathias Seuret
 */
public class TeiGraphic extends TeiElement {
    /**
     * Address/path of the image.
     */
    private String url;
    // Accessors OK

    /**
     * Description of the image.
     */
    private String desc;
    // Accessors OK

    /**
     * Creates a new graphic.
     *
     * @param parent page
     */
    public TeiGraphic(TeiSurface parent) {
        super(parent);
    }

    /**
     * Creates and initializes a new graphics.
     *
     * @param parent page
     * @param url    image's location
     * @param desc   description of the image
     */
    public TeiGraphic(TeiSurface parent, String url, String desc) {
        this(parent);
        this.url = url;
        this.desc = desc;
    }

    private TeiGraphic(TeiSurface parent, Element el) {
        this(parent);

        url = consumeAttributeStr(el, "url", NoNS, true);

        Element d = consumeChild(el, "desc", TeiNS, true);
        desc = d.getText();

        consume(el);
    }

    /**
     * Loads a graphics from an XML element
     *
     * @param parent page
     * @param el     XML element
     * @return the new graphics object
     */
    public static TeiGraphic load(TeiSurface parent, Element el) {
        return new TeiGraphic(parent, el);
    }

    @Override
    public String getElementName() {
        return "graphic";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.setAttribute("url", url, NoNS);

        Element e = new Element("desc", TeiNS);
        e.setText(desc);
        el.addContent(e);

        return el;
    }

    @Override
    public void generateDefaultId() {
        String pid = ((TeiElement) parent).getId();
        if (pid == null) {
            pid = "";
        }
        int n = ((TeiSurface)parent).getIndex(this) + 1;
        setId(pid + "G" + n);
    }

    @Override
    public String toString() {
        return "Graphic: " + desc;
    }
    
    /*
        Accessors
    */

    /**
     * @return the URL of the graphic
     */
    public String getURL() {
        return url;
    }

    /**
     * Changes the URL
     *
     * @param u new url
     */
    public void setURL(String u) {
        url = u;
    }

    /**
     * @return the description of the graphic
     */
    public String getDescription() {
        return desc;
    }

    /**
     * Changes the description
     *
     * @param d new description
     */
    public void setDescription(String d) {
        desc = d;
    }

    /**
     * @return the buffered image indicated by the url
     * @throws IOException if the image cannot be loaded for some reason
     */
    public BufferedImage loadImage() throws IOException {
        if (url.startsWith("http")) {
            return ImageIO.read(new URL(url));
        } else {
            return ImageIO.read(new File(getDirectory() + File.separator + url));
        }
    }
    
    public String getFolder() {
        if (url.startsWith("http")) {
            return null;
        }
        File file = new File(url);
        return file.getParent();
    }
}
