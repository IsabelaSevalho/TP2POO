package br.edu.uea.chat.cliente;
/**
 * Esta classe serve para manter a comunicação entre servidor e cliente.
 * Responsável por escutar respostas vindas do servidor.
 * 
 * Recebe respostas: Servidor → Cliente
 * 
 * @version 1.0
 */

public class ServidorThread implements Runnable {

    @Override
    public void run() {

        while (true) {

            try {

                /*
                 * Aqui futuramente você
                 * lerá objetos vindos
                 * do servidor.
                 */

                Thread.sleep(1000); //so enquanto nao implementa outros itens

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }
}