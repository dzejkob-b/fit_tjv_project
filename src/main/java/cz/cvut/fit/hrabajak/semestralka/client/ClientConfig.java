package cz.cvut.fit.hrabajak.semestralka.client;

import cz.cvut.fit.hrabajak.semestralka.client.consume.ConsumeProduct;
import cz.cvut.fit.hrabajak.semestralka.client.gui.EntryForm;
import cz.cvut.fit.hrabajak.semestralka.client.gui.ProductEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ClientConfig {

	@Bean
	ClientApp getClientApp() {
		return new ClientApp();
	}

	@Bean
	ConsumeProduct getConsumeProduct() {
		return new ConsumeProduct();
	}

	@Bean
	EntryForm getEntryForm() {
		return new EntryForm();
	}

	@Bean()
	ProductEditor getProductEditor() {
		return new ProductEditor();
	}

}
