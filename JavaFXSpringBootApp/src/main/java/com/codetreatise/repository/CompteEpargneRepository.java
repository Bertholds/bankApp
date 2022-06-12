package com.codetreatise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.Adherent;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

@Repository
@Transactional
public interface CompteEpargneRepository extends JpaRepository<CompteEpargne, Long> {

	CompteEpargne findByAdherent(Adherent adherent);
	List<CompteEpargne> findByStatut(String statut);
	
	@Query("select c from CompteEpargne c where c.lacarte >= 1")
	ArrayList<CompteEpargne> findByLaCartePositive();
}
