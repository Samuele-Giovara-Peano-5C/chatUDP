/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient1file;

import chatudpclient.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matteo
 */
public class ChatUDPclient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {

        String IP_address = "127.0.0.1";
        InetAddress address = InetAddress.getByName(IP_address);
        int UDP_port = 1077;


        DatagramSocket socket;
        try {

            socket = new DatagramSocket();
            //socket.setSoTimeout(1000);
            


        Thread sendUserInput = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    byte[] buffer;
                    String messaggio;
                    Scanner tastiera = new Scanner(System.in);
                    DatagramPacket userDatagram;
                    
                    System.out.print("> ");
                    do {
                        //Leggo da tastiera il messaggio utente vuole inviare
                         messaggio = tastiera.nextLine();

                        //Trasformo in array di byte la stringa che voglio inviare
                        buffer = messaggio.getBytes("UTF-8");

                        // Costruisco il datagram di richiesta
                            userDatagram = new DatagramPacket(buffer, buffer.length, address, UDP_port);
                        // spedisco il datagram
                            socket.send(userDatagram);
                    } 
                    while (messaggio.compareTo("quit") != 0);
                } 
                catch (IOException ex) {
                    Logger.getLogger(ChatUDPclient.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        Thread receiveAndPrint = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    byte[] buffer = new byte[100];
                    String received;
                    DatagramPacket serverDatagram;
                    
                    // Costruisco il datagram di richiesta
                    serverDatagram = new DatagramPacket(buffer, buffer.length);
                    // ascolto 
                    while (!Thread.interrupted()){
                            socket.receive(serverDatagram);
                            received = new String(serverDatagram.getData(), 0, serverDatagram.getLength(), "ISO-8859-1");
                        //System.out.print("\33[1A\33[2K");
                        //System.out.print("\b\b\b\b\b\b");
                            System.out.print("\b\b\b\b\b\b> server: " + received);
                            System.out.print("\n> ");
                    }

                } 
                catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(ChatUDPclient.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (IOException ex) {
                    Logger.getLogger(ChatUDPclient.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

            System.out.println("connessione server riuscita");
        
        //mi metto in ascolto per eventuali messaggi che arrivano dal server e quindi li scrivo su terminale
            receiveAndPrint.start();
            System.out.println("sono in ascolto...");
        
        //mi metto in attesa per messaggi che arrivano da utente (tramite tastiera) e quindi li invio al server
            sendUserInput.start();
            System.out.println("utente e' invitato di inserire un messaggio da inviare al server...");
        
        //mi metto in attesa che utente voglia terminare digitando "quit" e quindi chiudo baracca e burattini
            sendUserInput.join();
            receiveAndPrint.interrupt();
            socket.close();
        
        } 
        catch (SocketException ex) {
            System.out.println("ERROR: connessione server non riuscita");
        } 
        catch (InterruptedException ex) {
            Logger.getLogger(ChatUDPclient.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    
}
