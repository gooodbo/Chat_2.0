package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ArrayList<ClientServis> clients = new ArrayList<>();
    private final static int PORT = 8888;

    public Server() {
        Socket clientSocket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Пошла ебатория...(кто то врубил сервер)");

            while (true) {
                clientSocket = serverSocket.accept();
                ClientServis client = new ClientServis(clientSocket, this);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {

                assert clientSocket != null;
                clientSocket.close();
                System.out.println("Сервер остановал свою работу");
                //Саня писос код не чекает полностью
                serverSocket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void sendMsgToAll(String msg) {
        for (ClientServis xyi : clients) {
            xyi.sendMsg(msg);
        }
    }

    public void deleteClient(ClientServis client) {
        clients.remove(client);
    }
}
