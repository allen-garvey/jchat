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
    private boolean isQuitting;

    public NetworkClient(Socket tcpSocket){
        this.connectionSocket = tcpSocket;
        currentMessageIndex = 0;
        userName = null;
        isQuitting = false;
    }

    public Socket getConnectionSocket(){
        return connectionSocket;
    }

    private synchronized String getSetUserName(String userName){
        if(userName != null){
           //remove extra whitespace
           String cleanedUserName = userName.replaceAll("[\\s\\n\\t]", "");
           boolean isUserNameAvailable = Main.addUserName(cleanedUserName);
           if(isUserNameAvailable){
               this.userName = cleanedUserName;
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
                //System.out.println("Received: " + clientData);
                //check to see if client sends \quit - first remove trailing newlines
                if(clientData.replaceAll("[\\n]+$", "").equals("\\quit")){
                    break;
                }
                //if username not set, try setting username
                if(!isUserNameInitialized()){
                    getSetUserName(clientData);
                    //check if setting username succeeded
                    if(!isUserNameInitialized()){
                        outToClient.writeBytes("ERROR: "  + clientData + " is not available\n");
                    }
                    else{
                        outToClient.writeBytes("UNAME: " + userName + "\n");
                    }

                }
                else{
                    Main.addGetNewMessages(new ChatMessage(userName, clientData), -1);
                }
            }
        } catch (IOException e) {
            System.out.println("Problem reading line from client " + userName);
        }
        finally{
            //called when client sends \\quit
            isQuitting = true;
        }
    }
    public void broadcastAction(){
        try {
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            while(!isQuitting){
                if(!isUserNameInitialized()){
                    continue;
                }
                else{
                    ChatMessage[] unreadMessages = Main.addGetNewMessages(null, currentMessageIndex);
                    for(ChatMessage message : unreadMessages){
                        outToClient.writeBytes(message.toString());
                        currentMessageIndex++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Problem broadcasting to client " + userName);
        }
        finally{
            try{
                //System.out.println("Closing connection with " + userName);
                connectionSocket.close();
            }catch(IOException e1){
                //e1.printStackTrace();
            }
        }

    }


}
