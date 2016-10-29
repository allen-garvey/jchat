package com.allengarvey.jchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by allen on 10/29/16.
 */
public class TerminalClient{
    //shared flag between threads so that listenAction thread can communicate to
    //broadcast action to quit
    private volatile boolean isQuitting;
    //flag used to keep track if user just entered input, so there is no double newlines
    private volatile boolean isNewline;
    //terminal client's username
    private String userName;
    //index of last message successful broadcast
    private int currentMessageIndex;


    public TerminalClient(String userName){
        setUserName(userName);
        //initialize variables
        isQuitting = false;
        currentMessageIndex = 0;
        isNewline = false;
    }

    //set username for terminal client and add to username's hash
    private void setUserName(String userName){
        //going to assume this succeeds, since userName is hardcoded
        Main.addUserName(userName);
        this.userName = userName;
    }

    private String getPrompt(){
        return userName + "> ";
    }

    //get input from terminal and create message based on that
    //based on: http://stackoverflow.com/questions/4644415/java-how-to-get-input-from-system-console
    void listenAction(){
        String clientInput = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a message to send, or \\quit to exit");
        try{
            //print out prompt to start-broadcastAction will take care of it from there
            System.out.print(getPrompt());
            while((clientInput = br.readLine()) != null){
                //check to see if client sends \quit - first remove trailing newlines
                if(clientInput.replaceAll("[\\n]+$", "").equals("\\quit")){
                    break;
                }
                //newline created when user pressed enter, used to keep track so there is no double newlines
                isNewline = true;
                //add message to queue
                Main.addGetNewMessages(new ChatMessage(userName, clientInput), -1);
            }
        }
        catch(IOException e){

        }
        finally{
            //client entered \quit, so signal broadcastAction to quit
            isQuitting = true;
        }
    }

    //loop that gets unread messages since last checked and broadcasts them to client
    void broadcastAction(){
        //check if should stop, because client sent \quit
        while(!isQuitting){
            //get all unread messages, broadcast them, and increment index
            ChatMessage[] unreadMessages = Main.addGetNewMessages(null, currentMessageIndex);
            //if unread messages, print newline before
            //if is newline, user just pressed enter, so no need for more newlines
            if(unreadMessages.length > 0 && !isNewline){
                System.out.print("\n");
            }
            for(ChatMessage message : unreadMessages){
                //check to make sure we shouldn't quit
                if(isQuitting){
                    break;
                }
                //print message to console - no println because message already ends in \n
                System.out.print(message.toString());
                //message now broadcast, so increment index
                currentMessageIndex++;
            }
            //reprint prompt after messages
            //and reset isNewLine
            if(unreadMessages.length > 0){
                System.out.print(getPrompt());
                isNewline = false;
            }
        }
        System.out.println("Exiting client session. Press control-c to stop the server.");
    }
}
