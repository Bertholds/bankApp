package com.codetreatise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.CompteTampon;

@Repository
public interface CompteTamponRepository extends JpaRepository<CompteTampon, Long>  {



}
