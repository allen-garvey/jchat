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
    private static ArrayList<ChatMessage> messagesList;
    private static HashSet<String> userNamesSet;


    public static synchronized boolean addUserName(String userName){
        if(userNamesSet.contains(userName)){
            return false;
        }
        userNamesSet.add(userName);
        return true;
    }

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
        messagesList = new ArrayList<>();
        userNamesSet = new HashSet<>();
        addUserName(SERVER_USER_NAME);

        int portNum = DEFAULT_PORT;
        //check to see if port given
        if(argv.length > 0){
            portNum = Integer.parseInt(argv[0]);
        }

        (new Thread(new ServerMainRunnable(portNum))).start();


    }
}
