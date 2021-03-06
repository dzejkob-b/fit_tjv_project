package cz.cvut.fit.hrabajak.semestralka.client.consume;

import cz.cvut.fit.hrabajak.semestralka.rest.dto.ProductDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * zpracovani api produktu
 */
public class ConsumeProduct {

	private static String restPrefix = "http://localhost:8080/product";

	public ProductDto GetProductById(long entity_id) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());
		
		ResponseEntity<ProductDto> response = t.getForEntity(ConsumeProduct.restPrefix + "/getid/" + Long.toString(entity_id), ProductDto.class);

		return response.getBody();
	}

	public ProductDto[] GetProducts(int page) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());

		ResponseEntity<ProductDto[]> response = t.getForEntity(ConsumeProduct.restPrefix + "/getall/" + Integer.toString(page), ProductDto[].class);

		return response.getBody();
	}

	public ProductDto UpdateOrCreateProduct(ProductDto p) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());

		HttpHeaders hd = new HttpHeaders();
		hd.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ProductDto> et = new HttpEntity<ProductDto>(p, hd);

		ResponseEntity<ProductDto> response = t.postForEntity(ConsumeProduct.restPrefix + "/update", et, ProductDto.class);

		return response.getBody();
	}

	public void DeleteProductById(long entity_id) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());

		t.delete(ConsumeProduct.restPrefix + "/delete/" + Long.toString(entity_id));
	}

}
