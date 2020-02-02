package cz.cvut.fit.hrabajak.semestralka.rest.dto;

import cz.cvut.fit.hrabajak.semestralka.orm.OrderProduct;
import cz.cvut.fit.hrabajak.semestralka.orm.Product;
import cz.cvut.fit.hrabajak.semestralka.rest.RestOrderRecordController;
import cz.cvut.fit.hrabajak.semestralka.rest.RestProductController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * prihradka zaznamu produktu
 */
@XmlRootElement(name = "orderProduct")
public class OrderProductDto extends ResourceSupport implements Serializable {
	
	private long entity_id;
	private long product_entity_id;
	private String name;
	private long price;
	private long quantity;
	private long totalPrice;

	public long getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(long entity_id) {
		this.entity_id = entity_id;
	}

	public long getProduct_entity_id() {
		return product_entity_id;
	}

	public void setProduct_entity_id(long product_entity_id) {
		this.product_entity_id = product_entity_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public long getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(long totalPrice) {
		this.totalPrice = totalPrice;
	}

	public static final ResourceAssemblerSupport<OrderProduct, OrderProductDto> toDto = new ResourceAssemblerSupport<OrderProduct, OrderProductDto>(RestOrderRecordController.class, OrderProductDto.class) {

		@Override
		public OrderProductDto toResource(OrderProduct op) {
			OrderProductDto dt = new OrderProductDto();
			
			dt.setEntity_id(op.getId());
			dt.setProduct_entity_id(op.getProduct().getId());
			dt.setName(op.getProduct().getName());
			dt.setPrice(op.getProduct().getPrice());
			dt.setQuantity(op.getQuantity());
			dt.setTotalPrice(op.getQuantity() * op.getProduct().getPrice());

			// odkaz na detail produktu
			dt.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestProductController.class).getId(op.getProduct().getId())).withRel("product"));

			return dt;
		}
	};

}
