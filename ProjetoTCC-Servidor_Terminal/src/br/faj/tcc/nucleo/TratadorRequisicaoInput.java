package br.faj.tcc.nucleo;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.regex.Pattern;

import br.faj.tcc.compartilhado.Mensagem;
import br.faj.tcc.servidor.gestao.TratadorEvento;
import br.faj.tcc.servidor.gestao.TratadorRequisicao;
import br.faj.tcc.servidor.util.Fila;
import br.faj.tcc.servidor.util.Requisicao;

/**
	A implementação de <b>TratadorRequisicao</b> para a definição do gerenciamento da conexão. Esta implementação
	infere o tipo <b>Mensagem</b> ao parâmetro de tipo da classe super <b>TratadorRequisicao</b>. O conteúdo da
	mensagem é convertido em inputs do teclado, simulando o pressionar das teclas.

	@author Ricardo Giovani Piantavinha Perandré
	@version 1.0
*/
public class TratadorRequisicaoInput extends TratadorRequisicao<Mensagem>
{	
	private Teclado teclado;
	
	public TratadorRequisicaoInput(Fila<Requisicao<Mensagem>> requisicoes, TratadorEvento<Mensagem> tratadorEvento)
	{
		super(requisicoes, tratadorEvento);
		this.teclado = null;
	}

	protected void gerenciar(Requisicao<Mensagem> requisicao) throws IOException
	{
		try
		{
			if (this.teclado == null)
				this.teclado = new Teclado();
			Mensagem mensagem = requisicao.ler();			
			if (mensagem != null)
			{
				String conteudo = mensagem.getConteudo();				
				if (!this.conteudoEValido(conteudo))
					requisicao.escrever(new Mensagem(false, "O conteúdo da mensagem é inválido."));
				else
				{
					this.digitar(mensagem.getConteudo());
					requisicao.escrever(new Mensagem(true, "Teste"));
				}
			}			
		} catch (ClassNotFoundException e)
		{
			requisicao.escrever(new Mensagem(false, "O objeto enviado não pôde ser identificado."));
		} catch (AWTException e)
		{
			requisicao.escrever(new Mensagem(false, "Não foi possível simular o input do conteúdo da mensagem."));
		}
	}
	
	private boolean conteudoEValido(String conteudo)
	{
		Pattern pattern = Pattern.compile("^(\\w)*$");
		return pattern.matcher(conteudo).matches();
	}

	private void digitar(String conteudo)
	{
		for (int i = 0; i < conteudo.length(); i++)
			this.teclado.pressionar(conteudo.charAt(i));			
	}
	
	private class Teclado
	{	
		private Robot robot;
		
		private Teclado() throws AWTException
		{
			this.robot = new Robot();
		}
		
		private boolean capsLockEstaAtivo()
		{
			return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
		}
		
		private boolean precisaShift(char simbolo)
		{
			return ((this.capsLockEstaAtivo()) && (Character.isLowerCase(simbolo)) ||
			       ((!this.capsLockEstaAtivo()) && (Character.isUpperCase(simbolo))));
		}
		
		private void pressionar(char simbolo)
		{
			int tecla = this.obterTecla(simbolo);
			if (tecla != -1)
			{
				boolean precisaShift = this.precisaShift(simbolo);
				if (precisaShift)
					this.robot.keyPress(KeyEvent.VK_SHIFT);
				this.robot.keyPress(tecla);
				this.robot.keyRelease(tecla);
				if (precisaShift)
					this.robot.keyRelease(KeyEvent.VK_SHIFT);				
			}
		}
		
		private int obterTecla(char simbolo)
		{
			simbolo = Character.toUpperCase(simbolo);
			switch (simbolo)
			{
				case '0': return KeyEvent.VK_0;
				case '1': return KeyEvent.VK_1;
				case '2': return KeyEvent.VK_2;
				case '3': return KeyEvent.VK_3;
				case '4': return KeyEvent.VK_4;
				case '5': return KeyEvent.VK_5;
				case '6': return KeyEvent.VK_6;
				case '7': return KeyEvent.VK_7;
				case '8': return KeyEvent.VK_8;
				case '9': return KeyEvent.VK_9;
				case 'A': return KeyEvent.VK_A;
				case 'B': return KeyEvent.VK_B;
				case 'C': return KeyEvent.VK_C;
				case 'D': return KeyEvent.VK_D;
				case 'E': return KeyEvent.VK_E;
				case 'F': return KeyEvent.VK_F;
				case 'G': return KeyEvent.VK_G;
				case 'H': return KeyEvent.VK_H;
				case 'I': return KeyEvent.VK_I;
				case 'J': return KeyEvent.VK_J;
				case 'K': return KeyEvent.VK_K;
				case 'L': return KeyEvent.VK_L;
				case 'M': return KeyEvent.VK_M;
				case 'N': return KeyEvent.VK_N;
				case 'O': return KeyEvent.VK_O;
				case 'P': return KeyEvent.VK_P;
				case 'Q': return KeyEvent.VK_Q;
				case 'R': return KeyEvent.VK_R;
				case 'S': return KeyEvent.VK_S;
				case 'T': return KeyEvent.VK_T;
				case 'U': return KeyEvent.VK_U;
				case 'V': return KeyEvent.VK_V;
				case 'W': return KeyEvent.VK_W;
				case 'X': return KeyEvent.VK_X;
				case 'Y': return KeyEvent.VK_Y;
				case 'Z': return KeyEvent.VK_Z;
				default: return -1;
			}
		}
	}
}