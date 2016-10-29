package com.allengarvey.jchat;

import java.net.Socket;

/**
 * Created by allen on 10/29/16.
 */
public class NetworkClient{
    private Socket tcpSocket;
    private String userName;
    private int currentMessageIndex;

    public NetworkClient(Socket tcpSocket){
        this.tcpSocket = tcpSocket;
        currentMessageIndex = 0;
        userName = null;
    }

    public Socket getTcpSocket(){
        return tcpSocket;
    }
}
