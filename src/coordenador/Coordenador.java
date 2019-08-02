package coordenador;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;



public class Coordenador implements MyCallBack{
	
	public Socket socketProcesso;
    private ArrayList<Socket> processos;
    
    private boolean bloqueado;
    
  
    
    public Coordenador()throws IOException, InterruptedException {
    	
    	processos = new ArrayList<>();
	    this.bloqueado = false;    
        
	}
    
	
	public void addSocket(Socket socket) throws IOException {
		
		System.out.println("Conexão adicionada a fila");
		processos.add(socket);
		
		if(processos.size() > 1) {
			DataOutputStream out2 = new DataOutputStream(socket.getOutputStream());
			out2.writeBoolean(false);
		} 
		
	}
	
	
	public void runProcess() throws InterruptedException, IOException {
		
		while(true) {
			
			// So roda se tiver processos em espera e nao tiver bloqueado o acesso ao recurso
			if(processos.size() > 0 && !this.bloqueado) {
				
				this.bloqueado = true;
				
				Executor exec = new Executor(this.processos.get(0), this);
		        Thread t = new Thread(exec);
		        t.start();
		        
			} else {
				TimeUnit.SECONDS.sleep(10);
			}
			
		}
		
	}
	


	@Override
	public void callBackRetorno() {
		// TODO Auto-generated method stub
		this.processos.remove(0);
		this.bloqueado = false;
	}
}
