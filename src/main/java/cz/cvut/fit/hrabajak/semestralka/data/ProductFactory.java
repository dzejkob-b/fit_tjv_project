package cz.cvut.fit.hrabajak.semestralka.data;

import cz.cvut.fit.hrabajak.semestralka.orm.Product;
import cz.cvut.fit.hrabajak.semestralka.repo.ProductRepository;
import javassist.NotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Service
public class ProductFactory {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private DataManager dt;
	@Autowired
	private ProductRepository rep;

	public Product getProduct(long id) {
		return this.em.find(Product.class, id);
	}

	public Product getProduct(String name) {
		try {
			return (Product)this.em.createQuery("SELECT p FROM Product p WHERE p.name = :name").setParameter("name", name).getSingleResult();
			
		} catch (NoResultException ex) {
			return null;
		}
	}

	public Product createProductQuick(String name, int price) {
		Product p = new Product();

		p.setName(name);
		p.setPrice(price);

		this.saveProduct(p);
		
		return p;
	}

	public void saveProduct(Product item) {
		this.rep.save(item);
	}

	public void saveProducts(List<Product> items) {
		this.rep.saveAll(items);
	}

	public void deleteProduct(Product item) {
		this.rep.delete(item);
	}

	@Transactional(readOnly = true)
	public Product lazyLoad(Product item) {
		// refetch objektu, nacteni referenci v ramci transakce a vraceni aktualizovane instance
		// (nenasel jsem jak to udelat jinak)

		Product p = this.rep.getOne(item.getId());

		Hibernate.initialize(p.getOrderProducts());

		return p;
	}

	public List<Product> getAllProducts() {
		return this.rep.findAll();
	}

	public List<Product> getAllProducts(int from, int length) {
		Query q = this.em.createQuery("SELECT p FROM Product p ORDER BY p.id ASC");

		q.setFirstResult(from);
		q.setMaxResults(length);

		return q.getResultList();
	}

	public void deleteAll() {
		this.rep.deleteAll();
	}
	
}
