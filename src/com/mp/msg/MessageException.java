/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.msg;

/**
 *
 * @author Martin Percossi
 */
public class MessageException extends RuntimeException {

    public MessageException(String string) {
        super(string);
    }

    public MessageException(Throwable e) {
        super(e);
    }
    
}
