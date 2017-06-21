/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package teihisdoc;

import ch.unifr.tei.TeiHisDoc;
import ch.unifr.tei.facsimile.surfacegrp.TeiSurfaceGrpFolio;
import ch.unifr.tei.facsimile.surfacegrp.TeiSurfaceGrpPart;
import ch.unifr.tei.facsimile.surfacegrp.surface.TeiSurface;
import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiTextSegZone;
import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiTextZone;
import ch.unifr.tei.facsimile.surfacegrp.surface.zone.TeiWordZone;
import ch.unifr.tei.teiheader.filedesc.editionstmt.respstmt.TeiRespStmt;
import ch.unifr.tei.utils.TeiAreaRect;
import org.jdom2.JDOMException;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Mathias Seuret
 */
public class Main {

    /**
     * @param args a TeiHisDoc file
     * @throws org.jdom2.JDOMException if the file does not contain valid XML
     * @throws java.io.IOException if the file cannot be read
     */
    public static void main(String[] args) throws JDOMException, IOException {
        TeiHisDoc thd1 = new TeiHisDoc(
                TeiRespStmt.method(
                        "TeiHisDoc",
                        "Document validation",
                        "Validated the document"
                )
        );
        thd1.load(args[0]);
        thd1.write(new FileWriter("loaded-saved-1.xml"));
        
        TeiHisDoc thd2 = new TeiHisDoc(
                TeiRespStmt.method(
                        "TeiHisDoc",
                        "Document validation",
                        "Checking load/save"
                )
        );
        thd2.load("loaded-saved-1.xml");
        thd2.write(new FileWriter("loaded-saved-2.xml"));
    }

}
