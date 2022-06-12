/**
 * 
 */
package com.codetreatise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.Avalise;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.CompteTampon;
import com.codetreatise.bean.Transaction;

/**
 * @author Chrispin
 *
 */
@Repository
public interface AvaliseRepository extends JpaRepository<Avalise, Long> {
   
	@Query("select a from Avalise a where a.compteEpargne.adherent.identifiant=(:id)")
	public Avalise findById(@Param("id")Long id);
	
	@Query("select a from Avalise a where a.transaction.adherent.identifiant=(:id) and a.remboursser=(:bool)")
	public List<Avalise> findByIdAndStatut(@Param("id")Long id, @Param("bool")Boolean bool);
	
	@Query("select a from Avalise a where a.compteEpargne=(:compteEpargne) and a.remboursser=(:bool) and a.compteTampon !=(:compteTampon)")
	public List<Avalise> findCompteByIdAndStatut(@Param("compteEpargne")CompteEpargne compteEpargne, @Param("bool")Boolean bool, @Param("compteTampon")CompteTampon compteTampon);
	
	@Query("select a from Avalise a where a.remboursser=(:bool)")
	public List<Avalise> findByStatut(@Param("bool")Boolean bool);
	
	@Query("select a from Avalise a where a.transaction.adherent.identifiant=(:id) and a.remboursser=(:bool)")
	public List<Avalise> findByIdAndStatutList(@Param("id")Long id, @Param("bool")Boolean bool);

	@Query("select a from Avalise a where a.transaction.transaction_id=(:id)")
	public Avalise findByTransactionId(@Param("id")Long id);
	
	
}
