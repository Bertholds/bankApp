package com.codetreatise.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.codetreatise.bean.Transaction;
import com.codetreatise.service.GlobalService;

@Repository
@Transactional
public class TransactionServiceImplement implements GlobalService<Transaction> {

	@PersistenceContext
	EntityManager em;
	
	@Override
	public Transaction update(Transaction transaction) {
		return em.merge(transaction);
	}

}
