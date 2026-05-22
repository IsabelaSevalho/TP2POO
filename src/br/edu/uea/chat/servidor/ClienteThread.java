package br.edu.uea.chat.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import br.edu.uea.chat.model.Mensagem;
/**
 * Esta classe serve para manter a comunicação entre cliente e servidor.
 * Responsável por atender conexões de clientes e fornecer respostas.
 * 
 * Recebe respostas: Cliente → Servidor
 * 
 * @version 1.0
 */

public class ClienteThread extends Thread{
	private Socket socket;
	
	public ClienteThread(Socket socket) {
        this.socket = socket;
    }
	
	public void run() {
		try {
            ObjectInputStream objectIS = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Object obj =objectIS.readObject();

                if (obj instanceof Mensagem) {
                    Mensagem msg = (Mensagem) obj;

                    System.out.println("\nMensagem recebida:");
                    System.out.println(msg);
                }
            }

        } catch (Exception e) {
            System.out.println("Cliente desconectado.");

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

}
