package chatudpclient;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class ClientFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtInput;
	private JTextField nickName;
	private static JTextArea textArea;
	private static DatagramSocket socket;
	private static String IP_address = "127.0.0.1";
	private static InetAddress address;
	private static int UDP_port;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		//String IP_address = "127.0.0.1";
        try {
		address = InetAddress.getByName(IP_address);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        UDP_port = 1077;
        try {
			socket= new DatagramSocket();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientFrame frame = new ClientFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws InterruptedException 
	 */
	public ClientFrame() throws InterruptedException {
		setTitle("Group CHAT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 320, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		scrollPane.setViewportView(textArea);
		
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		txtInput = new JTextField();
		panel.add(txtInput);
		txtInput.setColumns(15);
		
		JButton btnInvio = new JButton("INVIO");
		panel.add(btnInvio);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNickname = new JLabel("NICKNAME: ");
		lblNickname.setFont(new Font("Times New Roman", Font.BOLD, 14));
		panel_1.add(lblNickname);
		
		nickName = new JTextField();
		panel_1.add(nickName);
		nickName.setColumns(10);
		
		
		
		btnInvio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(nickName.getText().isEmpty()) {
					invia(txtInput.getText(), "UTENTE ");
				}
				else {
					invia(txtInput.getText(), nickName.getText());
				}
			}
			
		});
		
		Thread ricevi= new Thread() {
			@Override
			public void run() {
				try {
					ricevi();
				}
				catch(Exception ex) {
					
				}
			}
		};
		ricevi.start();
		textArea.append("Connessione server riuscita\n");
		
	}
	
	public static void inizioConnessione() {
		
	}
	
	public static void invia(String messaggio, String nome) {
		byte[] buffer;
        DatagramPacket userDatagram;

        try {

            //do {
                //Leggo da tastiera il messaggio utente vuole inviareciac
                messaggio= nome.concat(": "+messaggio);//Ogni volta al MEX viene concatenato il nickname per rendere riconoscibile il client nella chat
                //Trasformo in array di byte la stringa che voglio inviare
                buffer = messaggio.getBytes("UTF-8");

                // Costruisco il datagram (pacchetto UDP) di richiesta 
                // specificando indirizzo e porta del server a cui mi voglio collegare
                // e il messaggio da inviare che a questo punto si trova nel buffer
                userDatagram = new DatagramPacket(buffer, buffer.length, address, UDP_port);
                // spedisco il datagram
                socket.send(userDatagram);
            //} while (messaggio.compareTo("quit") != 0); //se utente digita quit il tread termina
            //socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatUDPclient.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public static void ricevi() {
		byte[] buffer = new byte[100];
        String received="abc";
        DatagramPacket serverDatagram;

        try {
            // Costruisco il datagram per ricevere i pacchetti inviati dal server
            serverDatagram = new DatagramPacket(buffer, buffer.length);
            // fino a quando il main non interrompe il thread rimango in ascolto 
            while (!Thread.interrupted()){
            	//System.out.println("Connessione server riuscita");
                socket.receive(serverDatagram);  //attendo il prossimo pacchetto da server
                //converto in string il messaggio contenuto nel buffer
                received = new String(serverDatagram.getData(), 0, serverDatagram.getLength(), "ISO-8859-1");
                textArea.append(received+"\n");
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
            socket.close();
            

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReceiveFromServerAndPrint.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReceiveFromServerAndPrint.class.getName()).log(Level.SEVERE, null, ex);
        }
		
	}

}