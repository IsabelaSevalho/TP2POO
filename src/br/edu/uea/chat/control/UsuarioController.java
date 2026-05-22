package br.edu.uea.chat.control;


import java.util.ArrayList;

import br.edu.uea.chat.model.Aluno;
import br.edu.uea.chat.model.Professor;
import br.edu.uea.chat.model.Tecnico;
import br.edu.uea.chat.model.Usuario;

import br.edu.uea.chat.dao.UsuarioDAO;

/**
 * Esta classe serve para capturar eventos da view e executar açoes referentes ao usuário.
 * Aqui, inclui-se: busca, cadastro, login,kill conexao e gets
 * 
 * @version 2.0
 */

public class UsuarioController {
	private ArrayList<Usuario> usuarios;
	private UsuarioDAO usuarioDAO;
	
	public UsuarioController() {
		usuarioDAO = new UsuarioDAO();
		usuarios = usuarioDAO.carregarUsuarios();
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
	    usuarioDAO.salvarUsuario(usuarios);
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
	
	public ArrayList<Usuario> getUsuarios(){//para listagem
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