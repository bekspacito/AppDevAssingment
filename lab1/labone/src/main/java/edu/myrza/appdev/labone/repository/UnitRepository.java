package edu.myrza.appdev.labone.repository;

import edu.myrza.appdev.labone.domain.Unit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitRepository extends CrudRepository<Unit,Long> {

    @Query("SELECT u FROM Unit u WHERE u.name LIKE '%:partname%' ")
    List<Unit> findAllByPartName(@Param("partname") String partName);

}
