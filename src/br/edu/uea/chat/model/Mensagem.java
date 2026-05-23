package br.edu.uea.chat.model;

import java.io.Serializable;

/**
 * Esta classe representa o protocolo de comunicação entre Cliente e Servidor.
 * Carrega a ação desejada, os dados do usuário e o conteúdo textual.
 * 
 * @version 2.0
 */

public class Mensagem implements Serializable{
	private String acao; //cadastrar, listar, kill, mandar texto, logar
	private String destinatario;
	private Usuario usuario;
	private String texto;
	
	public Mensagem(String acao, String destinatario, Usuario usuario, String texto) {
		this.acao = acao;
		this.destinatario = destinatario;
		this.usuario = usuario;
		this.texto = texto;
	}
	
	@Override
	public String toString() {
		return "Ação: " + acao + " | Texto: " + texto;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	
}
