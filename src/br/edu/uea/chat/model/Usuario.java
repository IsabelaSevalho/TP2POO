package br.edu.uea.chat.model;

public abstract class Usuario {
	private String cpf;
	private String nome;
	private String email;
	private String senha;
	
	public Usuario() {}
	
	public Usuario(String cpf, String nome, String email, String senha) {
		this.cpf = cpf;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public String getCpf() {
		return cpf;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public boolean login(String senha) {
		return this.senha==senha;
	}

}
