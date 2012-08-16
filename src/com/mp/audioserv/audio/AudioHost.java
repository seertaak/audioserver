/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.audio;

import com.mp.audioserv.audio.graph.AudioProcessor;

/**
 * This interface details the services provided by an audio host. Compare
 * to <code>AudioProcessor</code>.
 *
 * @author Martin Percossi
 */
public interface AudioHost extends AudioTransport {
    double time();
    AudioHost time(double time);
    
    int samplePos();
    double sampleRate();
    
    int frame();
    int frameSize();
    
    void setProcessor(AudioProcessor proc);
}
