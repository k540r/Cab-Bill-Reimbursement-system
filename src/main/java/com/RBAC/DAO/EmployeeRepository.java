package com.RBAC.DAO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.RBAC.Model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query(value = "SELECT e.* from Employee e, Logininformation l" + " where e.id=l.employee_id "
			+ " and l.username=?1 ", nativeQuery = true)
	Employee findAllByUsername(String username);

	@Query(value = "SELECT l.enabled, l.role, e.* FROM Employee e, Logininformation l WHERE (e.id = l.employee_id) AND e.id = ?1", nativeQuery = true)
	Employee getEmployeeById(Long id);

	// Method to find Employee by ID
	Employee findById(long id);

	Optional<Employee> findByEmail(String email);

	@Query(value = "SELECT COUNT(*) AS total_employees, "
			+ "SUM(CASE WHEN e.status = '1' THEN 1 ELSE 0 END) AS approved_employees, "
			+ "SUM(CASE WHEN e.status = '0' THEN 1 ELSE 0 END) AS pending_employees, "
			+ "SUM(CASE WHEN e.status = '-1' THEN 1 ELSE 0 END) AS rejected_employees "
			+ "FROM Employee e", nativeQuery = true)
	Map<String, Integer> getEmployeeStatistics();

	@Query("SELECT e FROM Employee e ORDER BY e.created_at DESC")
	List<Employee> findAllOrderByCreated_atDesc();

	// for fetch Reporting Personnel tabel
	@Query(value = "SELECT e.* FROM employee e JOIN logininformation li ON e.id = li.employee_id WHERE li.role = 'MANAGER' ORDER BY e.created_at Desc", nativeQuery = true)
	List<Employee> findAllEmployeesByManagerRole();

	// search emplyee name
	// List<Employee> findByFull_nameContainingIgnoreCase(String full_name);	 
//	 @Query("SELECT e FROM Employee e WHERE LOWER(e.full_name) LIKE LOWER(CONCAT('%', :full_name, '%'))")
//	 List<Employee> searchByFullName(@Param("full_name") String full_name);
	@Query("SELECT e.id, e.full_name FROM Employee e WHERE e.full_name LIKE CONCAT('%', :full_name, '%')")
	List<Object[]> searchByFullName(@Param("full_name") String full_name);



}
