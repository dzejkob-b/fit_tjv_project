package cz.cvut.fit.hrabajak.semestralka.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * prihradka pro manipulaci s produkty v objednavce (add / remove)
 */
@XmlRootElement(name = "orderAdd")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderAddDto extends ResourceSupport implements Serializable {

	private List<OrderAddProduct> addProducts;

	public List<OrderAddProduct> getAddProducts() {
		return addProducts;
	}

	public void setAddProducts(List<OrderAddProduct> addProducts) {
		this.addProducts = addProducts;
	}
	
}
