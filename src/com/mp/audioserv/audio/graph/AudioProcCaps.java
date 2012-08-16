/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.audio.graph;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Details the capabilities of an audio processor.
 * 
 * @author Martin Percossi
 */
public class AudioProcCaps {
   public final int minInputChannels;
   public final int defInputChannels;
   public final int maxInputChannels;
   
   public final int minOutputChannels;
   public final int defOutputChannels;
   public final int maxOutputChannels;

    public AudioProcCaps(int minInputChannels, int defInputChannels, 
            int maxInputChannels, int minOutputChannels, int defOutputChannels, 
            int maxOutputChannels) 
    {
        this.minInputChannels = minInputChannels;
        this.defInputChannels = defInputChannels;
        this.maxInputChannels = maxInputChannels;
        this.minOutputChannels = minOutputChannels;
        this.defOutputChannels = defOutputChannels;
        this.maxOutputChannels = maxOutputChannels;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, 
                ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    
    public static void main(String...args) {
        AudioProcCaps input = new AudioProcCaps(2, 1, 2, 1, 1, 1);
        AudioProcCaps output = new AudioProcCaps(2, 1, 2, 1, 1, 1);
        
    }

}
