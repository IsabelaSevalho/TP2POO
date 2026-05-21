package br.edu.uea.chat.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import br.edu.uea.chat.model.Mensagem;

public class Cliente {
    
    private Socket socket;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private boolean conectado;
    private String usuarioLogado;
    
    // deixe alguns comentarios durante o codigo 
    // EU ODEIO VOCE COM TODA A MINHA ALMA SOCKET E THEREAD COMO EU TE ODEIO 
    //METODOS DA NOSSA HUMILDE CLIENTELA
   
    
    public Cliente() {}
    
    // ja está na porta 500 , ali no this.socket no 2° parametro
    public boolean conectar(String ip) {
        try {
            this.socket = new Socket(ip, 500);
            this.saida = new ObjectOutputStream(socket.getOutputStream());
            this.entrada = new ObjectInputStream(socket.getInputStream());
            this.conectado = true;

            // Inicia a Thread
            new Thread(new EscutadorServidor()).start();
            
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao conectar no servidor: " + e.getMessage());
            return false;
        }
    }
    
    //metodo ja puxando os dados dos objetos/classes filhas , padrão feijão com arroz
    public void efetuarLogin(String login, String senha) {
        if (!conectado) return;
        try {
            this.usuarioLogado = login;
            saida.writeObject("LOGIN;" + login + ";" + senha);
            saida.flush();
        } catch (IOException e) {
            System.err.println("Erro ao tentar logar: " + e.getMessage());
        }
    }
    
    public void solicitarStatusUsuarios() {
        if (!conectado) return;
        try {
            saida.writeObject("STATUS");
            saida.flush();
        } catch (IOException e) {
            System.err.println("Erro ao pedir status: " + e.getMessage());
        }
    }
}

