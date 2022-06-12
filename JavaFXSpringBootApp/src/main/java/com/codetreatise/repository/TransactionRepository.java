package com.codetreatise.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.Avalise;
import com.codetreatise.bean.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByAvalise(Avalise avalise);
	
	@Query("select t from Transaction t order by t.transaction_id desc")
	public List<Transaction> findAllOrderByidDesc();
	
	@Query("select t from Transaction t order by t.transaction_id desc")
	public List<Transaction> findWithLimitOrderByidDesc(Pageable pageable);
	
	@Query("select count(t) from Transaction t")
	public int countTransaction();
	
	@Query("select t from Transaction t where t.transaction_id=(:id)")
	public Transaction getLastTransaction(@Param("id")Long id);
	
	@Query("select max(t.transaction_id) from Transaction t")
	public Long getLastInsertedId();
	
}
