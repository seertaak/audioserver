package com.mp.audioserv.audio.graph;

import com.mp.audioserv.audio.AudioHost;
import com.mp.audioserv.ringbuf.ChunkRingBuffer;
import com.mp.audioserv.ringbuf.MP1CMessageRingBuffer;
import java.io.Serializable;
import java.nio.FloatBuffer;

/**
 * This is the interface for units that can generate and process audio or 
 * messages. As such, it details only the obligations of the 
 * <code>AudioProcessor</code>.
 * To get a complete view, one must also consider the obligations of the 
 * <code>AudioHost</code>.
 * @author Martin Percossi
 */
public interface AudioProcessor extends Serializable {

    AudioProcCaps capabilities();
    
    int numIns();
    AudioProcessor numIns(int ins);
    int numOuts();
    AudioProcessor numOuts(int outs);
    
    void address(String address);
    String address();
    
    /**
     * The latency in samples introduced through this processor.
     */
    int latency();
    
    void init(AudioHost host, int id);

    /**
     * Process a frame of audio. You must be *very* careful about how you write
     * this method.
     * 
     * @param in - the input audio data, in interleaved format.
     * @param out - the buffer into which to put the output data, in interleaved 
     * form.
     * @param N
     * @param inbox
     * @param outbox 
     */
    void process(int samplePos, double time, FloatBuffer in, FloatBuffer out, 
            int N, ChunkRingBuffer inbox, MP1CMessageRingBuffer outbox);

    void release();
}
