package us.thirdbase.rspi.parts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import us.thirdbase.rspi.parts.model.Part;

@Repository
public interface PartRepository extends JpaRepository<Part, Long>, JpaSpecificationExecutor<Part> {
}
