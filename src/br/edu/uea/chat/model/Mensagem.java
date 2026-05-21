package br.edu.uea.chat.model;

import java.io.Serializable;

/**
 * Esta classe serve estabelece as regras de negócio das mensagens
 * 
 * @author Isabela Cunha
 * @version 1.0
 */

public class Mensagem implements Serializable{
	private String remetente;
	private String destinatario;
	private String assunto;
	private String mensagem;
	
	public Mensagem(String remetente, String destinatario, String assunto, String mensagem) {
		super();
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.assunto = assunto;
		this.mensagem = mensagem;
	}
	
	@Override
    public String toString() {
        return "De: "+remetente+"\nPara: "+destinatario+"\nAssunto: "+assunto+"\nMensagem: " + mensagem;
    }

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getRemetente() {
		return remetente;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	
	
}
