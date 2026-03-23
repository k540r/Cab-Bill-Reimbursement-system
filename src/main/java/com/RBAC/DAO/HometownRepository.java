package com.RBAC.DAO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RBAC.Model.Hometown;

public interface HometownRepository extends JpaRepository<Hometown, Long> {
	List<Hometown> findByCreatedAtBetweenOrUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate,
			LocalDateTime startDate2, LocalDateTime endDate2);

}
