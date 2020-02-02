package cz.cvut.fit.hrabajak.semestralka.data;

import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;

import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import cz.cvut.fit.hrabajak.semestralka.repo.OrderRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Service
public class OrderRecordFactory {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private DataManager dt;
	@Autowired
	private OrderRecordRepository rep;

	private static String prefixChars = "ABCDEFGH";
	private static String numericChars = "0123456789";

	public OrderRecord getOrder(long id) {
		return this.em.find(OrderRecord.class, id);
	}

	public OrderRecord getOrderByCode(String code) {
		try {
			return (OrderRecord) this.em.createQuery("SELECT o FROM OrderRecord o WHERE o.code = :code").setParameter("code", code).getSingleResult();

		} catch (NoResultException ex) {
			return null;
		}
	}

	public void saveOrder(OrderRecord item) {
		this.rep.save(item);
	}

	public void saveAllOrders(List<OrderRecord> items) {
		this.rep.saveAll(items);
	}

	public void deleteAll() {
		this.rep.deleteAll();
	}

	public void deleteOrderRecord(OrderRecord o) {
		this.rep.delete(o);
	}
	
	public List<OrderRecord> getAllByStatus(OrderRecord.Status status) {
		return this.rep.findAllByStatus(status);
		// !! return this.em.createQuery("SELECT o FROM OrderRecord o WHERE o.status = :status").setParameter("status", status).getResultList();
	}
	
	public String generateUniqueCode() {
		String result = "";

		do {
			StringBuilder code = new StringBuilder();

			for (int sf = 0; sf < 2; sf++) {
				code.append(OrderRecordFactory.prefixChars.charAt((int)Math.round(Math.random() * (OrderRecordFactory.prefixChars.length() - 1))));
			}

			for (int sf = 0; sf < 4; sf++) {
				code.append(OrderRecordFactory.numericChars.charAt((int)Math.round(Math.random() * (OrderRecordFactory.numericChars.length() - 1))));
			}

			result = code.toString();

			try {
				this.em.createQuery("SELECT o.id FROM OrderRecord o WHERE o.code = :code").setParameter("code", result).getSingleResult();

			} catch (NoResultException ex) {
				break;
			}

		} while (true);
		
		return result;
	}
}
