package br.edu.uea.chat.model;

public abstract class Usuario {
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
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	public boolean login(String senha) {
		return this.senha.equals(senha);
	}

}
