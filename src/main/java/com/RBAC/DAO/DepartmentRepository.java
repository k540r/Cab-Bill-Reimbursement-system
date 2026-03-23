package com.RBAC.DAO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RBAC.Model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
	
	List<Department> findByCreatedAtBetweenOrUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate,
			LocalDateTime startDate2, LocalDateTime endDate2);

}
