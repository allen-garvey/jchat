package com.allengarvey.jchat;
import java.util.*;

//run on command line java -classpath ./out/production/jChat/ com.allengarvey.jchat.Main 4000
/**
 * Created by allen on 10/28/16.
 */
//main class
public class Main {
    //default port server runs on if none given
    public static final int DEFAULT_PORT = 5000;
    public static final String SERVER_USER_NAME = "Oracle";
    public static final int MESSAGE_MAX_LENGTH = 500;
    //used to store all messages sent
    private static ArrayList<ChatMessage> messagesList;
    //used to determine unique usernames
    private static HashSet<String> userNamesSet;


    //returns false if username is not available, true if it is
    //if it is saves it, so no one else can use it
    //since this is accessed by multiple threads, must be synchronized
    //also why checking for availability and setting must be in combined
    //synchronized method
    public static synchronized boolean addUserName(String userName){
        //username taken
        if(userName == null || userNamesSet.contains(userName)){
            return false;
        }
        //username available, add to set
        userNamesSet.add(userName);
        return true;
    }
    //either adds message to queue, or returns all messages starting from startIndex
    //adding and retrieving must be combined into method because this is accessed by multiple threads
    //so access to messagesList has to be synchronized
    //to set, call with -1 as index
    //to get, call with null as message
    public static synchronized ChatMessage[] addGetNewMessages(ChatMessage message, int startIndex){
        if(message != null){
            messagesList.add(message);
        }
        int messageListSize = messagesList.size();
        if(startIndex < 0 || startIndex > messageListSize){
            return new ChatMessage[0];
        }

        int unreadMessagesLength = messageListSize - startIndex;
        ChatMessage[] unreadMessages = new ChatMessage[unreadMessagesLength];
        for(int i=0; i < unreadMessagesLength; i++){
            unreadMessages[i] = messagesList.get(i + startIndex);
        }
        return unreadMessages;
    }

    public static void main(String argv[]){
        //initialize containers
        messagesList = new ArrayList<>();
        userNamesSet = new HashSet<>();
        //add client for server terminal
        TerminalClient terminalClient = new TerminalClient(SERVER_USER_NAME);

        //initialize server listening port
        int portNum = DEFAULT_PORT;
        //check to see if port given
        if(argv.length > 0){
            portNum = Integer.parseInt(argv[0]);
        }

        //print title and extra credit notices
        System.out.println("CS372 Project 1 by Allen Garvey");
        System.out.println("**EC**: Server is multi-threaded");
        System.out.println("**EC**: Hosts are connected in one group chat");
        System.out.println("**EC**: Any host can send at any time");
        System.out.println("");


        //start main server thread
        (new Thread(new ServerMainRunnable(portNum))).start();

        //start thread for terminal broadcast
        (new Thread(new Runnable(){
            @Override
            public void run(){
                terminalClient.broadcastAction();
            }
        })).start();

        //start thread waiting for client terminal input
        (new Thread(new Runnable(){
            @Override
            public void run(){
                terminalClient.listenAction();
            }
        })).start();


    }
}
