package com.allengarvey.jchat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by allen on 10/29/16.
 */
public class NetworkClient{
    private Socket connectionSocket;
    private String userName;
    private int currentMessageIndex;

    public NetworkClient(Socket tcpSocket){
        this.connectionSocket = tcpSocket;
        currentMessageIndex = 0;
        userName = null;
    }

    public Socket getConnectionSocket(){
        return connectionSocket;
    }

    private synchronized String getSetUserName(String userName){
        if(userName != null){
           boolean isUserNameAvailable = Main.addUserName(userName);
           if(isUserNameAvailable){
               this.userName = userName;
           }
        }

        return this.userName;
    }

    public  boolean isUserNameInitialized(){
        return getSetUserName(null) != null;
    }

    public void listenAction(){
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            String clientData;
            while((clientData = inFromClient.readLine()) != null) {
                System.out.println("Received: " + clientData);
                if(clientData.equals("quit")){
                    break;
                }
                if(!isUserNameInitialized()){
                    getSetUserName(clientData);
                }
                else{
                    ChatMessage[] unreadMessages = Main.addGetNewMessages(new ChatMessage(userName, clientData), currentMessageIndex);
                    for(ChatMessage message : unreadMessages){
                        outToClient.writeBytes(message.toString());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Problem reading line from client " + userName);
            try{
                connectionSocket.close();
            }catch(IOException e1){
                //e1.printStackTrace();
            }
        }
    }


}
