import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
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
    private TicTacToeGame game = null;
    private ServerSocket serverSocket = null;

    /**
     * ChatServer constructor
     *
     * @param port - the port the server is being hosted on
     */
    private ChatServer(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * starts the ChatServer.
    */
    private void start() {
        try {
            //ServerSocket serverSocket = new ServerSocket(port);
            //skeleton
            /*Socket socket = serverSocket.accept();
            Runnable r = new ClientThread(socket, uniqueId++);
            Thread t = new Thread(r);
            clients.add((ClientThread) r);
            t.start();*/

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String time = sdf.format(new Date());
            System.out.println(time + " Server waiting for clients on port " + this.port + ".");
            //my implementation: keeps server in infinite loop
            while (true) {
                Socket socket = serverSocket.accept();
                // server waits until a client opens a Socket with the same address and port number
                Runnable r = new ClientThread(socket, uniqueId++);

                if(socket.isClosed()){
                    uniqueId--;
                }
                else{
                    Thread t = new Thread(r);
                    clients.add((ClientThread) r);
                    t.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void directMessage(String message, String username, String userSending) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        String formattedMessage = time  + " " + userSending + ": " + message + "\n";
        System.out.print(formattedMessage);

        if(!username.equalsIgnoreCase(userSending)) {
            for (ClientThread clientThread : clients) {
                if (clientThread.username.equalsIgnoreCase(username)) {
                    clientThread.writeMessage(formattedMessage);
                }
            }
        }
        else{
            for (ClientThread clientThread : clients) {
                if (clientThread.username.equalsIgnoreCase(username)) {
                    clientThread.writeMessage("You cannot send a Direct Message to yourself.\n");
                }
            }
        }
    }

    public static void main(String[] args) {
        int port = 1500;

        //switch port to a different value if one is passed
        if(args.length == 1){
            port = Integer.parseInt(args[0]);
        }

        ChatServer server = new ChatServer(port);
        server.start();
    }
    public int getPort(){
        return this.port;
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

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String time = sdf.format(new Date());
                System.out.println(time + " " + this.username + " just connected.");

                for (int x = 0; x < clients.size(); x++) {
                    if (clients.get(x).username.equalsIgnoreCase(this.username)) {
                        this.writeMessage("This client is already connected to the server.");
                        this.socket.close();
                        this.sOutput.close();
                        this.sInput.close();
                        remove(x);
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            // Read the username sent by client
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
                while(true) {
                    writeMessage("> ");
                    cm = (ChatMessage) sInput.readObject();
                    //my implementation
                    if (cm.getMsgType() == 1) {
                        System.out.println(this.username + " has logged out.");
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
                        //FIND RECIPIENT
                        int recipientIndex = 0;
                        for (int x = 0; x < clients.size(); x++) {
                            if (clients.get(x).username.equals(cm.getRecipient())) {
                                recipientIndex = x;
                                break;
                            }
                        }

                        if (count == 0) {
                            game = new TicTacToeGame(this.username, cm.getRecipient(), true);
                            writeMessage("Started TicTacToe with " + cm.getRecipient() + "\n");
                            clients.get(recipientIndex).writeMessage("\n" + this.username + " has started TicTacToe with you." + "\n");

                            //initialize array
                            game.initialize();
                            count++;
                        }
                        else {
                            if(game.currentPlayer.equals(this.username)) {
                                try {
                                    int move = Integer.parseInt(cm.getMsg().trim());
                                    game.playGame(move);
                                }catch(Exception e){
                                    writeMessage("Invalid move");
                                }
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

                                if (game.playerXCheck() == true) {
                                    writeMessage("PLAYER X WINS!\n");
                                    clients.get(recipientIndex).writeMessage("PLAYER X WINS!\n");
                                    count = 0;
                                    game = null;
                                    break;
                                } else if (game.playerOCheck() == true) {
                                    writeMessage("PLAYER O WINS!\n");
                                    clients.get(recipientIndex).writeMessage("PLAYER O WINS!\n");
                                    count = 0;
                                    game = null;
                                    break;
                                } else if (game.isTied() == true) {
                                    writeMessage("TIE GAME!\n");
                                    clients.get(recipientIndex).writeMessage("TIE GAME!\n");
                                    count = 0;
                                    game = null;
                                    break;
                                }
                            }
                            else{
                                writeMessage("It is currently the other player's turn.");
                            }
                        }

                    } else {
                        broadcast(cm.getMsg() + "\n");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
               //e.printStackTrace();
                System.out.println(this.username + " has logged out.");
                close();
                remove(this.id);
            }
        }

        private synchronized void broadcast(String message) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();

            for (int x = 0; x < clients.size(); x++) {
                clients.get(x).writeMessage(formatter.format(date) + " " + username + ": " + message);
            }

            System.out.println(formatter.format(date) + " " + username + ": " + message);
        }

        private boolean writeMessage(String msg) {
            if (socket.isConnected()) {
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
