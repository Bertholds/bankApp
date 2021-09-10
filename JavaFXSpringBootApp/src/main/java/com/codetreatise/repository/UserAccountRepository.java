package com.codetreatise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.CompteUtilisateur;
@Repository
public interface UserAccountRepository extends JpaRepository<CompteUtilisateur, Long> {
     
	@Query("select c from CompteUtilisateur c where c.login=(:login) and c.pass=(:pass)")
	public CompteUtilisateur login(@Param("login")String login, @Param("pass")String pass);
	
	@Query("select c from CompteUtilisateur c where c.login=(:login) and c.pass=(:pass) ")
	public CompteUtilisateur authentification(@Param("login")String login, @Param("pass")String pass);
}
