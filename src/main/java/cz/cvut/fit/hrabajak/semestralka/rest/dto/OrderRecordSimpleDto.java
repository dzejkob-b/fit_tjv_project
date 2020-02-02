package cz.cvut.fit.hrabajak.semestralka.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * prihradka zjednoduseneho zaznamu objednavky
 */
@XmlRootElement(name = "orderRecordSimple")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRecordSimpleDto extends ResourceSupport implements Serializable {

	private long entity_id;
	private String code;
	private OrderRecord.Status status;
	private long totalPrice;

	public long getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(long entity_id) {
		this.entity_id = entity_id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public OrderRecord.Status getStatus() {
		return status;
	}

	public void setStatus(OrderRecord.Status status) {
		this.status = status;
	}

	public long getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(long totalPrice) {
		this.totalPrice = totalPrice;
	}

}
