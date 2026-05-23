package br.edu.uea.chat.model;

import java.io.Serializable;

/**
 * Esta classe serve estabelece as regras de negócio do Usuário (superclasse).
 * Ela implemeta Serializable para poder converter o objeto em bytes para ser
 * enviado pela rede e reconstruido ao chegar no destino
 * 
 * @version 2.0
 */

public abstract class Usuario implements Serializable{
	private String user;
	private String senha;
	private boolean status;
	
	public Usuario() {}
	
	public Usuario(String user, String senha) {
		this.user = user;
		this.senha = senha;
		this.status = false;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public boolean login(String senha) {
		return this.senha.equals(senha);
	}

}
