/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.teiheader.filedesc.sourcedesc.msdesc.physdesc.handdesc.handnote;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiTextElement;
import org.jdom2.Element;

/**
 * @author ms
 */
public class TeiPersName extends TeiTextElement {
    private String persName; //fotini
    private String key;
    private String ref;
    private String role;
    private String cert;

    public TeiPersName(TeiElement parent) {
        super(parent);
    }

    private TeiPersName(TeiHandNote parent, Element el) {
        this(parent);
        setContent(el.getText());

        persName = el.getTextTrim(); //fotini

        key = consumeAttributeStr(el, "key", NoNS, false);
        ref = consumeAttributeStr(el, "ref", NoNS, false);
        role = consumeAttributeStr(el, "role", NoNS, false);
        cert = consumeAttributeStr(el, "cert", NoNS, false);

        consume(el);
    }

    public static TeiPersName load(TeiHandNote parent, Element el) {
        return new TeiPersName(parent, el);
    }

    @Override
    public String getElementName() {
        return "persName";
    }

    @Override
    public Element toXML() {
        Element el = getExportElement();

        addAttribute(el, "key", key, NoNS);
        addAttribute(el, "ref", ref, NoNS);
        addAttribute(el, "role", role, NoNS);
        addAttribute(el, "cert", cert, NoNS);

        el.setText(persName);//fotini

        return el;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    /* ********* ACCESSORS ********** */

    /**
     * Getter for property 'cert'.
     *
     * @return Value for property 'cert'.
     */
    public String getCert() {
        return cert;
    }

    /**
     * Setter for property 'cert'.
     *
     * @param cert Value to set for property 'cert'.
     */
    public void setCert(String cert) {
        this.cert = cert;
    }

    /**
     * Getter for property 'key'.
     *
     * @return Value for property 'key'.
     */
    public String getKey() {
        return key;
    }

    /**
     * Setter for property 'key'.
     *
     * @param key Value to set for property 'key'.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Getter for property 'ref'.
     *
     * @return Value for property 'ref'.
     */
    public String getRef() {
        return ref;
    }

    /**
     * Setter for property 'ref'.
     *
     * @param ref Value to set for property 'ref'.
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
     * Getter for property 'role'.
     *
     * @return Value for property 'role'.
     */
    public String getRole() {
        return role;
    }

    /**
     * Setter for property 'role'.
     *
     * @param role Value to set for property 'role'.
     */
    public void setRole(String role) {
        this.role = role;
    }

}
