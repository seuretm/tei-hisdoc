/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.tools;

import ch.unifr.tei.text.body.div.div.TeiDivDiv;
import ch.unifr.tei.text.body.div.div.ab.TeiAb;
import ch.unifr.tei.text.body.div.div.ab.TeiAbContainer;
import ch.unifr.tei.text.body.div.div.ab.TeiAbPart;
import ch.unifr.tei.text.body.div.div.ab.TeiAbPartContainer;
import ch.unifr.tei.text.body.div.div.ab.TeiAbSeg;
import ch.unifr.tei.text.body.div.div.ab.TeiC;
import ch.unifr.tei.text.body.div.div.ab.TeiG;
import ch.unifr.tei.text.body.div.div.ab.TeiL;
import ch.unifr.tei.text.body.div.div.ab.TeiLb;
import ch.unifr.tei.text.body.div.div.ab.TeiPc;
import ch.unifr.tei.text.body.div.div.ab.TeiW;
import ch.unifr.tei.text.body.div.div.ab.TeiWPartContainer;
import ch.unifr.tei.text.body.div.div.ab.TeiWSeg;
import ch.unifr.tei.tools.exception.InvalidTranscriptionException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author Mathias Seuret
 */
public class TranscriptionManager {
    private static String[] punctuationList = {
        ".",
        ",",
        ":",
        ";",
        "?",
        "!"
    }; // TODO 2 : load these from a settings file
    private HashSet<Integer> punctuation = new HashSet<>();
    private final int space = Character.codePointAt(" ", 0);
    private final int lineBreak = '\n';
    
    public TranscriptionManager() {
        for (String str : punctuationList) {
            ArrayList<Integer> cps = getCodePoints(str);
            if (cps.size()!=1) {
                throw new Error("punctuation of two codepoints not supported yet");
            }
            punctuation.add(cps.get(0));
        }
    }
    
    public TeiAb getStructuredAB(TeiAbContainer parent, String transcription) throws JDOMException, IOException, InvalidTranscriptionException {
        TeiAb ab = new TeiAb(parent);
        fill(ab, transcription);
        return ab;
    }
    
    public TeiL getStructuredL(TeiAbContainer parent, String transcription) throws JDOMException, IOException, InvalidTranscriptionException {
        TeiL l = new TeiL(parent);
        fill(l, transcription);
        return l;
    }
    
    private void fill(TeiAbPartContainer abc, String transcription) throws JDOMException, IOException, InvalidTranscriptionException {
        Element root = stringToElement(transcription, "ab");
        fill((TeiAb)abc, root);
    }
    
    private void fill(TeiAbPartContainer abc, Element root) throws InvalidTranscriptionException {
        for (Content content : root.getContent()) {
            if (content instanceof Element) {
                Element e = (Element)content;
                switch (e.getName()) {
                    case "w":
                        addW(abc, e);
                        break;
                    case "seg":
                        addAbSeg(abc, e);
                        break;
                    case "lb":
                        abc.add(new TeiLb(abc));
                        break;
                    case "pc":
                        abc.add(new TeiPc(abc, e.getText()));
                        break;
                    default:
                        throw new InvalidTranscriptionException("Unexpected tag "+e.getName());
                }
                
            } else {
                Text text = (Text)content;
                String str = text.getText();
                
                ArrayList<Integer> cps = getCodePoints(str);
                ArrayList<Integer> store = new ArrayList<>();
                
                for (int cp : cps) {
                    if (cp=='\r') {
                        continue; // Let's ignore uselesses Windows carriage returns
                    }
                    if (cp==space) {
                        if (store.size()>0) {
                            abc.add(new TeiW(abc, store));
                            store.clear();
                        }
                        abc.add(new TeiC(abc, " "));
                        continue;
                    }
                    
                    if (cp==space) {
                        if (store.size()>0) {
                            abc.add(new TeiW(abc, store));
                            store.clear();
                        }
                        abc.add(new TeiLb(abc));
                        continue;
                    }
                    
                    if (punctuation.contains(cp)) {
                        if (store.size()>0) {
                            abc.add(new TeiW(abc, store));
                            store.clear();
                        }
                        abc.add(new TeiPc(abc, new String(Character.toChars(cp))));
                        continue;
                    }
                    
                    store.add(cp);
                }
                if (store.size()>0) {
                    abc.add(new TeiW(abc, store));
                    store.clear();
                }
            }
        }
    }
    
    private static void addW(TeiAbPartContainer ab, Element e) throws InvalidTranscriptionException {
        TeiW w = new TeiW(ab);
        fillW(w, e);
        if (w.countParts()!=0) {
            ab.add(w);
        }
    }
    
    private static void fillW(TeiWPartContainer wpc, Element e) throws InvalidTranscriptionException {
        for (Content content : e.getContent()) {
            if (content instanceof Element) {
                Element el = (Element)content;
                switch (el.getName()) {
                    case "c":
                        wpc.add(
                                new TeiC(wpc, el.getText())
                        );
                        break;
                    case "g":
                        wpc.add(
                                new TeiG(wpc, el.getText())
                        );
                        break;
                    case "lb":
                        wpc.add(
                                new TeiLb(wpc)
                        );
                        break;
                    case "seg":
                        if (!(wpc instanceof TeiW)) {
                            throw new InvalidTranscriptionException("<seg> can appear only in <ab>, <l> and <w>");
                        }
                        TeiW w = (TeiW)wpc;
                        TeiWSeg seg = new TeiWSeg(w);
                        w.add(seg);
                        fillW(seg, el);
                        break;
                    default:
                        throw new InvalidTranscriptionException("Unexpected <"+el.getName()+"> in <w>");
                }
            } else {
                
                // Adding characters
                Text text = (Text)content;
                System.out.println("addW:text("+text.getText()+")");
                String str = text.getText();
                for (int codepoint : getCodePoints(str)) {
                    wpc.add(
                            new TeiC(
                                    wpc,
                                    new String(Character.toChars(codepoint))
                            )
                    );
                }
            }
        }
    }
    
    public void addAbSeg(TeiAbPartContainer abc, Element e) throws InvalidTranscriptionException {
        if (! (abc instanceof TeiAb)) {
            throw new InvalidTranscriptionException("<seg> can appear only in <ab>, <l> and <w>");
        }
        TeiAb ab = (TeiAb)abc;
        TeiAbSeg seg = new TeiAbSeg(ab);
        ab.add(seg);
        fill(seg, e);
    }
    
    private static Element stringToElement(String str, String elementName) throws JDOMException, IOException {
        str = str.trim();
        if (! str.startsWith("<"+elementName+">")) {
            str = "<"+elementName+">"+str+"</"+elementName+">";
        }
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(new ByteArrayInputStream(str.getBytes("UTF-8")));
        return doc.getRootElement();
    }
    
    private static ArrayList<Integer> getCodePoints(String str) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int cp, o = 0; o < str.length(); o += Character.charCount(cp)) {
            cp = str.codePointAt(o);
            res.add(cp);
        }
        return res;
    }
    
    public String getTagSoup(TeiAb ab) {
        StringBuilder sb = new StringBuilder();
        
        for (TeiAbPart abp : ab.getParts()) {
            appendTagSoup(sb, abp);
        }
        
        return sb.toString();
    }
    
    private void appendTagSoup(StringBuilder sb, TeiAbPart abp) {
        if (abp instanceof TeiC) {
            TeiC c = (TeiC) abp;
            if (c.getText().length()==1) {
                sb.append(c.getText());
            } else {
                sb.append("<c>").append(c.getText()).append("</c>");
            }
        }
    }
}
