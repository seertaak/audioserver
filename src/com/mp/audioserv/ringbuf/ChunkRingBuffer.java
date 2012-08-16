/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.ringbuf;

import com.mp.audioserv.ringbuf.MP1CMessageRingBuffer.Chunk;
import com.mp.msg.ByteBufferMessage;
import java.nio.ByteBuffer;

/**
 *
 * @author Martin
 */
public class ChunkRingBuffer {
    
    private static final int BUF_SIZE = 32768;
    private static final int BUF_SIZE_M1 = BUF_SIZE-1;
   
    private Chunk[] entries;
    private long start;
    private long end;
    private MP1CMessageRingBuffer msgBuffer;
    
    public ChunkRingBuffer(MP1CMessageRingBuffer msgBuffer) {
        entries = new Chunk[BUF_SIZE];
        start = end = 0;
        this.msgBuffer = msgBuffer;
    }
    
    public void add(Chunk chunk) {
        entries[(int)(end & BUF_SIZE_M1)].copyFrom(chunk);
        end++;
        while (end - start > BUF_SIZE_M1-1)
            start++;
    }
    
    public boolean peek(ByteBufferMessage out) {
        if (start == end)
            return false;
        Chunk chunk = entries[(int)(start&BUF_SIZE_M1)];
        ByteBuffer buffer = msgBuffer.buffer();
        buffer.position(
                (int)(chunk.start&MP1CMessageRingBuffer.BUF_SIZE_M1));
        out.setBuffer(buffer);
        return true;
    }
    
    public void pop() {
        if (start == end)
            throw new IllegalStateException();
        start++;
    }
}
