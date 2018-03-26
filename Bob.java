/*
 Name: Sarah Helen Bednar
 Student number: A0179788X
 Is this a group submission (yes/no)? no
 */

import java.net.*;
import java.nio.*;
import java.io.*;
import java.util.Arrays;

class Bob {

    private int expectedSeqNum = 0;
    private int numBytesRead = 0;
    private long fileSize = 0;
    private DatagramSocket socket;
    private String filename;
    private BufferedOutputStream bos;
    private FileOutputStream fos;
    private static final int MAX_SIZE = 1024;
    private static final int ACK = 0;
    private static final int NAK = 1;

    public static void main(String[] args) throws Exception {
        // Do not modify this method
        if (args.length != 1) {
            System.out.println("Usage: java Bob <port>");
            System.exit(1);
        }
        new Bob(Integer.parseInt(args[0]));
    }

    public Bob(int port) throws Exception {
        socket = new DatagramSocket(port);
        byte[] buffer = new byte[MAX_SIZE];
        while (true) {
            DatagramPacket recievedPkt = new DatagramPacket(buffer, buffer.length);
            socket.receive(recievedPkt);
            int serverPort = recievedPkt.getPort();
            InetAddress address = recievedPkt.getAddress();
            byte[] recievedData = recievedPkt.getData();
            byte[] checkSumByte = Arrays.copyOfRange(recievedData, 0, Long.BYTES);
            long checkSum = ByteBuffer.wrap(checkSumByte).getLong();
            byte[] seqNumByte = Arrays.copyOfRange(recievedData, Long.BYTES, Long.BYTES + Integer.BYTES);
            int seqNum = ByteBuffer.wrap(seqNumByte).getInt();
            byte[] sizeByte = Arrays.copyOfRange(recievedData, Long.BYTES + Integer.BYTES, Long.BYTES + (Integer.BYTES * 2));
            int size = ByteBuffer.wrap(sizeByte).getInt();
            byte[] data = Arrays.copyOfRange(recievedData, Long.BYTES + (Integer.BYTES * 2), Long.BYTES + (Integer.BYTES * 2) + size);
            Packet p = new Packet(data, port, seqNum);
            long expectedCheckSum = p.checkSum();
            if (expectedCheckSum != checkSum) {
                //send NAK
                System.out.println("mis match check sum  ");
                Packet nakPkt = new Packet(NAK, seqNum);
                DatagramPacket NAK = new DatagramPacket(nakPkt.getData(), nakPkt.getData().length, address, serverPort);
                socket.send(NAK);
                System.out.println("NAK sent");
            } else if (expectedSeqNum != seqNum) {
                //duplicate packet so drop 
                // send ACK 
                System.out.println("mis match seq num  ");
                Packet ackPkt = new Packet(ACK, seqNum);
                DatagramPacket ACK = new DatagramPacket(ackPkt.getData(), ackPkt.getData().length, address, serverPort);
                socket.send(ACK);
                System.out.println("ACK sent ");
            } else {
                //send ACK 
                if (seqNum == 0) {
                    // filename
                    filename = new String(data);
                    fos = new FileOutputStream(filename);
                    bos = new BufferedOutputStream(fos);
                    System.out.println("file name recieved " + filename);
                } else if (seqNum == 1) {
                    fileSize = ByteBuffer.wrap(data).getLong();
                    System.out.println("file size recieved " + fileSize);
                } else {
                    //write to file 
                    bos.write(data);
                    numBytesRead += data.length;
                }
                Packet ackPkt = new Packet(ACK, seqNum);
                DatagramPacket ACK = new DatagramPacket(ackPkt.getData(), ackPkt.getData().length, address, serverPort);
                socket.send(ACK);
                System.out.println("ACK sent ");
                expectedSeqNum++;
            }
            if (fileSize != 0 && numBytesRead == fileSize) {
                bos.flush();
                bos.close();
                fos.close();
                break;
            }
        }
    }

}
