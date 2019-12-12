package chatudpclient;

import java.awt.GridLayout;
import java.awt.event.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.net.*;
import java.io.UnsupportedEncodingException;


public class Gui extends JFrame implements ActionListener {

    JPanel p;
    JLabel i;
    JTextField input;
    JTextArea output;
    JScrollPane sjp;
    JButton invia;
    JButton inizia;

    DatagramSocket socket = new DatagramSocket();

    public Gui() throws SocketException {
       
    

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[100];
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                for(;;){
                try {
                    socket.receive(dp);
                    String received = new String(dp.getData(), 0, dp.getLength(), "ISO-8859-1");
                    output.setText(received);

                } catch (IOException ex) {
                    Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }}
        });

        socket = new DatagramSocket();
            this.setBounds(500, 200, 200, 500);
                this.setTitle("UDP chat");
                    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.p = new JPanel();
                p.setLayout(new GridLayout(4, 1));
            this.i = new JLabel("Metti qua il messaggio");
                i.setEnabled(false);
            this.input = new JTextField();
            this.output = new JTextArea();
            this.sjp = new JScrollPane(output);
            this.invia = new JButton("Invia Il messaggio");
                invia.addActionListener(this);
            this.inizia = new JButton("Start!");
                inizia.addActionListener(this);

        this.add(p);
        p.add(i);
        p.add(input);
        p.add(inizia);
        p.add(invia);
        p.add(sjp);

            //this.pack();
            this.setSize(500, 700);
            this.setVisible(true);
    }

    String username;

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Start!")) {
            username = JOptionPane.showInputDialog(p,
                    "Inserisci user name", "Setting...", JOptionPane.ERROR_MESSAGE);
            output.append("User: " + username + "\n");
        }
        if (ae.getActionCommand().equals("Start!")) {
            String i = input.getText();
            output.setText(username + "> " + i);
            try {
                this.Invia(i);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void Invia(String messaggio) throws UnknownHostException, IOException {

        InetAddress ind = InetAddress.getByName("localhost");
            int UDP_port = 1088;
                byte[] buffer = new byte[100];
                    buffer = messaggio.getBytes();
                        DatagramPacket userDatagram = new DatagramPacket(buffer, buffer.length, ind, UDP_port);
                            socket.send(userDatagram);
                                   JOptionPane.showMessageDialog(p, "Messaggio Inviato", "Send", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) throws SocketException {

        Gui g = new Gui();

    }
}