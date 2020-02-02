package cz.cvut.fit.hrabajak.semestralka.client.consume;

import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import cz.cvut.fit.hrabajak.semestralka.rest.dto.OrderRecordDto;
import cz.cvut.fit.hrabajak.semestralka.rest.dto.OrderRecordSimpleDto;
import cz.cvut.fit.hrabajak.semestralka.rest.dto.ProductDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ConsumeOrderRecord {

	private static String restPrefix = "http://localhost:8080/orderrecord";

	public OrderRecordDto GetOrderRecordByCode(String code) {
		RestTemplate t = new RestTemplate();

		ResponseEntity<OrderRecordDto> response = t.getForEntity(ConsumeOrderRecord.restPrefix + "/get/" + code, OrderRecordDto.class);

		return response.getBody();
	}

	public OrderRecordSimpleDto[] GetOrderRecords(String status) {
		RestTemplate t = new RestTemplate();

		ResponseEntity<OrderRecordSimpleDto[]> response = t.getForEntity(ConsumeOrderRecord.restPrefix + "/get/" + status, OrderRecordSimpleDto[].class);

		return response.getBody();
	}

	public void SetOrderRecordStatus(String code, OrderRecord.Status status) {
		RestTemplate t = new RestTemplate();

		t.put(ConsumeOrderRecord.restPrefix + "/set/" + code + "/" + status.toString().toLowerCase(), null);
	}

	public void DeleteOrderRecordByCode(String code) {
		RestTemplate t = new RestTemplate();

		t.delete(ConsumeOrderRecord.restPrefix + "/delete/" + code);
	}

}
