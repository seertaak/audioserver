/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.audio;

/**
 * 
 * This interface represents the functionality offered by a simple transport.
 *
 * @author Martin Percossi
 */
public interface AudioTransport {
    boolean isPlaying();
    void play();
    void stop();
    void bpm(double bpm);
    double bpm();
    TimeSignature meter();
    void meter(TimeSignature meter);
    
    double beatsToSamples(double beat);
    double samplesToBeats(int sample);
}
