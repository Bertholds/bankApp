package com.codetreatise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.CompteEpargneDetail;

@Repository
public interface CompteEpargneDetailRepository extends JpaRepository<CompteEpargneDetail, Long> {

	@Query("select cd from CompteEpargneDetail cd where cd.date like(:date) and cd.compteEpargne=(:ce) and cd.idCompteEpargneDetail=(:id)")
	public CompteEpargneDetail findByCompteEpargneAndDateAndMaxidPerMont(@Param("date")String date, @Param("ce")CompteEpargne ce, @Param("id")Long id);
	
	@Query("select max(cd.idCompteEpargneDetail) from CompteEpargneDetail cd where cd.date like(:date) and cd.compteEpargne=(:ce)")
	public Long getReport(@Param("date")String date, @Param("ce")CompteEpargne ce);
	
	@Query("select max(cd.idCompteEpargneDetail) from CompteEpargneDetail cd where cd.date=(:date) and cd.compteEpargne=(:ce) ")
	public Long getSoldeAu(@Param("date")String date, @Param("ce")CompteEpargne ce);
	
	@Query("select sum(cd.montantTransaction) from CompteEpargneDetail cd where cd.date like (:date) and cd.compteEpargne=(:ce)")
	public Long getDepotPerMont(@Param("date")String date, @Param("ce")CompteEpargne ce);
	
	@Query("select cd from CompteEpargneDetail cd where cd.date like(:date) and cd.compteEpargne=(:ce)")
	public List<CompteEpargneDetail> getCompteEpargneDetailByDateAndCompteEpargne(@Param("date")String date, @Param("ce")CompteEpargne ce);
}
