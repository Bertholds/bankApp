package com.codetreatise.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.codetreatise.bean.Avalise;
import com.codetreatise.service.GlobalService;

@Repository
@Transactional
public class AvaliseServiceImplement implements GlobalService<Avalise>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	public Avalise update(Avalise avalise) {
		
		return em.merge(avalise);
	}

}
