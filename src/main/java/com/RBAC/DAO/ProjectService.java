package com.RBAC.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RBAC.Model.Employee;
import com.RBAC.Model.Project;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public void saveProject(Project project) {
        projectRepository.save(project);
    }
    
    // Return all projects in descending order by id
    // Return all projects by a specific employee, ordered by id in descending order
    // Method to fetch projects by employee and order by project ID in descending order
    public List<Project> getProjectsByEmployeeOrderByIdDesc(Long employeeId) {
        return projectRepository.findAllByEmployeeIdOrderByIdDesc(employeeId);
    }
    
    public Project findById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }
}








