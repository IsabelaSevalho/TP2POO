package br.edu.uea.chat.test;

import java.util.ArrayList;

import br.edu.uea.chat.dao.UsuarioDAO;
import br.edu.uea.chat.model.Professor;
import br.edu.uea.chat.model.Usuario;
/**
 * Esta classe serve para testar a criação do arquivo de usuários
 * 
 * Recebe respostas: Cliente → Servidor
 * 
 * @version 1.0
 */

public class TesteCadastro {

    public static void main(String[] args) {

        ArrayList<Usuario> usuarios = new ArrayList<>();

        // Cria usuário teste.
        Professor professor = new Professor("girafales", "123","Mestre");
        usuarios.add(professor);

        UsuarioDAO dao = new UsuarioDAO();
        dao.salvarUsuario(usuarios);

        System.out.println("Teste finalizado.");
    }
}