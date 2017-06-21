/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teihisdoc;

import ch.unifr.tei.text.body.div.div.ab.TeiAb;
import ch.unifr.tei.tools.TranscriptionManager;
import ch.unifr.tei.tools.exception.InvalidTranscriptionException;
import java.io.IOException;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;

/**
 *
 * @author Mathias Seuret
 */
public class TranscriptionTest {
    public static void main(String[] args) throws JDOMException, IOException, InvalidTranscriptionException {
        TranscriptionManager tm = new TranscriptionManager();
        
        TeiAb ab = tm.getStructuredAB(
                null,
                "<w><c>He</c>llo</w> World"
        );
        
        printRec("", ab.toXML());
        
        System.out.println(ab.getTagSoup());
        
        String ts = ab.getTagSoup();
        
        ab = tm.getStructuredAB(
                null,
                ab.getTagSoup()
        );
        
        printRec("", ab.toXML());
        System.out.println(ab.getTagSoup());
        System.out.println(ab.toString());
    }
    
    private static void printRec(String head, Content content) {
        if (content instanceof Text) {
            System.out.println(head+content);
        } else {
            Element e = (Element) content;
            System.out.println(head+e);
            for (Content c : e.getContent()) {
                printRec(head+"  ", c);
            }
        }
    }
}
