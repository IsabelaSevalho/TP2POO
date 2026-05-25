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

public class TelaServidor {
    private Cliente cliente;
    private BufferedReader leitor;

    public TelaServidor() {
        cliente = new Cliente();
        leitor = new BufferedReader(new InputStreamReader(System.in));
    }

    public void iniciar() throws IOException {
        System.out.println("=== SERVIDOR CHAT UEA - MÓDULO ADMINISTRATIVO ===");
        System.out.print("Digite o IP do servidor: ");
        String ip = leitor.readLine();

        if (!cliente.conectar(ip)) {
            System.out.println("Não foi possível conectar ao servidor.");
            return;
        }

        while (!fazerLoginTecnico()) {
            System.out.println("Login falhou. Tente novamente.");
            System.out.print("Deseja tentar de novo? (S/N): ");
            String resposta = leitor.readLine();
            
            if (resposta.equalsIgnoreCase("N")) {
                System.out.println("Encerrando o programa...");
                cliente.desconectar();
                return;
            }
        }

        System.out.println("\n--- MODO ADMINISTRADOR (TÉCNICO) ATIVADO ---");

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
                    derrubarUsuarios();
                    break;
                case 6:
                    System.out.println("Encerrando módulo servidor...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        
            System.out.println("\nPressione Enter para continuar...");
            leitor.readLine();
           
        } while (opcao != 6);

        cliente.desconectar();
    }

    private boolean fazerLoginTecnico() throws IOException {
        System.out.println("\n--- LOGIN DE TÉCNICO ---");
        System.out.print("Login: ");
        String login = leitor.readLine();
        System.out.print("Senha: ");
        String senha = leitor.readLine();

        Tecnico tecnico = new Tecnico(login, senha);
        return cliente.loginSincrono(tecnico);
    }

    private void exibirMenu() {
        System.out.println("\n=== MENU TÉCNICO ===");
        System.out.println("1. Cadastrar Professor");
        System.out.println("2. Cadastrar Aluno");
        System.out.println("3. Cadastrar Técnico");
        System.out.println("4. Listar todos os usuários (com status)");
        System.out.println("5. Derrubar conexão de usuário(s)");
        System.out.println("6. Sair");
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
        System.out.println("Solicitação de listagem enviada. Aguarde a exibição...");
    }

    private void derrubarUsuarios() throws IOException {
        System.out.println("Digite o(s) login(s) dos usuários a derrubar (separados por vírgula)");
        System.out.println("Ou digite 'TODOS' para derrubar todos: ");
        String alvos = leitor.readLine();
        if (alvos == null || alvos.trim().isEmpty()) {
            System.out.println("Nenhum alvo informado.");
            return;
        }
        Mensagem msg = new Mensagem("KILL", alvos, null, null);
        Object resposta = cliente.enviarComandoComResposta(msg);
        if (resposta != null && resposta instanceof String && ((String)resposta).startsWith("ERRO_PERMISSAO")) {
            System.out.println("Erro de permissão: apenas técnicos podem derrubar conexões.");
        } else if (resposta != null && resposta instanceof String && ((String)resposta).startsWith("KILL_OK")) {
            System.out.println("Comando executado: " + resposta);
        } else {
            System.out.println("Comando KILL enviado.");
        }
    }

    private int lerInteiro(String mensagem) throws IOException {
        System.out.print(mensagem);
        String linha = leitor.readLine();
        try {
            return Integer.parseInt(linha);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}