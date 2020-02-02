package cz.cvut.fit.hrabajak.semestralka.repo;

import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRecordRepository extends JpaRepository<OrderRecord, Long> {
	
	@Override
	@Transactional
	List<OrderRecord> findAll();

	@Transactional
	List<OrderRecord> findAllByStatus(OrderRecord.Status status);

}
