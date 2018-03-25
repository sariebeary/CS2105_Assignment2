/*
 Name: Sarah Helen Bednar
 Student number: A0179788X
 Is this a group submission (yes/no)? no
 */


import java.net.*;
import java.nio.*;
import java.io.*;


class Bob {
    private int seqNum = 0;
    private DatagramSocket socket;
    private static final int DATA = -1;
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
        // Implement me
    }
}