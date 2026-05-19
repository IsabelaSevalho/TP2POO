package br.edu.uea.chat.model;

public class Aluno extends Usuario{
	
	private String instituicaoDeEnsino;
	private String curso;
	private float notaUltimoENEM;
	
	public Aluno() {}

	public Aluno(String cpf, String nome, String email, String instituicaoDeEnsino, String curso, float notaUltimoENEM, String senha) {
		super(cpf, nome, email, senha);
		this.instituicaoDeEnsino = instituicaoDeEnsino;
		this.curso = curso;
		this.notaUltimoENEM = notaUltimoENEM;
	}

	public String getInstituicaoDeEnsino() {
		return instituicaoDeEnsino;
	}

	public String getCurso() {
		return curso;
	}

	public float getNotaUltimoENEM() {
		return notaUltimoENEM;
	}

	public void setInstituicaoDeEnsino(String instituicaoDeEnsino) {
		this.instituicaoDeEnsino = instituicaoDeEnsino;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public void setNotaUltimoENEM(float notaUltimoENEM) {
		this.notaUltimoENEM = notaUltimoENEM;
	}

}
