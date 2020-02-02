package cz.cvut.fit.hrabajak.semestralka;

import cz.cvut.fit.hrabajak.semestralka.data.ProductFactory;
import cz.cvut.fit.hrabajak.semestralka.orm.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // <= toto zajisti "nejak", ze pri kazdem testu se zresetuje id
public class ProductTest {
	
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private ProductFactory productFactory;

	/*
	private ApplicationContext ctx;
	private ProductFactory productFactory;

	@Before
	public void init() {
		this.ctx = new AnnotationConfigApplicationContext(AppTestConfig.class);
		this.productFactory = this.ctx.getBean(ProductFactory.class);
	}

	@After
	public void destroy() {
		(this.ctx.getBean(EntityManagerFactory.class)).close();
	}
	*/

	@Test
	public void createOneTest() {
		this.productFactory.deleteAll();

		Product p = new Product();

		p.setPrice(599);
		p.setName("Klavesnice");

		this.productFactory.saveProduct(p);

		// test vytvoreni a query dle id
		assertEquals(this.productFactory.getProduct(1).toString(), p.toString());

		// test query dle nazvu
		assertNotNull(this.productFactory.getProduct("Klavesnice"));

		p = this.productFactory.getProduct(1);
		p.setName("Zmena");

		this.productFactory.saveProduct(p);

		// test zmeny nazvu
		assertNotNull(this.productFactory.getProduct("Zmena"));
	}

	@Test
	public void collisionTest() {
		this.productFactory.deleteAll();

		Product p = new Product();

		p.setPrice(350);
		p.setName("Kladivo");

		this.productFactory.saveProduct(p);

		Product p2 = new Product();

		p2.setPrice(50);
		p2.setName("Kladivo");

		boolean failed = false;

		try {
			this.productFactory.saveProduct(p2);

		} catch (Exception ex) {
			failed = true;
		}

		// vytvoreni duplicitniho nazvu musi selhat (u anotace jako expected moc nejde najit spravnou exception)
		assertTrue(failed);
	}

	@Test
	public void createMultipleTest() {
		this.productFactory.deleteAll();

		ArrayList<Product> items = new ArrayList<Product>();

		int genSum = 0, checkSum = 0;

		for (int sf = 0; sf < 10; sf++) {
			Product p = new Product();

			p.setPrice((int)Math.round(Math.random() * 1000));
			p.setName("Neco" + Integer.toString(sf));

			items.add(p);

			genSum += p.getPrice();
		}

		this.productFactory.saveProducts(items);

		for (Product p : this.productFactory.getAllProducts()) {
			checkSum += p.getPrice();
		}

		// test vicenasobneho ulozeni a selectu
		assertEquals(genSum, checkSum);
	}

}
