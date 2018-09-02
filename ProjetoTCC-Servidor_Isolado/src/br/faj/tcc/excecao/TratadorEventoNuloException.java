package br.faj.tcc.excecao;

/**
	Exce��o a ser lan�ada quando o tratador de eventos passado ao construtor do servidor for nulo.
	
	@author Ricardo Giovani Piantavinha Perandr�
	@version 1.0
*/
public class TratadorEventoNuloException extends Exception
{
	private static final long serialVersionUID = 1L;

	/**
		Construtor padr�o com a mensagem: "O tratador de eventos n�o pode ser nulo".
	*/
	public TratadorEventoNuloException()
	{
		super("O tratador de eventos n�o pode ser nulo.");
	}
}