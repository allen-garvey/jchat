package com.allengarvey.jchat;
import java.util.*;

//run on command line java -classpath ./out/production/jChat/ com.allengarvey.jchat.Main 4000
/**
 * Created by allen on 10/28/16.
 */
//main class
//server code based on: https://systembash.com/a-simple-java-tcp-server-and-tcp-client/
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

    public static synchronized ChatMessage[] addGetNewMessages(ChatMessage message, int index){
        if(message != null){
            messagesList.add(message);
        }
        int messageListSize = messagesList.size();
        if(index < 0 || index > messageListSize){
            return new ChatMessage[0];
        }

        ChatMessage[] ret = new ChatMessage[messageListSize - index];
        for(int i=index; i < messageListSize; i++){
            ret[i] = messagesList.get(i);
        }
        return ret;
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
