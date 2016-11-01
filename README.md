# jChat

Multi-threaded TCP command-line chat server, written in Java.

## Dependencies

* openjdk version "1.8.0_91" or later

## Getting Started

* `cd` into the main project directory (the directory with this README file)
* Type `make server` (without the backticks) to compile the server
* Type `java -classpath ./out/production/jChat/ com.allengarvey.jchat.Main <port_num>` where `<port_num>` is an integer port number for the server to run on.

## Connecting to the Server

To connect to the server, use any TCP client, such as netcat. To connect to the server using netcat:

* Start the server using the instructions above
* Open a new terminal tab and type `nc 127.0.0.1 <port_num>` where `<port_num>` is the integer port number the server is running on.
* If you do not see an error message you are now connected. To select your username, type in your desired username and hit enter.

## Application Layer Protocol Description

* Client makes TCP connection to server
* Client sends text string containing desired username
* If it is accepted by the server, server will send back a message with the format `UNAME: <username>` where `<username>` is replaced by the assigned username
* If the username is not accepted (such as it is already in use) server will respond with `ERROR: <error_message>` where `<error_message>` will be an appropriate error message
* Once a client has a username assigned, indicated by the `UNAME: ` response, any text sent by the client with the exception of `\quit` will be interpreted as a message to be broadcast to all connected hosts, as well as any hosts that join the chat later. The client is also free to send messages at any time.
*  Once a username has been assigned, clients will also receive any messages that occurred since the server was started, and then any messages as they are sent by any connected host. Messages received will be in the general format `username> message` where `username` is the username of the host that sent the message, then `> ` and then the message sent by that user.
* At any time that the client is connected to the server, a message of `\quit` will be interpreted as a signal to immediately close the connection with the server

## License

jChat is released under the MIT License. See license.txt for more details.