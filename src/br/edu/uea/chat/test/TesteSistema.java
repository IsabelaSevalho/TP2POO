package br.edu.uea.chat.test;

import br.edu.uea.chat.servidor.Servidor;
import br.edu.uea.chat.cliente.Cliente;
import br.edu.uea.chat.model.Aluno;
import br.edu.uea.chat.model.Tecnico;

/**
 * Pedi da IA
 * Classe humilde para testar o funcionamento das Threads, Sockets e os DAOs.
 * Roda tudo em lote sequencial simulando a interação dos usuários.
 */

public class TesteSistema {

        public static void main(String[] args) {
        System.out.println("=== INICIANDO AMBIENTE DE TESTES ===");

        // PONTO CHAVE: GERA OS USUÁRIOS NO ARQUIVO ANTES DO SERVIDOR LIGAR
        System.out.println("[TESTE] Simulando carga inicial de usuários no arquivo...");
        br.edu.uea.chat.control.UsuarioController cargaInicial = new br.edu.uea.chat.control.UsuarioController();
        
        // Cadastra o Técnico no arquivo físico
        Tecnico tec = new Tecnico();
        tec.setUser("admin_tec");
        tec.setSenha("1234");
        cargaInicial.cadastrarUsuario(tec);

        // Cadastra o Aluno no arquivo físico
        Aluno novoAluno = new Aluno();
        novoAluno.setUser("aluno_uea");
        novoAluno.setSenha("senha123");
        cargaInicial.cadastrarUsuario(novoAluno);
        
        pausar(1500); // Aguarda o arquivo ser gravado em disco

        // 1. LIGA O SERVIDOR EM SEGUNDO PLANO
        System.out.println("[TESTE] Inicializando o Servidor na porta 5000...");
        Servidor servidor = new Servidor();
        servidor.start();

        // Pausa pequena para dar tempo do ServerSocket abrir de verdade
        pausar(1500);

        // 2. CRIAÇÃO E CONEXÃO DO PRIMEIRO CLIENTE (O TÉCNICO)
        System.out.println("\n[TESTE] Criando conexão do Técnico na porta 5000...");
        Cliente clienteTecnico = new Cliente();
        if (clienteTecnico.conectar("127.0.0.1")) {
            System.out.println("[TESTE] Técnico conectado à rede.");
        }
        
        // Agora o login vai dar TRUE porque o admin_tec já existe no arquivo
        clienteTecnico.efetuarLogin(tec);
        pausar(1000);

        // 3. TÉCNICO CADASTRA UM NOVO ALUNO VIA SERVIDOR (Opcional, pois já criamos acima)
        System.out.println("\n[TESTE] Técnico online monitorando a rede.");
        pausar(1000);

        // 4. CRIAÇÃO E CONEXÃO DO SEGUNDO CLIENTE (O ALUNO QUE FOI CADASTRADO)
        System.out.println("\n[TESTE] Criando conexão do Aluno na porta 5000...");
        Cliente clienteAluno = new Cliente();
        clienteAluno.conectar("127.0.0.1");
        pausar(500);

        System.out.println("[TESTE] Aluno tentando acessar a conta...");
        // Agora o login do aluno também vai dar TRUE
        clienteAluno.efetuarLogin(novoAluno);
        pausar(1500);

        // 5. TESTE DE TROCA DE MENSAGENS TEXTUAIS (ALUNO -> TÉCNICO)
        System.out.println("\n[TESTE] Aluno enviando mensagem para 'admin_tec'...");
        clienteAluno.enviarMensagemDeTexto("admin_tec", "Olá Técnico! Consegui logar no chat!");
        pausar(1500);

        // 6. TESTE DO COMANDO DO TÉCNICO (DERRUBAR O ALUNO)
        System.out.println("\n[TESTE] Técnico executando comando KILL para derrubar 'aluno_uea'...");
        clienteTecnico.enviarComandoKill("aluno_uea");
        pausar(2000);

        // 7. FINALIZAÇÃO DO AMBIENTE
        System.out.println("\n=== ROTINA DE TESTES CONCLUÍDA ===");
        System.out.println("Verifique os logs do console acima para confirmar se:");
        System.out.println("- O Aluno efetuou login com sucesso");
        System.out.println("- A ClienteThread leu '[Nova Mensagem]'");
        System.out.println("- O Aluno capturou o 'Você foi desconectado' disparado pelo KILL");
        
        System.exit(0); // Força a finalização das threads de loop infinito
    }


    /**
     * Método auxiliar simples para dar tempo entre as requisições de rede
     */
    private static void pausar(int milissegundos) {
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
