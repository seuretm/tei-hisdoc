/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.editionstmt.respstmt;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ms
 */
public class TeiDate extends TeiTextElement {
    public TeiDate(TeiElement parent) {
        super(parent);
        setContent(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private TeiDate(TeiElement parent, Element el) {
        this(parent);

        setContent(el.getTextNormalize());

        consume(el);
    }

    public static TeiDate load(TeiElement parent, Element el) {
        return new TeiDate(parent, el);
    }

    public String getDate() {
        return getContent();
    }

    public void setDate(String dateStr) {
        setContent(dateStr);
    }

    @Override
    public String getElementName() {
        return "date";
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }
}
