package com.petrov.content_processor.repository;

import com.petrov.content_processor.entity.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {

	Optional<Estate> findByCadastrNumberAndSource(String cadastrNumber, String source);

	List<Estate> findByCadastrNumber(String cadastrNumber);

	List<Estate> findBySource(String source);

	@Modifying
	@Query("UPDATE Estate e SET e.price = :price, e.square = :square, e.roomCount = :roomCount, " +
			"e.floor = :floor, e.totalFloors = :totalFloors, e.address = :address, " +
			"e.type = :type, e.updatedAt = CURRENT_TIMESTAMP, e.version = e.version + 1 " +
			"WHERE e.cadastrNumber = :cadastrNumber AND e.source = :source")
	int updateEstate(@Param("cadastrNumber") String cadastrNumber,
					 @Param("source") String source,
					 @Param("price") BigDecimal price,
					 @Param("square") BigDecimal square,
					 @Param("roomCount") Integer roomCount,
					 @Param("floor") Integer floor,
					 @Param("totalFloors") Integer totalFloors,
					 @Param("address") String address,
					 @Param("type") String type);

	@Modifying
	@Query("DELETE FROM Estate e WHERE e.cadastrNumber = :cadastrNumber AND e.source = :source")
	void deleteByCadastrNumberAndSource(@Param("cadastrNumber") String cadastrNumber,
										@Param("source") String source);
}