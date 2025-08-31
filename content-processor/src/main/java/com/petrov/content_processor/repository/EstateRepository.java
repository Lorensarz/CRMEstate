package com.petrov.content_processor.repository;

import com.petrov.content_processor.entity.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {


	@Modifying
	@Query(value = """
			INSERT INTO estates (cadastr_number, source, price, type, square, room_count,
			                     floor, total_floors, address, updated_at, version)
			   VALUES (:#{#estate.cadastrNumber}, :#{#estate.source}, :#{#estate.price},
			           :#{#estate.type}, :#{#estate.square}, :#{#estate.roomCount},
			           :#{#estate.floor}, :#{#estate.totalFloors}, :#{#estate.address},
			           :#{#estate.updatedAt}, 0)
			ON CONFLICT (cadastr_number, source)
			DO UPDATE SET
			    price = EXCLUDED.price,
			    type = EXCLUDED.type,
			    square = EXCLUDED.square,
			    room_count = EXCLUDED.room_count,
			    floor = EXCLUDED.floor,
			    total_floors = EXCLUDED.total_floors,
			    address = EXCLUDED.address,
			    updated_at = EXCLUDED.updated_at,
			    version = estates.version + 1
			""", nativeQuery = true)
	void upsertEstate(@Param("estate")Estate estate);

}
