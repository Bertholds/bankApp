package com.codetreatise.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codetreatise.bean.CompteUtilisateur;
import com.codetreatise.repository.UserAccountRepository;
import com.codetreatise.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Override
	public CompteUtilisateur save(CompteUtilisateur entity) {
		return userAccountRepository.save(entity);
	}

	@Override
	public CompteUtilisateur update(CompteUtilisateur entity) {
		return userAccountRepository.save(entity);
	}

	@Override
	public void delete(CompteUtilisateur entity) {
		userAccountRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		userAccountRepository.delete(id);
	}

	@Override
	public CompteUtilisateur find(Long id) {
		return userAccountRepository.findOne(id);
	}

	@Override
	public List<CompteUtilisateur> findAll() {
		return userAccountRepository.findAll();
	}

	@Override
	public void deleteInBatch(List<CompteUtilisateur> users) {
		userAccountRepository.deleteInBatch(users);
	}

	@Override
	public boolean authenticate(String login, String password) {
		CompteUtilisateur compteUtilisateur = userAccountRepository.login(login, password);
		if (compteUtilisateur != null) {
			return true;
		} else {
			return false;
		}

	}

}
