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
    private int dataSize, type, port, seqNum;
    private String rcvFileName;
    private byte[] packetData, fileData;

    //Data constructor
    public Packet(byte[] data, int numBytes, int port, int seqNum, String fileName) {
        fileData = data;
        dataSize = numBytes;
        this.port = port;
        this.seqNum = seqNum;
        rcvFileName = fileName;

    }

    // ACK/NAK constructor 
    public Packet(int type, int seqNum) {
        this.type = type;

    }

    public DatagramPacket getDataPacket() throws UnknownHostException {
        InetAddress address = InetAddress.getByName("localhost");
        packetData = addHeaderData();
        DatagramPacket p = new DatagramPacket(packetData, packetData.length, address, port);
        return p;
    }

    public byte[] addHeaderData() {
        ByteBuffer b = ByteBuffer.allocate(1024);
        //Add checksum
        b.putLong(checkSum());
        //Add seqNum
        b.putInt(seqNum);
        //Add rcvFileName
        b.put(rcvFileName.getBytes());
        //Add data
        b.put(fileData);
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
