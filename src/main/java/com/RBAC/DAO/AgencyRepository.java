package com.RBAC.DAO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RBAC.Model.Agency;


public interface AgencyRepository extends JpaRepository<Agency, Long> {
	List<Agency> findByCreatedAtBetweenOrUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate,
			LocalDateTime startDate2, LocalDateTime endDate2);

}
