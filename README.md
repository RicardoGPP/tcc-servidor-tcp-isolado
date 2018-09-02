# Servidor TCP Isolado
Este módulo é parte do desenvolvimento do trabalho de conclusão de curso do projeto de coleta e integração de dados no contexto da Indústria 4.0. Refere-se ao núcleo do servidor a ficar em execução em um computador monitorando uma determinada porta a fim de processar requisições de clientes.

Este servidor foi projetado de modo a prover um framework TCP facilmente adaptável ao contexto da necessidade do programador. Além de facilmente defini-lo a observar uma determinada porta na espera de conexões, a estrutura permite a criação de tratamentos específicos aos dados recebidos, bem como uma facilidade em determinar callbacks aos eventos do servidor, como sua inicialização e paragem, as conexões de clientes, o gerenciamento das requisições, entre outros.

O programador que utilizar este módulo deve apenas criar uma classe que herde de <b>TratadorRequisicao</b> e implementar o método <b>gerenciar</b>. Após definir como a requisição será tratada, o programador deverá instanciar o servidor passando a classe que acabou de criar como argumento no construtor. Veja um exemplo:

O tratador de requisições definido pelo programador:
<pre>
public class MeuTratadorRequisicao extends TratadorRequisicao&lt;String&gt;
{
  public void gerenciar(Requisicao&lt;String&gt; requisicao)
  {
    System.out.println("O cliente me enviou: " + requisicao.ler() + ".");
  }
}
</pre>

A classe que instancia o servidor:
<pre>
...
Servidor&lt;String&gt; servidor = new Servidor(MeuTratadorRequisicao.class);
...
</pre>

Observação: o tipo paramétrico a ser definido nas classes faz referência ao tipo de objeto a ser transportado entre cliente e servidor.

Agora, caso o programador sinta a necessidade de executar ações sobre as etapas do ciclo do vida do servidor, ele deverá utilizar o <b>tratador de eventos</b>. Este tratador deverá ser uma classe definida pelo programador e deverá herdar de <b>TratadorEvento</b>. Nela, o programador deverá sobrescrever os métodos cujo os quais ele deseja que sejam executados antes e/ou depois de cada evento do servidor. Os métodos que podem ser sobrescritos são:

<ul>
  <li>antesDeIniciar;</li>
  <li>depoisDeIniciar;</li>
  <li>antesDeAceitarRequisicao;</li>
  <li>depoisDeAceitarRequisicao;</li>
  <li>antesDeGerenciarRequisicao;</li>
  <li>depoisDeGerenciarRequisicao;</li>
  <li>antesDeParar;</li>
  <li>depoisDeParar.</li>
</ul>

Depois de sobrescrito(s) o(s) método(s), o programador deve informar ao servidor qual é o tratador de eventos a ser utilizado, passando um objeto do mesmo como argumento do construtor, que neste caso é sobrecarregado para recebê-lo. Veja um exemplo:

O tratador de eventos definido pelo programador:
<pre>
public class MeuTratadorEvento extends TratadorEvento
{
  @override
  public void depoisDeIniciar(ServerSocket serverSocket)
  {
    System.out.println("Servidor iniciado na porta " + serverSocket.getLocalPort() + ".");
  }
}
</pre>

A classe que instancia o servidor:
<pre>
...
Servidor&lt;String&gt; servidor = new Servidor(MeuTratadorRequisicao.class, new MeuTratadorEvento&lt;String&gt;());
...
</pre>

É importante ressaltar que esta é a estrutura inicial do núcleo do servidor TCP. Ela foi desenvolvida mirando as necessidades de adaptação, tempo e facilidade de uso em um contexto geral do ambiente de trabalho, mas seu intuito, a priori, é suprir as demandas de um trabalho puramente acadêmico.

Alguns adendos ao conteúdo:

<ul>
  <li>
    As requisições são objetos da classe <b>Requisicao</b> e encapsulam o socket de comunicação entre cliente e servidor e os streams       de I/O gerados a partir dele. Esta classe provê métodos para leitura e escrita de dados dentro do canal de comunicação sem expor         diretamente as dependências necessárias para estas operações.
  </li>
  <li>
    O <b>tratador de requisições</b> pode ser entendido como o handler para as requisições recebidas, permitindo que dados sejam lidos e     enviados pelo canal de comunicação com o cliente.  
  </li>
  <li>
    O <b>tratador de eventos</b> pode ser entendido como o handler dos eventos do ciclo de vida do servidor. Seus métodos são uma           espécie de "callback", porém sem a existência direta da passagem dos métodos para o servidor. Quando o servidor é instanciado sem       um tratador de eventos definido, uma instancia do tratador padrão é criada e o servidor trabalha sem a execução de ações para seus       eventos.
  </li>
  <li>
    O <b>tratador de requisições</b> é passado ao servidor mencionando sua classe porque é preciso garantir a integridade da estrutura       de dependência. Assim que o servidor é instanciado, é gerada uma ligação entre ele e o tratador de modo a garantir que toda a           requisição recebida seja compartilhada entre ambos os objetos sem interferência externa.
  </li>
</ul>

Qualquer dúvida, mande um e-mail para ricardo.piantavinha@gmail.com.
