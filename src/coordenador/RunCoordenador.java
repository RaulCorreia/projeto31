package coordenador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class RunCoordenador {
	
	public static void main(String[] args) throws IOException, InterruptedException {

		ServerSocket socketServidor = new ServerSocket(12345);
		Coordenador coord = new Coordenador();
		
		int count = 0;
		
		System.out.println("Aguardando conexão dos processos...");
		
		while (true) {
			
	        Socket socket = socketServidor.accept();
	        coord.addSocket(socket);
	        
//	        count++;
//	        if(count == 2) {
	        	coord.runProcess();
//	        }
	        
	        
        }
		
	}

}
