/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.audioserv.audio.graph;

import com.mp.audioserv.AudioServerException;
import com.mp.audioserv.audio.AudioHost;
import com.mp.audioserv.audio.DummyAudioEngine;
import com.mp.audioserv.ringbuf.MP1CMessageRingBuffer;
import com.mp.bb.audio.plugin.AudioPlugin;
import java.nio.FloatBuffer;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

/**
 * An audio processor graph has a set of inputs, a set of outputs, and a 
 * set of intermediary nodes. It can route audio through the graph, and 
 * can relay a message to one of the elements.
 *
 * @author Martin Percossi
 */
public class AudioProcessorGraph extends AudioPlugin {
    
    public static final int MIN_INPUTS = 1;
    public static final int DEF_INPUTS = 2;
    public static final int MAX_INPUTS = 1024;

    public static final int MIN_OUTPUTS = 1;
    public static final int DEF_OUTPUTS = 2;
    public static final int MAX_OUTPUTS = 1024;
    
    private Graph<Node, Connection> graph;
    private AudioHost host;
    private int nodeIdSeq;
    
    public AudioProcessorGraph(AudioHost host) {
        this.host = host;
        this.nodeIdSeq = 0;
        graph = new DirectedAcyclicGraph<>(Connection.class);
    }

    public Node getNode(int id) {
        return null;
    }
    
    public Node addNode(AudioProcessor processor) {
        Node result = new Node(nodeIdSeq++, processor, this);
        graph.addVertex(result);
        return result;
    }
    
    public void removeNode(int id) {
        Node node = getNode(id);
        if (node == null)
            throw new AudioServerException("No node found for id " + id);
        graph.removeVertex(node);
    }
    
    public Set<Connection> connections() {
        return graph.edgeSet();
    }
    
    public Set<Node> nodes() {
        return graph.vertexSet();
    }    

    @Override
    public AudioProcCaps capabilities() {
        return new AudioProcCaps(MIN_INPUTS, DEF_INPUTS, MAX_INPUTS,
                MIN_OUTPUTS, DEF_OUTPUTS, MAX_OUTPUTS);
    }

    @Override
    public int latency() {
        // the latency of the graph is the maximum of the latencies of its 
        // nodes (assuming all nodes are actually connected!)
        int latency = 0;
        for (Node node: graph.vertexSet()) {
            int l = node.processor.latency();
            if (l > latency)
                latency = l;
        }
        return latency;
    }

    public static void main(String...args) {
        // here's an example use of the graph; would be called by the audio
        // host.
     
        DummyAudioEngine host = new DummyAudioEngine();
        AudioProcessorGraph graph = new AudioProcessorGraph(host);

        host.setProcessor(graph);
        
    }

    @Override
    public void process(int samplePos, double time, FloatBuffer in, FloatBuffer out, int N, MP1CMessageRingBuffer inbox, MP1CMessageRingBuffer outbox) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
