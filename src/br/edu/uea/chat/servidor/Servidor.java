package br.edu.uea.chat.servidor;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import br.edu.uea.chat.control.UsuarioController;
import br.edu.uea.chat.control.MensagemController;
import br.edu.uea.chat.model.Tecnico;

public class Servidor extends Thread {
    private static final int PORTA = 5000;
    private ServerSocket serverSocket;
    private UsuarioController usuarioController;
    private MensagemController mensagemController;
    private static ArrayList<ClienteThread> clientesOnline = new ArrayList<>();

    public Servidor() {
        this.usuarioController = new UsuarioController();
        this.mensagemController = new MensagemController();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(PORTA);
            System.out.println("Servidor iniciado na porta " + PORTA);
            while (true) {
                System.out.println("Aguardando conexão...");
                Socket connection = serverSocket.accept();
                System.out.println("Cliente conectado: " + connection.getInetAddress().getHostName());
                ClienteThread clienteThread = new ClienteThread(connection, this);
                clienteThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UsuarioController getUsuarioController() { return usuarioController; }
    public MensagemController getMensagemController() { return mensagemController; }

    public void close() throws IOException {
        if (serverSocket != null) serverSocket.close();
    }

    public static ArrayList<ClienteThread> getClientesOnline() { return clientesOnline; }
    public static void addClienteOnline(ClienteThread ct) { clientesOnline.add(ct); }
    public static void removeClienteOnline(ClienteThread ct) { clientesOnline.remove(ct); }

    // MÉTODO MAIN AQUI
    public static void main(String[] args) {
        // Cria técnico inicial se não houver nenhum
        UsuarioController uc = new UsuarioController();
        if (uc.getTecnicos().isEmpty()) {
            Tecnico admin = new Tecnico("admin_tec", "1234");
            uc.cadastrarUsuario(admin);
            System.out.println("Técnico inicial criado: login=admin_tec, senha=1234");
        }

        Servidor servidor = new Servidor();
        servidor.start();
        System.out.println("Servidor iniciado. Pressione Enter para encerrar.");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            servidor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}