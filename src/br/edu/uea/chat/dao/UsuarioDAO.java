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

	//apenas 1 thread executa por vez
	public synchronized void salvarUsuario(ArrayList<Usuario> usuarios) {
		try{
			//conecta ou cria o arquivo
			FileOutputStream fileOS = new FileOutputStream(arquivoBD);

			//para permitir escrever o objeto Usuario no arquivo
			ObjectOutputStream objectOS =new ObjectOutputStream(fileOS);

			//salva lista, sobrescreve, fecha
			objectOS.writeObject(usuarios);
			objectOS.close();

			System.out.println("DAO: Usuário salvo com sucesso.");

		}catch(IOException e){
			System.out.println("DAO: Erro ao salvar usuário.");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked") //so p silenciar um aviso de uma linha que disse nao ser seguro, podemos ver isso dps
	public synchronized ArrayList<Usuario> carregarUsuarios() {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		
		try{
			//conecta ou cria o arquivo para leitura
			FileInputStream fileIS = new FileInputStream(arquivoBD);

			//permite ler
			ObjectInputStream objectIS = new ObjectInputStream(fileIS);

			//converte obj p arraylist
			usuarios =(ArrayList<Usuario>) objectIS.readObject();

            objectIS.close();
            System.out.println("DAO: Usuários carregados.");

		}catch(IOException e){
			System.out.println("DAO: Arquivo de banco de dados não encontrado. Criando uma nova lista limpa...");
		}catch (ClassNotFoundException e) {
			System.out.println("DAO: Classe não encontrada.");
            e.printStackTrace();
        }

		return usuarios;
	}
}
