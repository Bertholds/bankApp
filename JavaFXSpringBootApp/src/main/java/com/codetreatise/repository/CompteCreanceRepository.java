package com.codetreatise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.CompteCreance;

@Repository
public interface CompteCreanceRepository extends JpaRepository<CompteCreance, Long>{

}
