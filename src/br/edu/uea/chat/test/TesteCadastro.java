package br.edu.uea.chat.test;

import br.edu.uea.chat.control.UsuarioController;
import br.edu.uea.chat.model.Professor;
/**
 * Esta classe serve para testar a criação do arquivo de usuários
 * 
 * @version 2.0
 */

public class TesteCadastro {

    public static void main(String[] args) {
        // Cria usuário teste.
        Professor professor = new Professor("girafales", "123","Mestre");

        UsuarioController controller = new UsuarioController();
        System.out.println(controller.cadastrarUsuario(professor) ? "Teste finalizado." : "O usuário já existe.");


    }
}