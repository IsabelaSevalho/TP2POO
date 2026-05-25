package br.edu.uea.chat.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import br.edu.uea.chat.control.MensagemController;
import br.edu.uea.chat.control.UsuarioController;
import br.edu.uea.chat.model.Aluno;
import br.edu.uea.chat.model.Mensagem;
import br.edu.uea.chat.model.Professor;
import br.edu.uea.chat.model.Tecnico;
import br.edu.uea.chat.model.Usuario;

/**
 * @version 3.2 (corrigido: cadastro permitido apenas para técnicos)
 */

public class ClienteThread extends Thread{
	private Socket socket;
    private ObjectInputStream objectIS;
	private ObjectOutputStream objectOS;
    private Usuario usuarioLogado;
    private Servidor servidor;
	
	public ClienteThread(Socket socket) {
        this.socket = socket;
    }

    public ClienteThread(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;
    }

    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }
	
	public void run() {
		try {
            this.objectOS = new ObjectOutputStream(socket.getOutputStream());
			this.objectIS = new ObjectInputStream(socket.getInputStream());
            
            UsuarioController usuarioControl = servidor.getUsuarioController();
            MensagemController msgControl = servidor.getMensagemController();

            while (true) {
                Object obj = objectIS.readObject();

                if (obj instanceof Mensagem) {
                    Mensagem msg = (Mensagem) obj;
                    String acao = msg.getAcao();

                    switch(acao){
                        case "CADASTRO":
                            if (usuarioLogado == null || !(usuarioLogado instanceof Tecnico)) {
                                enviarObjetoAoServidor("ERRO_PERMISSAO: Apenas técnicos podem cadastrar usuários.");
                                System.out.println("Servidor/ClienteThread: Tentativa de cadastro sem permissão.");
                                break;
                            }
                            boolean cadastroRealizado = usuarioControl.cadastrarUsuario(msg.getUsuario());
                            if (cadastroRealizado) {
                                enviarObjetoAoServidor("CADASTRO_OK");
                                System.out.println("Servidor/ClienteThread: Usuário " + msg.getUsuario().getUser() + " cadastrado pelo técnico.");
                            } else {
                                enviarObjetoAoServidor("CADASTRO_ERRO: Usuário já existe.");
                                System.out.println("Servidor/ClienteThread: Falha ao cadastrar. Usuário já existe.");
                            }
                            break;

                        case "LOGIN":
                            Usuario usuarioLogin = msg.getUsuario();
                            boolean loginRealizado = usuarioControl.login(usuarioLogin.getUser(), usuarioLogin.getSenha());

                            if(loginRealizado){
                                this.usuarioLogado = usuarioControl.buscarUsuario(usuarioLogin.getUser());
                                Servidor.addClienteOnline(this);
                                
                                enviarObjetoAoServidor(this.usuarioLogado); 
                                System.out.println("Servidor/ClienteThread: Usuário " + this.usuarioLogado.getUser() + " entrou online.");

                                ArrayList<Mensagem> pendentes = msgControl.entregarMensagensPendentes(this.usuarioLogado.getUser());
                                for (Mensagem msgPendente : pendentes) {
                                    enviarObjetoAoServidor(msgPendente); 
                                }
                            } else {
                                System.out.println("Servidor/ClienteThread: Tentativa de login inválida para " + usuarioLogin.getUser());
                                enviarObjetoAoServidor("LOGIN_INVALIDO");
                            }
                            break;


                        case "LISTAR":
                            ArrayList<Usuario> todosUsuarios = usuarioControl.getUsuarios();
                            ArrayList<Usuario> listaComStatus = new ArrayList<>();
                            
                            for (Usuario u : todosUsuarios) {
                                boolean online = false;
                                for (ClienteThread ct : Servidor.getClientesOnline()) {
                                    if (ct.getUsuarioLogado() != null && ct.getUsuarioLogado().getUser().equals(u.getUser())) {
                                        online = true;
                                        break;
                                    }
                                }
                                Usuario usuarioComStatus = criarCopiaComStatus(u, online);
                                listaComStatus.add(usuarioComStatus);
                            }
                            enviarObjetoAoServidor(listaComStatus);
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

                        case "KILL":
                            if (usuarioLogado == null || !(usuarioLogado instanceof Tecnico)) {
                                enviarObjetoAoServidor("ERRO_PERMISSAO: Apenas técnicos podem desconectar usuários.");
                                System.out.println("Servidor/ClienteThread: Tentativa de KILL sem permissão.");
                                break;
                            }
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
                                enviarObjetoAoServidor("KILL_OK: Todos os usuários foram desconectados.");
                            } else {
                                String[] usuariosParaDerrubar = alvo.split(",");
                                int derrubados = 0;
                                for (String loginAlvo : usuariosParaDerrubar) {
                                    String loginLimpo = loginAlvo.trim();
                                    ArrayList<ClienteThread> ativos = Servidor.getClientesOnline();
                                    boolean encontrado = false;
                                    for (int i = ativos.size() - 1; i >= 0; i--) {
                                        ClienteThread ctOn = ativos.get(i);
                                        if (ctOn.getUsuarioLogado() != null && ctOn.getUsuarioLogado().getUser().equals(loginLimpo)) {
                                            ctOn.desconectar();
                                            Servidor.removeClienteOnline(ctOn);
                                            System.out.println("Servidor/ClienteThread: Técnico derrubou o usuário " + loginLimpo);
                                            derrubados++;
                                            encontrado = true;
                                            break;
                                        }
                                    }
                                    if (!encontrado) {
                                        System.out.println("Servidor/ClienteThread: Usuário " + loginLimpo + " não está online.");
                                    }
                                }
                                enviarObjetoAoServidor("KILL_OK: " + derrubados + " usuário(s) desconectado(s).");
                            }
                            break;
                        default:
                            System.out.println("Servidor/ClienteThread: Ação desconhecida recebida.");
                            break;
                    }
                }
            }
        }catch (java.net.SocketException e) {
            System.out.println("Servidor: Conexão encerrada com o cliente (" + 
                (usuarioLogado != null ? usuarioLogado.getUser() : "Desconhecido") + ").");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro de classe não encontrada: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de E/S na thread do cliente: " + e.getMessage());
        } finally {
            if (this.usuarioLogado != null) {
                Servidor.removeClienteOnline(this);
                System.out.println("Servidor: " + usuarioLogado.getUser() + " removido da lista de ativos.");
            }
            desconectar();
        }
	}
    
    private Usuario criarCopiaComStatus(Usuario original, boolean status) {
        Usuario copia = null;
        if (original instanceof Aluno) {
            Aluno a = (Aluno) original;
            copia = new Aluno(a.getUser(), a.getSenha(), a.getAnoDeIngresso());
        } else if (original instanceof Professor) {
            Professor p = (Professor) original;
            copia = new Professor(p.getUser(), p.getSenha(), p.getTitulacao());
        } else if (original instanceof Tecnico) {
            copia = new Tecnico(original.getUser(), original.getSenha());
        }
        if (copia != null) {
            copia.setStatus(status);
        }
        return copia;
    }

    public void enviarObjetoAoServidor(Object obj) {
        try {
            if (objectOS != null) {
                objectOS.writeObject(obj);
                objectOS.flush();
            }
        } catch (IOException e) {
            System.err.println("Servidor/ClienteThread: Erro ao enviar objeto ao cliente.");
        }
    }

    public void desconectar() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Servidor/ClienteThread: Erro ao fechar socket.");
        }
    }
}