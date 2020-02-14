package edu.myrza.appdev.labone.repository;

import edu.myrza.appdev.labone.domain.Unit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UnitRepository extends CrudRepository<Unit,Long> {

    List<Unit> findByNameLike(String part);

    boolean existsUnitByName(String name);
}
