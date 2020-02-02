package cz.cvut.fit.hrabajak.semestralka.rest.dto;

import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * prihradka s detailem upravy produktu v objednavce (viz. OrderAddDto)
 */
@XmlRootElement(name = "orderAddProduct")
public class OrderAddProduct extends ResourceSupport implements Serializable {

	private long product_id;
	private long quantity;

	public long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	
}
