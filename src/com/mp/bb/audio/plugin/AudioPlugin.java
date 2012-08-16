/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.bb.audio.plugin;

import com.mp.audioserv.audio.AudioHost;
import com.mp.audioserv.audio.graph.AudioProcessor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Martin Percossi
 */
public abstract class AudioPlugin implements AudioProcessor {
    
    transient protected AudioHost host;
    transient protected int id;
    
    protected int numIns;
    protected int numOuts;
    protected String address;
    
    @Override
    public void init(AudioHost host, int id) {
        this.host = host;
        this.id = id;
    }
    
    @Override
    public void release() {
        // NOP.
    }
    
    public int id() {
        return id;
    }
    
    public void setIOs(int numIns, int numOuts) {
        this.numIns = numIns;
        this.numOuts = numOuts;
    }
    
    @Override
    public int numIns() {
        return numIns;
    }
    
    @Override
    public AudioProcessor numIns(int ins) {
        numIns = ins;
        return this;
    }
    
    @Override
    public AudioProcessor numOuts(int outs) {
        numOuts = outs;
        return this;
    }
    
    @Override
    public int numOuts() {
        return numOuts;
    }
    
    @Override
    public void address(String address) {
        this.address = address;
    }
    
    @Override
    public String address() {
        return address;
    }
    
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, 
                ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AudioPlugin other = (AudioPlugin) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
    
    public static void main(String...args) {
//        System.out.println("Testing annotations.");
//        AudioPlugin plugin = new GainPlugin();
//        plugin.init(null, 0);
    }

}
