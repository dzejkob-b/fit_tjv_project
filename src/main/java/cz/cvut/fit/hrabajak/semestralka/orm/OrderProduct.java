package cz.cvut.fit.hrabajak.semestralka.orm;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"orderRecord_id", "product_id"})
})
@NamedQuery(name = "OrderProduct.findAll", query = "SELECT r FROM OrderProduct r")
public class OrderProduct implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	@Column(nullable = false)
	private long quantity;

	// JoinColumn - vlastnik relace - sloupec vzdaleneho klice

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderRecord_id")
	OrderRecord orderRecord;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	Product product;

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public long getId() {
		return id;
	}

	public OrderRecord getOrderRecord() {
		return orderRecord;
	}

	public void setOrderRecord(OrderRecord orderRecord) {
		this.orderRecord = orderRecord;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String toString() {
		return
				"id: " + Long.toString(this.id) +
				", quantity: " + Long.toString(this.quantity) +
				", orderRecord_id: " + (this.orderRecord == null ? "null" : this.orderRecord.getId()) +
				", product_id: " + (this.product == null ? "null" : this.product.getId());
	}

}
