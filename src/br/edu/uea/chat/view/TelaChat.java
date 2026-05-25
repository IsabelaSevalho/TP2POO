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
 * Interface de console única para o CHAT UEA.
 * Identifica o tipo de usuário no login e muda o menu dinamicamente.
 * 
 * @version 3.0
 */

public class TelaChat {
    private Cliente cliente;
    private BufferedReader leitor;
    private Usuario usuarioLogado;

    public TelaChat() {
        cliente = new Cliente();
        leitor = new BufferedReader(new InputStreamReader(System.in));
    }

    public void iniciar() throws IOException {
        System.out.println("=== CHAT UEA ===");
        System.out.print("Digite o IP do servidor: ");
        String ip = leitor.readLine();

        if (!cliente.conectar(ip)) {
            System.out.println("Não foi possível conectar ao servidor.");
            return;
        }

        while (!fazerLogin()) {
            System.out.println("Login falhou. Tente novamente.");
            System.out.print("Deseja tentar de novo? (S/N): ");
            String resposta = leitor.readLine();
            
            if (resposta.equalsIgnoreCase("N")) {
                System.out.println("Encerrando o programa...");
                cliente.desconectar();
                return;
            }
        }

        System.out.println("Classe carregada na View: " + usuarioLogado.getClass().getSimpleName());

        if (usuarioLogado instanceof Tecnico) {
            rodarMenuTecnico();
        } else {
            rodarMenuComum();
        }

        cliente.desconectar();
    }

    private boolean fazerLogin() throws IOException {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Login: ");
        String login = leitor.readLine();
        System.out.print("Senha: ");
        String senha = leitor.readLine();

        Usuario userTemp = new Aluno(login, senha, 0);
        boolean logou = cliente.loginSincrono(userTemp);
        
        if (logou) {
            this.usuarioLogado = cliente.getUsuarioLogado(); 
        }
        return logou;
    }

    private void rodarMenuComum() throws IOException {
        System.out.println("\nBem-vindo ao Chat, " + usuarioLogado.getUser() + "!");
        int opcao;
        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Listar usuários (online/offline)");
            System.out.println("2. Enviar mensagem");
            System.out.println("3. Sair");
            opcao = lerInteiro("Opção: ");
            
            switch (opcao) {
                case 1: listarUsuarios(); break;
                case 2: enviarMensagem(); break;
                case 3: System.out.println("Saindo do chat..."); break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 3);
    }

    private void rodarMenuTecnico() throws IOException {
        System.out.println("\n--- MODO ADMINISTRADOR (TÉCNICO) ATIVADO ---");
        System.out.println("Bem-vindo, " + usuarioLogado.getUser() + "!");
        int opcao;
        do {
            System.out.println("\n=== MENU TÉCNICO ===");
            System.out.println("1. Cadastrar Professor");
            System.out.println("2. Cadastrar Aluno");
            System.out.println("3. Cadastrar Técnico");
            System.out.println("4. Listar todos os usuários (com status)");
            System.out.println("5. Derrubar conexão de usuário(s)");
            System.out.println("6. Sair");
            opcao = lerInteiro("Opção: ");
            
            switch (opcao) {
                case 1: cadastrarProfessor(); break;
                case 2: cadastrarAluno(); break;
                case 3: cadastrarTecnico(); break;
                case 4: listarUsuarios(); break;
                case 5: derrorbarUsuarios(); break;
                case 6: System.out.println("Encerrando módulo técnico..."); break;
                default: System.out.println("Opção inválida.");
            }
            if (opcao != 6) {
                System.out.println("\nPressione Enter para continuar...");
                leitor.readLine();
            }
        } while (opcao != 6);
    }

    private void listarUsuarios() {
        cliente.solicitarStatusUsuarios();
        System.out.println("Solicitação enviada. Aguarde a listagem...");
    }

    private void enviarMensagem() throws IOException {
        System.out.print("Destinatário (login): ");
        String destino = leitor.readLine();
        System.out.print("Mensagem: ");
        String texto = leitor.readLine();

        if (destino.isEmpty() || texto.isEmpty()) {
            System.out.println("Destinatário e mensagem não podem ser vazios.");
            return;
        }
        cliente.enviarMensagemDeTexto(destino, texto);
        System.out.println("Mensagem enviada.");
    }

    private void cadastrarProfessor() throws IOException {
        System.out.print("Login: "); String login = leitor.readLine();
        System.out.print("Senha: "); String senha = leitor.readLine();
        System.out.print("Titulação: "); String titulacao = leitor.readLine();
        enviarCadastro(new Professor(login, senha, titulacao));
    }

    private void cadastrarAluno() throws IOException {
        System.out.print("Login: "); String login = leitor.readLine();
        System.out.print("Senha: "); String senha = leitor.readLine();
        int ano = lerInteiro("Ano de ingresso: ");
        enviarCadastro(new Aluno(login, senha, ano));
    }

    private void cadastrarTecnico() throws IOException {
        System.out.print("Login: "); String login = leitor.readLine();
        System.out.print("Senha: "); String senha = leitor.readLine();
        enviarCadastro(new Tecnico(login, senha));
    }

    private void enviarCadastro(Usuario usuario) {
        Mensagem msg = new Mensagem("CADASTRO", null, usuario, null);
        // Mudamos para enviar apenas o comando, sem travar a tela esperando o readObject
        cliente.enviarComandoSemResposta(msg); 
        System.out.println("Solicitação de cadastro enviada ao servidor. Aguarde a confirmação no chat...");
    }


    private void derrorbarUsuarios() throws IOException {
        System.out.println("Digite o(s) login(s) a derrubar (ou 'TODOS'): ");
        String alvos = leitor.readLine();
        if (alvos == null || alvos.trim().isEmpty()) return;
        
        cliente.enviarComandoKill(alvos); 
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

    public static void main(String[] args) {
        try {
            new TelaChat().iniciar();
        } catch (IOException e) {
            System.out.println("Erro de entrada/saída: " + e.getMessage());
        }
    }
}
