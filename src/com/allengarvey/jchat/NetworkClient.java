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
    //socket to send and receive data
    private Socket connectionSocket;
    //username of client
    private String userName;
    //index of last message successful broadcast
    private int currentMessageIndex;
    //shared flag between threads so that both threads have exited while loop
    //before closing connection
    private boolean isQuitting;

    public NetworkClient(Socket tcpSocket){
        this.connectionSocket = tcpSocket;
        //initialize variables
        currentMessageIndex = 0;
        userName = null;
        isQuitting = false;
    }

    public Socket getConnectionSocket(){
        return connectionSocket;
    }

    //since two threads are potentially writing to and reading from username,
    //need combined synchronized method
    //calling with username as null is the same as getter, having a string username
    //attempts to set user name if it is not taken
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

    //checks if username has been set
    //returns false if username attempt fails because it is taken
    public  boolean isUserNameInitialized(){
        return getSetUserName(null) != null;
    }

    //listens for user input
    //if user doesn't have username, attempts to set username
    //if that succeeds, sends username back, otherwise sends error
    //otherwise add user message to queue
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
                        //username setting succeeded, send it back
                        outToClient.writeBytes("UNAME: " + userName + "\n");
                    }

                }
                else{
                    //add message to queue
                    Main.addGetNewMessages(new ChatMessage(userName, clientData), -1);
                }
            }
        } catch (IOException e) {
            System.out.println("Problem reading line from client " + userName);
        }
        finally{
            //called when client sends \quit
            isQuitting = true;
        }
    }
    //loop that gets unread messages since last checked and broadcasts them to client
    public void broadcastAction(){
        try {
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            //check if connection needs to be shut down, because client sent \quit
            while(!isQuitting){
                //don't send messages until client has a username
                if(!isUserNameInitialized()){
                    continue;
                }
                //get all unread messages, broadcast them, and increment index
                else{
                    ChatMessage[] unreadMessages = Main.addGetNewMessages(null, currentMessageIndex);
                    for(ChatMessage message : unreadMessages){
                        outToClient.writeBytes(message.toString());
                        //message now broadcast, so increment index
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
