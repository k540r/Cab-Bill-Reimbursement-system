package com.RBAC.DAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RBAC.Model.Project;
import com.RBAC.Model.ProjectAdd;

@Service
public class ProjectAddService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectAddRepository projectAddRepository;

//    public void saveProject(Project project) {
//        projectRepository.save(project);
//    }

	// Add an employee to a project
	public void addEmployeeToProject(ProjectAdd projectAdd) {
		projectAddRepository.save(projectAdd);
	}

	public boolean isEmployeeAlreadyAddedToProject(Long projectId, Long employeeId) {
		return projectAddRepository.existsByProjectIdAndEmployeeId(projectId, employeeId);
	}

	// Get employee names associated with a project
	/// public List<String> getEmployeeNamesByProjectId(Long projectId) {
	// return projectAddRepository.findEmployeeNamesByProjectId(projectId);
	// }

	public List<Map<String, Object>> getEmployeeIdsAndNamesByProjectId(Long projectId) {
		List<Object[]> results = projectAddRepository.findEmployeeIdsAndNamesByProjectId(projectId);

		// Convert Object[] to Map for easier handling
		return results.stream().map(result -> {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeId", result[0]);
			map.put("fullName", result[1]);
			map.put("projectId", projectId); // Pass projectId directly as it is the same for all entries
			return map;
		}).collect(Collectors.toList());
	}

	// Remove employee from the project
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		projectAddRepository.deleteByProjectIdAndEmployeeId(projectId, employeeId);
	}
	
	 // Find projects by employee ID
	public List<Project> getProjectsByEmployeeId(Long employeeId) {
        return projectAddRepository.findProjectsByEmployeeId(employeeId);
    }
	
}
