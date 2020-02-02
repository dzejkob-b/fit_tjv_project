package cz.cvut.fit.hrabajak.semestralka.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.cvut.fit.hrabajak.semestralka.orm.OrderProduct;
import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import cz.cvut.fit.hrabajak.semestralka.rest.RestOrderRecordController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * prihradka zaznamu objednavky
 */
@XmlRootElement(name = "orderRecord")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRecordDto extends ResourceSupport implements Serializable {

	private long entity_id;
	private String code;
	private OrderRecord.Status status;
	private String custFirstName;
	private String custSurName;
	private String deliveryAddress;
	private String deliveryCity;
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
	
	public String getCustFirstName() {
		return custFirstName;
	}

	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}

	public String getCustSurName() {
		return custSurName;
	}

	public void setCustSurName(String custSurName) {
		this.custSurName = custSurName;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryCity() {
		return deliveryCity;
	}

	public void setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}

	public long getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(long totalPrice) {
		this.totalPrice = totalPrice;
	}

	public static final ResourceAssemblerSupport<OrderRecord, OrderRecordDto> toDto = new ResourceAssemblerSupport<OrderRecord, OrderRecordDto>(RestOrderRecordController.class, OrderRecordDto.class) {

		// tahle vec zajistuje konverzi orm entity na serializovatelny datovy objekt pro rest

		@Override
		public OrderRecordDto toResource(OrderRecord o) {
			OrderRecordDto dt = new OrderRecordDto();

			dt.setEntity_id(o.getId());
			dt.setCode(o.getCode());
			dt.setStatus(o.getStatus());
			dt.setTotalPrice(o.getTotalPrice());

			dt.setCustFirstName(o.getCustFirstName());
			dt.setCustSurName(o.getCustSurName());
			dt.setDeliveryAddress(o.getDeliveryAddress());
			dt.setDeliveryCity(o.getDeliveryCity());

			// odkaz na detail
			dt.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).get(o.getCode())).withRel("self"));
			dt.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).getProducts(o.getCode())).withRel("selfWithProducts"));

			return dt;
		}
	};

	public static final ResourceAssemblerSupport<OrderRecord, OrderRecordSimpleDto> toSimpleDto = new ResourceAssemblerSupport<OrderRecord, OrderRecordSimpleDto>(RestOrderRecordController.class, OrderRecordSimpleDto.class) {

		@Override
		public OrderRecordSimpleDto toResource(OrderRecord o) {
			OrderRecordSimpleDto dt = new OrderRecordSimpleDto();

			dt.setEntity_id(o.getId());
			dt.setCode(o.getCode());
			dt.setStatus(o.getStatus());
			dt.setTotalPrice(o.getTotalPrice());

			dt.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).get(o.getCode())).withRel("self"));
			dt.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(RestOrderRecordController.class).getProducts(o.getCode())).withRel("selfWithProducts"));
			
			return dt;
		}
	};

}
