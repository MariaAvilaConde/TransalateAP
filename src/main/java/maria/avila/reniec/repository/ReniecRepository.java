package maria.avila.reniec.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import maria.avila.reniec.model.DataReniec;

@Repository
public interface ReniecRepository extends ReactiveCrudRepository<DataReniec, Long> {

}

