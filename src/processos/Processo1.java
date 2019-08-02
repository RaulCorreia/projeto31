package processos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Processo1 {

	public static void main(String[] args) throws IOException, InterruptedException {

		Socket socket = new Socket("127.0.0.1", 12345);
		DataInputStream in = new DataInputStream(socket.getInputStream()); 
	 	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	 	
	 	System.out.println("enter()");
	 	//out.writeBoolean(true);
	 	
	 	
	 	while(true) {

		 	System.out.println("Aguardando resposta");
		 	boolean conn = in.readBoolean();
		 	System.out.println("Resposta recebida: "+ conn);
		 	
		 	if(conn) {
		 		
		 		System.out.println("Sessão critica liberada");
		 		System.out.println("resourceAccesses()");
		 		System.out.println("Acessando o recurso compartilhado");
		 		
		 		
		 		out.writeUTF("Escrita do processo 1");
		 		System.out.println("Enviou mensagem a ser escrita no arquivo");
		 		
		 		String result = in.readUTF();
		 		System.out.println("Resultado: " + result);
		 		
		 		System.out.println("exit()");
		 		System.out.println("Saindo da sessão critica, liberando recurso");
		 		System.out.println("Finalizado processo");
		 		
		 		break;
		 		
		 	} else {
		 		System.out.println("No momento o recurso compartilhado esta bloqueado por outro processo");
		 		System.out.println("Voce esta na fila, aguarde ate ser chamado");
		 	}
	 	
	 	}
	 	
	 	
	 	socket.close();
        in.close(); 
        out.close();
		
	}
}
