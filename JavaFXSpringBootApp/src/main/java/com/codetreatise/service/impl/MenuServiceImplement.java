package com.codetreatise.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.codetreatise.bean.Menu;
import com.codetreatise.service.GlobalService;

@Repository
@Transactional
public class MenuServiceImplement implements GlobalService<Menu> {

	@PersistenceContext
	private EntityManager em;
	
	public MenuServiceImplement() {
		
	}

	@Override
	public Menu update(Menu menu) {
		return em.merge(menu);
	}

}
