package br.edu.uea.chat.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import br.edu.uea.chat.model.Mensagem;
import br.edu.uea.chat.model.Usuario;
import br.edu.uea.chat.view.TelaChat;

/**
 * Esta classe serve para manter a comunicação entre servidor e cliente.
 * Responsável por escutar respostas vindas do servidor.
 * 
 * Recebe respostas: Servidor → Cliente
 * 
 * @version 2.0
 */

public class ServidorThread implements Runnable{
    private Socket socket;
    private ObjectInputStream objectIS;
    private TelaChat telaChat;

    public ServidorThread(Socket socket){
        this.socket = socket;
        this.telaChat = null;
    }

    public ServidorThread(Socket socket, TelaChat telaChat){
        this.socket = socket;
        this.telaChat = telaChat;
    }

    @Override
     public void run(){
        try{
            this.objectIS = new ObjectInputStream(socket.getInputStream());

            while(true){
                Object obj = objectIS.readObject();
                
                if (obj instanceof String) {
                    String respostaTexto = (String) obj;
                    
                    if ("CONFIRMACAO_LOGIN_OK".equals(respostaTexto)) {
                        System.out.println("Cliente/ServidorThread: Autenticação confirmada pelo servidor de forma segura!");
                    }
                }else if(obj instanceof Mensagem){
                    Mensagem msg = (Mensagem) obj;
                    System.out.println("\n[Nova Mensagem] " + msg);

                } else if(obj instanceof ArrayList){
                    @SuppressWarnings("unchecked")
                    ArrayList<Usuario> listaUsuarios = (ArrayList<Usuario>) obj;
                    System.out.println("Cliente: Lista de usuários actualizada recebida.");
                }
            }
        } catch (IOException e) {
            System.out.println("Cliente/ServidorThread: Você foi desconectado do servidor (Conexão encerrada pelo Técnico/Servidor).");
            desconectar();
                
        } catch (ClassNotFoundException e) {
            System.out.println("Cliente/ServidorThread: Erro ao converter objeto recebido.");
            e.printStackTrace();
        }
    }

    public void desconectar(){
        try {
            if (objectIS != null) {
                objectIS.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Cliente/ServidorThread: Recursos locais liberados após desconexão.");
        } catch (IOException e) {
            System.out.println("Cliente/ServidorThread: Erro ao fechar streams da ServidorThread.");
        }
    }
}