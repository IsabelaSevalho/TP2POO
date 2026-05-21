package br.edu.uea.chat.cliente;

import java.net.Socket;

/**
 * Esta classe serve para manter a comunicação entre cliente e servidor.
 * Recebe mensagens, trata mensagens de envia respostas.
 * 
 * @author xx
 * @version 1.0
 */

public class ClienteThread extends Thread{
	private Socket socket;
	
	public ClienteThread(Socket socket) {
        this.socket = socket;
    }
	
	public void run() {
		
	}

}
