package cz.cvut.fit.hrabajak.semestralka.data;

import cz.cvut.fit.hrabajak.semestralka.orm.OrderProduct;
import cz.cvut.fit.hrabajak.semestralka.repo.OrderProductRepository;
import cz.cvut.fit.hrabajak.semestralka.repo.ProductRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class OrderProductFactory {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private DataManager dt;
	@Autowired
	private OrderProductRepository rep;

	public OrderProduct getOrderProduct(long id) {
		return this.em.find(OrderProduct.class, id);
	}

	public void saveOrderProduct(OrderProduct item) {
		this.rep.save(item);
	}

	public void deleteAll() {
		this.rep.deleteAll();
	}

	public OrderProduct lazyLoad(OrderProduct item) {
		// refetch objektu, nacteni referenci v ramci transakce a vraceni aktualizovane instance
		// (nenasel jsem jak to udelat jinak)

		OrderProduct op = this.rep.getOne(item.getId());

		Hibernate.initialize(op.getOrderRecord());
		Hibernate.initialize(op.getProduct());

		return op;
	}
}
