/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.ringbuf;

import com.mp.msg.ByteBufferMessage;
import com.mp.msg.Message;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A ring buffer in which we claim chunks rather that individual slots.
 *
 * @author Martin Percossi
 */
public class MPMCMessageRingBuffer {
 
    private static final int MAX_MSG_SIZE = Message.MAX_SIZE;
    public static final int MIN_SUPPORTED_MESSAGES = 32768;
    public static final int BUF_SIZE = MIN_SUPPORTED_MESSAGES*MAX_MSG_SIZE;
    public static final long BUF_SIZE_M1 = BUF_SIZE - 1;
    
    private ByteBuffer buffer;
    private ThreadLocal<ByteBuffer> threadLocalBufferView;
    
    private AtomicLong long start;
    private volatile long dataEnd;
    private AtomicLong claimEnd;
    
    public MPMCMessageRingBuffer() {
        buffer = ByteBuffer.allocateDirect(BUF_SIZE+MAX_MSG_SIZE);  // ~128MB.
        threadLocalBufferView = new ThreadLocal<ByteBuffer>() {
            @Override
            protected ByteBuffer initialValue() {
                return buffer.slice();
            }
        };
        claimEnd = new AtomicLong(0L);
        start = 0;
        dataEnd = 0;
    }

    public void advance(int delta) {
        this.start += delta;
    }
    
    public ByteBuffer buffer() {
        return threadLocalBufferView.get();
    }
    
    public static class Chunk {
        public long start;
        public int size;
        public void copyFrom(Chunk chunk) {
            this.start = chunk.start;
            this.size = chunk.size;
        }
    }
    
    public boolean claimChunk(Chunk msg) {
        msg.start = claimEnd.getAndAdd(msg.size);
        
        if (msg.start - start >= BUF_SIZE)
            return false;
        
        int ix = (int) (msg.start & BUF_SIZE_M1);
        threadLocalBufferView.get().position(ix);
        
        return true;
    }
    
    public void commitChunk(Chunk msg) {
        while (dataEnd != msg.start) {
        }
        dataEnd = msg.start + msg.size;
    }
    
    public boolean peek(ByteBufferMessage msg) {
        //System.out.println("Calling peek");
        if (dataEnd > start) {
            int ix = (int) (start & BUF_SIZE_M1);
            buffer.position(ix);
            msg.setBuffer(buffer);
            return true;
        } else {
            //System.out.println("No data.");
            return false;
        }
    }
    
    public void printDebug() {
        System.out.println("RingBuffer[start=" + start
                + ", dataEnd=" + dataEnd + ", claimEnd=" + claimEnd.get());
    }
    
    public void seek(int pos) {
        this.start = pos;
    }
}
