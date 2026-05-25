package br.edu.uea.chat.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import br.edu.uea.chat.cliente.Cliente;
import br.edu.uea.chat.model.Aluno;
import br.edu.uea.chat.model.Professor;
import br.edu.uea.chat.model.Tecnico;
import br.edu.uea.chat.model.Usuario;

/**
 * Interface de console para o usuário comum (Cliente).
 * Permite login, listagem de usuários com status, envio de mensagens.
 * @version 1.1
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
        System.out.println("=== CHAT UEA - CLIENTE ===");
        System.out.print("Digite o IP do servidor: ");
        String ip = leitor.readLine();

        if (!cliente.conectar(ip)) {
            System.out.println("Não foi possível conectar ao servidor.");
            return;
        }

        if (!fazerLogin()) {
            System.out.println("Login falhou. Encerrando.");
            cliente.desconectar();
            return;
        }

        System.out.println("\nBem-vindo, " + usuarioLogado.getUser() + "!");
        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Opção: ");
            switch (opcao) {
                case 1:
                    listarUsuarios();
                    break;
                case 2:
                    enviarMensagem();
                    break;
                case 3:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 3);

        cliente.desconectar();
    }

    private boolean fazerLogin() throws IOException {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Login: ");
        String login = leitor.readLine();
        System.out.print("Senha: ");
        String senha = leitor.readLine();

        // O tipo real será verificado no servidor; aqui usamos Aluno como placeholder
        usuarioLogado = new Aluno(login, senha, 0);
        return cliente.loginSincrono(usuarioLogado);
    }

    private void exibirMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Listar usuários (online/offline)");
        System.out.println("2. Enviar mensagem");
        System.out.println("3. Sair");
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