package cz.cvut.fit.hrabajak.semestralka;

import cz.cvut.fit.hrabajak.semestralka.data.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
public class AppConfig {

	//@Autowired
	//private EntityManagerFactory mngFactory;

	@Bean
	public ProductFactory getProductFactory() {
		return new ProductFactory();
	}

	@Bean
	public OrderRecordFactory getOrderRecordFactory() {
		return new OrderRecordFactory();
	}

	@Bean
	public OrderProductFactory getOrderProductFactory() {
		return new OrderProductFactory();
	}

	@Bean
	public AppLogic getAppMain() {
		return new AppLogic();
	}

	@Bean
	public DataManager getDataManager() {
		return new DataManager();
	}

	@Bean
	public DataCreator getDataCreator() {
		return new DataCreator();
	}

	// entity managery reseny zde - annotaci @PersistenceContext se mi rozbehat nepodarilo

	/*
	@Bean(destroyMethod = "close")
	public EntityManagerFactory getEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("persistenceUnit");
	}
	
	@Bean(destroyMethod = "close")
	public EntityManager getEntityManager() {
		return this.mngFactory.createEntityManager();
	}
	*/

}
