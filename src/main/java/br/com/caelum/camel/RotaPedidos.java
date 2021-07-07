package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();

		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file:pedidos?delay=5s&noop=true") //Diretoio entrada dos arquivos.

						.split()
							.xpath("/pedido/itens/item") // separando os itens dentro do xml
							.log("${id}") //Verificar se está pegando os itens separados por split
							.log("${body}") //Verificar o conteudo do split

						// Agora o filter deverá ser de acordo com o body
						.filter() //fitrando o item
							.xpath("/item/formato[text()='EBOOK']")// filtrando no xml os pedidos que contem itens = 'EBOOK'.

						.log("${id}")
						.marshal().xmljson() //converter para xml para json.
						.log(" -- FORMAT JSON ${body}")
						.setHeader("CamelFileName", simple("${file:name.noext}.json")) //Saida do arquivo em json, ( noext ) - tira a extenção na entrada do arquivo.
						.to("file:saida"); // Diretorio saida dos arquivos.
			}
		});

		context.start();
		Thread.sleep(20000);
		context.stop();
	}	
}
