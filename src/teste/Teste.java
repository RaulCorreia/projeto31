package teste;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Teste {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		ArrayList<String> processos = new ArrayList<>();
		processos.add("TESTE 1");
		processos.add("TESTE 2");
		processos.add("TESTE 3");
		
		
		Teste2 teste2 = new Teste2(processos);
		
		while(true) {
			
			System.out.println(processos.size());
			System.out.println(processos.get(0));
			
			teste2.run();
			
			TimeUnit.SECONDS.sleep(10);
			
		}

	}

}
