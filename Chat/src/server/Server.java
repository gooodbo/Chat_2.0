package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static ArrayList<ClientServis> clients = new ArrayList<>();
    private final static int PORT = 8888;

    public Server() {
        Socket socket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Пошла ебатория...(кто то врубил сервер)");

            while (true) {
                socket = serverSocket.accept();
                clients.add(new ClientServis(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {

                assert socket != null;
                socket.close();
                serverSocket.close();
                System.out.println("Сервер экстренно остановал свою работу...");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
