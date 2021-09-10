package com.codetreatise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.Menu;
import java.lang.String;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>{
 
	@Query("select m from Menu m where m.nom=(:nom) and m.droitAcces=(:droitAcces)")
   public Menu findByNomAndDroitAccess(@Param("nom")String nom, @Param("droitAcces")String droitAcces);
  
    public  List<Menu> findByDroitAcces(String droitacces);
	
}
