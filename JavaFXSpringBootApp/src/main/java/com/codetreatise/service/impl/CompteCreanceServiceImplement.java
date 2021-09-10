package com.codetreatise.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.codetreatise.bean.CompteCreance;
import com.codetreatise.service.GlobalService;

@Repository
@Transactional
public class CompteCreanceServiceImplement implements GlobalService<CompteCreance> {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public CompteCreance update(CompteCreance compteCreance) {
		return em.merge(compteCreance);
	}

}
