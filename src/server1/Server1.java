package server1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


public class Server1 {
	
	private DatagramSocket multiSocket;
	private InetAddress group;
    private Scanner teclado;
    private String receiver;
    
    private ArrayList<String> results;

	public Server1() throws IOException{
		
		// Enviar msg
		this.multiSocket = new DatagramSocket();
		this.group = InetAddress.getByName("230.0.0.0");
		
		results = new ArrayList<>();
        this.teclado = new Scanner(System.in);

	}
	
	
	public void init() throws IOException {
		
		
		// Fazer a ação aqui
		System.out.println("Iniciar sendo o principal digite sim:");
		String option = teclado.nextLine();
		
		
		if(option.equalsIgnoreCase("sim")) {
			
			String mensagem = "calcular";
			byte[] buf = mensagem.getBytes();
	    	DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
			this.multiSocket.send(packet);
			
			
			// Fazer algo aqui
			
			
		}
		
		
		System.out.println("Finalizar");
	}
	
	
	private void sendMsg(String mensagem) {
		
		try {
			
			byte[] buf = mensagem.getBytes();
	    	DatagramPacket packet = new DatagramPacket(buf, buf.length, this.group, 4446);
			this.multiSocket.send(packet);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void receivedMsg() {
		
		try {
			
			MulticastSocket multicast = new MulticastSocket(4446);
			byte[] buf = new byte[256];
    		
    		while (this.results.size() < 2) {
        	
        		DatagramPacket packet = new DatagramPacket(buf, buf.length);
        		multicast.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                
                
	            System.err.println(received);
	            
	            this.results.add(received);  
           
            }
    		
    		
    		multicast.leaveGroup(this.group);
    		multicast.close();
        
    	 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	}
	

}
