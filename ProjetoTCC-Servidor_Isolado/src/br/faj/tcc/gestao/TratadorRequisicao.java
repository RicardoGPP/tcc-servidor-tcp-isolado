package br.faj.tcc.gestao;

import java.io.IOException;
import java.io.Serializable;
import br.faj.tcc.util.Requisicao;
import br.faj.tcc.util.Fila;

/**
	Módulo de tratamento de requisições, garantindo o fluxo de abertura, execução e fechamento. No uso do
	método <b>gerenciar()</b>, segue-se o padrão de projeto <b>Template Method</b>.
	
	@author Ricardo Giovani Piantavinha Perandré
	@version 1.1
	@param <T> O tipo de objeto a ser transportado na comunicação entre cliente e servidor.
*/
public abstract class TratadorRequisicao<T extends Serializable> implements Runnable
{
	private Fila<Requisicao<T>> requisicoes;
	private TratadorEvento<T> tratadorEvento;
	private Thread thread;
	
	/**
		Instancia um objeto <b>TratadorRequisicao</b> com a fila de requisições e o tratador injetados pelo
		servidor.<br>
		<font color="red">Não instancie objetos desta classe. Apenas objetos de <b>Servidor</b> podem fazer
		uso coerente dos tratadores de requisição</font>.
	
		@param requisicoes A fila de requisições a ser consumida pelo tratador.
		@param tratadorEvento O objeto <b>TratadorEvento</b> para execução dos métodos antes e após o
		gerenciamento da requisição.
	*/
	public TratadorRequisicao(Fila<Requisicao<T>> requisicoes, TratadorEvento<T> tratadorEvento)
	{
		this.requisicoes = requisicoes;
		this.tratadorEvento = tratadorEvento;
		this.thread = null;
	}
	
	/**
		Implementação do método obrigado pela interface <b>Runnable</b>.<br>
		<font color="red">Este método não deve ser chamado fora do contexto da classe <b>Thread</b></font>.
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
		Inicializa a execução do tratador de requisições.<br>
		<font color="red">Este método não deve ser chamado fora do contexto da classe <b>Servidor</b></font>.
	*/
	public void iniciar()
	{
		if ((this.thread == null) || (!this.thread.isAlive()))
			(this.thread = new Thread(this)).start();
	}
	
	/**
		Para a execução do tratador de requisições.<br>
		<font color="red">Este método não deve ser chamado fora do contexto da classe <b>Servidor</b></font>.
	*/
	public void parar()
	{
		if ((this.thread != null) || (this.thread.isAlive()))
			this.thread.interrupt();
	}
	
	/**
		Verifica se o tratador de requisições está em execução.<br>
		<font color="red">Este método não deve ser chamado fora do contexto da classe <b>Servidor</b></font>.
		
		@return <b>true</b> se o tratador de requisições estiver em execução e <b>false</b> se não estiver.
	*/
	public boolean estaEmExecucao()
	{
		return ((this.thread != null) && (this.thread.isAlive()));
	}
	
	/**
		Limpa a fila de requisições, fechando o socket e os streams de I/O.
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
		Gerencia a requisição.
		
		@param requisicao O objeto <b>requisicao</b> a ser processado.
		@throws IOException
	*/
	protected abstract void gerenciar(Requisicao<T> requisicao) throws IOException;
}