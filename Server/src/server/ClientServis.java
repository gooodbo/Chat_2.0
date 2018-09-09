package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientServis implements Runnable {

    private Server server;
    private PrintWriter out;
    private Scanner in;
    private static final int PORT = 8888;
    private Socket clientSocket;
    private static int clientCount = 0;


    public ClientServis(Socket socket, Server server) {

        clientCount++;
        this.server = server;
        this.clientSocket = socket;
        try {

            this.out = new PrintWriter(socket.getOutputStream());
            this.in = new Scanner(socket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        while (true) {
            server.sendMsgToAll("Кто-то вошёл...");
            server.sendMsgToAll("Уже в чаике " + clientCount + " ...");
            break;
        }

        while (true) {

            if (in.hasNext()) {
                String clientMsg = in.nextLine();

                if (clientMsg.equalsIgnoreCase("exitnow")) {
                    System.out.println("Кто-то вышел НАХУЙ...");
                    break;
                }

                System.out.println(clientMsg);
                server.sendMsgToAll(clientMsg);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMsg(String msg) {
        try {
            out.println(msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        server.deleteClient(this);
        clientCount--;
        server.sendMsgToAll("Людей в чате: " + clientCount);
    }
}
