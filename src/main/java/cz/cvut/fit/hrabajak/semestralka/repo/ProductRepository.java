package cz.cvut.fit.hrabajak.semestralka.repo;

import cz.cvut.fit.hrabajak.semestralka.orm.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Override
	@Transactional
	List<Product> findAll();

}
