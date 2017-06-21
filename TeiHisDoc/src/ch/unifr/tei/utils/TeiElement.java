/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei.utils;

import ch.unifr.tei.TeiHisDoc;
import ch.unifr.tei.teiheader.filedesc.editionstmt.respstmt.TeiRespStmt;
import ch.unifr.tei.utils.postponed.PostponedTask;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * @author Mathias Seuret
 */
public abstract class TeiElement {
    protected static Namespace TeiNS = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
    protected static Namespace NoNS = Namespace.NO_NAMESPACE;
    protected static Namespace XmlNS = Namespace.getNamespace("xml", "http://www.w3.org/XML/1998/namespace");
    protected TeiElement parent = null;
    protected String lang = null;
    protected String resp = null;
    protected String sameAs = null;
    protected Map<String, String> attributes = new HashMap<>();
    protected List<Element> unknownTags = new LinkedList<>();
    protected List<Attribute> unknownAttributes = new LinkedList<>();
    protected List<TeiIgnoredElement> ignoredElements = new LinkedList<>();
    private String id = null;
    
    /**
     * Constructs an element.
     *
     * @param parent element
     */
    public TeiElement(TeiElement parent) {
        this.parent = parent;
        updateResp();
//        setId(getRandomId());
    }
    
    /**
     * Indicates that the element has been deleted. Call it whenever you
     * remove an element, and call also this method for all of its children.
     * Please override it when you need to have another behavior.
     */
    public void notifyDeletion() {
        unregisterId(id);
    }

    /**
     * @return the id, eventually null
     */
    public String getId() {
        return id;
    }

    /**
     * Changes the ID.
     *
     * @param id new id
     */
    public final void setId(String id) {
        if (id==this.id || (id!=null && id.equals(this.id))) {
            return;
        }
        
        if (this.id != null) {
            unregisterId(this.id);
        }

        this.id = id;

        //noinspection ConstantConditions
        if (id != null) {
            registerId(this, id);
        }
    }
    
    public TeiElement getElement(String id) {
        return parent.getElement(id);
    }

    /**
     * When modifying IDs, it might be necessary to attribute a temporary
     * one to an element.
     *
     * @return a random and unique ID
     */
    public String getRandomId() {
        return parent.getRandomId();
    }

    protected void setAttribute(String key, String val) {
        attributes.put(key, val);
    }

    protected String getResp() {
        return resp;
    }

    protected Element getExportElement() {
        Element el = new Element(getElementName(), TeiNS);

        if (id != null) {
            el.setAttribute("id", id, XmlNS);
        }
        if (lang != null) {
            el.setAttribute("lang", lang, XmlNS);
        }
        if (getResp() != null) {
            el.setAttribute("resp", resp, NoNS);
        }
        if (sameAs != null) {
            el.setAttribute("sameAs", sameAs, NoNS);
        }

        for (Map.Entry<String, String> e : attributes.entrySet()) {
            el.setAttribute(e.getKey(), e.getValue(), TeiNS);
        }

        for (TeiIgnoredElement ie : ignoredElements) {
            el.addContent(ie.toXML());
        }

        for (Element e : unknownTags) {
            el.addContent(e.clone().detach());
        }

        return el;
    }

    protected void addIgnoredElement(Element e) {
        if (e == null) {
            return;
        }
        ignoredElements.add(new TeiIgnoredElement(this, e));
    }

    protected void addUnknownElement(Element e) {
        System.err.println("Warning: unexpected element " + e.getName() + " in " + getPath());
        unknownTags.add(e);
        addUnkownTagName(e.getName());
    }

    protected void addUnkownTagName(String name) {
        parent.addUnkownTagName(name);
    }

    protected void addUnknownAttribute(Attribute a) {
        System.err.println("Warning: unexpected attribute " + a.getName() + " in " + getPath());
        unknownAttributes.add(a);
        addUnkownAttributeName(a.getName());
    }

    protected void addUnkownAttributeName(String name) {
        parent.addUnkownAttributeName(name);
    }

