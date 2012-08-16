/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.sillytest;

/**
 *
 * @author Martin Percossi
 */
public class Foo2 implements IFoo {

    public float calcSampleVirtual(final float left, final float right) {
        return 0.5F * (left + right) * (left + right);
    }

    @Override
    public float calcSampleIface(float left, float right) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
