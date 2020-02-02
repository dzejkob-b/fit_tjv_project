package cz.cvut.fit.hrabajak.semestralka.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class DataManager {
	// tahle classa byl pokus, jak resit obecne praci s databazi - nakonec to "nejak" resi ty repositories
	// jinym zpusobem se mi to zprovoznit nepodarilo

	//@Autowired
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void persistAll(List<?> items) {
		for (Object it : items) {
			this.em.persist(it);
		}

		/*
		EntityTransaction tran = null;

		try{
			tran = this.em.getTransaction();
			tran.begin();

			for (Object it : items) {
				this.em.persist(it);
			}

			tran.commit();

		} catch (Exception ex){
			tran.rollback();
			throw ex;
		}
		*/
	}

	@Transactional
	public void persistOne(Object item) {
		this.em.persist(item);

		/*
		EntityTransaction tran = null;

		try{
			tran = this.em.getTransaction();
			tran.begin();

			this.em.persist(item);

			tran.commit();

		} catch (Exception ex){
			tran.rollback();
			throw ex;
		}
		*/
	}

	@Transactional
	public void deleteAllFromTable(String tableName) {
		Query q = this.em.createQuery("DELETE FROM " + tableName);

		q.executeUpdate();

		/*
		EntityTransaction tran = null;

		try{
			tran = this.em.getTransaction();
			tran.begin();
			
			Query q = this.em.createQuery("DELETE FROM " + tableName);

			q.executeUpdate();

			tran.commit();

		} catch (Exception ex){
			tran.rollback();
			// NOT throw
		}
		*/
	}

	@Transactional(readOnly = true)
	public List<?> getAllRecordsFromTable(String tableName) {
		Query q = this.em.createQuery("SELECT t FROM " + tableName + " t");

		return q.getResultList();
	}

}
