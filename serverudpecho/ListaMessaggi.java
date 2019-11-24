/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverudpecho;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pc15
 */
public class ListaMessaggi implements Runnable{
    
    private ArrayList<String> lista = new ArrayList<String>();
    private DatagramSocket ds;
    private Clients c;

    public ListaMessaggi() {
    }
    
    public void aggiungiMessaggio(String a){
        
        if(lista.size()<= 10){
            lista.add(a);  
        }else{
            lista.remove(0);
            lista.add(a);
        }   
    }
    
    public void mandaMessaggi(DatagramSocket socket, Clients c){  
        this.ds= socket;
        this.c= c;
        new Thread(this).start();
    }

    @Override
    public void run() {
        for(int i=0;i<lista.size();i++){
            try {
                byte[] buffer = new byte[lista.get(i).length()];
                buffer = lista.get(i).getBytes("UTF-8");
                DatagramPacket mess = new DatagramPacket(buffer,buffer.length,c.addr,c.port);
                ds.send(mess);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ListaMessaggi.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ListaMessaggi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
