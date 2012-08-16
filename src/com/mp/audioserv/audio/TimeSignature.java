/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.audio;

/**
 *
 * @author Martin Percossi
 */
public class TimeSignature {
    private static final TimeSignature STD = new TimeSignature(4, 4);
    
    private int numerator;
    private int denominator;
    
    public TimeSignature(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int getDenominator() {
        return denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TimeSignature other = (TimeSignature) obj;
        if (this.numerator != other.numerator) {
            return false;
        }
        if (this.denominator != other.denominator) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.numerator;
        hash = 17 * hash + this.denominator;
        return hash;
    }

    @Override
    public String toString() {
        return "TimeSignature[" + numerator + "/" + denominator + "]";
    }
    
    public static TimeSignature std() {
        return STD;
    }
    
    public static void main(String...args) {
        System.out.println(TimeSignature.STD);
    }
    
}
