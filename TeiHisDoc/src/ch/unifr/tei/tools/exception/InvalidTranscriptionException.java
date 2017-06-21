/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unifr.tei.tools.exception;

/**
 *
 * @author Mathias Seuret
 */
public class InvalidTranscriptionException extends Exception {
    public InvalidTranscriptionException(String cause) {
        super(cause);
    }
}
