/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.ringbuf;

import com.mp.msg.ByteBufferMessage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * Ideally this will be a multiple-producer, single-consumer ring buffer.
 *
 * @author Martin Percossi
 */
public class FixedSizeMessageRingBuffer {
    
    private final int MAX_MSG_SIZE = 4096;
    private final int BUF_SIZE = 32768;
    private final long BUF_SIZE_M1 = BUF_SIZE - 1;
    
    private volatile long start;
    private volatile long consumerEnd;   // volatile, shared between prod and cons
    private AtomicLong producerEnd;  // shared between producers
    private ByteBufferMessage[] entries;
    
    public FixedSizeMessageRingBuffer() {
        entries = new ByteBufferMessage[BUF_SIZE];
    }
    
    public static class MsgSlot {
        public ByteBufferMessage msg;
        public long slot;
    }
    
    public void claimSlot(MsgSlot msgSlot) {
        msgSlot.slot = producerEnd.getAndIncrement();
        while (msgSlot.slot - start > BUF_SIZE_M1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        msgSlot.msg = entries[(int) (msgSlot.slot & BUF_SIZE_M1)];
    }
    
    public void commitSlot(MsgSlot slot) {
        while (consumerEnd != slot.slot-1) {
        }
        this.consumerEnd = slot.slot;
    }
    
    public void peekMsg(ByteBufferMessage msg) {
        
    }
    
    public void drainMsgs(int n) {
        start += n;
    }

    
}
