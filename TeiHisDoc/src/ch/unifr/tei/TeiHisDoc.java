/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

package ch.unifr.tei;

import ch.unifr.tei.facsimile.TeiFacsimile;
import ch.unifr.tei.teiheader.TeiHeader;
import ch.unifr.tei.teiheader.filedesc.editionstmt.respstmt.TeiRespStmt;
import ch.unifr.tei.text.TeiText;
import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.postponed.PostponedTask;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Mathias Seuret
 */
public class TeiHisDoc extends TeiElement {

    /**
     * Current version of the TeiHisDoc. Please update before every
     * release.
     */
    public static final String VERSION = "0.0.2";
    /**
     * Log4j logger
     */
    private static final Logger logger = Logger.getLogger(TeiHisDoc.class);
    // No getter, no setter needed.
    /**
     * Stores the unknown tags met while loading a TeiHisDoc file.
     */
    private final Set<String> unknownTagNames = new HashSet<>();
    // Getter implemented
    // No setter needed

    /**
     * Stores the unknown attribute names met while loading a TeiHisDoc
     * file.
     */
    private final Set<String> unknownAttributeNames = new HashSet<>();
    // Getter implemented
    // No setter needed

    /**
     * Stores the list of all IDs of elements in the TeiHisDoc.
     */
    private final Set<String> registeredIds = new HashSet<>();
    // No getter or setter needed
    
    /**
     * Allows to recover an element from its ID.
     */
    private HashMap<String, TeiElement> idToElement = new HashMap<>();

    /**
     * Stores the header of the TeiHisDoc.
     */
    // getter
    private TeiHeader teiHeader = new TeiHeader(this);
    // Getter implemented
    // No setter needed

    /**
     * Stores the facsimile of the TeiHisDoc.
     */
    // getter
    private TeiFacsimile facsimile = new TeiFacsimile(this);
    // Getter implemented
    // No setter needed

    /**
     * Stores the text of the TeiHisDoc.
     */
    // getter
    private TeiText text = new TeiText(this);
    // Getter implemented
    // No setter needed

    /**
     * Stores a ref to who is responsible for the next changes.
     */
    private TeiRespStmt currentRespStmt;
    // Setter implemented
    // Getter implemented

    /**
     * Directory containing the XML file. This is used for being able
     * to use relative paths when accessing resources.
     */
    private File directory = new File(".");

    /**
     * Stores an integer that can be used for generating (probably) unique
     * IDs. As there is nothing in the TEI format for storing metadata, it
     * is not possible to keep track of unique IDs. A solution is to take
     * the system time in milliseconds at the instance creation, and then
     * increment it for each new ID. We can make the assertion that between
     * two opening of a file, more milliseconds will be elapsed than there
     * will be new IDs created.
     */
    private long randomIdCounter = System.currentTimeMillis();
    
    private List<PostponedTask> postponed = new LinkedList<>();

    /**
     * Create a new TeiHisDoc instance. You have to specify who/what will be
     * responsible for all modifications done to this file. Passing "null" is
     * possible, however not recommended if you do intend to apply modifications
     * to this file. The TeiRespStmt class provides static methods for creating
     * responsibility statements.
     *
     * @param stmt responsibility statement
     */
    public TeiHisDoc(TeiRespStmt stmt) {
        super(null);
        logger.info("TeiHisDoc version " + VERSION);

        if (stmt == null) {
            throw new IllegalArgumentException(
                    "TeiHisDoc instances cannot be created without responsibility"
                            + " statements. Please use static methods from TeiRespStmt to"
                            + " create new TeiRespStmt objects."
            );
        }
        currentRespStmt = getHeader().getFileDesc().getEditionStmt().addRespStmt(stmt);
    }

    /**
     * Writes the TeiHisDoc to an output stream.
     *
     * @param os output stream
     * @throws IOException can be thrown by XMLOutputter.output(…)
     */
    public void write(OutputStream os) throws IOException {
        XMLOutputter output = new XMLOutputter();
        output.setFormat(Format.getPrettyFormat());
        Document doc = new Document(this.toXML());
        output.output(doc, os);
    }

    /**
     * Writes the TeiHisDoc to a Writer.
     *
     * @param w the writer
     * @throws IOException can be thrown by XMLOutputter.output(…)
     */
    public void write(Writer w) throws IOException {
        XMLOutputter output = new XMLOutputter();
        output.setFormat(Format.getPrettyFormat());
        Document doc = new Document(this.toXML());
        output.output(doc, w);
    }

    /**
     * Loads the TeiHisDoc from a file.
     *
     * @param filename the filename
     * @throws JDOMException can be thrown by SAXBuilder.build(…)
     * @throws IOException can be thrown by SAXBuilder.build(…)
     */
    public void load(String filename) throws JDOMException, IOException {
        File f = new File(filename);
        SAXBuilder builder = new SAXBuilder();
        Document xml = builder.build(f);
        load(xml);
        directory = new File(f.getAbsolutePath());
        updateChildrenIDs();
    }