    /**
     * Adds as unknown content anything that's in this element.
     *
     * @param el the xml element
     */
    protected void consume(Element el) {
        // Known stuff
        setId(
                consumeAttributeStr(el, "id", XmlNS, false)
        );
        lang = consumeAttributeStr(el, "lang", XmlNS, false);
        resp = consumeAttributeStr(el, "resp", NoNS, false);
        sameAs = consumeAttributeStr(el, "sameAs", NoNS, false);

        // Children elements
        for (Element e : el.getChildren()) {
            if (e.getName().equals("p")) {
                System.err.println("Please don't <p> in TEI files");
            }
            addUnknownElement(e);
        }

        for (Attribute a : el.getAttributes()) {
            addUnknownAttribute(a);
        }
    }

    protected void addContent(Element el, TeiElement e) {
        if (e != null) {
            el.addContent(e.toXML());
        }
    }

    protected void addAttribute(Element el, String name, String val, Namespace ns) {
        if (val != null) {
            el.setAttribute(name, val, ns);
        }
    }

    /**
     * @return the full path of the element
     */
    public String getPath() {
        String pp = (parent == null) ? "" : parent.getPath() + "/";
        if (id != null) {
            return pp + getElementName() + "[xml:id=" + getId() + "]";
        } else {
            return pp + getElementName();
        }
    }

    protected Element consumeChild(Element parent, String name, Namespace ns, boolean isMandatory) {
        Element res = parent.getChild(name, ns);
        if (res != null) {
            parent.removeChild(name, ns);
        } else if (isMandatory) {
            throw new Error("<" + name + "> required in " + getPath());
        }
        return res;
    }

    protected String consumeAttributeStr(Element parent, String name, Namespace ns, boolean isMandatory) {
        Attribute a = parent.getAttribute(name, ns);
        if (a != null) {
            parent.removeAttribute(name, ns);
        } else if (isMandatory) {
            throw new Error("attribute " + name + " required in " + getPath());
        } else {
            return null;
        }
        return a.getValue();
    }

    protected float consumeAttributeFloat(Element parent, String name, Namespace ns, boolean isMandatory) {
        String str = consumeAttributeStr(parent, name, ns, isMandatory);
        float res;

        try {
            res = Float.parseFloat(str);
        } catch (Exception e) {
            throw new Error("non-numeric content in attribute " + name + " from " + getPath());
        }

        return res;
    }

    protected int consumeAttributeInt(Element parent, String name, Namespace ns, boolean isMandatory) {
        String str = consumeAttributeStr(parent, name, ns, isMandatory);
        int res;

        try {
            res = Integer.parseInt(str);
        } catch (Exception e) {
            throw new Error("non-numeric content in attribute " + name + " from " + getPath());
        }

        return res;
    }

    protected void registerId(TeiElement element, String id) {
        parent.registerId(element, id);
    }

    protected void unregisterId(String id) {
        parent.unregisterId(id);
    }

    public boolean idExists(String id) {
        return parent.idExists(id);
    }

    public void generateDefaultId() {
        System.err.println(this.getClass().getSimpleName() + " has no generateDefaultId()");
    }

    public TeiRespStmt getCurrentResponsible() {
        if (parent != null) {
            return parent.getCurrentResponsible();
        } else {
            return null;
        }
    }

    //TODO: (Mathias) 2 The RespStmt should not store a String ID... Also please do not store a RespStmt in each and every tag.

    protected void updateResp() {
        if (getCurrentResponsible() == null) {
            return;
        }
        if (parent.getCurrentResponsible() == getCurrentResponsible()) {
            //no need to store too many RespStmts
            return;
        }
        this.resp = "#" + getCurrentResponsible().getId();
    }

    public File getDirectory() {
        return parent.getDirectory();
    }

    @Override
    public String toString() {
        if (id != null) {
            return id;
        }
        return getElementName();
    }

    /**
     * @return the TeiHisDoc root
     */
    public TeiHisDoc getRoot() {
        return parent.getRoot();
    }

    /**
     * @return the tag name in the xml file
     */
    protected abstract String getElementName();

    /**
     * @return a jdom2 element storing everything about the tei element
     */
    public abstract Element toXML();

    /**
     * Modifies the IDs of the children so that they are ordered correctly.
     */
    public void updateChildrenIDs() {
        // Nothing to do by default
    }
    
    protected void postpone(PostponedTask task) {
        parent.postpone(task);
    }
    
    protected Logger logger() {
        return parent.logger();
    }
}
