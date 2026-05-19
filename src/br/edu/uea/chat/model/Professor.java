package br.edu.uea.chat.model;

public class Professor extends Usuario{
	
	private String titulacao;
	
	public Professor() {}
	
	public Professor(String cpf, String nome, String email, String titulacao, String senha) {
		super(cpf, nome, email, senha);
		this.titulacao = titulacao;
	}

	public String getTitulacao() {
		return titulacao;
	}


	public void setTitulacao(String titulacao) {
		this.titulacao = titulacao;
	}
	
}
