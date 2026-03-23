package com.RBAC.DAO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.RBAC.Model.JobTitle;

public interface JobTitleRepository extends JpaRepository<JobTitle, Long> {
	List<JobTitle> findByCreatedAtBetweenOrUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate,
			LocalDateTime startDate2, LocalDateTime endDate2);
}
