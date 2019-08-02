package coordenador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
			 
			 this.out.writeBoolean(true);
			 
			 int x = this.in.readInt();
			 int y = this.in.readInt();
			 
			 
			 System.out.println("X: " + x);
			 System.out.println("Y: " + y);
			 int result = multi(x, y);
			 System.out.println("RESULTADO: " + result);
			 TimeUnit.SECONDS.sleep(10);
			 
			 this.out.writeUTF(Integer.toString(result));
			 
			 this.in.close();
			 this.out.close();
			 this.socketProcesso.close();
			 
			 this.callback.callBackRetorno();
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		 
		 
	 }
	 
	 
	 private int multi(int x, int y) {
		return x * y;
	 }
}
