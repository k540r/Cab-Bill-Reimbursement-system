package com.RBAC.DAO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.RBAC.Model.ReportingPersonnel;

public interface ReportingPersonnelRepository extends JpaRepository<ReportingPersonnel, Long>{
	List<ReportingPersonnel> findByCreatedAtBetweenOrUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate,
			LocalDateTime startDate2, LocalDateTime endDate2);

}
