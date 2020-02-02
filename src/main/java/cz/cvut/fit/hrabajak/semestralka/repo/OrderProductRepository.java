package cz.cvut.fit.hrabajak.semestralka.repo;

import cz.cvut.fit.hrabajak.semestralka.orm.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

	@Override
	@Transactional
	List<OrderProduct> findAll();

}
