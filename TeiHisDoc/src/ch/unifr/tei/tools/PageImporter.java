/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.tools;

import ch.unifr.tei.TeiHisDoc;
import ch.unifr.tei.facsimile.surfacegrp.surface.TeiSurface;
import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiTextSegZone;
import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiTextZone;
import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiZoneType;
import ch.unifr.tei.teiheader.filedesc.editionstmt.respstmt.TeiRespStmt;
import ch.unifr.tei.utils.TeiAreaPoly;
import ch.unifr.tei.utils.TeiAreaRect;
import ch.unifr.tei.utils.TeiAreaUnlocalized;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class PageImporter {

    private static final Logger logger = Logger.getLogger(PageImporter.class);

    private static final String methodName = "PageImporter-v0.0";

    private static TeiHisDoc currentRoot = null;
    private static TeiRespStmt resp = null;

    public static void replaceSurfaceContent(TeiSurface target, String pageFile, String graphicsRelPath) throws JDOMException, IOException {
        TeiRespStmt prevResp = target.getRoot().getCurrentResponsible();

        if (currentRoot != target.getRoot()) {
            resp = TeiRespStmt.method(methodName, "Page file import", "Imported Page file " + pageFile);
            currentRoot = target.getRoot();
        } else {
            resp.addResp("Imported Page file " + pageFile);
        }
        target.getRoot().setRespStmt(resp);

        while (!target.getTextZones().isEmpty()) {
            target.removeTextZone(0);
        }
        while (!target.getDecorationZones().isEmpty()) {
            target.removeDecorationZone(0);
        }

        while (!target.getGraphics().isEmpty()) {
            target.removeGraphics(0);
        }

        File f = new File(pageFile);
        SAXBuilder builder = new SAXBuilder();
        Document xml = builder.build(f);
        Element root = xml.getRootElement();

        target.addGraphic(
                ((graphicsRelPath == null) ? "" : graphicsRelPath + "/") + root.getChild("Page").getAttributeValue("imageFilename"),
                "Original document image"
        );

        addWithTextZones(target, root);

        target.getRoot().setRespStmt(prevResp);
    }


    private static void addWithTextZones(TeiSurface target, Element root) {
        List<TeiTextZone> textZones = new ArrayList<>();
        List<Rectangle2D> textBounds = new ArrayList<>();

        TeiTextZone unlocZone = target.addTextZone(new TeiAreaUnlocalized());
        unlocZone.setType(TeiZoneType.MAIN_TEXT);

        for (Element e : root.getChild("Page").getChildren("TextRegion")) {
            if ("text".equals(e.getAttributeValue("type"))) {
                Polygon polygon = getPolygon(e);
                TeiTextZone z = target.addTextZone(new TeiAreaPoly(polygon));
                z.setType(TeiZoneType.MAIN_TEXT);
                textZones.add(z);
                textBounds.add(polygon.getBounds2D());
            }
        }

        for (Element e : root.getChild("Page").getChildren("TextRegion")) {
            if ("text".equals(e.getAttributeValue("type"))) {
                // Already processed
                logger.trace("already processed");
            } else if ("page".equals(e.getAttributeValue("type"))) {
                // Not existing in TEI
                logger.error("not existing in TEI");
            } else if ("textline".equals(e.getAttributeValue("type"))) {
                Polygon polygon = getPolygon(e);

                int optIndex = -1;
                double score = Double.NEGATIVE_INFINITY;
                for (int i = 0; i < textBounds.size(); i++) {
                    Rectangle2D overlap = textBounds.get(i).createIntersection(polygon.getBounds2D());
                    if (overlap.getWidth() <= 0 || overlap.getHeight() <= 0) {
                        continue;
                    }
                    double s = overlap.getWidth() * overlap.getHeight();
                    if (s > score) {
                        score = s;
                        optIndex = i;
                    }
                }

                if (optIndex == -1) {
                    unlocZone.addTextSegZone(new TeiAreaPoly(polygon));
                } else {
                    textZones.get(optIndex).addTextSegZone(new TeiAreaPoly(polygon));
                }
            } else if ("decoration".equals(e.getAttributeValue("type"))) {
                Polygon polygon = getPolygon(e);
                target.addDecorationZone(new TeiAreaPoly(polygon));
            } else if ("comment".equals(e.getAttributeValue("type"))) {
                Polygon polygon = getPolygon(e);
                TeiTextZone commentZone = target.addTextZone(new TeiAreaRect(polygon.getBounds()));
                commentZone.setType(TeiZoneType.COMMENT_TEXT);
                TeiTextSegZone z = commentZone.addTextSegZone(new TeiAreaPoly(polygon));
                z.setType(TeiZoneType.COMMENT_TEXT);
            } else {
                System.err.println("Unknown zone type: " + e.getAttributeValue("type"));
            }
        }

        if (unlocZone.getTextSegZones().isEmpty()) {
            target.removeTextZone(unlocZone);
        } else {
            unlocZone.fitChildrenBounds();
        }

        target.updateChildrenIDs();
    }

    private static Polygon getPolygon(Element z) {
        Polygon p = new Polygon();

        for (Element e : z.getChild("Coords").getChildren("Point")) {
            int x = (int) Math.round(Double.parseDouble(e.getAttributeValue("x")));
            int y = (int) Math.round(Double.parseDouble(e.getAttributeValue("y")));
            p.addPoint(x, y);
        }

        return p;
    }

    public static TeiRespStmt getRespStmt(String pageFile) throws JDOMException, IOException {
        File f = new File(pageFile);
        SAXBuilder builder = new SAXBuilder();
        Document xml = builder.build(f);
        Element root = xml.getRootElement();
        Element meta = root.getChild("Metadata");
        if (meta == null) {
            return null;
        }
        TeiRespStmt stmt = TeiRespStmt.person(
                meta.getChildText("Creator"),
                null,
                null,
                "Annotator",
                "Polygon creation"
        );

        if (meta.getChild("LastChange") != null) {
            stmt.getPersName().setDate(meta.getChildText("LastChange"));
        } else if (meta.getChild("Created") != null) {
            stmt.getPersName().setDate(meta.getChildText("Created"));
        }

        return stmt;
    }
}
