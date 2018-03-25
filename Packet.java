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
    private int dataSize;
    private byte[] data;
    
    //Data constructor
    public Packet(int type, byte[] buffer, int numBytes, int port, int seqNum, String fileName){
        dataSize = numBytes;
        data = buffer;
    }
    // ACK/NAK constructor 
    public Packet(int type, int seqNum) {
        
    }
    
    public DatagramPacket getDataPacket() throws UnknownHostException{
        InetAddress address = InetAddress.getByName("localhost");
        //DatagramPacket p = new DatagramPacket(packetData, packetSize, address, port); 
        return null; 
    }
    
    public byte[] getData() {
        //type and seqNum concat
        return null;
    }
   
    public void checkSum() {
        CRC32 crc = new CRC32();
        crc.update(data);
    }
    
}