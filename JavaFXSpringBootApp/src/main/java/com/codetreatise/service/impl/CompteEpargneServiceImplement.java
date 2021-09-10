package com.codetreatise.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.service.GlobalService;

@Repository
@Transactional
public class CompteEpargneServiceImplement implements GlobalService<CompteEpargne> {

	@PersistenceContext
	EntityManager em;
	
	@Override
	public CompteEpargne update(CompteEpargne compteEpargne) {
		// TODO Auto-generated method stub
		return em.merge(compteEpargne);
	}

}
