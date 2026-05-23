package br.edu.uea.chat.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import br.edu.uea.chat.model.Mensagem;
import br.edu.uea.chat.model.Usuario;

/**
 * Esta classe faz o gerenciamento do socket, emite comandos e recebe respostas do servidor
 * Conecta o servidor, envia objetos e recebe respostas
 * 
 * @version 2.0
 */

public class Cliente {
    
    private Socket socket;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private boolean conectado;
    private Usuario usuarioLogado;
    
    // deixe alguns comentarios durante o codigo 
    // EU ODEIO VOCE COM TODA A MINHA ALMA SOCKET E THEREAD COMO EU TE ODEIO 
    //METODOS DA NOSSA HUMILDE CLIENTELA
   
    
    public Cliente() {
        this.conectado = false;
    }
    
    public boolean conectar(String ip) {
         try {
            this.socket = new Socket(ip, 5000);
            
            this.saida = new ObjectOutputStream(socket.getOutputStream());
            this.saida.flush();

            this.entrada = new ObjectInputStream(socket.getInputStream());
            this.conectado = true;

            // Inicia a Thread
            ServidorThread ouvinte = new ServidorThread(this.socket);
            new Thread(ouvinte).start();
            
            System.out.println("Cliente: Conectado com sucesso na porta 5000.");
            return true;

        } catch (IOException e) {
            System.err.println("Erro ao conectar no servidor: " + e.getMessage());
            this.conectado = false;
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
        } catch (IOException e) {
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

    public void enviarMensagemDeTexto(String loginDestinatario, String textoConversa) {
        if (!conectado) return;
        try {
            Mensagem protocoloTexto = new Mensagem("TEXTO", loginDestinatario, null, textoConversa);
            saida.writeObject(protocoloTexto);
            saida.flush();

        } catch (IOException e) {
            System.err.println("Erro ao enviar mensagem de texto: " + e.getMessage());
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
}

