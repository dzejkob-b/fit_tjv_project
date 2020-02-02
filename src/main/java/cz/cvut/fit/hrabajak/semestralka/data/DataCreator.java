package cz.cvut.fit.hrabajak.semestralka.data;

import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import cz.cvut.fit.hrabajak.semestralka.orm.Product;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * vytvarec testovacich dat
 */
public class DataCreator {

	@Autowired
	private ProductFactory productFactory;
	@Autowired
	private OrderProductFactory orderProductFactory;
	@Autowired
	private OrderRecordFactory orderRecordFactory;

	private ArrayList<Product> pList = null;

	public void createSomeEntites() {

		this.createProducts();
		this.createOrderRecords();

	}

	public void createProducts() {

		this.productFactory.createProductQuick("Samsung 970 EVO 1TB", 3990);
		this.productFactory.createProductQuick("Xiaomi Mi Band 3", 579);
		this.productFactory.createProductQuick("BOSU NexGen Pro Balance Trainer", 4290);
		this.productFactory.createProductQuick("SCHOLL Velvet Smooth Diamond růžový", 359);
		this.productFactory.createProductQuick("Zaklínač Skleněný dům", 225);

		this.productFactory.createProductQuick("Máme to rádi zdravě: Hodně dobré recepty pro celou rodinu", 249);
		this.productFactory.createProductQuick("Concept RK4030", 399);
		this.productFactory.createProductQuick("ASUS ZenBook 14 UX434FL-A6013T", 29990);
		this.productFactory.createProductQuick("SNAIGE SPS 1501", 3499);
		this.productFactory.createProductQuick("EMOS LED P4525", 499);

		this.productFactory.createProductQuick("Destrukční deník Tentokrát barevně", 169);
		this.productFactory.createProductQuick("Sunar Complex 2 - 6× 600 g", 999);
		this.productFactory.createProductQuick("Acer Predator Gaming Roll Top Backpack", 2129);
		this.productFactory.createProductQuick("OMEN by HP Gaming Backpack 17.3", 899);
		this.productFactory.createProductQuick("Marshall MID ANC Bluetooth", 4490);

		this.productFactory.createProductQuick("Panasonic RP-HF100E-A modrá", 333);
		this.productFactory.createProductQuick("Disney 5minutové Mickeyho pohádky", 249);
		this.productFactory.createProductQuick("Hot Wheels Zero Gravity 660 cm s adaptérem", 1089);
		this.productFactory.createProductQuick("Run sport černý", 6999);
		this.productFactory.createProductQuick("GILLETTE Fusion ProGlide Styler + hlavice 1 ks", 419);

		this.pList = new ArrayList<Product>();
		this.pList.addAll(this.productFactory.getAllProducts());
	}

	public void createOrderRecords() {

		OrderRecord o = null;

		o = new OrderRecord();
		o.setCode(this.orderRecordFactory.generateUniqueCode());
		o.setCustFirstName("Petr");
		o.setCustSurName("Sokol");
		o.setDeliveryAddress("Kádovská 42");
		o.setDeliveryCity("Ondřejov");
		this.addRandomProductsToOrder(o);

		this.orderRecordFactory.saveOrder(o);

		o = new OrderRecord();
		o.setCode(this.orderRecordFactory.generateUniqueCode());
		o.setCustFirstName("Jitka");
		o.setCustSurName("Menclová");
		o.setDeliveryAddress("Ondřejovská 17");
		o.setDeliveryCity("Povýšilovo pole");
		this.addRandomProductsToOrder(o);

		this.orderRecordFactory.saveOrder(o);

		o = new OrderRecord();
		o.setCode(this.orderRecordFactory.generateUniqueCode());
		o.setCustFirstName("Zdeněk");
		o.setCustSurName("Novák");
		o.setDeliveryAddress("U Habru 5");
		o.setDeliveryCity("Struhařov");
		this.addRandomProductsToOrder(o);

		this.orderRecordFactory.saveOrder(o);

		o = new OrderRecord();
		o.setCode(this.orderRecordFactory.generateUniqueCode());
		o.setCustFirstName("Věra");
		o.setCustSurName("Krkavcová");
		o.setDeliveryAddress("Voděradská 402");
		o.setDeliveryCity("Karkoška");
		this.addRandomProductsToOrder(o);

		this.orderRecordFactory.saveOrder(o);

		o = new OrderRecord();
		o.setCode(this.orderRecordFactory.generateUniqueCode());
		o.setCustFirstName("Martin");
		o.setCustSurName("Trejbal");
		o.setDeliveryAddress("Borecká 96");
		o.setDeliveryCity("Bubeník");
		this.addRandomProductsToOrder(o);

		this.orderRecordFactory.saveOrder(o);
	}

	private void addRandomProductsToOrder(OrderRecord o) {
		ArrayList<Product> ls = new ArrayList<Product>(this.pList);

		int count = 1 + (int)Math.round(Math.random() * 8);

		for (int sf = 0; sf < count; sf++) {
			Product p = ls.get((int)Math.round(Math.random() * (ls.size() - 1)));

			o.addProduct(p, 1 + (int)Math.round(Math.random() * 20));

			ls.remove(p);
		}
	}

}
