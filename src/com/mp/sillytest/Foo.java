/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.sillytest;

/**
 *
 * @author Martin Percossi
 */
public class Foo implements IFoo {

    @Override
    public float calcSampleIface(final float left, final float right) {
        return 0.5F * (left + right) * (left + right);
    }
    
}
