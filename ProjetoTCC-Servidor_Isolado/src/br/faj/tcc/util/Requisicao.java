package br.faj.tcc.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
	Encapsulamento do socket e das streams de I/O para comunica��o entre o cliente e o servidor.

	@author Ricardo Giovani Piantavinha Perandr�
	@version 1.0
	@param <T> O tipo de objeto a ser transportado na comunica��o entre cliente e servidor.
*/

public class Requisicao<T extends Serializable>
{
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	
	/**
		Instancia uma requisi��o encapsulando o socket de comunica��o e gerando as respectivas
		streams de I/O.<br>
		<font color="red">N�o instancie objetos desta classe. Apenas objetos de <b>Servidor</b> e
		<b>TratadorRequisicao</b> podem fazer uso coerente das requisi��es</font>.
		
	 	@param socket O objeto <b>Socket</b> de comunica��o com o cliente.
		@throws IOException
	*/
	public Requisicao(Socket socket) throws IOException
	{
		this.socket = socket;
		this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		this.objectInputStream = new ObjectInputStream(socket.getInputStream());
	}
	
	/**
		Faz a leitura do objeto enviado pelo cliente.
	
		@return Um objeto do tipo param�trico <b>T</b> lido da stream de entrada.
		@throws IOException
		@throws ClassNotFoundException
	*/
	@SuppressWarnings("unchecked")
	public T ler() throws IOException, ClassNotFoundException
	{
		Object objeto = this.objectInputStream.readObject();
		return (objeto == null) ? null : (T) objeto;
	}
	
	/**
		Escreve um objeto para o cliente na stream de sa�da.
		
		@param objeto O objeto do tipo param�trico <b>T</b> a ser escrito.
		@throws IOException
	*/
	public void escrever(T objeto) throws IOException
	{
		this.objectOutputStream.writeObject(objeto);
		this.objectOutputStream.flush();
	}

	/**
		Fecha as streams e o socket de comunica��o.
		
		@throws Exception
	*/
	public void fechar() throws Exception
	{
		if ((this.socket != null) && (!this.socket.isClosed()))
			this.socket.close();		
		if (this.objectOutputStream != null)
			this.objectOutputStream.close();
		if (this.objectInputStream != null)
			this.objectInputStream.close();
	}
}