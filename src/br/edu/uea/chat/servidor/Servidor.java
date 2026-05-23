package br.edu.uea.chat.servidor;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * Esta classe serve para estabelecer conexão com uma porta, de modo a permitir a comunicação entre usuários pela porta 5000
 * 
 * Essa versão abre a porta 5000, espera uma conexão com cliente (ClienteThread),
 * inicia a thread e fecha as conexões ao finalizar o servidor
 * 
 * @version 2.0
 */

//processo independente
public class Servidor extends Thread{
	private static final int PORTA = 5000;
	private ServerSocket serverSocket;

	private static ArrayList<ClienteThread> clientesOnline = new ArrayList<>();
	
	public void run(){
		//criando servidor
		try {
			serverSocket = new ServerSocket(PORTA);
			
			while (true) {
	            System.out.println("Aguardando conexão...");
	            Socket connection = serverSocket.accept();

	            System.out.println("Cliente conectado: "+ connection.getInetAddress().getHostName());
	            ClienteThread clienteThread = new ClienteThread(connection);

	            clienteThread.start();
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void close() throws IOException {
		if(serverSocket != null) {
			serverSocket.close();
			System.out.println("Fechando conexões.");
		}
	}

	public static ArrayList<ClienteThread> getClientesOnline(){
		return clientesOnline;
	}

	public static void addClienteOnline(ClienteThread clienteOnline){
		clientesOnline.add(clienteOnline);
	}

	public static void removeClienteOnline(ClienteThread clienteOnline){
		clientesOnline.remove(clienteOnline);
	}
	
}
