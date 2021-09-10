package com.codetreatise.service;

import com.codetreatise.bean.CompteUtilisateur;
import com.codetreatise.bean.User;
import com.codetreatise.generic.GenericService;

public interface UserService extends GenericService<CompteUtilisateur> {

	boolean authenticate(String login, String password);
		
}
