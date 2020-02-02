package cz.cvut.fit.hrabajak.semestralka.rest.dto;

import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * prihradka pro manipulaci s produkty v objednavce (add / remove)
 */
@XmlRootElement(name = "orderAdd")
public class OrderAddDto extends ResourceSupport implements Serializable {

	private List<OrderAddProduct> addProducts;

	public List<OrderAddProduct> getAddProducts() {
		return addProducts;
	}

	public void setAddProducts(List<OrderAddProduct> addProducts) {
		this.addProducts = addProducts;
	}
	
}
