package client;

import server.ClientServis;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static server.ClientServis.clientCount;

public class ClientWindow extends JFrame {

    private static final int PORT = 8888;
    private static final String IP = "127.0.0.1";
    private Socket clientSocket;
    private Scanner in;
    private PrintWriter out;
    private JTextArea msgAreaOut;
    private JTextField nameField;
    private JTextField msgAreaIn;
    private String clientName = "";

    public ClientWindow() {
        try {
            clientSocket = new Socket(IP, PORT);
            in = new Scanner(clientSocket.getInputStream());
            out = new PrintWriter(clientSocket.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

        setBounds(600, 300, 700, 600);
        setTitle("Залупенище");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        msgAreaOut = new JTextArea();
        msgAreaOut.setEditable(false);
        msgAreaOut.setLineWrap(true);

        JScrollPane jScrollPane = new JScrollPane(msgAreaOut);
        add(jScrollPane, BorderLayout.CENTER);

        JPanel bottom1 = new JPanel(new BorderLayout());
        add(bottom1, BorderLayout.SOUTH);

        JButton jButton = new JButton("Адаслаць");
        bottom1.add(jButton, BorderLayout.EAST);

        msgAreaIn = new JTextField("Увазди свой мэсэдж...");
        bottom1.add(msgAreaIn, BorderLayout.CENTER);

        nameField = new JTextField("Пут ин ёр нэйм");
        bottom1.add(nameField, BorderLayout.WEST);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!msgAreaIn.getText().trim().isEmpty() && !nameField.getText().trim().isEmpty()) {

                    clientName = nameField.getText();
                    sendMsg();
                    msgAreaIn.grabFocus();
                }
            }
        });

        msgAreaIn.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                msgAreaIn.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });

        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                nameField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if (in.hasNext()) {

                        String msg = in.nextLine();
                        msgAreaOut.append(msg);
                        msgAreaOut.append("\n");
                    }
                }
            }
        }).start();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                try {
                    if (!clientName.isEmpty() && !clientName.equals("Увазди свой мэсэдж...")) {
                        out.println("[ \"" + clientName + "\" ливнул]");
                    } else {
                        out.println("[Вышел ноунейм]");
                    }

                    out.println("exit");
                    out.flush();
                    out.close();
                    clientSocket.close();

                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
        });

        setVisible(true);

    }

    public synchronized void sendMsg() {

        String msg = nameField.getText() + ": " + msgAreaIn.getText();
        out.println(msg);
        out.flush();
        msgAreaIn.setText("");
    }

}
