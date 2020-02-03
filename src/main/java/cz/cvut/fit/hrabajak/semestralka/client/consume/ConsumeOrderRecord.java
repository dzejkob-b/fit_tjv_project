package cz.cvut.fit.hrabajak.semestralka.client.consume;

import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import cz.cvut.fit.hrabajak.semestralka.rest.dto.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * zpracovani api objednavek
 */
public class ConsumeOrderRecord {

	private static String restPrefix = "http://localhost:8080/orderrecord";

	public OrderRecordDto GetOrderRecordByCode(String code) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());

		ResponseEntity<OrderRecordDto> response = t.getForEntity(ConsumeOrderRecord.restPrefix + "/get/" + code, OrderRecordDto.class);

		return response.getBody();
	}

	public OrderRecordProductsDto GetOrderRecordProductsByCode(String code) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());

		ResponseEntity<OrderRecordProductsDto> response = t.getForEntity(ConsumeOrderRecord.restPrefix + "/get/" + code + "/products", OrderRecordProductsDto.class);

		return response.getBody();
	}

	public OrderRecordSimpleDto[] GetOrderRecords(String status) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());

		ResponseEntity<OrderRecordSimpleDto[]> response = t.getForEntity(ConsumeOrderRecord.restPrefix + "/get/" + status, OrderRecordSimpleDto[].class);

		return response.getBody();
	}

	public void SetOrderRecordStatus(String code, OrderRecord.Status status) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());

		t.put(ConsumeOrderRecord.restPrefix + "/set/" + code + "/" + status.toString().toLowerCase(), null);
	}

	public OrderRecordProductsDto AddProducts(String code, OrderAddDto ap) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());

		HttpHeaders hd = new HttpHeaders();
		hd.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<OrderAddDto> et = new HttpEntity<OrderAddDto>(ap, hd);

		ResponseEntity<OrderRecordProductsDto> response = t.postForEntity(ConsumeOrderRecord.restPrefix + "/add/" + code + "/products", et, OrderRecordProductsDto.class);

		return response.getBody();
	}

	public OrderRecordProductsDto RemoveProducts(String code, OrderAddDto ap) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());

		HttpHeaders hd = new HttpHeaders();
		hd.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<OrderAddDto> et = new HttpEntity<OrderAddDto>(ap, hd);

		ResponseEntity<OrderRecordProductsDto> response = t.exchange(ConsumeOrderRecord.restPrefix + "/remove/" + code + "/products", HttpMethod.DELETE, et, OrderRecordProductsDto.class);

		return response.getBody();
	}

	public OrderRecordDto UpdateOrCreateOrderRecord(OrderRecordDto o) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());

		HttpHeaders hd = new HttpHeaders();
		hd.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<OrderRecordDto> et = new HttpEntity<OrderRecordDto>(o, hd);

		ResponseEntity<OrderRecordDto> response = t.postForEntity(ConsumeOrderRecord.restPrefix + "/update", et, OrderRecordDto.class);

		return response.getBody();
	}

	public void DeleteOrderRecordByCode(String code) {
		RestTemplate t = new RestTemplate();
		t.setErrorHandler(new ConsumeErrorHandler());

		t.delete(ConsumeOrderRecord.restPrefix + "/delete/" + code);
	}

}
