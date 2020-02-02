package cz.cvut.fit.hrabajak.semestralka.orm;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NamedQueries({
	@NamedQuery(name = "OrderRecord.findAll", query = "SELECT r FROM OrderRecord r"),
	@NamedQuery(name = "OrderRecord.findAllByStatus", query = "SELECT r FROM OrderRecord r WHERE r.status = ?1"),
	@NamedQuery(name = "OrderRecord.findByCode", query = "SELECT r FROM OrderRecord r WHERE r.code =?1")
})
public class OrderRecord implements Serializable {

	public enum Status {
		OPENED,
		PROCESSING,
		FINISHED,
		REVERSAL
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	@Column(nullable = false, unique = true)
	private String code;
	@Column(nullable = false)
	private OrderRecord.Status status = Status.OPENED;
	@Column
	private String custFirstName;
	@Column
	private String custSurName;
	@Column
	private String deliveryAddress;
	@Column
	private String deliveryCity;
	
	// FetchType.EAGER - v pripade fetche zaznamu je potreba taktez kolekce produktu v objednavce
	// CascadeType.PERSIST - persist taktez persistne relace v kolekcich
	// orphanRemoval - zaznam odstraneny z kolekce bude pri persist taktez smazan z databaze

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "orderRecord", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderProduct> orderProducts;

	public OrderRecord() {
		this.orderProducts = new ArrayList<OrderProduct>();
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean mayChangeStatus(Status status) {
		switch (this.status) {
			case OPENED:
				return status == Status.PROCESSING || status == Status.REVERSAL;

			case PROCESSING :
				return status == Status.FINISHED || status == Status.OPENED || status == Status.REVERSAL;

			case FINISHED :
				return status == Status.PROCESSING;

			case REVERSAL :
				return status == Status.OPENED;
		}

		return false;
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

	public List<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public long getTotalPrice() {
		long sum = 0;
		
		for (OrderProduct p : this.orderProducts) {
			sum += p.getProduct().getPrice() * p.getQuantity();
		}

		return sum;
	}

	public void addProduct(Product p, long quantity) {
		OrderProduct toAdd = null;

		for (OrderProduct op : this.orderProducts) {
			if (op.getProduct().getId() == p.getId()) {
				toAdd = op;
				break;
			}
		}

		if (toAdd == null) {
			toAdd = new OrderProduct();

			toAdd.setOrderRecord(this);
			toAdd.setProduct(p);
			toAdd.setQuantity(quantity);

			this.orderProducts.add(toAdd);
			
		} else {
			toAdd.setQuantity(toAdd.getQuantity() + quantity);
		}
	}

	public boolean removeProduct(Product p, long quantity) {
		boolean result = false;
		OrderProduct toRemove = null;

		for (OrderProduct op : this.orderProducts) {
			if (op.getProduct().getId() == p.getId()) {

				if (op.getQuantity() > quantity) {
					op.setQuantity(op.getQuantity() - quantity);
				} else {
					toRemove = op;
				}

				result = true;
				break;
			}
		}

		if (toRemove != null) {
			this.orderProducts.remove(toRemove);
		}

		return result;
	}

	public String toString() {
		String result = "";

		result += "id: " + Long.toString(this.id) +
				", code: " + this.code +
				", status: " + this.status.toString() +
				", custFirstName: " + this.custFirstName +
				", custSurName: " + this.custSurName +
				", deliveryAddress: " + this.deliveryAddress +
				", deliveryCity: " + this.deliveryCity;

		for (OrderProduct op : this.orderProducts) {
			result += "\n#~P~# " + op.toString();
		}

		return result;
	}

}
