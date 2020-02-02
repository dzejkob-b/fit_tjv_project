package cz.cvut.fit.hrabajak.semestralka;

import cz.cvut.fit.hrabajak.semestralka.rest.RestProductController;
import cz.cvut.fit.hrabajak.semestralka.rest.RestRootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * vychozi bod ktery spousti springboot jenz spusti AppLogic
 */
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = RestProductController.class )
@ComponentScan(basePackageClasses = RestRootController.class )
@Import({ AppConfig.class })
public class AppRunner implements CommandLineRunner {

	@Autowired
	AppLogic m;

	public AppRunner() {
	}

	@Override
	public void run(String[] args)  {

		m.run(args);
		
	}

}
