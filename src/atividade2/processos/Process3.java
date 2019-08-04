package atividade2.processos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Process3 implements MyCallBack2{

	
	// Multicast
    private DatagramSocket sendMultiCast;
    private InetAddress group;
    private byte[] buf;
	
    private String assinatura = "process3";
    private Scanner teclado;
	
	public Process3() throws IOException {
		
		this.sendMultiCast = new DatagramSocket();
        this.group = InetAddress.getByName("230.0.0.0");
		
		
		// Se fez o login corretamente inicia o ouvinte do multicast
		CastReceiver multicast = new CastReceiver(this.assinatura, this);
        Thread t = new Thread(multicast);
        t.start();
        
        
        this.teclado = new Scanner(System.in);
        System.out.println("Processo " + this.assinatura + " pronto.");
	}
	
	public void menu() {
    	System.out.println("Iniciar apresentações?");
    	String option = teclado.nextLine();
    	
    	if(option.equalsIgnoreCase("Sim")) {
    		sendCast("presentation");
    	}
    	
    		
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
	    protected Map<String, Integer> processos;
	    
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
		                	this.processos.put(split[1], 0);
		                	callback.callBackRetorno(split[0]+",");
		                	
		                } else if("myname".equalsIgnoreCase(split[0])) {
		                	
		                	System.out.println("Processo: " + split[1] + " se apresentou.");
		                	this.processos.put(split[1], 0);
		                	
		                } else if("exclude".equalsIgnoreCase(split[0])) {
		                	
		                	
		                	if(split[1].equalsIgnoreCase(this.sign)) {
		                		System.out.println("Processo finalizado por mal funcionamento");
	                			break;
	                		} else {
	                			System.out.println("Processo: " + split[1] + " foi excluido do grupo.");
	                		}
		                	
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
		                			
		                		} else if (!split[1].equalsIgnoreCase(this.sign)){
		                			
		                			System.out.println("Processo de id: " + split[1] + " respondeu: " + teste[1]);
		                			System.out.println("Aparentemente o processo esta com problemas, sera rmeovido do grupo");
		                			callback.callBackRetorno("voteTerminate," + split[1]);
		                			
		                			
		                		}
		                		
		                	} else if ("voteTerminate".equalsIgnoreCase(teste[0])) {
		                		
		                		System.out.println("Votação para finalizar o processo: " + teste[1] + " recebida");
		                		
		                		// Armazena a votação
		                		int vote = processos.get(teste[1]) + 1;
		                		processos.put(teste[1], vote);
		                		
		                		if(vote >= 2) {
		                			callback.callBackRetorno("terminate," + teste[1]);
		                			processos.put(teste[1], 0);
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
				
				System.out.println("Enviando resposta 3");
				sendCast("respCom@3");
				
			} else if(opcao[0].equalsIgnoreCase("voteTerminate")) {
				
				System.out.println("Votação terminar o processo: " + opcao[1]);
				sendCast("voteTerminate@"+opcao[1]);
				
			} else if(opcao[0].equalsIgnoreCase("terminate")) {
				
				System.out.println("Excluir processo por votação: " + opcao[1]);
				sendCast("exclude!"+opcao[1]);
				
			}
			
		}

}
