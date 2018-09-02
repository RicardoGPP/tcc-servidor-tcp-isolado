package br.faj.tcc.servidor.gestao;

import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import br.faj.tcc.servidor.util.Requisicao;

/**
	� respons�vel por conter os m�todos que ser�o chamados antes e ap�s os eventos espec�ficos
	do servidor. Esta implementa��o cont�m os m�todos com o corpo vazio, ou seja, n�o haver�
	a��o a ser realizada quando os m�todos forem executados.
	
	@author Ricardo Giovani Piantavinha Perandr�
	@version 1.0
	@param <T> O tipo de objeto a ser transportado na comunica��o entre cliente e servidor.
*/
public class TratadorEvento<T extends Serializable>
{
	/**
		M�todo a ser executado antes do servidor iniciar.
	*/
	public void antesDeIniciar() {}
	
	/**
		M�todo a ser executado depois do servidor iniciar.
		
		@param serverSocket O objeto <b>ServerSocket</b> criado ap�s a inicializa��o do servidor.
	*/
	public void depoisDeIniciar(ServerSocket serverSocket) {}
	
	/**
		M�todo a ser executado antes do servidor aceitar uma requisi��o.
	*/
	public void antesDeAceitarRequisicao() {}
	
	/**
		M�todo a ser executado depois do servidor aceitar uma requisi��o.
		
		@param socket O objeto <b>Socket</b> criado ap�s o aceite de requisi��o. 
	*/
	public void depoisDeAceitarRequisicao(Socket socket) {}
	
	/**
		M�todo a ser executado antes do tratador de requisi��es gerenciar uma requisi��o.
		
		@param requisicao O objeto <b>Requisicao</b> a ser processado. 
	*/
	public void antesDeGerenciarRequisicao(Requisicao<T> requisicao) {}
	
	/**
		M�todo a ser executado depois do tratador de requisi��es gerenciar uma requisi��o.
		
		@param requisicao O objeto <b>Requisicao</b> processado. 
	*/
	public void depoisDeGerenciarRequisicao(Requisicao<T> requisicao) {}
	
	/**
		M�todo a ser executado antes de parar o servidor.
		
		@param socket O objeto <b>ServerSocket</b> do servidor. 
	*/
	public void antesDeParar(ServerSocket serverSocket) {}
	
	/**
		M�todo a ser executado depois de parar o servidor.
		
		@param socket O objeto <b>ServerSocket</b> do servidor. 
	*/
	public void depoisDeParar(ServerSocket serverSocket) {}
}