/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.utils;

/**
 *
 * @author Mathias Seuret
 */
public interface TeiFacsReferrer<T> {
    public void setFacs(T t);
    public T getFacs();
}
