package cz.cvut.fit.hrabajak.semestralka;

import cz.cvut.fit.hrabajak.semestralka.data.DataManager;
import cz.cvut.fit.hrabajak.semestralka.data.OrderRecordFactory;
import cz.cvut.fit.hrabajak.semestralka.data.ProductFactory;
import cz.cvut.fit.hrabajak.semestralka.display.TableDisplay;
import cz.cvut.fit.hrabajak.semestralka.orm.OrderProduct;
import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // <= toto zajisti "nejak", ze pri kazdem testu se zresetuje id
public class OrderRecordTest {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private ProductFactory productFactory;
	@Autowired
	private OrderRecordFactory orderRecordFactory;
	@Autowired
	private DataManager dm;

	/*
	private ApplicationContext ctx;
	private EntityManager em;
	private OrderRecordFactory orderRecordFactory;
	private ProductFactory productFactory;

	@Before
	public void init() {
		this.ctx = new AnnotationConfigApplicationContext(AppTestConfig.class);
		//this.em = this.ctx.getBean(EntityManager.class);
		this.orderRecordFactory = this.ctx.getBean(OrderRecordFactory.class);
		this.productFactory = this.ctx.getBean(ProductFactory.class);
	}

	@After
	public void destroy() {
		(this.ctx.getBean(EntityManagerFactory.class)).close();
	}
 	*/

	@Test
	public void createOneTest() {
		this.orderRecordFactory.deleteAll();

		OrderRecord o = new OrderRecord();

		o.setCode(this.orderRecordFactory.generateUniqueCode());
		o.setCustFirstName("Pepa");
		o.setCustSurName("Zdepa");
		o.setDeliveryAddress("Zizkov");
		o.setDeliveryCity("Praha");

		this.orderRecordFactory.saveOrder(o);

		// test vytvoreni
		assertEquals(this.orderRecordFactory.getOrder(1).toString(), o.toString());
	}

	@Test
	public void addProductTest() {
		this.orderRecordFactory.deleteAll();
		this.productFactory.deleteAll();

		OrderRecord o = new OrderRecord();

		o.setCode(this.orderRecordFactory.generateUniqueCode());
		o.setCustFirstName("Pepa");
		o.setCustSurName("Zdepa");
		o.setDeliveryAddress("Zizkov");
		o.setDeliveryCity("Praha");
		o.addProduct(this.productFactory.createProductQuick("Pivo", 50), 2);
		o.addProduct(this.productFactory.createProductQuick("CocaCola", 69), 10);

		this.orderRecordFactory.saveOrder(o);
		
		OrderRecord fetch_o = this.orderRecordFactory.getOrder(1);

		// porovnani dle fetche
		assertEquals(fetch_o.toString(), o.toString());

		List<OrderProduct> fetch_ps = fetch_o.getOrderProducts();
		List<OrderProduct> ps = o.getOrderProducts();

		// stejny pocet
		assertEquals(fetch_ps.size(), ps.size());

		// nasledujici dva prvky musi byt shodne
		assertEquals(fetch_ps.iterator().next().toString(), ps.iterator().next().toString());
		assertEquals(fetch_ps.iterator().next().toString(), ps.iterator().next().toString());

		//(new TableDisplay("OrderRecord", this.dm)).displayToConsole();
		//(new TableDisplay("Product", this.dm)).displayToConsole();
		//(new TableDisplay("OrderProduct", this.dm)).displayToConsole();

	}

}
