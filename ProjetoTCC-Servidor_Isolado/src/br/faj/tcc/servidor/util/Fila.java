package br.faj.tcc.servidor.util;

import java.util.ArrayList;
import java.util.List;

/**
	Armazena e gerencia uma cole��o de elementos no comportamento de fila. Est� preparada
	para trabalhar no contexto do acesso concorrente das threads do servidor.
	
	@author Ricardo Giovani Piantavinha Perandr�
	@version 1.0
	@param <T> Um tipo qualquer.
*/
public class Fila<T>
{
	private List<T> elementos;
	
	/**
		Instancia uma fila vazia.
	*/
	public Fila()
	{
		this.elementos = new ArrayList<>();
	}
	
	/**
		Adiciona um elemento � fila.
		
		@param elemento O elemento a ser adicionado.
	*/
	public synchronized void adicionar(T elemento)
	{
		this.elementos.add(elemento);
	}
	
	/**
		Retorna o elemento na primeira posi��o da fila.
		
		@return <b>null</b> se a fila estiver vazia ou um objeto do tipo param�trico <b>T</b>
		se n�o estiver. 
	*/
	public synchronized T remover()
	{
		return (this.elementos.size() > 0) ? this.elementos.remove(0) : null;
	}
}