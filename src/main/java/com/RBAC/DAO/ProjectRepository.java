package com.RBAC.DAO;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.RBAC.Model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	// Fetch all projects added by a specific employee
    List<Project> findAllByEmployeeId(Long employeeId);
    
    @Query("SELECT p FROM Project p WHERE p.employee.id = :employeeId ORDER BY p.id DESC")
    List<Project> findAllByEmployeeIdOrderByIdDesc(@Param("employeeId") Long employeeId);
   
}
