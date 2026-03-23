package com.RBAC.DAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import com.RBAC.Model.Department;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	public List<Department> getAllDepartment() {
	
		// Sort by 'updatedAt' descending, and then by 'createdAt' descending as
		// secondary
		return departmentRepository
				.findAll(Sort.by(Sort.Direction.DESC, "updatedAt").and(Sort.by(Sort.Direction.ASC, "createdAt")));
	}

	public void saveOrUpdateDepartment(Department department) {
		departmentRepository.save(department);
	}

	public void deleteDepartment(Long id) {
		departmentRepository.deleteById(id);
	}

	public List<Department> getDepartmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return departmentRepository.findByCreatedAtBetweenOrUpdatedAtBetween(startDate, endDate, startDate, endDate);
    }

}
