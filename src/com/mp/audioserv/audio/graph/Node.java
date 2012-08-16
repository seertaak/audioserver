/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.audio.graph;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Martin Percossi
 */
public class Node {
    public final int id;
    public final AudioProcessor processor;
    private boolean initialized;
    private final AudioProcessorGraph outer;

    public Node(int id, AudioProcessor proc, final AudioProcessorGraph outer) {
        this.outer = outer;
        this.id = id;
        this.processor = proc;
    }

    public void init(AudioProcessorGraph graph) {
        if (!initialized) {
            initialized = true;
        }
    }

    public void release() {
        
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, 
                ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node &&
                id == ((Node)obj).id;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
}
