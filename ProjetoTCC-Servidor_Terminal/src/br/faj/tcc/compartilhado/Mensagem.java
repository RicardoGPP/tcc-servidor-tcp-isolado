package br.faj.tcc.compartilhado;

import java.io.Serializable;

/**
	Representa o objeto serializável que servirá para transporte de dados
	entre o cliente e o servidor.
	
	@author Ricardo Giovani Piantavinha Perandré
	@version 1.2
*/
public class Mensagem implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private short sucesso;
	private String conteudo;
	
	public boolean isSucesso()
	{
		return (this.sucesso == 1);
	}
	
	public String getConteudo()
	{
		return conteudo;
	}
	
	public Mensagem(boolean sucesso, String conteudo)
	{
		this.sucesso = (short) ((sucesso) ? 1 : 0);
		this.conteudo = (conteudo == null) ? "" : conteudo;
	}
}