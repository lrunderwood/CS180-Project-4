import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.util.Scanner;

final class ChatClient {
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private Socket socket;

    private final String server;
    private final String username;
    private final int port;

    private int error = 0;
    /* ChatClient constructor
     * @param server - the ip address of the server as a string
     * @param port - the port number the server is hosted on
     * @param username - the username of the user connecting
     */
    private ChatClient(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
    }

    /**
     * Attempts to establish a connection with the server
     * @return boolean - false if any errors occur in startup, true if successful
     */
    private boolean start() {
        // Create a socket
        try {
            socket = new Socket(server, port);
        } catch (IOException e) {
                System.out.println("Exception connecting to socket: java.net.ConnectException: Connection refused: connect\nClient could not be started");
                System.exit(0);
            return false;
        }

        // Attempt to create output stream
        try {
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Attempt to create input stream
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Create client thread to listen from the server for incoming messages
        Runnable r = new ListenFromServer();
        Thread t = new Thread(r);
        t.start();

        // After starting, send the clients username to the server.
        try {
            sOutput.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    /*
     * Sends a string to the server
     * @param msg - the message to be sent
     */
    private void sendMessage(ChatMessage msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * To start the Client use one of the following command
     * > java ChatClient
     * > java ChatClient username
     * > java ChatClient username portNumber
     * > java ChatClient username portNumber serverAddress
     *
     * If the portNumber is not specified 1500 should be used
     * If the serverAddress is not specified "localHost" should be used
     * If the username is not specified "Anonymous" should be used
     */
    public static void main(String[] args) {
        // Get proper arguments and override defaults

        // Create your client and start it
        //skeleton
       /* ChatClient client = new ChatClient("localhost", 1500, "CS 180 Student");
        client.start();*/

        //my implementation: create a client with correct parameters
        String server = "localhost";
        int port = 1500;
        String username = "Anonymous";

        if(args.length == 1){
            username = args[0];
        }
        else if(args.length == 2){
            username = args[0];
            port = Integer.parseInt(args[1]);
        }
        else if(args.length == 3){
            username = args[0];
            port = Integer.parseInt(args[1]);
            server = args[2];
        }

        ChatClient client = new ChatClient(server, port, username);
        client.start();

        //if(client.start()) {
            System.out.println("Connection accepted " + server + "/" + port);
        //}

        int msgType = 0;
        String msg = null;
        String recipient = null;
        String[] fullMessage;

        while(true) {
            // Send an empty message to the server
            //skeleton
            //client.sendMessage(new ChatMessage());

            //my implementation: TODO: part 2
            System.out.print("> ");
            Scanner in = new Scanner(System.in);
            //System.out.print("> ");
            msg = in.nextLine();

            if (msg.contains("/logout")) {
                msgType = 1;
                try {
                    //close input/output/socket
                    client.sInput.close();
                    client.sOutput.close();
                    client.socket.close();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (msg.contains("/msg")) {
                msgType = 2;
                fullMessage = msg.split(" ");

                recipient = fullMessage[1];

                //make message the text that the user would like to send
                msg = "";
                for(int x = 2; x < fullMessage.length; x++){
                    msg = msg + " " + fullMessage[x];
                }
            } else if (msg.contains("/list")) {
                msgType = 3;
            } else if (msg.contains("/ttt")) {
                msgType = 4;

                fullMessage = msg.split(" ");

                recipient = fullMessage[1];

                //make message the player's move
                msg = "";
                for(int x = 2; x < fullMessage.length; x++){
                    msg = msg + " " + fullMessage[x];
                }
            }

            client.sendMessage(new ChatMessage(msgType, msg, recipient));
        }
    }

    /*
     * This is a private class inside of the ChatClient
     * It will be responsible for listening for messages from the ChatServer.
     * ie: When other clients send messages, the server will relay it to the client.
     */
    private final class ListenFromServer implements Runnable {
        public void run() {
                try {
                    while(true) {
                        //my implementation: keeps client in infinite loop
                        String msg = (String) sInput.readObject();
                        System.out.print(msg);
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
        }
    }
}