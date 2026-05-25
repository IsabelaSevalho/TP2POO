package br.edu.uea.chat.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import br.edu.uea.chat.model.Mensagem;

/**
 * Esta classe serve para registrar os dados de mensagens pendentes (offline) em arquivos (a decidir)
 * 
 * @version 2.0
 */

public class MensagemDAO {
    private static final String arquivoBD = "mensagens_offline.txt";

    public MensagemDAO(){}

    public synchronized void salvarMensagens(ArrayList<Mensagem> mensagens) {
		try {
			FileOutputStream fileOS = new FileOutputStream(arquivoBD);
			ObjectOutputStream objectOS = new ObjectOutputStream(fileOS);

			objectOS.writeObject(mensagens);
			objectOS.close();
			fileOS.close();
			System.out.println("MensagemDAO: Banco de dados offline atualizado.");

		} catch (IOException e) {
			System.err.println("MensagemDAO: Erro ao salvar mensagens offline.");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized ArrayList<Mensagem> carregarMensagens() {
		ArrayList<Mensagem> mensagens = new ArrayList<Mensagem>();
		try {
			FileInputStream fileIS = new FileInputStream(arquivoBD);
			ObjectInputStream objectIS = new ObjectInputStream(fileIS);

			mensagens = (ArrayList<Mensagem>) objectIS.readObject();

			objectIS.close();
			fileIS.close();
            
		} catch (IOException e) {
			System.err.println("MensagemDAO: Nenhum histórico de mensagens pendentes encontrado.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return mensagens;
	}
}
