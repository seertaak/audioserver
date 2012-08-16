/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.audio.graph;

import com.mp.audioserv.audio.AudioHost;
import com.mp.audioserv.ringbuf.MP1CMessageRingBuffer;
import java.nio.FloatBuffer;

/**
 *
 * @author Martin Percossi
 */
public class AudioGraphIOProcessor implements AudioProcessor {
    
    private Type type;
    private AudioProcessorGraph graph;
    

    @Override
    public AudioProcCaps capabilities() {
        if (type == Type.Input) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public int numIns() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AudioProcessor numIns(int ins) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int numOuts() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AudioProcessor numOuts(int outs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void address(String address) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String address() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int latency() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void init(AudioHost host, int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void release() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void process(int samplePos, double time, FloatBuffer in, 
    FloatBuffer out, int N, MP1CMessageRingBuffer inbox, MP1CMessageRingBuffer outbox) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static enum Type {
        Input, Output
    }
    
    public Type type() {
        return type;
    }
    
    
    
}
