package coordenador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;



public class Executor implements Runnable{
	
	public Socket socketProcesso;
	private DataInputStream in; 
    private DataOutputStream out;
    private MyCallBack callback;
    
	public Executor (Socket socketProcesso, MyCallBack callback) throws IOException {
		
		this.socketProcesso = socketProcesso;
        this.in = new DataInputStream(this.socketProcesso.getInputStream());
        this.out = new DataOutputStream(this.socketProcesso.getOutputStream());
        this.callback = callback;
        
	}

	 public void run(){
		 
		 
		 try {
			 System.out.println("INICIOU EXECUTOR");
			 this.out.writeBoolean(true);
			 
			 String mensagem = this.in.readUTF();
			 
			 //----
			 // Sessão critica, gravando no arquivo
			 FileWriter arq = new FileWriter("arquivoCompartilhado.txt", true);
			 PrintWriter gravarArq = new PrintWriter(arq);
			 gravarArq.println(mensagem);
			 arq.close();
			 //----
			 
			 TimeUnit.SECONDS.sleep(10);
			 this.out.writeUTF("Escrita concluida");
			 
			 this.in.close();
			 this.out.close();
			 this.socketProcesso.close();
			 
			 this.callback.callBackRetorno();
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		 
		 
	 }
	 
}
