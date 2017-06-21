/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.utils.postponed;

import ch.unifr.tei.utils.TeiElement;
import ch.unifr.tei.utils.TeiFacsReferrer;

/**
 *
 * @author Mathias Seuret
 * @param <T> type of element containing the facs
 * @param <U> type of element reffered to
 */
public class FacsSetter<T extends TeiFacsReferrer<U>,U> implements PostponedTask {
    /**
     * Element containing the facs.
     */
    protected T element;
    
    /**
     * ID in the facs.
     */
    protected String id;
    
    /**
     * Creates a new facs setter.
     * @param element element containing the facs
     * @param id content of the facs attribute
     */
    public FacsSetter(T element, String id) {
        this.element = element;
        this.id = id;
    }
    
    @Override
    public void Execute() {
        
        U f;
        TeiElement e = ((TeiElement)element).getElement(id);
        if (e==null) {
            throw new Error(
                    "cannot find element with ID \""+id+"\""
            );
        }
        
        try {
            f = (U)e;
        } catch (Throwable t) {
            throw new Error(
                    "Type mismatch in facs of "
                            +((TeiElement)element).getPath()
            );
        }
        element.setFacs(f);
    }
    
}
