package com.codetreatise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
  
	@Query("select u from Utilisateur u where u.pseudo=(:pseudo)")
	public Utilisateur findByPseudo(@Param("pseudo")String pseudo);
}
