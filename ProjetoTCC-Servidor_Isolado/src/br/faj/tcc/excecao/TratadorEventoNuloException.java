package br.faj.tcc.excecao;

/**
	Exceção a ser lançada quando o tratador de eventos passado ao construtor do servidor for nulo.
	
	@author Ricardo Giovani Piantavinha Perandré
	@version 1.0
*/
public class TratadorEventoNuloException extends Exception
{
	private static final long serialVersionUID = 1L;

	/**
		Construtor padrão com a mensagem: "O tratador de eventos não pode ser nulo".
	*/
	public TratadorEventoNuloException()
	{
		super("O tratador de eventos não pode ser nulo.");
	}
}