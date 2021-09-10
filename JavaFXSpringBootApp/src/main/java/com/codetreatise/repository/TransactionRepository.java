package com.codetreatise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
