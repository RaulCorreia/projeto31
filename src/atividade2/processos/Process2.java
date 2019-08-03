package atividade2.processos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Process2 implements MyCallBack2{

	
	// Multicast
    private DatagramSocket sendMultiCast;
    private InetAddress group;
    private byte[] buf;
	
    private String assinatura = "process2";
    private Scanner teclado;
	
	public Process2() throws IOException {
		
		this.sendMultiCast = new DatagramSocket();
        this.group = InetAddress.getByName("230.0.0.0");
		
		
		// Se fez o login corretamente inicia o ouvinte do multicast
		CastReceiver multicast = new CastReceiver(this.assinatura, this);
        Thread t = new Thread(multicast);
        t.start();
        
        
        this.teclado = new Scanner(System.in);
	}
	
	public void menu() {
    	System.out.println("Iniciar apresentações?");
    	String option = teclado.nextLine();
    	
    	if(option.equalsIgnoreCase("Sim")) {
    		sendCast("presentation");
    	} else {
    		
    		while(true) {
    			
    			System.out.println("Iniciar comunicação (sem) falha ou (com) falha?");
    			option = teclado.nextLine();
    			
    			if(option.equalsIgnoreCase("sem")) {
    				
    				System.out.println("Quanto é 2 + 2: ");
    				sendCast("sem");
    				
    			} else {
    				
    				System.out.println("Quanto é 2 + 2: ");
    				sendCast("com");
    			}
    			
    		}
    		
    		
    	}
    }
	
	
	private void sendCast(String mensagem) {
    	
    	try {
    		
    		mensagem = mensagem + "!" + this.assinatura ;
	    	buf = mensagem.getBytes();
	    	DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
	    	sendMultiCast.send(packet);
	    	
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	
	
	
	// Thread para ouvir os multicast
	//--------------------------------------------------------------------------
	public class CastReceiver extends Thread {
		
		protected MulticastSocket receivMultiCast;
		protected InetAddress group;
	    protected byte[] buf = new byte[256];
	    protected String sign;
	    protected Map<String, String> processos;
	    
	    protected MyCallBack2 callback;
	    
	    
	    public CastReceiver(String sign, MyCallBack2 callback) throws IOException {
	    	
	    	this.sign = sign;
	    	
	    	this.receivMultiCast = new MulticastSocket(4446);
	    	this.group = InetAddress.getByName("230.0.0.0");
	        this.receivMultiCast.joinGroup(this.group);
	        
	        
	        this.callback = callback;
	        this.processos = new HashMap<>();
	        
	    }
	    
	 
	    public void run() {
	    	
	    	try {
	    		
	    		while (true) {
	        	
	        		DatagramPacket packet = new DatagramPacket(buf, buf.length);
	            
					this.receivMultiCast.receive(packet);
				
					
	                String received = new String(packet.getData(), 0, packet.getLength());
	                String split[] = received.split("!");
	                System.out.println("Mensagem Recebida");
	                
		               if("presentation".equalsIgnoreCase(split[0])) {
		                	
		            	   System.out.println("Se aprezente...");
		                	// Recebeu msg para se apresentar, ja adiciona quem enviou a lista
		                	this.processos.put(split[1], split[1]);
		                	callback.callBackRetorno(split[0]+",");
		                	
		                } else if("myname".equalsIgnoreCase(split[0])) {
		                	
		                	System.out.println("Processo: " + split[1] + " se apresentou.");
		                	this.processos.put(split[1], split[1]);
		                	
		                } else if("exclude".equalsIgnoreCase(split[0])) {
		                	
		                	
		                	if(split[1].equalsIgnoreCase(this.sign)) {
		                		System.out.println("Processo finalizado por mal funcionamento");
	                			break;
	                		} else {
	                			System.out.println("Processo: " + split[1] + " foi excluido do grupo.");
	                		}
		                	
//		                	boolean exclude = true;
//		                	for(int i = 1; i < split.length; i++) {
//		                		
//		                		// Se tiver o nome na lista, se mantem
//		                		// se nao encontrar o nome na lista sai do grupo
//		                		if(split[i].equalsIgnoreCase(this.sign)) {
//		                			exclude = false;
//		                			break;
//		                		}
//		                		
//		                	}
		                	
		                	// Nao encontrou o nome na lista finaliza o processo
//		                	if(exclude) {
//		                		break;
//		                	}
		                	
		                } else if ("sem".equalsIgnoreCase(split[0])){
		                	
		                	System.out.println("Quanto é 2 + 2?");
		                	callback.callBackRetorno("respSem,");
		                	
		                } else if ("com".equalsIgnoreCase(split[0])) {
		                	
		                	System.out.println("Quanto é 2 + 2?");
		                	callback.callBackRetorno("respCom,");
		                	
		                } else {
		                	
		                	// Respostas
		                	String teste[] = split[0].split("@");
		                	
		                	if("respSem".equalsIgnoreCase(teste[0])) {
		                		
		                		
	                			System.out.println("Processo de id: " + split[1] + " respondeu: " + teste[1]);
		                			
		                		
		                	} else if("respCom".equalsIgnoreCase(teste[0])) {
		                		
	                			if(teste[1].equalsIgnoreCase("4")) {
		                			
		                			System.out.println("Processo de id: " + split[1] + " respondeu: " + teste[1]);
		                			
		                		} else {
		                			
		                			System.out.println("Processo de id: " + split[1] + " respondeu: " + teste[1]);
		                			System.out.println("Aparentemente o processo esta com problemas, sera rmeovido do grupo");
		                			callback.callBackRetorno("end," + split[1]);
		                			
		                		}
		                		
		                	}
		                	
		                }
		                
		           
		            }
		    		
	    			callback.callBackRetorno("endMe,");
		    		System.out.println("Processo: " + this.sign + " excluido do grupo");
		    		this.receivMultiCast.leaveGroup(group);
		    		this.receivMultiCast.close();
		        
		    	 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		    	 }
		    	
		    	
		    }

		    
		}


		@Override
		public void callBackRetorno(String acao) {
			
			String opcao[] = acao.split(",");
			
			if(opcao[0].equalsIgnoreCase("presentation")) {
				
				System.out.println("Me apresentando");
				sendCast("myname");
				
			} else if(opcao[0].equalsIgnoreCase("respSem")) {
				
				System.out.println("Enviando resposta 4");
				sendCast("respSem@4");
				
			} else if(opcao[0].equalsIgnoreCase("respCom")) {
				
				System.out.println("Enviando resposta 4");
				sendCast("respCom@4");
				
			} else if(opcao[0].equalsIgnoreCase("end")) {
				
				System.out.println("Excluir processo: " + opcao[1]);
				sendCast("exclude!"+opcao[1]);
				
			}
			
		}

}
