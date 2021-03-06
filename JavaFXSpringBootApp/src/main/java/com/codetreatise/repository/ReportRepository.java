package com.codetreatise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
