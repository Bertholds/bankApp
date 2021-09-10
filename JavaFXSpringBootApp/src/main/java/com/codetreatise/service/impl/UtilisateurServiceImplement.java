package com.codetreatise.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.codetreatise.bean.Utilisateur;
import com.codetreatise.service.GlobalService;

@Repository
@Transactional
public class UtilisateurServiceImplement implements GlobalService<Utilisateur> {

	@PersistenceContext
	EntityManager em;
	
	@Override
	public Utilisateur update(Utilisateur utilisateur) {
		// TODO Auto-generated method stub
		return em.merge(utilisateur);
	}

}
