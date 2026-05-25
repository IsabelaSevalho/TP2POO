package br.edu.uea.chat.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import br.edu.uea.chat.cliente.ServidorThread;
import br.edu.uea.chat.model.Mensagem;
import br.edu.uea.chat.model.Tecnico;
import br.edu.uea.chat.model.Usuario;

/**
 * Esta classe faz o gerenciamento do socket, emite comandos e recebe respostas do servidor
 * Conecta o servidor, envia objetos e recebe respostas
 * * @version 3.0
 */

public class Cliente {
    
    private Socket socket;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private boolean conectado;
    private Usuario usuarioLogado;
    private Object ultimaResposta;
    
    public Cliente() {
        this.conectado = false;
    }

    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }

    
    public boolean conectar(String ip) {
         try {
            this.socket = new Socket(ip, 5000);
            
            this.saida = new ObjectOutputStream(socket.getOutputStream());
            this.saida.flush();

            this.entrada = new ObjectInputStream(socket.getInputStream());
            this.conectado = true;
            
            System.out.println("Cliente: Conectado com sucesso na porta 5000.");
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
            return false;
        }
    }
    
    public void efetuarLogin(Usuario usuario) {
        if (!conectado) return;
        try {
            Mensagem protocoloLogin = new Mensagem("LOGIN", null, usuario, null);
            
            saida.writeObject(protocoloLogin);
            saida.flush();
            System.out.println("Cliente: Solicitação de login enviada.");
            
            //ler a confirmação do Servidor antes de disparar a Thread
            Object resposta = entrada.readObject();
            
            if (resposta instanceof String && "CONFIRMACAO_LOGIN_OK".equals(resposta)) {
                this.usuarioLogado = usuario;
                
                // Inicializa a thread de escuta passando a stream de entrada 
                ServidorThread ouvinte = new ServidorThread(this.socket, this.entrada);
                new Thread(ouvinte).start();
            }

        } catch (Exception e) {
            System.err.println("Erro ao tentar logar: " + e.getMessage());
        }
    }

    public void solicitarStatusUsuarios() {
        if (!conectado) return;
        try {
            Mensagem protocoloListar = new Mensagem("LISTAR", null, null, null);
            
            saida.writeObject(protocoloListar);
            saida.flush();
            System.out.println("Cliente: Pedido de listagem enviado ao servidor.");
        
        } catch (IOException e) {
            System.err.println("Erro ao pedir status: " + e.getMessage());
        }
    }

    public void enviarMensagemDeTexto(String destinatario, String texto) {
        if (!conectado) return;
        try {
            Mensagem msg = new Mensagem("TEXTO", destinatario, this.usuarioLogado, texto);
            saida.writeObject(msg);
            saida.flush();
        } catch (IOException e) {
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
        }
    }

    public void enviarComandoKill(String alvos) {
        if (!conectado) return;
        try {
            Mensagem protocoloKill = new Mensagem("KILL", alvos, null, null);
            saida.writeObject(protocoloKill);
            saida.flush();
            System.out.println("Cliente: Comando de derrubada enviado pelo Tecnico.");
        
        } catch (IOException e) {
            System.err.println("Erro ao disparar comando KILL: " + e.getMessage());
        }
    }
    
    public void desconectar() {
        try {
            if (saida != null) saida.close();
            if (entrada != null) entrada.close();
            if (socket != null) socket.close();
            this.conectado = false;
        } catch (IOException e) {
            System.err.println("Erro ao desconectar cliente: " + e.getMessage());
        }
    }
    
        public boolean loginSincrono(Usuario usuario) {
        if (!conectado) return false;
        try {
            Mensagem protocoloLogin = new Mensagem("LOGIN", null, usuario, null);
            saida.writeObject(protocoloLogin);
            saida.flush();

            // O cliente lê o objeto que o Servidor enviou
            Object resposta = entrada.readObject();
            
            // Se a resposta for a instância real do Usuário (Tecnico/Aluno/Professor)
            if (resposta instanceof Usuario) {
                this.usuarioLogado = (Usuario) resposta; // Guarda a instância correta vinda do banco!
                
                // Inicializa a thread de escuta
                ServidorThread ouvinte = new ServidorThread(this.socket, this.entrada);
                new Thread(ouvinte).start();
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Erro no login síncrono: " + e.getMessage());
            return false;
        }
    }

    // Método para enviar comando e aguardar resposta 
    public Object enviarComandoComResposta(Mensagem msg) {
        if (!conectado) return null;
        try {
            saida.writeObject(msg);
            saida.flush();
            return entrada.readObject();
        } catch (Exception e) {
            System.err.println("Erro no comando com resposta: " + e.getMessage());
            return null;
        }
    }

    public void enviarComandoSemResposta(Mensagem msg) {
        if (!conectado) return;
        try {
            saida.writeObject(msg);
            saida.flush();
        } catch (IOException e) {
            System.err.println("Erro ao enviar comando assíncrono: " + e.getMessage());
        }
    }

}