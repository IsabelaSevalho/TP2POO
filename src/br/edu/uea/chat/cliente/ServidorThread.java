package br.edu.uea.chat.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import br.edu.uea.chat.model.Mensagem;
import br.edu.uea.chat.model.Usuario;

/**
 * Thread responsável por ouvir o servidor e exibir respostas formatadas.
 * @version 3.1
 */
public class ServidorThread implements Runnable {
    private Socket socket;
    private ObjectInputStream objectIS;

    public ServidorThread(Socket socket, ObjectInputStream objectIS) {
        this.socket = socket;
        this.objectIS = objectIS;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = objectIS.readObject();

                if (obj instanceof String) {
                    String resposta = (String) obj;
                    if (resposta.equals("CONFIRMACAO_LOGIN_OK")) {
                        System.out.println("Login realizado com sucesso!\n");
                    } else if (resposta.startsWith("ERRO_PERMISSAO")) {
                        System.out.println("Permissão negada: " + resposta);
                    } else if (resposta.startsWith("CADASTRO_OK")) {
                        System.out.println("Cadastro realizado com sucesso.");
                    } else if (resposta.startsWith("CADASTRO_ERRO")) {
                        System.out.println("Erro no cadastro: " + resposta);
                    } else if (resposta.startsWith("KILL_OK")) {
                        System.out.println("Comando KILL executado: " + resposta);
                    } else if (resposta.equals("LOGIN_INVALIDO")) {
                        System.out.println("Login inválido. Tente novamente.");
                    } else {
                        System.out.println("[Servidor]: " + resposta);
                    }

                } else if (obj instanceof Mensagem) {
                    Mensagem msg = (Mensagem) obj;
                    System.out.println("\n[Mensagem de " + msg.getUsuario().getUser() + "]: " + msg.getTexto());
                    System.out.print("> ");

                } else if (obj instanceof ArrayList) {
                    @SuppressWarnings("unchecked")
                    ArrayList<Usuario> lista = (ArrayList<Usuario>) obj;
                    exibirListaUsuarios(lista);
                }
            }
        } catch (IOException e) {
            System.out.println("\nConexão com o servidor foi encerrada.");
        } catch (ClassNotFoundException e) {
            System.out.println("Erro ao processar dado recebido.");
        }
    }

    private void exibirListaUsuarios(ArrayList<Usuario> usuarios) {
        System.out.println("\n========== USUÁRIOS ONLINE ==========");
        boolean temOnline = false;
        for (Usuario u : usuarios) {
            if (u.getStatus()) {
                System.out.println("  " + u.getUser() + " (" + getTipoUsuario(u) + ")");
                temOnline = true;
            }
        }
        if (!temOnline) System.out.println("  Nenhum usuário online.");

        System.out.println("\n========== USUÁRIOS OFFLINE ==========");
        boolean temOffline = false;
        for (Usuario u : usuarios) {
            if (!u.getStatus()) {
                System.out.println("  " + u.getUser() + " (" + getTipoUsuario(u) + ")");
                temOffline = true;
            }
        }
        if (!temOffline) System.out.println("  Nenhum usuário offline.");
        System.out.println();
    }

    private String getTipoUsuario(Usuario u) {
        if (u instanceof br.edu.uea.chat.model.Aluno) return "Aluno";
        if (u instanceof br.edu.uea.chat.model.Professor) return "Professor";
        if (u instanceof br.edu.uea.chat.model.Tecnico) return "Técnico";
        return "Usuário";
    }

    public void desconectar() {
        try {
            if (objectIS != null) objectIS.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao fechar conexão.");
        }
    }
}