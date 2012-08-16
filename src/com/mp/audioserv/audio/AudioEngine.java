/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.audio;

import com.mp.audioserv.AudioServer;
import java.nio.FloatBuffer;

/**
 *
 * @author Martin Percossi
 */
public interface AudioEngine extends AudioHost {
   
    /**
     * Audio Server
     * 
     * We need to provide these basic services:
     * /adevice
     *   - querying for a list of audio devices
     *   - selection of audio device.
     *   - buffer size, samples size, etc.
     * /transport
     *   - play (receives play start, end, and boolean if looping)
     *   - stop
     *   - bpm
     *   - time signature
     * /agraph (audio graph)
     *   - add/delete anode
     *   - add/delte aconnection
     * /timesync
     *   - gets sent from the server to clients with the current audio
     *     time.
     *   - set rate (i.e. how many times per second we send a time sync message)
     */

    public int callback(FloatBuffer input, FloatBuffer output,
            int N, double time, int status);
    
    
    public void init(AudioServer server);

}
