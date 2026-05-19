package br.edu.uea.chat.model;

public abstract class Usuario {
	private String user;
	private String senha;
	
	public Usuario() {}
	
	public Usuario(String user, String senha) {
		this.user = user;
		this.senha = senha;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public boolean login(String senha) {
		return this.senha==senha;
	}

}
