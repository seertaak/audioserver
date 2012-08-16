/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.audio;

import com.mp.audioserv.AudioServer;
import com.mp.audioserv.audio.graph.AudioProcessor;
import com.mp.audioserv.audio.graph.GainProcessor;
import com.mp.bb.audio.plugin.MetronomePlugin;
import java.nio.FloatBuffer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jrtaudio.DeviceInfo;
import org.jrtaudio.JRtAudio;
import org.jrtaudio.StreamOptions;
import org.jrtaudio.StreamParameters;

/**
 * A simple test of the audio host functionality.
 *
 * @author Martin Percossi
 */
public class Metronome implements AudioEngine {
    public static final int FRAME_SIZE = 512;
    public static final int SAMPLE_RATE = 44100;
 
    private AudioServer server;
    private double bpm;
    private JRtAudio audio;
    private int samplePos;
    private int frame;
    private AudioProcessor processor;
    private boolean playing;
    
    public Metronome() {
        bpm = 120;
        samplePos = 0;
        audio = null;
    }
    
    @Override
    public int callback(FloatBuffer input, FloatBuffer output, int N, 
        double time, int status)
    {
        //System.out.println("Callback " + time);
        processor.process(samplePos, samplesToBeats(samplePos), input, output, 
                N, null, null);
        
        samplePos += N;
        
        return 0;
    }
    
    @Override
    public void init(AudioServer server) {
        this.server = server;
        
        audio = new JRtAudio();
        
        audio.showWarnings();
        
        for (int i = 0; i < audio.getDeviceCount(); i++) {
            System.out.println("Device " + i + ":"
                    + ToStringBuilder.reflectionToString(
                    audio.getDeviceInfo(i),
                    ToStringStyle.SHORT_PREFIX_STYLE));
        }
        
        int out = audio.getDefaultOutputDevice();
        int in = audio.getDefaultInputDevice();

        DeviceInfo devin = audio.getDeviceInfo(in);
        System.out.println("Input device: "
                + ToStringBuilder.reflectionToString(devin,
                ToStringStyle.SHORT_PREFIX_STYLE));
        StreamParameters inParams = new StreamParameters();
        inParams.nChannels = devin.inputChannels;
        inParams.deviceId = in;
        
        DeviceInfo devout = audio.getDeviceInfo(out);
        System.out.println("Output device: "
                + ToStringBuilder.reflectionToString(devout,
                ToStringStyle.SHORT_PREFIX_STYLE));
        StreamParameters oParams = new StreamParameters();
        oParams.nChannels = devout.outputChannels;
        oParams.deviceId = out;
        StreamOptions streamOptions = new StreamOptions();
        streamOptions.flags = 0x2 /* minimize latency */
                | /* schedule realtime */ 0x8 | 0x4; 
        
        processor = new MetronomePlugin();
        processor.numIns(2).numOuts(2).init(this, 0);
        
        audio.openStream(oParams, inParams, DeviceInfo.RTAUDIO_FLOAT32, 
                SAMPLE_RATE, FRAME_SIZE, this, streamOptions);
        audio.startStream();
    }

    @Override
    public double time() {
        return samplesToBeats(samplePos);
    }

    @Override
    public AudioHost time(double time) {
        samplePos = (int) beatsToSamples(time);
        return this;
    }

    @Override
    public int samplePos() {
        return samplePos;
    }

    @Override
    public double sampleRate() {
        return SAMPLE_RATE;
    }

    @Override
    public int frame() {
        return frame;
    }

    @Override
    public int frameSize() {
        return FRAME_SIZE;
    }

    @Override
    public void setProcessor(AudioProcessor proc) {
        this.processor = proc;
    }

    @Override
    public double beatsToSamples(double beats) {
        return beats * SAMPLE_RATE / bpm * 60.0;
    }
    
    @Override
    public double samplesToBeats(int spos) {
        return spos / SAMPLE_RATE * bpm / 60.0;
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
        return TimeSignature.std();
    }

    @Override
    public void meter(TimeSignature meter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String...args) throws InterruptedException {
        final Metronome metronome = new Metronome();
        metronome.init(null);
        
        while (true) {
            Thread.sleep(1000);
        }
    }
}
