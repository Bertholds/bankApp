package com.codetreatise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
	

}
