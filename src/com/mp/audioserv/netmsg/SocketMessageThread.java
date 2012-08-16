/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.netmsg;

import com.mp.audioserv.AudioServer;
import com.mp.audioserv.AudioServerException;
import com.mp.audioserv.ringbuf.MP1CMessageRingBuffer;
import com.mp.msg.Message;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

/**
 *
 * @author Martin Percossi
 */
public class SocketMessageThread implements Runnable {
    public static final String UNEXPECTED_END_OF_DATA 
            = "Unexpected end of data";
    
    private ByteBuffer tmpbuf;
    
    private final MP1CMessageRingBuffer inbox;
    private final MP1CMessageRingBuffer outbox;
    private final MP1CMessageRingBuffer score;
    private final SocketChannel channel;
    private final boolean isScoreThread;
    
    private byte[] msgbuf;
    
    public SocketMessageThread(AudioServer server, SocketChannel channel) {
        this(false, server, channel);
    }

    public SocketMessageThread(boolean isScoreThread, AudioServer server, 
            SocketChannel channel) 
    {
        this.isScoreThread = isScoreThread;
        this.tmpbuf = ByteBuffer.allocate(20);  // enough for size, guid, time.
        this.msgbuf = new byte[Message.MAX_SIZE];
        this.inbox = server.inbox();
        this.outbox = server.outbox();
        this.score = server.score();
        this.channel = channel;
    }

    @Override
    public void run() {
        System.out.println((isScoreThread ? "Score" : "Client") 
                + " network socket thread started.");
        try (DataInputStream dis = 
                new DataInputStream(Channels.newInputStream(channel)))
        {
            MP1CMessageRingBuffer.Chunk chunk = new MP1CMessageRingBuffer.Chunk();
            MP1CMessageRingBuffer ringbuf;
            do {
//                System.out.println("Channel:" + channel);
//                System.out.println("TmpBuf:" + tmpbuf);
//                System.out.println("Yo 1");
                tmpbuf.clear();
                while (tmpbuf.hasRemaining())
                    channel.read(tmpbuf);
                
                tmpbuf.flip();
                chunk.size = tmpbuf.getInt();
                tmpbuf.position(12);
                
                if (isScoreThread) {
                    double time = tmpbuf.getDouble();
                
                    if (Double.isNaN(time)) {
                        ringbuf = inbox;
                    } else {
                        ringbuf = score;
                    }
                } else {
                    ringbuf = inbox;
                }
                
                  // claim a chunk of the ring buffer for exclusive use by us.
                ringbuf.claimChunk(chunk);

                tmpbuf.position(0);
                
                ByteBuffer buf = ringbuf.buffer();
                // stuff the message into the chunk.
                buf.position((int)(chunk.start&MP1CMessageRingBuffer.BUF_SIZE_M1));
                buf.put(tmpbuf);
                int nread = 0;
                int total = chunk.size - 20;
                while (nread < total) {
                    int n = dis.read(msgbuf, nread, total-nread);
                    if (n < 0)
                        throw new AudioServerException(UNEXPECTED_END_OF_DATA);
                    nread += n;
                    //System.out.println("Read " + nread + " of " + chunk.size);
                }
                ringbuf.buffer().put(msgbuf, 0, total);
                if (nread > chunk.size)
                    throw new AudioServerException(String.valueOf(nread));
                
                // we now advertise that we're done adding the message, this
                // this blocks until we're next in the "commit queue" (which
                // is not a real queue, just a sequence).
                //System.out.println("Committing chunk");
                ringbuf.commitChunk(chunk);
                
                // at this point, the message is visible to the audio thread.
                // huzzah!
            } while (true);
        } catch (IOException e) {
            throw new AudioServerException(e);
        }
    }
}
