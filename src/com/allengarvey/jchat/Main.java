package com.allengarvey.jchat;
import java.io.*;
import java.net.*;
import java.util.*;

//run on command line java -classpath ./out/production/jChat/ com.allengarvey.jchat.Main 4000
/**
 * Created by allen on 10/28/16.
 */
//main class
//server code based on: https://systembash.com/a-simple-java-tcp-server-and-tcp-client/
public class Main {
    //default port server runs on if none given
    public static final int DEFAULT_PORT = 5000;

    public static void main(String argv[]){
        int portNum = DEFAULT_PORT;
        //check to see if port given
        if(argv.length > 0){
            portNum = Integer.parseInt(argv[0]);
        }

        String clientData = "";

        System.out.printf("j Chat server starting on 127.0.0.1:%d\n", portNum);
        ServerSocket tcpSocket = null;
        try {
            tcpSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            System.out.printf("Couldn't start server of port %d, check if port is being used by another process\n", portNum);
            //exit program because we can't start server
            return;
        }

        //start server listening
        while(true){
            Socket connectionSocket = null;
            BufferedReader inFromClient = null;
            DataOutputStream outToClient = null;
            try{
                //create a new thread here
                connectionSocket = tcpSocket.accept();
                System.out.println("new tcp connection created");
            } catch (IOException e) {
                System.out.println("Server can't accept anymore tcp connections");
                continue;
            }
            try {
                inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            } catch (IOException e) {
                System.out.println("Problem creating input reader or output stream");
                continue;
            }
            try {
                while((clientData = inFromClient.readLine()) != null) {
                    System.out.println("Received: " + clientData);
                    if (clientData.equals("quit")) {
                        break;
                    }
                    outToClient.writeBytes("Received: " + clientData + "\n");
                }
            } catch (IOException e) {
                System.out.println("Problem reading line from client");
                continue;
            }
//            if(clientData.equals("quit")){
//                break;
//            }
//            try{
//                outToClient.writeBytes("Received: " + clientData + "\n");
//            } catch (IOException e) {
//                System.out.println("Couldn't send data to client");
//            }
        }
        //close socket
        /*
        System.out.println("Server shutting down");
        try {
            tcpSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}
