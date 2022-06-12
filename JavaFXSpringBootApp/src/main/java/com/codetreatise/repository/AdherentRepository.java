package com.codetreatise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.codetreatise.bean.Adherent;

@Repository
@Transactional
public interface AdherentRepository extends JpaRepository<Adherent, Long> {
  
	@Query("select a from Adherent a where a.situation=(:situation)")
	public List<Adherent> findBySituation(@Param("situation")String situation);
	
	@Query("select max(a.identifiant) from Adherent a")
	public int generateId();
	
	public Adherent findByUniqueName(String uniqueName);
	
}
