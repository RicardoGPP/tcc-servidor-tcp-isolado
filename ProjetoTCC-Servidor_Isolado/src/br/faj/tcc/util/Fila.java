package br.faj.tcc.util;

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
	private List<T> objetos;
	
	/**
		Instancia uma fila vazia.
	*/
	public Fila()
	{
		this.objetos = new ArrayList<>();
	}
	
	/**
		Adiciona um elemento � fila.
		
		@param elemento O elemento a ser adicionado.
	*/
	public synchronized void adicionar(T elemento)
	{
		this.objetos.add(elemento);
	}
	
	/**
		Retorna o elemento na primeira posi��o da fila.
		
		@return <b>null</b> se a fila estiver vazia ou um objeto do tipo param�trico <b>T</b>
		se n�o estiver. 
	*/
	public synchronized T remover()
	{
		return (this.objetos.size() > 0) ? this.objetos.remove(0) : null;
	}
}