package br.edu.uea.chat.main;

import br.edu.uea.chat.servidor.Servidor;

/**
 * Esta classe serve para executar o processo do servidor
 * 
 * @author Isabela Cunha
 * @version 1.0
 */

public class MainServidor{
	
	public static void main(String[] args){
        Servidor servidor = new Servidor();
        servidor.run();
    }
}
