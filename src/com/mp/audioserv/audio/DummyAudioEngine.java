/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.audio;

import com.mp.audioserv.AudioServer;
import com.mp.audioserv.audio.graph.AudioProcessor;
import com.mp.audioserv.ringbuf.MP1CMessageRingBuffer;
import com.mp.msg.ByteBufferMessage;
import java.nio.FloatBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Martin Percossi
 */
public class DummyAudioEngine implements AudioEngine, Runnable {
    public static final int SLEEP_TIME = 50;
    
    private AudioServer server;
    private MP1CMessageRingBuffer inbox;
    private ByteBufferMessage currMsg = new ByteBufferMessage();
    private byte[] tmpbuf = new byte[1024];
    private AudioProcessor processor;    

    private double bpm;
    private double time;
    private boolean click;
    private int samplePos;
    private int loopLengthBeats;
    private TimeSignature timeSignature;
    private boolean playing;
    private int frame;
    
    @Override
    public void run() {
        callback(null, null, 0, 0, 0);
    }
    
    @Override
    public int callback(FloatBuffer input, FloatBuffer output, int N, 
        double time, int status) 
    {
        //inbox.printDebug();
        while (inbox.peek(currMsg)) {
            int size = currMsg.readSize();
            currMsg.readAddress(tmpbuf);
            
            //System.out.println("Message Received:");
            //System.out.println(currMsg);

            currMsg.beginReadArgs();
            while (currMsg.argsRemaining()) {
                switch (currMsg.readArgType()) {
                    case 'f':
                        currMsg.readFloatArg();
                        //System.out.println("Float:" + currMsg.readFloatArg());
                        break;
                    case 's':
                        currMsg.readStringZeroArg(tmpbuf);
                        //System.out.println("String:" + new String(tmpbuf));
                        break;
                    case 'i':
                        currMsg.readIntArg();
                        //System.out.println("Int:" + currMsg.readIntArg());
                        break;
                    case 'T':
                        //System.out.println("Boolean: TRUE");
                        break;
                    case 'F':
                        //System.out.println("Boolean: FALSE");
                        break;
                    case 'N':
                        //System.out.println("Null");
                        break;
                    case 'b':
                        //System.out.println("Null");
                        currMsg.readBlob(tmpbuf);
                        break;
                }
            }

            //System.out.println("----");
            
            inbox.advance(size);
            
            //System.out.println("Advanced by " + size);
        }
        
        return 0;
    }
    
    @Override
    public void init(AudioServer server) {
        this.server = server;
        this.inbox = server.inbox();
        System.out.println("Starting the fake audio thread.");
        ScheduledExecutorService thread = Executors.newScheduledThreadPool(1);
        thread.scheduleWithFixedDelay(this, 0, SLEEP_TIME, 
                TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void play() {
        playing = true;
    }

    @Override
    public void stop() {
        playing = false;
    }

    @Override
    public void bpm(double bpm) {
        this.bpm = bpm;
    }

    @Override
    public double bpm() {
        return bpm;
    }

    @Override
    public TimeSignature meter() {
        return timeSignature;
    }

    @Override
    public void meter(TimeSignature meter) {
        timeSignature = meter;
    }

    @Override
    public double time() {
        return time;
    }

    @Override
    public AudioHost time(double time) {
        this.time = time;
        return this;
    }

    @Override
    public int samplePos() {
        return samplePos;
    }

    @Override
    public int frame() {
        return frame;
    }

    @Override
    public double sampleRate() {
        return 44100;
    }

    @Override
    public int frameSize() {
        return 512;
    }

    @Override
    public void setProcessor(AudioProcessor proc) {
        this.processor = proc;
    }

    @Override
    public double beatsToSamples(double beat) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double samplesToBeats(int sample) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    
}
