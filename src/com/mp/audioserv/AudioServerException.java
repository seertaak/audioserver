/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv;

/**
 *
 * @author Martin Percossi
 */
public class AudioServerException extends RuntimeException {

    public AudioServerException(Throwable ex) {
        super(ex);
    }

    public AudioServerException(String msg) {
        super(msg);
    }
    
}
