package com.RBAC.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.RBAC.Model.Project;
import com.RBAC.Model.ProjectAdd;

@Repository
public interface ProjectAddRepository extends JpaRepository<ProjectAdd, Long> {
	@Query(value = "SELECT EXISTS(SELECT 1 FROM project_add_employee WHERE project_id = :projectId AND employee_id = :employeeId)", nativeQuery = true)
    boolean existsByProjectIdAndEmployeeId(@Param("projectId") Long projectId, @Param("employeeId") Long employeeId);
	
	
//	@Query("SELECT pa.employeeName FROM ProjectAdd pa WHERE pa.projectId.id = :projectId")
//	List<String> findEmployeeNamesByProjectId(@Param("projectId") Long projectId);
	
	 // Join ProjectAdd and Employee tables to get employee name for a given projectId
 //   @Query("SELECT e.id, e.name FROM ProjectAdd pa JOIN pa.employeeId e WHERE pa.projectId.id = :projectId")
 //   List<Object[]> findEmployeeIdsAndNamesByProjectIdx(@Param("projectId") Long projectId);
	@Query("SELECT e.id, e.full_name FROM ProjectAdd pa JOIN pa.employeeId e WHERE pa.projectId.id = :projectId")
	List<Object[]> findEmployeeIdsAndNamesByProjectId(@Param("projectId") Long projectId);


	@Modifying
    @Transactional
	@Query("DELETE FROM ProjectAdd pa WHERE pa.projectId.id = :projectId AND pa.employeeId.id = :employeeId")
    void deleteByProjectIdAndEmployeeId(@Param("projectId") Long projectId, @Param("employeeId") Long employeeId);
	
	
	@Query("SELECT p.projectId  FROM ProjectAdd p WHERE p.employeeId.id = :employeeId")
    List<Project> findProjectsByEmployeeId(@Param("employeeId") Long employeeId);
	
}

