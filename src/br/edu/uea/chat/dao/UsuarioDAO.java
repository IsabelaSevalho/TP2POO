package br.edu.uea.chat.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import br.edu.uea.chat.model.Usuario;

/**
 * Esta classe serve para registrar os dados do
 * cliente em um arquivo txt.
 * 
 * @version 2.0
 */

public class UsuarioDAO {
	private static final String arquivoBD = "usuarios_chat.txt";

	public UsuarioDAO(){}

	public synchronized void salvarUsuario(ArrayList<Usuario> usuarios) {
		try{
			FileOutputStream fileOS = new FileOutputStream(arquivoBD);

			ObjectOutputStream objectOS =new ObjectOutputStream(fileOS);

			objectOS.writeObject(usuarios);
			objectOS.close();

			System.out.println("DAO: Usuário salvo com sucesso.");

		}catch(IOException e){
			System.err.println("DAO: Erro ao salvar usuário.");
			e.printStackTrace();
		}
	}
	
	public synchronized ArrayList<Usuario> carregarUsuarios() {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		
		try{
			FileInputStream fileIS = new FileInputStream(arquivoBD);
			ObjectInputStream objectIS = new ObjectInputStream(fileIS);

			usuarios =(ArrayList<Usuario>) objectIS.readObject();

            objectIS.close();
            System.out.println("DAO: Usuários carregados.");

		}catch(IOException e){
			System.err.println("DAO: Arquivo de banco de dados não encontrado. Criando uma nova lista limpa...");
		}catch (ClassNotFoundException e) {
			System.err.println("DAO: Classe não encontrada.");
            e.printStackTrace();
        }

		return usuarios;
	}
}
