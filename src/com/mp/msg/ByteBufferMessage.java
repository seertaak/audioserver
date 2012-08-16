/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.msg;

import com.mp.msg.Message;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * A
 * <code>ByteBufferMessage</code> basically represents a message stored within a
 * <code>ByteBuffer</code>.
 *
 * @author Martin Percossi
 */
public class ByteBufferMessage {

    private static final Charset ASCII = Charset.forName("US-ASCII");
    private static final CharsetEncoder ENCODER = ASCII.newEncoder();
    private static final CharsetDecoder DECODER = ASCII.newDecoder();
    private static final int MASK = 3;
    private final int MAX_MSG_SIZE = 1 << 10;
    private ByteBuffer buffer;
    private int addressSize;
    private int size;

    public ByteBuffer buffer() {
        return buffer;
    }
    
    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public long readGuid() {
        buffer.position(4);
        return buffer.getLong();
    }

    public double readTime() {
        buffer.position(12);
        return buffer.getDouble();
    }

    public int readSize() {
        buffer.position(0);
        return size = buffer.getInt();
    }

    /**
     * Note, you shouldn't use this because it will create garbage, albeit
     * minimal.
     */
    public String readAddress() {
        int pos = 20;
        assert pos % 4 == 0;
        buffer.position(pos);
        addressSize = 0;
        while (buffer.get() != 0) {
            addressSize++;
        }
        buffer.position(pos);
        byte[] buf = new byte[addressSize];
        buffer.get(buf);
        while ((buffer.position() & MASK) != 0) {
            addressSize++;
            buffer.get();
        }
        return new String(buf, ASCII);
    }
    
    public void readAddress(byte[] str) {
        int pos = 20;
        assert pos % 4 == 0;
        buffer.position(pos);
        byte b;
        int i = 0;
        while ((b = buffer.get()) != 0)
            str[i++] = b;
        addressSize = i;
        while ((buffer.position() & MASK) != 0) {
            addressSize++;
            buffer.get();
        }
    }
    
    public int readStringZero(int pos, byte[] str) {
        assert pos % 4 == 0;
        buffer.position(pos);
        byte b;
        int i = 0;
        while ((b = buffer.get()) != 0)
            str[i++] = b;
        while ((buffer.position() & MASK) != 0) {
            buffer.get();
        }
        return i;
    }

    public String readStringZero(int pos) {
        assert pos % 4 == 0;
        buffer.position(pos);
        int n = 0;
        while (buffer.get() != 0) {
            n++;
        }
        buffer.position(pos);
        byte[] buf = new byte[n];
        buffer.get(buf);
        while ((buffer.position() & MASK) != 0) {
            buffer.get();
        }
        return new String(buf, ASCII);
    }

    public void beginReadArgs() {
        buffer.position(20 + addressSize);
    }

    public boolean argsRemaining() {
        return buffer.position() < size;
    }

    public char readArgType() {
        byte b = buffer.get();
        assert b == (byte) ',';
        b = buffer.get();
        buffer.get();
        buffer.get();
        return (char) b;
    }

    public int readIntArg() {
        return buffer.getInt();
    }

    public void readStringZeroArg(byte[] string) {
        int pos = buffer.position();
        assert pos % 4 == 0;
        int n = 0;
        while (buffer.get() != 0)
            n++;
        buffer.position(pos);
        buffer.get(string, 0, n);
        while ((buffer.position() & MASK) != 0)
            buffer.get();
   }
    
    public void readBlob(byte[] dst) {
        assert buffer.position() % 4 == 0;
        int n = buffer.getInt();
        buffer.get(dst, 0, n);
        while ((buffer.position() & MASK) != 0)
            buffer.get();
    }

    public float readFloatArg() {
        return buffer.getFloat();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("guid", readGuid())
                .append("time", readTime())
                .append("address", readAddress())
                .append("size", readSize())
                .build();
    }

    public static void main(String... args) {
        Message inMsg = Message.to("/server/foo/bar/volume")
                               .at(4.0f)
                               .addArg(1.0F)
                               .addArg("Foobaryap");

        inMsg.setGuidSequence(232342342L);

        ByteBufferMessage outMsg = new ByteBufferMessage();
        outMsg.setBuffer(ByteBuffer.allocate(1024));
        outMsg.buffer().put(inMsg.pack());
        
        outMsg.readSize();
        outMsg.readAddress();

        System.out.println(inMsg);
        System.out.println(outMsg);
        
        byte[] tmpbuf = new byte[1024];

        outMsg.beginReadArgs();
        while (outMsg.argsRemaining()) {
            switch (outMsg.readArgType()) {
                case 'f':
                    System.out.println("Float:" + outMsg.readFloatArg());
                    break;
                case 's':
                    outMsg.readStringZeroArg(tmpbuf);
                    System.out.println("String:" + new String(tmpbuf));
                    break;
                case 'i':
                    System.out.println("Int:" + outMsg.readFloatArg());
                    break;
                case 'T':
                    System.out.println("Boolean: TRUE");
                    break;
                case 'F':
                    System.out.println("Boolean: FALSE");
                    break;
                case 'N':
                    System.out.println("Null");
                    break;
                case 'b':
                    System.out.println("Null");
                    outMsg.readBlob(tmpbuf);
                    break;
            }
        }


    }
}
