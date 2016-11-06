package com.allengarvey.jchat;


import java.io.IOException;
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

    //server code based on: https://systembash.com/a-simple-java-tcp-server-and-tcp-client/
    @Override
    public void run(){
        System.out.printf("jChat server starting on 127.0.0.1:%d\n", portNum);
        ServerSocket tcpSocket = null;
        try {
            tcpSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            //port is busy or need to use 'sudo' to bind to port
            System.out.printf("Couldn't start server of port %d, check if port is busy or you have suitable permissions to bind to that port\n", portNum);
            //exit thread because we can't start server
            return;
        }

        //start server listening for new connections
        while(true){
            try{
                Socket connectionSocket = tcpSocket.accept();
                //System.out.println("new tcp connection created");
                NetworkClient newClient = new NetworkClient(connectionSocket);
                //start subthread to listen to client requests
                (new Thread(() -> newClient.listenAction())).start();

                //start thread to broadcast messages to client
                (new Thread(() -> newClient.broadcastAction())).start();

            } catch (IOException e) {
                System.out.println("Server can't accept anymore tcp connections");
                continue;
            }

        }
    }
}
