package br.faj.tcc.configuracao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
	Representa a configuração do servidor, permitindo salvar e recuperar
	em um arquivo serializado a opção da porta de escuta do servidor.
	Segue o padrão <b>Singleton</b>, ou seja, deve-se utilizar o método
	<b>recuperar()</b> para se obter uma instância desta classe.
	
	@author Ricardo Giovani Piantavinha Perandré
	@version 1.0
*/
public class Configuracao implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private static final String NOME_ARQUIVO = "servidor.cfg";
	
	private static Configuracao instancia = null;
	private int porta;

	public int getPorta()
	{
		return porta;
	}

	public void setPorta(int porta)
	{
		this.porta = (porta < 0) ? 0 : (porta > 65536) ? 65536 : porta;
	}

	private Configuracao()
	{
		this.porta = 5555;
	}
	
	public void salvar() throws IOException
	{
		this.serializar();
	}
	
	public static Configuracao recuperar() throws IOException, ClassNotFoundException
	{
		if (instancia == null)
		{
			File arquivo = new File(NOME_ARQUIVO);
			if (arquivo.exists())
				instancia = deserializar(arquivo);
			else
				instancia = new Configuracao();
		}
		return instancia;
	}
	
	private void serializar() throws IOException
	{
		try (FileOutputStream stream = new FileOutputStream(new File(NOME_ARQUIVO));
			 ObjectOutputStream serializador = new ObjectOutputStream(stream))
		{
			serializador.writeObject(this);
		}
	}
	
	private static Configuracao deserializar(File arquivo) throws IOException, ClassNotFoundException
	{
		try (FileInputStream stream = new FileInputStream(arquivo);
			 ObjectInputStream deserializador = new ObjectInputStream(stream))
		{
			return (Configuracao) deserializador.readObject();
		}
	}
}