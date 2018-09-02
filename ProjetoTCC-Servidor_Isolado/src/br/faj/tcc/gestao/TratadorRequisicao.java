package br.faj.tcc.gestao;

import java.io.IOException;
import java.io.Serializable;
import br.faj.tcc.util.Requisicao;
import br.faj.tcc.util.Fila;

/**
	M�dulo de tratamento de requisi��es, garantindo o fluxo de abertura, execu��o e fechamento. No uso do
	m�todo <b>gerenciar()</b>, segue-se o padr�o de projeto <b>Template Method</b>.
	
	@author Ricardo Giovani Piantavinha Perandr�
	@version 1.1
	@param <T> O tipo de objeto a ser transportado na comunica��o entre cliente e servidor.
*/
public abstract class TratadorRequisicao<T extends Serializable> implements Runnable
{
	private Fila<Requisicao<T>> requisicoes;
	private TratadorEvento<T> tratadorEvento;
	private Thread thread;
	
	/**
		Instancia um objeto <b>TratadorRequisicao</b> com a fila de requisi��es e o tratador injetados pelo
		servidor.<br>
		<font color="red">N�o instancie objetos desta classe. Apenas objetos de <b>Servidor</b> podem fazer
		uso coerente dos tratadores de requisi��o</font>.
	
		@param requisicoes A fila de requisi��es a ser consumida pelo tratador.
		@param tratadorEvento O objeto <b>TratadorEvento</b> para execu��o dos m�todos antes e ap�s o
		gerenciamento da requisi��o.
	*/
	public TratadorRequisicao(Fila<Requisicao<T>> requisicoes, TratadorEvento<T> tratadorEvento)
	{
		this.requisicoes = requisicoes;
		this.tratadorEvento = tratadorEvento;
		this.thread = null;
	}
	
	/**
		Implementa��o do m�todo obrigado pela interface <b>Runnable</b>.<br>
		<font color="red">Este m�todo n�o deve ser chamado fora do contexto da classe <b>Thread</b></font>.
	*/
	public void run()
	{
		while (!this.thread.isInterrupted())
		{
			Requisicao<T> conexao = this.requisicoes.remover();
			if (conexao != null)
			{			
				try
				{
					this.tratadorEvento.antesDeGerenciarRequisicao(conexao);
					this.gerenciar(conexao);
					this.tratadorEvento.depoisDeGerenciarRequisicao(conexao);
				} catch (IOException e)
				{
					e.printStackTrace();
				} finally
				{
					try
					{
						conexao.fechar();
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		this.limparFila();
	}
	
	/**
		Inicializa a execu��o do tratador de requisi��es.<br>
		<font color="red">Este m�todo n�o deve ser chamado fora do contexto da classe <b>Servidor</b></font>.
	*/
	public void iniciar()
	{
		if ((this.thread == null) || (!this.thread.isAlive()))
			(this.thread = new Thread(this)).start();
	}
	
	/**
		Para a execu��o do tratador de requisi��es.<br>
		<font color="red">Este m�todo n�o deve ser chamado fora do contexto da classe <b>Servidor</b></font>.
	*/
	public void parar()
	{
		if ((this.thread != null) || (this.thread.isAlive()))
			this.thread.interrupt();
	}
	
	/**
		Verifica se o tratador de requisi��es est� em execu��o.<br>
		<font color="red">Este m�todo n�o deve ser chamado fora do contexto da classe <b>Servidor</b></font>.
		
		@return <b>true</b> se o tratador de requisi��es estiver em execu��o e <b>false</b> se n�o estiver.
	*/
	public boolean estaEmExecucao()
	{
		return ((this.thread != null) && (this.thread.isAlive()));
	}
	
	/**
		Limpa a fila de requisi��es, fechando o socket e os streams de I/O.
	*/
	private void limparFila()
	{
		while (true)
		{
			Requisicao<T> conexao = this.requisicoes.remover();
			if (conexao == null)
				break;
			else
			{
				try
				{
					conexao.fechar();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
		Gerencia a requisi��o.
		
		@param requisicao O objeto <b>requisicao</b> a ser processado.
		@throws IOException
	*/
	protected abstract void gerenciar(Requisicao<T> requisicao) throws IOException;
}