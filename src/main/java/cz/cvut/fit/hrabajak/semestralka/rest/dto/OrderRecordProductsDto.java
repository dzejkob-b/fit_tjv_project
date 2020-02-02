package cz.cvut.fit.hrabajak.semestralka.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.cvut.fit.hrabajak.semestralka.orm.OrderProduct;
import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import cz.cvut.fit.hrabajak.semestralka.rest.RestOrderRecordController;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * prihradka zaznamu produktu v objednavce
 */
@XmlRootElement(name = "orderRecordProducts")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRecordProductsDto extends OrderRecordDto {

	private ArrayList<OrderProductDto> products;

	public ArrayList<OrderProductDto> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<OrderProductDto> products) {
		this.products = products;
	}

	public static final ResourceAssemblerSupport<OrderRecord, OrderRecordProductsDto> toDto = new ResourceAssemblerSupport<OrderRecord, OrderRecordProductsDto>(RestOrderRecordController.class, OrderRecordProductsDto.class) {

		// tahle vec zajistuje konverzi orm entity na serializovatelny datovy objekt pro rest

		@Override
		public OrderRecordProductsDto toResource(OrderRecord o) {
			OrderRecordProductsDto dt = new OrderRecordProductsDto();

			dt.setEntity_id(o.getId());
			dt.setCode(o.getCode());
			dt.setStatus(o.getStatus());
			dt.setTotalPrice(o.getTotalPrice());

			dt.setCustFirstName(o.getCustFirstName());
			dt.setCustSurName(o.getCustSurName());
			dt.setDeliveryAddress(o.getDeliveryAddress());
			dt.setDeliveryCity(o.getDeliveryCity());

			ArrayList<OrderProductDto> products = new ArrayList<OrderProductDto>();

			for (OrderProduct op : o.getOrderProducts()) {
				products.add(OrderProductDto.toDto.toResource(op));
			}

			dt.setProducts(products);

			// odkaz na detail
			dt.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).get(o.getCode())).withRel("self"));

			return dt;
		}
	};

}
