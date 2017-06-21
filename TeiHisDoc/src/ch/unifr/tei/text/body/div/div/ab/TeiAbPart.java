/*
 * Copyright (c) 2016 - UniFr.
 * DIVA group, University of Fribourg, Switzerland.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.text.body.div.div.ab;

import ch.unifr.tei.text.body.div.div.TagSoupSource;
import org.jdom2.Element;


/**
 *
 * @author Mathias Seuret
 */
public interface TeiAbPart extends TagSoupSource {
    Element toXML();
}
