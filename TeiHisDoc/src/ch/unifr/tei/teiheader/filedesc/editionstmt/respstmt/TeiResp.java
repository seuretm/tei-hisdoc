/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.editionstmt.respstmt;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author Mathias Seuret
 */
public class TeiResp extends TeiTextElement {
    private TeiRespStmt parent;

    public TeiResp(TeiElement parent) {
        super(parent);
    }

    public TeiResp(TeiElement parent, String content) {
        this(parent);
        setContent(content);
    }

    private TeiResp(TeiRespStmt parent, Element el) {
        this(parent);
        this.parent = parent;

        setContent(el.getText());

        consume(el);
    }

    public static TeiResp load(TeiRespStmt parent, Element el) {
        return new TeiResp(parent, el);
    }

    @Override
    public String getElementName() {
        return "resp";
    }

    @Override
    public void generateDefaultId() {
        setId("resp-" + parent.getIndex(this) + 1);
    }

    protected String getResp() {
        return null;
    }

}
