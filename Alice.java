/*
 Name: Sarah Helen Bednar
 Student number: A0179788X
 Is this a group submission (yes/no)? no
 */

import java.net.*;
import java.nio.*;
import java.util.Arrays;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

class Alice {

    private int seqNum = 0;
    private DatagramSocket socket;
    private static final int MAX_SIZE = (1024 - (Long.BYTES + Integer.BYTES * 2)); // max num bytes per packet 1024 - header size
    private static final int ACK = 0;
    private static final int NAK = 1;

    public static void main(String[] args) throws Exception {
        // Do not modify this method
        if (args.length != 3) {
            System.out.println("Usage: java Alice <path/filename> <unreliNetPort> <rcvFileName>");
            System.exit(1);
        }
        new Alice(args[0], Integer.parseInt(args[1]), args[2]);
    }

    public Alice(String fileToSend, int port, String filenameAtBob) throws Exception {
        // create client socket
        socket = new DatagramSocket();

        Path p = Paths.get(fileToSend);
        File f = p.getFileName().toFile();
        if (!f.canRead() || !f.isFile()) {
            System.out.println("Error: File " + f + " does not exist.");
            System.exit(1);
        }
        sendFile(f, port, filenameAtBob);

    }

    public void sendFile(File fileToSend, int port, String filenameAtBob) throws FileNotFoundException, IOException {
        // send filename packet
        byte[] filename = filenameAtBob.getBytes();
        Packet pkt = new Packet(filename, port, seqNum);
        DatagramPacket dp = pkt.getDataPacket();
        sendPacket(dp);
        FileInputStream input = new FileInputStream(fileToSend);
        long bytesToRead = fileToSend.length();
        byte[] packetData = new byte[MAX_SIZE];
        while (bytesToRead > 0) {
            if (bytesToRead > MAX_SIZE) {
                packetData = new byte[MAX_SIZE];
            } else {
                packetData = new byte[(int) bytesToRead];
            }
            int numDataBytes = input.read(packetData);
            bytesToRead -= numDataBytes;
            pkt = new Packet(packetData, port, seqNum);
            dp = pkt.getDataPacket();
            sendPacket(dp);
        }
        input.close();
        socket.close();
    }

    public void sendPacket(DatagramPacket packet) {
        try {
            socket.send(packet);
            socket.setSoTimeout(100);
            // Wait for ACK or NAK to ensure packet received or needs retransmission 
            byte[] buffer = new byte[MAX_SIZE];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);
            byte[] replyData = reply.getData();
            Packet ackPkt = new Packet(ACK, seqNum);
            if (Arrays.equals(ackPkt.getData(), replyData)) {
                seqNum++;
            } else {
                sendPacket(packet);
            }
            reply.getData();

        } catch (SocketTimeoutException e) {
            System.out.printf("Timer expired. Resend packet");
            sendPacket(packet);
        } catch (IOException ex) {
            System.out.println("Unable to send packet.");
            System.out.println(ex.getMessage());
            sendPacket(packet);
        }
    }

}
