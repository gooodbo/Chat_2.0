package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientServis extends Thread {
    private PrintWriter out;
    private Scanner in;
    private static int clientCount = 0;
    private boolean flag = false;

    public ClientServis(Socket socket) {
        try {
            clientCount++;
            this.out = new PrintWriter(socket.getOutputStream());
            this.in = new Scanner(socket.getInputStream());
            start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            sendMsg("[Кто-то вошёл, уже в чате " + clientCount + "]");

            while (!flag) {
                if (in.hasNext()) {

                    String clientMsg = in.nextLine();

                    if (clientMsg.equals("exit")) {
                        flag = true;
                        break;
                    }

                    for (ClientServis xyi : Server.clients) {
                        xyi.sendMsg(clientMsg);
                    }

                    System.out.println(clientMsg);

                }

                Thread.sleep(100);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Я в файнали");
            this.close();
        }
    }

    private synchronized void close() {
        clientCount--;
       this.sendMsg("[Людей в чате: " + clientCount + "]");
        Server.clients.remove(this);
    }

    private void sendMsg(String msg) {
        try {
            out.println(msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
