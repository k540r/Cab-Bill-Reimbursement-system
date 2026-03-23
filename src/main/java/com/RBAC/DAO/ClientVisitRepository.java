package com.RBAC.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.RBAC.Model.ClientVisit;
import com.RBAC.Model.Employee;

public interface ClientVisitRepository extends JpaRepository<ClientVisit, Long> {

	 @Query("SELECT cv FROM ClientVisit cv WHERE cv.project.id = :projectId")
	    List<ClientVisit> findByProjectId(@Param("projectId") Long projectId);
	
	 @Query("SELECT cv FROM ClientVisit cv ORDER BY cv.created_at DESC")
		List<ClientVisit> findAllOrderByCreated_atDesc();

}
