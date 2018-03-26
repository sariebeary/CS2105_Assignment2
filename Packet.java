/*
 Name: Sarah Helen Bednar
 Student number: A0179788X
 Is this a group submission (yes/no)? no
 */

import java.net.*;
import java.nio.*;
import java.util.zip.CRC32;

// This class is not absolutely necessary as you can mash everything
// directly into Alice and Bob classes. However, it might be nicer
// to have this class, which makes your code more organized and readable.
// Furthermore, it makes the assignment easier, as you might be able to
// reuse code
class Packet {

    // Implement me
    private int type, port, seqNum;
    private byte[] packetData, fileData;

    //Data constructor
    public Packet(byte[] data, int port, int seqNum) {
        fileData = data;
        this.port = port;
        this.seqNum = seqNum;

    }

    // ACK/NAK constructor 
    public Packet(int type, int seqNum) {
        this.type = type;

    }

    public DatagramPacket getDataPacket() throws UnknownHostException {
        InetAddress address = InetAddress.getByName("localhost");
        packetData = addHeaderData(fileData);
        DatagramPacket p = new DatagramPacket(packetData, packetData.length, address, port);
        return p;
    }

    public byte[] addHeaderData(byte[] data) {
        ByteBuffer b = ByteBuffer.allocate(Long.BYTES + Integer.BYTES * 2 + data.length);
        //Add checksum
        b.putLong(checkSum());
        //Add seqNum
        b.putInt(seqNum);
        //Add data size
        b.putInt(type);
        //Add data
        b.put(data);
        return b.array();
    }

    public byte[] getData() {
        //type and seqNum concat
        ByteBuffer b = ByteBuffer.allocate(16);
        b.putInt(type);
        b.putInt(seqNum);
        return b.array();
    }

    public long checkSum() {
        CRC32 crc = new CRC32();
        crc.update(packetData);
        return crc.getValue();
    }

}
