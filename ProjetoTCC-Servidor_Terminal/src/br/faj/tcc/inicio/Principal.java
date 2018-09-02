package br.faj.tcc.inicio;

import java.net.ServerSocket;
import java.net.Socket;
import br.faj.tcc.compartilhado.Mensagem;
import br.faj.tcc.nucleo.TratadorRequisicaoInput;
import br.faj.tcc.servidor.gestao.TratadorEvento;
import br.faj.tcc.servidor.nucleo.Servidor;
import br.faj.tcc.servidor.util.Requisicao;

public class Principal
{
	public static void main(String[] args)
	{
		try
		{	
			Servidor<Mensagem> servidor = new Servidor<>(TratadorRequisicaoInput.class, new TratadorEvento<Mensagem>()
			{
				@Override
				public void depoisDeIniciar(ServerSocket serverSocket)
				{
					System.out.println("Servidor iniciado na porta " + serverSocket.getLocalPort() + ".");
				}
				
				@Override
				public void antesDeAceitarRequisicao()
				{
					System.out.println("Aguardando conexão...");
				}
				
				@Override
				public void depoisDeAceitarRequisicao(Socket socket)
				{
					System.out.println("Cliente conectado (" + socket.getInetAddress() + ").");
				}
				
				@Override
				public void depoisDeGerenciarRequisicao(Requisicao<Mensagem> requisicao)
				{
					System.out.println("Requisição gerenciada.");
				}
			});			
			servidor.iniciar(5557);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
