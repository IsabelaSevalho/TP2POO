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
 * cliente em um arquivo dat (textos simples).
 * 
 * @version 2.0
 */

public class UsuarioDAO {
	private static final String arquivoBD = "usuarios_chat.dat";

	public UsuarioDAO(){}

	public void salvarUsuario(ArrayList<Usuario> usuarios) {
		try{
			//conecta ou cria o arquivo
			FileOutputStream fileOS = new FileOutputStream(arquivoBD);

			//para permitir escrever o objeto Usuario no arquivo
			ObjectOutputStream objectOS =new ObjectOutputStream(fileOS);

			//salva lista, sobrescreve, fecha
			 objectOS.writeObject(usuarios);
			 objectOS.close();

			 System.out.println("Usuário salvo com sucesso.");

		}catch(IOException e){
			System.out.println("Erro ao salvar usuário.");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked") //so p silenciar um aviso de uma linha que disse nao ser seguro, podemos ver isso dps
	public ArrayList<Usuario> carregarUsuarios() {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		
		try{
			//conecta ou cria o arquivo para leitura
			FileInputStream fileIS = new FileInputStream(arquivoBD);

			//permite ler
			ObjectInputStream objectIS = new ObjectInputStream(fileIS);

			//converte obj p arraylist
			usuarios =(ArrayList<Usuario>) objectIS.readObject();

            objectIS.close();
            System.out.println("Usuários carregados.");

		}catch(IOException e){
			System.out.println("Erro ao carregar usuários.");
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			System.out.println("Classe não encontrada.");
            e.printStackTrace();
        }

		return usuarios;
	}
}
