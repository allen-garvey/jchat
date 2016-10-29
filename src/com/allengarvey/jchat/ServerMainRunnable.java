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

        //start server listening for new connections
        while(true){
            try{
                Socket connectionSocket = tcpSocket.accept();
                System.out.println("new tcp connection created");
                NetworkClient newClient = new NetworkClient(connectionSocket);
                //start subthread to listen to client requests
                (new Thread(new Runnable(){
                    @Override
                    public void run(){
                        newClient.listenAction();
                    }
                })).start();

                //start thread to broadcast messages to client
                (new Thread(new Runnable(){
                    @Override
                    public void run(){
                        newClient.broadcastAction();
                    }
                })).start();

            } catch (IOException e) {
                System.out.println("Server can't accept anymore tcp connections");
                continue;
            }

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
