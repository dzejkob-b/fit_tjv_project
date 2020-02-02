package cz.cvut.fit.hrabajak.semestralka;

import cz.cvut.fit.hrabajak.semestralka.data.DataCreator;
import cz.cvut.fit.hrabajak.semestralka.data.DataManager;
import cz.cvut.fit.hrabajak.semestralka.display.TableDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * logika serverove aplikace
 * (rest server spousti automaticky springboot)
 */
@Service
public class AppLogic {

	@Autowired
	private DataManager mg;
	@Autowired
	private DataCreator cre;

	public void run(String[] args) {

		System.out.println("== AppMain ==");

		this.cre.createSomeEntites();

		(new TableDisplay("OrderRecord", this.mg)).displayToConsole();
		(new TableDisplay("OrderProduct", this.mg)).displayToConsole();
		(new TableDisplay("Product", this.mg)).displayToConsole();

	}

}
