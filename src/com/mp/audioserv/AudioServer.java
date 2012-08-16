/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv;

import com.mp.audioserv.audio.AudioEngine;
import com.mp.audioserv.audio.DummyAudioEngine;
import com.mp.audioserv.netmsg.SocketMessageThread;
import com.mp.audioserv.ringbuf.MP1CMessageRingBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author Martin Percossi
 */
public class AudioServer {
    
    public static final int AUDIO_SCORE_PORT = 4243;
    public static final int AUDIO_CLIENT_PORT = 4242;
    
    private AudioEngine engine;
    private ExecutorService threadPool;
    private ServerSocketChannel scoreServerChannel;
    private ServerSocketChannel clientServerChannel;
    
    private MP1CMessageRingBuffer inbox;
    private MP1CMessageRingBuffer outbox;
    private MP1CMessageRingBuffer score;
    
    public AudioServer() {
        inbox = new MP1CMessageRingBuffer();
        outbox = new MP1CMessageRingBuffer();
        score = new MP1CMessageRingBuffer();
        
        engine = new DummyAudioEngine();
        threadPool = Executors.newCachedThreadPool();
    }
    
    public void start() {
        System.out.println("Initializing audio engine...");
        engine.init(this);
        System.out.println("Initializing audio engine...done.");
        
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Creating score message socket...");
                    scoreServerChannel = ServerSocketChannel.open()
                            .bind(new InetSocketAddress(AUDIO_SCORE_PORT));
                    System.out.println("Creating score message socket...done.");
                    System.out.println(
                            "Launching score message socket thread...");
                    SocketChannel channel = scoreServerChannel.accept();
                    threadPool.execute(new SocketMessageThread(true, 
                            AudioServer.this, channel));
                    System.out.println(
                            "Launching score message socket thread...done.");
                } catch (IOException ex) {
                    throw new AudioServerException(ex);
                }
            }
        });

        
        try {
            System.out.println("Creating client message socket...");
            clientServerChannel = ServerSocketChannel.open()
                    .bind(new InetSocketAddress(AUDIO_CLIENT_PORT));
            System.out.println("Creating client message socket...done.");
        } catch (IOException ex) {
            throw new AudioServerException(ex);
        }
        
        // any number of (GUI) clients can connect.
        while (true) {
            try {
                System.out.println("Connection requested...accepting socket");
                //Socket socket = clientServerSocket.accept();
                SocketChannel channel = clientServerChannel.accept();
                threadPool.execute(new SocketMessageThread(this, channel));
                System.out.println("Connection requested...dispatched.");
            } catch (IOException ex) {
                throw new AudioServerException(ex);
            }
        }
    }
    
    public MP1CMessageRingBuffer inbox() {
        return inbox;
    }
    
    public MP1CMessageRingBuffer outbox() {
        return outbox;
    }
   
    public MP1CMessageRingBuffer score() {
        return score;
    }
    
    public static void main(String...args) {
        AudioServer server = new AudioServer();
        server.start();
    }
}
