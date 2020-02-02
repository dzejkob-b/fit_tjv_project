package cz.cvut.fit.hrabajak.semestralka;

import cz.cvut.fit.hrabajak.semestralka.data.DataManager;
import cz.cvut.fit.hrabajak.semestralka.data.OrderProductFactory;
import cz.cvut.fit.hrabajak.semestralka.data.OrderRecordFactory;
import cz.cvut.fit.hrabajak.semestralka.data.ProductFactory;
import cz.cvut.fit.hrabajak.semestralka.display.TableDisplay;
import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import cz.cvut.fit.hrabajak.semestralka.orm.Product;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // <= toto zajisti "nejak", ze pri kazdem testu se zresetuje id
public class OrderProductTest {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private ProductFactory productFactory;
	@Autowired
	private OrderRecordFactory orderRecordFactory;
	@Autowired
	private OrderProductFactory orderProductFactory;
	@Autowired
	private DataManager dm;

	/*
	private ApplicationContext ctx;
	private OrderRecordFactory orderRecordFactory;
	private ProductFactory productFactory;

	@Before
	public void init() {
		this.ctx = new AnnotationConfigApplicationContext(AppTestConfig.class);
		this.orderRecordFactory = this.ctx.getBean(OrderRecordFactory.class);
		this.productFactory = this.ctx.getBean(ProductFactory.class);
	}

	@After
	public void destroy() {
		(this.ctx.getBean(EntityManagerFactory.class)).close();
	}
	*/

	@Test
	public void createOrderProductTest() {
		this.orderRecordFactory.deleteAll();
		this.productFactory.deleteAll();

		OrderRecord o1 = new OrderRecord();

		o1.setCode(this.orderRecordFactory.generateUniqueCode());
		o1.setCustFirstName("name");
		o1.setCustSurName("surname");
		o1.setDeliveryAddress("address");
		o1.setDeliveryCity("city");

		OrderRecord o2 = new OrderRecord();

		o2.setCode(this.orderRecordFactory.generateUniqueCode());
		o2.setCustFirstName("name");
		o2.setCustSurName("surname");
		o2.setDeliveryAddress("address");
		o2.setDeliveryCity("city");

		Product p1 = this.productFactory.createProductQuick("P1", 10);
		Product p2 = this.productFactory.createProductQuick("P2", 20);

		o1.addProduct(p1, 2);
		o1.addProduct(p2, 4);

		o2.addProduct(p1, 10);
		o2.addProduct(p2, 16);

		this.orderRecordFactory.saveOrder(o1);
		this.orderRecordFactory.saveOrder(o2);

		String str_p1 = p1.toString();
		String str_p2 = p2.toString();

		//p1 = this.productFactory.getProduct("P1");
		//p2 = this.productFactory.getProduct("P2");

		p1 = this.productFactory.getProduct(1);
		p2 = this.productFactory.getProduct(2);

		// pred lazyloadingem
		assertEquals(str_p1, p1.toString());
		assertEquals(str_p2, p2.toString());

		p1 = this.productFactory.lazyLoad(p1);
		p2 = this.productFactory.lazyLoad(p2);

		// po lazyloadingu nemuze byt stejne
		assertNotEquals(str_p1, p1.toString());
		assertNotEquals(str_p2, p2.toString());

		//(new TableDisplay("OrderRecord", this.ctx.getBean(DataManager.class))).displayToConsole();
		//(new TableDisplay("OrderProduct", this.ctx.getBean(DataManager.class))).displayToConsole();
		//(new TableDisplay("Product", this.ctx.getBean(DataManager.class))).displayToConsole();

	}

}
