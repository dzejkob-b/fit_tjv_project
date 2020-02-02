package cz.cvut.fit.hrabajak.semestralka.orm;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	@Column(nullable = false, unique = true)
	private String name;
	@Column
	private long price;

	// FetchType.LAZY - kolekci objednavek ve kterych je produkt obsazen je treba fetchnout dodatecne

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
	private List<OrderProduct> orderProducts;

	public Product() {
		this.orderProducts = new ArrayList<OrderProduct>();
	}

	public long getId() {
		return id;
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
	
	public List<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public String toString() {
		String result = "";
		
		result += "id: " + Long.toString(this.id) + ", name: " + this.name + ", price: " + Long.toString(this.price);

		if (Hibernate.isInitialized(this.orderProducts)) {
			for (OrderProduct op : this.orderProducts) {
				result += "\n#~P~# " + op.toString();
			}
		}

		return result;
	}

}