/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.audio.graph;

import com.mp.audioserv.audio.AudioHost;
import com.mp.audioserv.ringbuf.MP1CMessageRingBuffer;
import com.mp.bb.audio.plugin.AudioPlugin;
import java.nio.FloatBuffer;

/**
 *
 * @author Martin Percossi
 */
public class GainProcessor extends AudioPlugin {
    
    private int channels;
    private float gain;

    @Override
    public void init(AudioHost host, int id) {
        super.init(host, id);
        this.channels = Math.min(numIns, numOuts);
        gain = 1f;
    }
    
    @Override
    public void process(int samplePos, double time, FloatBuffer in, 
    FloatBuffer out, int N, MP1CMessageRingBuffer inbox, MP1CMessageRingBuffer outbox) 
    {
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < channels; j++) {
                out.put(gain*in.get());
            }
        }
    }

    @Override
    public AudioProcCaps capabilities() {
        return new AudioProcCaps(1, 1, 1024, 1, 1, 1024);
    }

    @Override
    public int latency() {
        return 0;
    }

    
}
