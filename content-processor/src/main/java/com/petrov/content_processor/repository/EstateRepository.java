package com.petrov.content_processor.repository;

import com.petrov.content_processor.entity.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {


}
