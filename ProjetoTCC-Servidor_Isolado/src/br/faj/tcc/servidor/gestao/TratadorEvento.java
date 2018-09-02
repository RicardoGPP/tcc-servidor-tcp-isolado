package br.faj.tcc.servidor.gestao;

import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import br.faj.tcc.servidor.util.Requisicao;

/**
	É responsável por conter os métodos que serão chamados antes e após os eventos específicos
	do servidor. Esta implementação contém os métodos com o corpo vazio, ou seja, não haverá
	ação a ser realizada quando os métodos forem executados.
	
	@author Ricardo Giovani Piantavinha Perandré
	@version 1.0
	@param <T> O tipo de objeto a ser transportado na comunicação entre cliente e servidor.
*/
public class TratadorEvento<T extends Serializable>
{
	/**
		Método a ser executado antes do servidor iniciar.
	*/
	public void antesDeIniciar() {}
	
	/**
		Método a ser executado depois do servidor iniciar.
		
		@param serverSocket O objeto <b>ServerSocket</b> criado após a inicialização do servidor.
	*/
	public void depoisDeIniciar(ServerSocket serverSocket) {}
	
	/**
		Método a ser executado antes do servidor aceitar uma requisição.
	*/
	public void antesDeAceitarRequisicao() {}
	
	/**
		Método a ser executado depois do servidor aceitar uma requisição.
		
		@param socket O objeto <b>Socket</b> criado após o aceite de requisição. 
	*/
	public void depoisDeAceitarRequisicao(Socket socket) {}
	
	/**
		Método a ser executado antes do tratador de requisições gerenciar uma requisição.
		
		@param requisicao O objeto <b>Requisicao</b> a ser processado. 
	*/
	public void antesDeGerenciarRequisicao(Requisicao<T> requisicao) {}
	
	/**
		Método a ser executado depois do tratador de requisições gerenciar uma requisição.
		
		@param requisicao O objeto <b>Requisicao</b> processado. 
	*/
	public void depoisDeGerenciarRequisicao(Requisicao<T> requisicao) {}
	
	/**
		Método a ser executado antes de parar o servidor.
		
		@param socket O objeto <b>ServerSocket</b> do servidor. 
	*/
	public void antesDeParar(ServerSocket serverSocket) {}
	
	/**
		Método a ser executado depois de parar o servidor.
		
		@param socket O objeto <b>ServerSocket</b> do servidor. 
	*/
	public void depoisDeParar(ServerSocket serverSocket) {}
}