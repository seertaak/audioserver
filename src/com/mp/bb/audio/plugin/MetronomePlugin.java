/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.bb.audio.plugin;

import com.mp.audioserv.audio.AudioHost;
import com.mp.audioserv.audio.graph.AudioProcCaps;
import com.mp.audioserv.ringbuf.MP1CMessageRingBuffer;
import java.nio.FloatBuffer;

public class MetronomePlugin extends AudioPlugin {
    
    private float[] wnhat;
    
    public static final AudioProcCaps CAPS 
            = new AudioProcCaps(0, 0, 0, 2, 2, 2);

    @Override
    public AudioProcCaps capabilities() {
        return CAPS;
    }

    @Override
    public int latency() {
        return 0;
    }
    
    @Override
    public void init(AudioHost host, int id) {
        super.init(host, id);
        wnhat = new float[(int) host.sampleRate() / 10];
        
        for (int i = 0; i < wnhat.length; i++) {
            float x = (1.f - (float) i / (float) wnhat.length);
            wnhat[i] = 0.5f * ((float) Math.random() - 0.5f) * x * x;
        }
    }

    @Override
    public void process(int samplePos, double time, FloatBuffer in, 
        FloatBuffer out, int N, MP1CMessageRingBuffer inbox, 
        MP1CMessageRingBuffer outbox) 
    {
        out.rewind();
        for (int i = 0; i < N; i++)
            out.put(0).put(0);
        int beat = (int) time;
        int beatStart = (int) host.beatsToSamples(beat);
        int nextBeatStart = (int) host.beatsToSamples(beat + 1);
        int clickEnd = beatStart + wnhat.length;
        out.rewind();
        if (samplePos < clickEnd) {
            for (int i = 0; i < N; i++) {
                int index = i + samplePos - beatStart;
                if (index >= 0 && index < wnhat.length) {
                    out.put(2*i, wnhat[index]).put(2*i+1,wnhat[index]);
                }
            }
        } else if (samplePos + N > nextBeatStart) {
            for (int i = 0; i < N; i++) {
                int index = i + samplePos - nextBeatStart;
                if (index >= 0 && index < wnhat.length) {
                    out.put(2*i, wnhat[index]).put(2*i+1,wnhat[index]);
                }
            }
        }
    }
    
}
