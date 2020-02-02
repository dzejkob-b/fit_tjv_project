package cz.cvut.fit.hrabajak.semestralka.rest.dto;

import cz.cvut.fit.hrabajak.semestralka.orm.Product;
import cz.cvut.fit.hrabajak.semestralka.rest.RestProductController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "product")
public class ProductDto extends ResourceSupport implements Serializable {
	
	private long entity_id = 0;
	private String name;
	private int price;
	
	public long getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(long entity_id) {
		this.entity_id = entity_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}

	public static final ResourceAssemblerSupport<Product, ProductDto> toDto = new ResourceAssemblerSupport<Product, ProductDto>(RestProductController.class, ProductDto.class) {

		// tahle vec zajistuje konverzi orm entity na serializovatelny datovy objekt pro rest

		@Override
		public ProductDto toResource(Product p) {
			ProductDto dt = new ProductDto();

			dt.setEntity_id(p.getId());
			dt.setName(p.getName());
			dt.setPrice(p.getPrice());

			// odkaz na detail
			dt.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestProductController.class).getId(p.getId())).withRel("self"));

			return dt;
		}
	};

}
