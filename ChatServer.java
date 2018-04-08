import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

final class ChatServer {
    private static int uniqueId = 0;
    // Data structure to hold all of the connected clients
    private final List<ClientThread> clients = new ArrayList<>();
    private final int port;            // port the server is hosted on
    private int count = 0;

    /**
     * ChatServer constructor
     *
     * @param port - the port the server is being hosted on
     */
    private ChatServer(int port) {
        this.port = port;
    }

    /*
     * This is what starts the ChatServer.
     * Right now it just creates the socketServer and adds a new ClientThread to a list to be handled
     */
    private void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            //skeleton
            /*Socket socket = serverSocket.accept();
            Runnable r = new ClientThread(socket, uniqueId++);
            Thread t = new Thread(r);
            clients.add((ClientThread) r);
            t.start();*/

            //my implementation: keeps server in infinite loop
            while (true) {
                Socket socket = serverSocket.accept();
                // server waits until a client opens a Socket with the same address and port number
                Runnable r = new ClientThread(socket, uniqueId++);
                Thread t = new Thread(r);
                clients.add((ClientThread) r);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sample code to use as a reference for Tic Tac Toe
     * <p>
     * directMessage - sends a message to a specific username, if connected
     *
     * @param //message  - the string to be sent
     * @param //username - the user the message will be sent to
     */
    private synchronized void directMessage(String message, String username, String userSending) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        String formattedMessage = time  + " " + userSending + ": " + message + "\n";
        System.out.print(formattedMessage);

        for (ClientThread clientThread : clients) {
            if (clientThread.username.equalsIgnoreCase(username)) {
                clientThread.writeMessage(formattedMessage);
            }
        }
    }


    /*
     *  > java ChatServer
     *  > java ChatServer portNumber
     *  If the port number is not specified 1500 is used
     */
    public static void main(String[] args) {
        ChatServer server = new ChatServer(1500);
        server.start();
    }


    /*
     * This is a private class inside of the ChatServer
     * A new thread will be created to run this every time a new client connects.
     */
    private final class ClientThread implements Runnable {
        Socket socket;                  // The socket the client is connected to
        ObjectInputStream sInput;       // Input stream to the server from the client
        ObjectOutputStream sOutput;     // Output stream to the client from the server
        String username;                // Username of the connected client
        ChatMessage cm;                 // Helper variable to manage messages
        int id;

        /*
         * socket - the socket the client is connected to
         * id - id of the connection
         */
        private ClientThread(Socket socket, int id) {
            this.id = id;
            this.socket = socket;
            try {
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                username = (String) sInput.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        /*
         * This is what the client thread actually runs.
         */
        @Override
        public void run() {
            // Read the username sent to you by client
            /*try {
                cm = (ChatMessage) sInput.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(username + ": Ping");


            // Send message back to the client
            try {
                sOutput.writeObject("Pong");
            } catch (IOException e) {
                e.printStackTrace();
            }*/


            try {
                while (true) {
                    writeMessage("> ");
                    cm = (ChatMessage) sInput.readObject();
                    //my implementation
                    if (cm.getMsgType() == 1) {
                        System.out.println("case 1");
                        close();
                        remove(this.id);
                    } else if (cm.getMsgType() == 2) {
                        directMessage(cm.getMsg(), cm.getRecipient(), this.username);
                    } else if (cm.getMsgType() == 3) {
                        writeMessage("List of users:\n");
                        for (int x = 0; x < clients.size(); x++) {
                            if (clients.get(x).username == this.username) {
                            } else {
                                writeMessage(clients.get(x).username + "\n");
                            }
                        }
                    } else if (cm.getMsgType() == 4) {
                        //TODO: CALL PART 3
                        //FIND RECIPIENT
                        int recipientIndex = 0;
                        for (int x = 0; x < clients.size(); x++) {
                            if (clients.get(x).username.equals(cm.getRecipient())) {
                                recipientIndex = x;
                                System.out.println(recipientIndex);
                                break;
                            }
                        }

                        TicTacToeGame game = new TicTacToeGame(this.username, cm.getRecipient(), true);
                        if (count == 0) {
                            writeMessage("Started TicTacToe with " + cm.getRecipient());
                            clients.get(recipientIndex).writeMessage(this.username + " has started TicTacToe with you.");

                            //initialize array
                            game.initialize();
                            count++;
                        }

                        //display tic tac toe board
                        int move = 0;

                        //display board
                        writeMessage("\n");
                        writeMessage("----------\n");
                        for (int x = 0; x < game.getArray().length; x++) {
                            for (int y = 0; y < game.getArray().length; y++) {
                                if (y == 2) {
                                    writeMessage(game.getArray()[x][y] + "");
                                } else {
                                    writeMessage(game.getArray()[x][y] + " | ");
                                }
                            }
                            writeMessage("\n");
                            writeMessage("----------\n");
                        }

               /* try {
                        writeMessage("Your turn: ");
                        move = (String) sInput.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }*/

                        game.playGame(move);

                        if (game.playerXCheck() == true) {
                            writeMessage("PLAYER X WINS!");
                            clients.get(recipientIndex).writeMessage("PLAYER X WINS!");
                            count = 0;
                        } else if (game.playerOCheck() == true) {
                            writeMessage("PLAYER O WINS!");
                            clients.get(recipientIndex).writeMessage("PLAYER O WINS!");
                            count = 0;
                        } else if (game.isTied() == true) {
                            writeMessage("TIE GAME!");
                            clients.get(recipientIndex).writeMessage("TIE GAME!");
                            count = 0;
                        }

                    } else {
                        broadcast(cm.getMsg() + "\n");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        //TODO: PART 3
        private synchronized void broadcast(String message) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();

            for (int x = 0; x < clients.size(); x++) {
                clients.get(x).writeMessage(formatter.format(date) + " " + username + ": " + message);
            }

            System.out.println(formatter.format(date) + " " + username + ": " + message);
        }

        private boolean writeMessage(String msg) {
            if (socket.isConnected()) { //TODO: this line might be wrong
                try {
                    sOutput.writeObject(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return false;
            }
        }

        private synchronized void remove(int id) {
            for (int x = 0; x < clients.size(); x++) {
                if (clients.get(x).id == id) {
                    clients.remove(x);
                }
            }
        }

        private void close() {
            try {
                sOutput.close();
                sInput.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}