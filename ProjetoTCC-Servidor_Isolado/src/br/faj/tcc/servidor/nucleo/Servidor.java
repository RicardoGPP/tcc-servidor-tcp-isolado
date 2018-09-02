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
	Controla a comunicação com os clientes, permitindo novas conexões, adicionando-as a uma fila
	que será processada pelo <b>tratador de requisições</b>. Cada evento do servidor será tratado pelo
	<b>tratador de eventos</b>, que executará métodos específicos antes e depois de cada ação ocorrida.
	
	@author Ricardo Giovani Piantavinha Perandré
	@version 1.2
	@param <T> O tipo de objeto a ser transportado na comunicação entre cliente e servidor.
*/
public class Servidor<T extends Serializable> implements Runnable
{
	private ServerSocket serverSocket;
	private Fila<Requisicao<T>> requisicoes;	
	private TratadorRequisicao<T> tratadorRequisicao;
	private TratadorEvento<T> tratadorEvento;
	private Thread thread;
	
	/**
		Instancia um objeto de <b>Servidor</b> com um tratador de requisições pré-definido e um tratador
		de eventos padrão, ou seja, não haverão ações decorrentes dos eventos disparados durante o ciclo
		de vida do objeto.
		
		@param tratadorRequisicao A classe do tratador de requisições a ser utilizado pelo servidor.
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
		Instancia um objeto de <b>Servidor</b> com um tratador de requisições e de eventos pré-definido.
		
		@param tratadorRequisicao A classe do tratador de requisições a ser utilizado pelo servidor.
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
		Implementação do método obrigado pela interface <b>Runnable</b>.<br>
		<font color="red">Este método não deve ser chamado fora do contexto da classe <b>Thread</b></font>.
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
		Inicializa a execução do servidor.
		
		@param porta O número da porta a ser monitorada pelo servidor.
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
		Para a execução do servidor.
		
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