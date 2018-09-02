package br.faj.tcc.servidor.nucleo;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import br.faj.tcc.servidor.excecao.TratadorEventoNuloException;
import br.faj.tcc.servidor.gestao.TratadorEvento;
import br.faj.tcc.servidor.gestao.TratadorRequisicao;
import br.faj.tcc.servidor.util.Fila;
import br.faj.tcc.servidor.util.Requisicao;

/**
	Controla a comunica��o com os clientes, permitindo novas conex�es, adicionando-as a uma fila
	que ser� processada pelo <b>tratador de requisi��es</b>. Cada evento do servidor ser� tratado pelo
	<b>tratador de eventos</b>, que executar� m�todos espec�ficos antes e depois de cada a��o ocorrida.
	
	@author Ricardo Giovani Piantavinha Perandr�
	@version 1.2
	@param <T> O tipo de objeto a ser transportado na comunica��o entre cliente e servidor.
*/
public class Servidor<T extends Serializable> implements Runnable
{
	private ServerSocket serverSocket;
	private Fila<Requisicao<T>> requisicoes;	
	private TratadorRequisicao<T> tratadorRequisicao;
	private TratadorEvento<T> tratadorEvento;
	private Thread thread;
	
	/**
		Instancia um objeto de <b>Servidor</b> com um tratador de requisi��es pr�-definido e um tratador
		de eventos padr�o, ou seja, n�o haver�o a��es decorrentes dos eventos disparados durante o ciclo
		de vida do objeto.
		
		@param tratadorRequisicao A classe do tratador de requisi��es a ser utilizado pelo servidor.
		@throws SecurityException
		@throws NoSuchMethodException
		@throws InvocationTargetException
		@throws IllegalArgumentException
		@throws IllegalAccessException
		@throws InstantiationException
	*/
	public Servidor(Class<? extends TratadorRequisicao<T>> tratadorRequisicao) throws InstantiationException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		this.serverSocket = null;
		this.requisicoes = new Fila<>();
		this.tratadorEvento = new TratadorEvento<T>();
		this.tratadorRequisicao = tratadorRequisicao.getDeclaredConstructor(Fila.class, TratadorEvento.class).newInstance(this.requisicoes, this.tratadorEvento);
		this.thread = null;
	}
	
	/**
		Instancia um objeto de <b>Servidor</b> com um tratador de requisi��es e de eventos pr�-definido.
		
		@param tratadorRequisicao A classe do tratador de requisi��es a ser utilizado pelo servidor.
		@param tratadorEvento O tratador de eventos a ser utilizado pelo servidor.
		@throws SecurityException
		@throws NoSuchMethodException
		@throws InvocationTargetException
		@throws IllegalArgumentException
		@throws IllegalAccessException
		@throws InstantiationException
		@throws TratadorEventoNuloException
	*/
	public Servidor(Class<? extends TratadorRequisicao<T>> tratadorRequisicao, TratadorEvento<T> tratadorEvento) throws InstantiationException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, TratadorEventoNuloException
	{
		this.serverSocket = null;
		this.requisicoes = new Fila<>();
		if (tratadorEvento == null)
			throw new TratadorEventoNuloException();
		else
			this.tratadorEvento = tratadorEvento;
		this.tratadorRequisicao = tratadorRequisicao.getDeclaredConstructor(Fila.class, TratadorEvento.class).newInstance(this.requisicoes, this.tratadorEvento);		
		this.thread = null;
	}
	
	/**
		Implementa��o do m�todo obrigado pela interface <b>Runnable</b>.<br>
		<font color="red">Este m�todo n�o deve ser chamado fora do contexto da classe <b>Thread</b></font>.
	*/
	public void run()
	{
		while ((!thread.isInterrupted()) && (!this.serverSocket.isClosed()))
		{
			try
			{
				this.tratadorEvento.antesDeAceitarRequisicao();
				Socket socket = this.serverSocket.accept();
				this.tratadorEvento.depoisDeAceitarRequisicao(socket);
				if (socket != null)
					this.requisicoes.adicionar(new Requisicao<T>(socket));
			} catch (SocketException e)
			{
				break;
			} catch (IOException e)
			{
				continue;
			} 
		}
	}
	
	/**
		Inicializa a execu��o do servidor.
		
		@param porta O n�mero da porta a ser monitorada pelo servidor.
		@throws IOException
	*/
	public void iniciar(int porta) throws IOException
	{		
		if ((this.thread == null) || (!this.thread.isAlive()))
		{
			this.tratadorEvento.antesDeIniciar();
			this.serverSocket = new ServerSocket(porta);
			if (this.tratadorRequisicao.estaEmExecucao())
				this.tratadorRequisicao.parar();
			(this.thread = new Thread(this)).start();
			this.tratadorRequisicao.iniciar();
			this.tratadorEvento.depoisDeIniciar(this.serverSocket);
		}
	}
	
	/**
		Para a execu��o do servidor.
		
		@throws IOException
	*/
	public void parar() throws IOException
	{
		if ((this.thread != null) && (this.thread.isAlive()))
		{
			this.tratadorEvento.antesDeParar(this.serverSocket);
			if (this.tratadorRequisicao.estaEmExecucao())
				this.tratadorRequisicao.parar();
			this.thread.interrupt();
			if ((this.serverSocket != null) && (!this.serverSocket.isClosed()))
				this.serverSocket.close();
			this.tratadorEvento.depoisDeParar(this.serverSocket);
		}
	}
}