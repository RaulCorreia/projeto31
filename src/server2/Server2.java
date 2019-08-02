package server2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Scanner;


public class Server2 {
	
	
	private DatagramSocket multiSocket;
	private InetAddress group;
    private Scanner teclado;
    private String receiver;
    
    private ArrayList<String> results;
	
	public Server2() throws IOException{
		
		// Enviar msg
		this.multiSocket = new DatagramSocket();
		this.group = InetAddress.getByName("230.0.0.0");
		
		results = new ArrayList<>();
		

		System.out.println(results.size());
		
		// Ficar escutando
		MulticastReceiver multicast = new MulticastReceiver(group);
        Thread t = new Thread(multicast);
        //t.start();
		
        
        this.teclado = new Scanner(System.in);
	}
	
	
	public void init() throws IOException {
		
		
		
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
	
	
	private String calc() {
		
		
		return "Result";
	}
	
	
	
	private void receivedMsg() {
		
		try {
			
			MulticastSocket multicast = new MulticastSocket(4446);
			byte[] buf = new byte[256];
    		
    		while (this.results.size() < 1) {
        	
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
	
	public class MulticastReceiver extends Thread {
		
	    protected MulticastSocket multicast = null;
	    protected byte[] buf = new byte[256];
	    protected InetAddress group;
	    protected boolean run = true;
	    
	    
	    public MulticastReceiver(InetAddress group) throws IOException{
	    	this.multicast = new MulticastSocket(4446);
	    	this.group = group;
	    }
	    
	    
	    
	 
	    public void run() {
	    	try {
	    		
	    		while (true) {
	        	
	        		DatagramPacket packet = new DatagramPacket(buf, buf.length);
	            
	        		multicast.receive(packet);
				
	                String received = new String(packet.getData(), 0, packet.getLength());
	                
	                if ("end".equals(received)) {
	                	
	                    break;
	                    
	                } else {
	                	
		                System.err.println(received);
	                	
	                	
	                }
	           
	            }
	    		
	    		
	    		multicast.leaveGroup(group);
	    		multicast.close();
	        
	    	 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
	    }
	    
	    
	}

}
