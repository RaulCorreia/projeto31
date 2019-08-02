package teste;

import java.util.ArrayList;

public class Teste2 {

	private ArrayList<String> processos;
	
	public Teste2(ArrayList<String> processos) {
		this.processos = processos;
		
	}
	
	
	public void run() {
		
		
		this.processos.remove(0);
		
	}
}
