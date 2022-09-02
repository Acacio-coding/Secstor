package com.ifsc.secstor.api.repository;


import com.ifsc.secstor.api.model.NumberModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberRepository extends JpaRepository<NumberModel, Long> {
    boolean existsByG1(String g1);
}
