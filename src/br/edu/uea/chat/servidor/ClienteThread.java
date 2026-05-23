package br.edu.uea.chat.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import br.edu.uea.chat.control.MensagemController;
import br.edu.uea.chat.control.UsuarioController;
import br.edu.uea.chat.model.Mensagem;
import br.edu.uea.chat.model.Usuario;
/**
 * Esta classe serve para manter a comunicação entre cliente e servidor.
 * Responsável por atender conexões de clientes e fornecer respostas.
 * 
 * Recebe respostas: Cliente → Servidor
 * 
 * @version 2.0
 */

public class ClienteThread extends Thread{
	private Socket socket;
    private ObjectInputStream objectIS;
	private ObjectOutputStream objectOS;
    private Usuario usuarioLogado;
	
	public ClienteThread(Socket socket) {
        this.socket = socket;
    }

    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }
	
	public void run() {
		try {
            this.objectOS = new ObjectOutputStream(socket.getOutputStream());
			this.objectIS = new ObjectInputStream(socket.getInputStream());
            
            UsuarioController usuarioControl = new UsuarioController();
            MensagemController msgControl = new MensagemController();

            while (true) {
                Object obj =objectIS.readObject(); //aguarda protocolo (msg)

                if (obj instanceof Mensagem) {
                    Mensagem msg = (Mensagem) obj;
                    String acao = msg.getAcao();

                    switch(acao){
                        case "CADASTRO":
                            boolean cadastroRealizado = usuarioControl.cadastrarUsuario(msg.getUsuario());
                            
                            if (cadastroRealizado) {
								System.out.println("Servidor/ClienteThread: Usuário " + msg.getUsuario().getUser() + " cadastrado.");
							} else {
								System.out.println("Servidor/ClienteThread: Falha ao cadastrar. Usuário já existe.");
							}

                            break;
                        case "LOGIN":
                            Usuario usuarioLogin = msg.getUsuario();
                            boolean loginRealizado = usuarioControl.login(usuarioLogin.getUser(), usuarioLogin.getSenha());

                            if(loginRealizado){
                                this.usuarioLogado = usuarioControl.buscarUsuario(usuarioLogin.getUser());
                                Servidor.addClienteOnline(this);
                                
                                enviarObjetoAoServidor("CONFIRMACAO_LOGIN_OK");

                                System.out.println("Servidor/ClienteThread: Usuário " + this.usuarioLogado.getUser() + " entrou online.");

                                ArrayList<Mensagem> pendentes = msgControl.entregarMensagensPendentes(this.usuarioLogado.getUser());
                                for (Mensagem msgPendente : pendentes) {
                                    enviarObjetoAoServidor(msgPendente); 
                                }

                            }else{
                                System.out.println("Servidor/ClienteThread: Tentativa de login inválida para " + usuarioLogin.getUser());
                                enviarObjetoAoServidor("LOGIN_INVALIDO");
                            }

                            break;

                        case "LISTAR":
                            ArrayList<Usuario> listaUsuarios = usuarioControl.getUsuarios();
							enviarObjetoAoServidor(listaUsuarios);
                            break;

                        case "TEXTO":
                            String destino = msg.getDestinatario();
							boolean msgEntregue = false;
                            
                            for(ClienteThread ctOn : Servidor.getClientesOnline()){
                                if(ctOn.getUsuarioLogado() != null && ctOn.getUsuarioLogado().getUser().equals(destino)){
                                    ctOn.enviarObjetoAoServidor(msg);
                                    System.out.println("Servidor/ClienteThread: Mensagem enviada para " + destino);
									msgEntregue = true;
									break;
                                }
                            }

                            if (!msgEntregue) {
								System.out.println("Servidor/ClienteThread: " + destino + " offline. Armazenando mensagem.");
								msgControl.armazenarMensagemOffline(msg);
							}
							break;

                        case "KILL"://como a ideia é a interface ter os botoes de acordo com o tipo de usuario, la vai ter a restrição p cada tipo
                            String alvo = msg.getDestinatario();

                            if ("TODOS".equals(alvo)) {
                                ArrayList<ClienteThread> ativos = Servidor.getClientesOnline();
                                for (int i = ativos.size() - 1; i >= 0; i--) {
                                    ClienteThread ctOn = ativos.get(i);
                                    if (ctOn != this) {
                                        ctOn.desconectar();
                                        Servidor.removeClienteOnline(ctOn);
                                    }
                                }
                                System.out.println("Servidor/ClienteThread: Técnico derrubou todas as conexões.");

                            } else {
                                String[] usuariosParaDerrubar = alvo.split(",");
                                
                                for (String loginAlvo : usuariosParaDerrubar) {
                                    String loginLimpo = loginAlvo.trim();
                                    ArrayList<ClienteThread> ativos = Servidor.getClientesOnline();
                                    
                                    for (int i = ativos.size() - 1; i >= 0; i--) {
                                        ClienteThread ctOn = ativos.get(i);
                                        
                                        if (ctOn.getUsuarioLogado() != null && ctOn.getUsuarioLogado().getUser().equals(loginLimpo)) {
                                            ctOn.desconectar();
                                            Servidor.removeClienteOnline(ctOn);
                                            System.out.println("Servidor/ClienteThread: Técnico derrubou o usuário " + loginLimpo);
                                            break;
                                        }
                                    }
                                }
                            }
                            break;

                        default:
                            System.out.println("Servidor/ClienteThread: Ação desconhecida recebida.");
							break;
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Servidor/ClienteThread: Erro fatal detectado no loop do servidor:");
            e.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
    
    public void enviarObjetoAoServidor(Object obj) {
        try {
            if (objectOS != null) {
                objectOS.writeObject(obj);
                objectOS.flush();
            }
        } catch (IOException e) {
            System.out.println("Servidor/ClienteThread: Erro ao enviar objeto ao cliente.");
        }
    }

    public void desconectar() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Servidor/ClienteThread: Erro ao fechar socket no método desconectar.");
        }
    }
}
