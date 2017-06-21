/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.editionstmt.respstmt;

import ch.unifr.tei.teiheader.filedesc.editionstmt.TeiEditionStmt;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiElementContainer;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathias Seuret
 */
public class TeiRespStmt extends TeiElement implements TeiElementContainer<TeiResp> {

    private TeiPersName persName = null;
    private TeiMethodName methodName = null;
    private List<TeiResp> resps = new LinkedList<>();

    private TeiRespStmt() {
        super(null);
    }

    private TeiRespStmt(TeiEditionStmt parent, Element el) {
        super(parent);

        Element epn = consumeChild(el, "persName", TeiNS, false);
        Element mn = consumeChild(el, "name", TeiNS, false);

        if (epn != null && mn == null) {
            persName = TeiPersName.load(this, epn);
        } else if (epn == null && mn != null) {
            methodName = TeiMethodName.load(this, mn);
        } else {
            throw new Error("either <persName> or <name> required in " + getPath());
        }

        for (Element e : el.getChildren("resp", TeiNS)) {
            resps.add(TeiResp.load(this, e));
        }
        el.removeChildren("resp", TeiNS);

        consume(el);
    }

    public static TeiRespStmt method(String name, String role, String resp, String... additionalResp) {
        TeiRespStmt stmt = new TeiRespStmt();
        stmt.methodName = new TeiMethodName(stmt, name, role);

        TeiResp re = new TeiResp(stmt);
        re.setContent(resp);
        stmt.resps.add(re);

        for (String r : additionalResp) {
            re = new TeiResp(stmt);
            re.setContent(r);
            stmt.resps.add(re);
        }

        return stmt;
    }

    public static TeiRespStmt person(String email, String name, String surname, String role, String... resp) {
        TeiRespStmt stmt = new TeiRespStmt();

        stmt.persName = new TeiPersName(stmt, email, surname, name, role);

        for (String r : resp) {
            TeiResp re = new TeiResp(stmt);
            re.setContent(r);
            stmt.resps.add(re);
        }

        return stmt;
    }

    public static TeiRespStmt load(TeiEditionStmt parent, Element el) {
        return new TeiRespStmt(parent, el);
    }

    public TeiMethodName getMethodName() {
        return methodName;
    }

    public TeiPersName getPersName() {
        return persName;
    }

    public void setParent(TeiEditionStmt parent) {
        this.parent = parent;
    }

    @Override
    public String getElementName() {
        return "respStmt";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        if (persName != null) {
            el.addContent(persName.toXML());
        }
        if (methodName != null) {
            el.addContent(methodName.toXML());
        }
        for (TeiResp e : resps) {
            el.addContent(e.toXML());
        }

        return el;
    }

    @Override
    public void generateDefaultId() {
//        if (parent == null) {
//            // Nothing to do
//        }
        int n = ((TeiEditionStmt) parent).indexOf(this) + 1;
        setId("resp-" + n);
    }

    @Override
    public int getIndex(TeiResp child) {
        return resps.indexOf(child);
    }

    public void addResp(String string) {
        resps.add(new TeiResp(this, string));
    }
}
