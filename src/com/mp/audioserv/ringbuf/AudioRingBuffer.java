/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.ringbuf;

import com.mp.msg.ByteBufferMessage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * 
 * @author Martin Percossi
 */
public class AudioRingBuffer {
    
    private final int MAX_FRAME_SIZE = 1<<14;     // 16KB
    private final int BUF_SIZE = 1<<24;    // 16MB
    private final long BUF_SIZE_M1 = BUF_SIZE - 1;

    private FloatBuffer buffer;
    private ThreadLocal<FloatBuffer> producerBuffer;
    
    private volatile long start;
    private volatile long dataEnd;
    private volatile long claimEnd;
    
    public AudioRingBuffer() {
        buffer = ByteBuffer.allocateDirect(BUF_SIZE+MAX_FRAME_SIZE)
                .asFloatBuffer();
        producerBuffer = new ThreadLocal<FloatBuffer>() {
            @Override
            protected FloatBuffer initialValue() {
                return buffer.slice();
            }
        };
        claimEnd = 0;
        start = 0;
        dataEnd = 0;
    }

    public void advance(int delta) {
        this.start += delta;
    }
    
    public static class Chunk {
        public FloatBuffer contents;
        public long start;
        public int size;
    }
    
    public boolean claimChunk(Chunk chunk) {
        chunk.start = claimEnd;
        claimEnd += chunk.size;
        
        if (chunk.start - start >= BUF_SIZE)
            return false;
        
        int ix = (int) (chunk.start & BUF_SIZE_M1);
        producerBuffer.get().position(ix);
        chunk.contents = producerBuffer.get();
        
        return true;
    }
    
    public void commitChunk(MP1CMessageRingBuffer.Chunk msg) {
        while (dataEnd != msg.start) {
        }
        dataEnd = msg.start + msg.size;
    }
    
    public boolean peek(Chunk chunk) {
        //System.out.println("Calling peek");
        if (dataEnd > start) {
            int ix = (int) (start & BUF_SIZE_M1);
            buffer.position(ix);
            //chunk.setBuffer(buffer);
            return true;
        } else {
            //System.out.println("No data.");
            return false;
        }
    }
    
    public void printDebug() {
//        System.out.println("RingBuffer[start=" + start
//                + ", dataEnd=" + dataEnd + ", claimEnd=" + claimEnd.get());
    }
    
    public void seek(int pos) {
        this.start = pos;
    }

}
