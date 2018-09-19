package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientServis implements Runnable {

    private Server server;
    private PrintWriter out;
    private Scanner in;
    private Socket clientSocket = null;
    public static int clientCount = 0;
    private boolean flag = false;


    public ClientServis() {

    }

    public void connect(Socket socket, Server server) {
        try {
            clientCount++;
            this.server = server;
            this.clientSocket = socket;
            this.out = new PrintWriter(socket.getOutputStream());
            this.in = new Scanner(socket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ClientServis getClient() {
        return this;
    }

    @Override
    public void run() {
        try {

            server.sendMsgToAll("[Кто-то вошёл, уже в чате " + clientCount + "]");

            while (!flag) {
                if (in.hasNext()) {

                    String clientMsg = in.nextLine();

                    if (clientMsg.equals("exit")) {
                        flag = true;
                        break;
                    }

                    System.out.println(clientMsg);
                    server.sendMsgToAll(clientMsg);
                }

                Thread.sleep(100);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }


    public synchronized void sendMsg(String msg) {
        try {
            out.println(msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void close() {
        server.deleteClient(this);
        clientCount--;
        server.sendMsgToAll("[Людей в чате: " + clientCount + "]");
    }
}
