package br.edu.uea.chat.model;

/**
 * Esta classe serve estabelece as regras de negócio do Aluno
 * 
 * @version 1.0
 */

public class Aluno extends Usuario{
	
	private int anoDeIngresso;
	
	public Aluno() {}

	public Aluno(String user, String senha, int anoDeIngresso) {
		super(user, senha);
		this.anoDeIngresso = anoDeIngresso;
	}

	public int getAnoDeIngresso() {
		return anoDeIngresso;
	}

	public void setAnoDeIngresso(int anoDeIngresso) {
		this.anoDeIngresso = anoDeIngresso;
	}


}
