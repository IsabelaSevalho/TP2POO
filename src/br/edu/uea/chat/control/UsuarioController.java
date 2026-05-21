package br.edu.uea.chat.control;


import java.util.ArrayList;

import br.edu.uea.chat.model.Aluno;
import br.edu.uea.chat.model.Professor;
import br.edu.uea.chat.model.Tecnico;
import br.edu.uea.chat.model.Usuario;

/**
 * Esta classe serve para capturar eventos da view e executar açoes referentes ao usuário, como cadastro e listagem
 * 
 * @author Isabela Cunha
 * @version 1.0
 */

public class UsuarioController {
	private ArrayList<Usuario> usuarios;
	
	public UsuarioController() {
		usuarios = new ArrayList<>();
	}
		
	public Usuario buscarUsuario(String user) {
		for(Usuario u : usuarios) {
			if(user.equals(u.getUser())) {
				return u;
			}
		}
		return null;
	}
	
	public boolean cadastrarUsuario(Usuario usuario) { //apenas tecnicos podem executar essa ação
		Usuario u = buscarUsuario(usuario.getUser());
	    if (u != null) return false;
	    
	    usuarios.add(usuario);
		return true;
	}
	
	public boolean login(String user, String senha) {
		Usuario u = buscarUsuario(user);
		if(u!=null) {
			return u.login(senha);
		}
		return false;
	}
	
	
	public boolean killConexao() {
		return true;
	}
	
	public ArrayList<Usuario> getUsuarios(){
		return usuarios;
	}
	
	public ArrayList<Tecnico> getTecnicos() {
	    ArrayList<Tecnico> funcionarios = new ArrayList<>();
	    for (Usuario u : usuarios) {
	        if (u instanceof Tecnico) {
	            funcionarios.add((Tecnico) u);
	        }
	    }
	    return funcionarios;
	}
	
	public ArrayList<Professor> getProfessores() {
	    ArrayList<Professor> professores = new ArrayList<>();
	    for (Usuario u : usuarios) {
	        if (u instanceof Professor) {
	        	professores.add((Professor) u);
	        }
	    }
	    return professores;
	}
	
	public ArrayList<Aluno> getAlunos() {
	    ArrayList<Aluno> alunos = new ArrayList<>();
	    for (Usuario u : usuarios) {
	        if (u instanceof Aluno) {
	            alunos.add((Aluno) u);
	        }
	    }
	    return alunos;
	}
	

}