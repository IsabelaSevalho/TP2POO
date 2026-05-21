package br.edu.uea.chat.model;

/**
 * Esta classe serve estabelece as regras de negócio do Professor
 * 
 * @author Isabela Cunha
 * @version 1.0
 */

public class Professor extends Usuario{
	
	private String titulacao;
	
	public Professor() {}
	
	public Professor(String user, String senha, String titulacao) {
		super(user, senha);
		this.titulacao = titulacao;
	}

	public String getTitulacao() {
		return titulacao;
	}


	public void setTitulacao(String titulacao) {
		this.titulacao = titulacao;
	}
	
}
