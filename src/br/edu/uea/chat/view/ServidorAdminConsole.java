package br.edu.uea.chat.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import br.edu.uea.chat.cliente.Cliente;
import br.edu.uea.chat.model.Aluno;
import br.edu.uea.chat.model.Mensagem;
import br.edu.uea.chat.model.Professor;
import br.edu.uea.chat.model.Tecnico;
import br.edu.uea.chat.model.Usuario;

/**
 * Console administrativo do servidor - apenas para TÉCNICOS.
 * Usa BufferedReader para entrada.
 * 
 * @version 2.0
 */
public class ServidorAdminConsole {
    private Cliente cliente;
    private BufferedReader leitor;
    private boolean logadoComoTecnico;

    public ServidorAdminConsole() {
        cliente = new Cliente();
        leitor = new BufferedReader(new InputStreamReader(System.in));
        logadoComoTecnico = false;
    }

    public void iniciar() throws IOException {
        System.out.println("=== MÓDULO ADMINISTRATIVO DO SERVIDOR ===");
        System.out.print("Digite o IP do servidor: ");
        String ip = leitor.readLine();

        if (!cliente.conectar(ip)) {
            System.out.println("Não foi possível conectar ao servidor.");
            return;
        }

        if (!fazerLoginTecnico()) {
            System.out.println("Login falhou. Encerrando.");
            return;
        }

        logadoComoTecnico = true;
        System.out.println("Login técnico realizado com sucesso!\n");

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Opção: ");
            switch (opcao) {
                case 1:
                    cadastrarProfessor();
                    break;
                case 2:
                    cadastrarAluno();
                    break;
                case 3:
                    cadastrarTecnico();
                    break;
                case 4:
                    listarUsuarios();
                    break;
                case 5:
                    System.out.println("Encerrando módulo servidor...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 5);

        cliente.desconectar();
    }

    private boolean fazerLoginTecnico() throws IOException {
        System.out.println("\n--- Login de Técnico ---");
        System.out.print("Login: ");
        String login = leitor.readLine();
        System.out.print("Senha: ");
        String senha = leitor.readLine();

        Tecnico tecnico = new Tecnico(login, senha);
    
        return cliente.loginSincrono(tecnico);
    }

    private void exibirMenu() {
        System.out.println("\n--- MENU TÉCNICO ---");
        System.out.println("1. Cadastrar Professor");
        System.out.println("2. Cadastrar Aluno");
        System.out.println("3. Cadastrar Técnico");
        System.out.println("4. Listar todos os usuários (com status)");
        System.out.println("5. Sair");
    }

    private void cadastrarProfessor() throws IOException {
        System.out.print("Login: ");
        String login = leitor.readLine();
        System.out.print("Senha: ");
        String senha = leitor.readLine();
        System.out.print("Titulação: ");
        String titulacao = leitor.readLine();
        Professor prof = new Professor(login, senha, titulacao);
        enviarCadastro(prof);
    }

    private void cadastrarAluno() throws IOException {
        System.out.print("Login: ");
        String login = leitor.readLine();
        System.out.print("Senha: ");
        String senha = leitor.readLine();
        System.out.print("Ano de ingresso: ");
        int ano = lerInteiro("Ano: ");
        Aluno aluno = new Aluno(login, senha, ano);
        enviarCadastro(aluno);
    }

    private void cadastrarTecnico() throws IOException {
        System.out.print("Login: ");
        String login = leitor.readLine();
        System.out.print("Senha: ");
        String senha = leitor.readLine();
        Tecnico tec = new Tecnico(login, senha);
        enviarCadastro(tec);
    }

    private void enviarCadastro(Usuario usuario) {
        Mensagem msg = new Mensagem("CADASTRO", null, usuario, null);
        Object resposta = cliente.enviarComandoComResposta(msg);
        if (resposta != null && resposta.equals("CADASTRO_OK")) {
            System.out.println("Cadastro realizado com sucesso!");
        } else if (resposta != null && resposta instanceof String && ((String)resposta).startsWith("CADASTRO_ERRO")) {
            System.out.println("Erro: " + resposta);
        } else {
            System.out.println("Falha no cadastro. Verifique se você está logado como técnico.");
        }
    }

    private void listarUsuarios() {
        cliente.solicitarStatusUsuarios();
        System.out.println("Pedido de listagem enviado. Aguarde a resposta no console.");
    }

    private int lerInteiro(String mensagem) throws IOException {
        System.out.print(mensagem);
        String linha = leitor.readLine();
        try {
            return Integer.parseInt(linha);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        try {
            new ServidorAdminConsole().iniciar();
        } catch (IOException e) {
            System.out.println("Erro de I/O: " + e.getMessage());
        }
    }
}