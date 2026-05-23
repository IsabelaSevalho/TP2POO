package br.edu.uea.chat.model;

/**
 * Esta classe serve estabelece as regras de negócio do Tecnico
 * 
 * @version 1.0
 */

public class Tecnico extends Usuario{
	private static final long serialVersionUID = 1L; 

	public Tecnico() {}
	
	public Tecnico(String user, String senha) {
		super(user, senha);
	}

}
