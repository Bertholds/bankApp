package com.codetreatise.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Controller;

import com.codetreatise.bean.CompteTampon;
import com.codetreatise.service.GlobalService;

@Controller
@Transactional
public class CompteTamponServiceImplement implements GlobalService<CompteTampon> {

	@PersistenceContext
	EntityManager em;
	
	@Transactional
	@Override
	public CompteTampon update(CompteTampon compteTampon) {
		// TODO Auto-generated method stub
		return em.merge(compteTampon);
	}

}
