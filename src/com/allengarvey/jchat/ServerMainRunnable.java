package com.allengarvey.jchat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by allen on 10/29/16.
 */
public class ServerMainRunnable implements Runnable{
    int portNum;

    public ServerMainRunnable(int portNum){
        this.portNum = portNum;
    }

    @Override
    public void run(){
        String clientData = "";

        System.out.printf("jChat server starting on 127.0.0.1:%d\n", portNum);
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
