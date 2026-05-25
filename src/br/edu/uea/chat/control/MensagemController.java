package br.edu.uea.chat.control;

import java.util.ArrayList;

import br.edu.uea.chat.dao.MensagemDAO;
import br.edu.uea.chat.model.Mensagem;

/**
 * Esta classe serve para capturar eventos da view e executar açoes referentes à troca de mensagens, solicitando uma comunicação com o servidor
 * 
 * @version 1.0
 */

public class MensagemController {
	private MensagemDAO mensagemDAO;

	public MensagemController() {
		this.mensagemDAO = new MensagemDAO();
	}

	public boolean enviarMensagem() {
		return true;
	}
	
	public synchronized void armazenarMensagemOffline(Mensagem msg) {
		ArrayList<Mensagem> filaPendentes = mensagemDAO.carregarMensagens();
		filaPendentes.add(msg);
		
		mensagemDAO.salvarMensagens(filaPendentes);
	}
	
	public synchronized ArrayList<Mensagem> entregarMensagensPendentes(String loginUsuario) {
		ArrayList<Mensagem> todasAsMensagens = mensagemDAO.carregarMensagens();
		ArrayList<Mensagem> mensagensDoUsuario = new ArrayList<Mensagem>();
		ArrayList<Mensagem> mensagensDeOutros = new ArrayList<Mensagem>();

		for (Mensagem msg : todasAsMensagens) {
			if (msg.getDestinatario().equals(loginUsuario)) {
				mensagensDoUsuario.add(msg); 
			} else {
				mensagensDeOutros.add(msg);
			}
		}

		if (!mensagensDoUsuario.isEmpty()) {
			mensagemDAO.salvarMensagens(mensagensDeOutros);
		}

		return mensagensDoUsuario;
	}
}
