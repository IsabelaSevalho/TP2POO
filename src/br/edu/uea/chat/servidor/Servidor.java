package br.edu.uea.chat.servidor;

import java.io.IOException;
import java.net.*;

import br.edu.uea.chat.cliente.ClienteThread;

/**
 * Esta classe serve para estabelecer conexão com uma porta, de modo a permitir a comunicação entre usuários pela porta 5000
 * 
 * @author Isabela Cunha
 * @version 1.0
 */

//processo independente
public class Servidor extends Thread{
	private static final int PORTA = 5000;
	private ServerSocket serverSocket;
	
	public void run(){
		//criando servidor
		try {
			serverSocket = new ServerSocket(PORTA);
			
			while (true) {
	            System.out.println("Aguardando conexão...");
	            Socket connection = serverSocket.accept();

	            System.out.println("ClienteThread conectado: "+ connection.getInetAddress().getHostName());
	            ClienteThread clienteThread = new ClienteThread(connection);

	            clienteThread.start();
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void close() throws IOException {
		if(serverSocket != null) {
			serverSocket.close();
			System.out.println("Fechando conexões.");
		}
	}
	
}
