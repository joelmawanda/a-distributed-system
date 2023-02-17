//import java.io.*;
//import java.net.*;
//import java.util.*;
//
//public class TCPServer {
//    private static final int MAX_CLIENTS = 10;
//    private static final List<Socket> clients = new ArrayList<>();
//    private static final Map<Socket, Integer> ranks = new HashMap<>();
//
//    public static void main(String[] args) throws IOException {
//        ServerSocket serverSocket = new ServerSocket(10000);
//
//        while (true) {
//            // wait for a client to connect
//            System.out.println("Waiting for a connection...");
//            Socket socket = serverSocket.accept();
//
//            if (clients.size() < MAX_CLIENTS) {
//                clients.add(socket);
//                assignRank(socket);
//            } else {
//                OutputStream os = socket.getOutputStream();
//                os.write("Sorry, the server is full".getBytes());
//                os.flush();
//                socket.close();
//            }
//
//            // create a new thread for the client
//            new Thread(() -> {
//                try {
//                    InputStream is = socket.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//
//                    // receive data from the client
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        String[] parts = line.split(":");
//                        String command = parts[0];
//                        int rank = Integer.parseInt(parts[1]);
//
//                        if (rank < ranks.get(socket)) {
//                            for (Socket s : clients) {
//                                if (ranks.get(s) == rank) {
//                                    OutputStream os = s.getOutputStream();
//                                    os.write(line.getBytes());
//                                    os.flush();
//                                }
//                            }
//                        } else {
//                            OutputStream os = socket.getOutputStream();
//                            os.write("You cannot execute a command of a higher rank client".getBytes());
//                            os.flush();
//                        }
//                    }
//
//                    // remove the disconnected client and reorder the ranks
//                    clients.remove(socket);
//                    reorderRanks();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
//    }
//
//    private static void assignRank(Socket client) {
//        for (int i = 0; i < MAX_CLIENTS; i++) {
//            if (!ranks.values().contains(i)) {
//                ranks.put(client, i);
//                return;
//            }
//        }
//    }
//
//    private static void reorderRanks() {
//        for (int i = 0; i < clients.size(); i++) {
//            ranks.put(clients.get(i), i);
//        }
//    }
//}

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
    private static final int MAX_CLIENTS = 10;
    private static final List<Socket> clients = new ArrayList<>();
    private static final Map<Socket, Integer> ranks = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(10000);

        while (true) {
            // wait for a client to connect
            System.out.println("Waiting for a connection...");
            Socket socket = serverSocket.accept();

            if (clients.size() < MAX_CLIENTS) {
                clients.add(socket);
                assignRank(socket);
            } else {
                OutputStream os = socket.getOutputStream();
                os.write("Sorry, the server is full".getBytes());
                os.flush();
                socket.close();
            }

            // create a new thread for the client
            new Thread(() -> {
                try {
                    InputStream is = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                    // receive data from the client
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(":");
                        String command = parts[0];
                        int rank = Integer.parseInt(parts[1]);

                        if (rank < ranks.get(socket)) {
                            for (Socket s : clients) {
                                if (ranks.get(s) == rank) {
                                    OutputStream os = s.getOutputStream();
                                    os.write(line.getBytes());
                                    os.flush();
                                }
                            }
                        } else {
                            OutputStream os = socket.getOutputStream();
                            os.write("You cannot execute a command of a higher rank client".getBytes());
                            os.flush();
                        }
                    }
                } catch (IOException e) {
                    // log the exception
                    System.err.println("Error handling client request: " + e.getMessage());
                    e.printStackTrace();

                    // send an error message to the client
                    try {
                        OutputStream os = socket.getOutputStream();
                        os.write("An error occurred while processing your request. Please try again later.".getBytes());
                        os.flush();
                    } catch (IOException ex) {
                        // ignore this exception, since the client is already disconnected
                    }
                } finally {
                    // remove the disconnected client and reorder the ranks
                    clients.remove(socket);
                    reorderRanks();
                }
            }).start();
        }
    }

    private static void assignRank(Socket client) {
        for (int i = 0; i < MAX_CLIENTS; i++) {
            if (!ranks.values().contains(i)) {
                ranks.put(client, i);
                return;
            }
        }
    }

    private static void reorderRanks() {
        for (int i = 0; i < clients.size(); i++) {
            ranks.put(clients.get(i), i);
        }
    }
}