    /**
     * Loads the TeiHisDoc from an XML Document loaded with JDOM.
     *
     * @param xmlDoc the XML document
     */
    private void load(Document xmlDoc) {
        Element root = xmlDoc.getRootElement();

        teiHeader = TeiHeader.load(this, consumeChild(root, "teiHeader", TeiNS, true));
        facsimile = TeiFacsimile.load(this, consumeChild(root, "facsimile", TeiNS, true));
        text = TeiText.load(this, consumeChild(root, "text", TeiNS, true));

        consume(root);

        if (!unknownTagNames.isEmpty()) {
            System.err.println("Warning, some unexpected tags have been met:");
            for (String s : getUnknownTagNames()) {
                System.err.println("\t" + s);
            }
        }

        if (!unknownAttributeNames.isEmpty()) {
            System.err.println("Warning, some unexpected attributes have been met:");
            for (String s : unknownAttributeNames) {
                System.err.println("\t" + s);
            }
        }

        currentRespStmt = setRespStmt(currentRespStmt);
        
        for (PostponedTask task : postponed) {
            task.Execute();
        }
    }

    /**
     * Indicates that a tag with an unknown name has been met. It will be
     * reported in the standard error output.
     *
     * @param name the unkown tag name
     */
    @Override
    protected void addUnkownTagName(String name) {
        getUnknownTagNames().add(name);
    }

    /**
     * Indicates that an attribute with an unknown name has been met. It will
     * be reported in the standard error output.
     *
     * @param name the unkown attribute name
     */
    @Override
    protected void addUnkownAttributeName(String name) {
        unknownAttributeNames.add(name);
    }

    /**
     * @return an XML representation of the TEIHisDoc file
     */
    @Override
    public Element toXML() {
        Element el = getExportElement();

        el.addContent(teiHeader.toXML());
        el.addContent(facsimile.toXML());
        el.addContent(text.toXML());

        return el;
    }

    /**
     * @return the name of the element
     */
    @Override
    protected String getElementName() {
        return "TEI";
    }

    /**
     * Registers the ID. As it does not propagate the change to a parent,
     * it must override the method.
     *
     * @param id the id to be registered
     */
    @Override
    protected void registerId(TeiElement e, String id) {
        if (registeredIds.contains(id)) {
            throw new Error("the ID \"" + id + "\" seems to be used by several elements");
        }
        registeredIds.add(id);
        idToElement.put(id, e);
    }

    /**
     * Unregisters the ID, i.e., indicates that it is now free for use.
     *
     * @param id the id to be unregistered
     */
    @Override
    protected void unregisterId(String id) {
        registeredIds.remove(id);
        idToElement.remove(id);
    }
    
    /**
     * @param id an element ID
     * @return the element corresponding to the id
     */
    @Override
    public TeiElement getElement(String id) {
        if (id!=null && id.startsWith("#")) {
            return idToElement.get(id.substring(1, id.length()));
        } else {
            return idToElement.get(id);
        }
    }

    @Override
    public void updateChildrenIDs() {
        this.facsimile.updateChildrenIDs();
        this.teiHeader.updateChildrenIDs();
        this.text.updateChildrenIDs();
    }
    
    
    /*
        Accessors
    */

    /**
     * @return the headers of the file
     */
    public TeiHeader getHeader() {
        return teiHeader;
    }

    /**
     * @return information about the elements of the document
     */
    public TeiFacsimile getFacsimile() {
        return facsimile;
    }

    /**
     * @return the transcription of the document
     */
    public TeiText getText() {
        return text;
    }

    /**
     * @param id any string
     * @return true if any element of the document has this ID
     */
    @Override
    public boolean idExists(String id) {
        return registeredIds.contains(id);
    }

    /**
     * When modifying IDs, it might be necessary to attribute a temporary
     * one to an element.
     *
     * @return a random and unique ID
     */
    @Override
    public String getRandomId() {
        String rndId = "rand-" + (++randomIdCounter);
        while (idExists(rndId)) {
            rndId = "rand-" + (++randomIdCounter);
        }
        return rndId;
    }

    @Override
    public void generateDefaultId() {
        // Nothing to do
    }

    public TeiRespStmt getCurrentResponsible() {
        return currentRespStmt;
    }

    public TeiRespStmt setRespStmt(TeiRespStmt s) {
        for (TeiRespStmt stmt : getHeader().getFileDesc().getEditionStmt().getRespStmts()) {
            if (stmt == s) {
                currentRespStmt = stmt;
                return stmt;
            }
        }
        currentRespStmt = getHeader().getFileDesc().getEditionStmt().addRespStmt(s);
        return currentRespStmt;
    }

    @Override
    public File getDirectory() {
        return directory;
    }

    public void changeDirectory(String dir) {
        directory = new File(dir);
    }

    /**
     * If a Tei-HisDoc file contains unknown tags, it is displayed in the
     * standard error output. These tags are also stored in a set which
     * is accessible through this method.
     *
     * @return the set containing unknown tag names
     */
    public Set<String> getUnknownTagNames() {
        return unknownTagNames;
    }

    /**
     * If a Tei-HisDoc file contains unknown attributes, it is displayed in the
     * standard error output. These attributes are also stored in a set which
     * is accessible through this method.
     *
     * @return the set containing unknown attribute names
     */
    public Set<String> getUnknownAttributeNames() {
        return unknownAttributeNames;
    }

    @Override
    public TeiHisDoc getRoot() {
        return this;
    }
    
    public void postpone(PostponedTask task) {
        postponed.add(task);
    }
    
    protected Logger logger() {
        return logger;
    }
}
