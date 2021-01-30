package us.thirdbase.rspi.parts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import us.thirdbase.rspi.parts.model.Image;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByPartId(@Param("id") Long id);
}
