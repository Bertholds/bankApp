package com.codetreatise.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.codetreatise.bean.CompteUtilisateur;
import com.codetreatise.service.GlobalService;

@Repository
@Transactional
public class UserAccountServiceImplement implements GlobalService<CompteUtilisateur> {

	@PersistenceContext 
	EntityManager em;
	
	@Override
	public CompteUtilisateur update(CompteUtilisateur compteUtilisateur) {
		
		return em.merge(compteUtilisateur);
	}

}
