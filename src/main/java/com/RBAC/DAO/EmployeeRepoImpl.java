package com.RBAC.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RBAC.Model.Employee;
import com.RBAC.Model.Project;

@Service
public class EmployeeRepoImpl {
	@Autowired
	private EmployeeRepository employeeRepo;
	
	 @Autowired
	 private ProjectRepository projectRepository;


	public Map<String, Integer> getEmployeeStatistics() {
		return employeeRepo.getEmployeeStatistics();
	}
	
	
	public List<Employee> findAllEmployeesOrderByCreated_atDesc() {
		 return employeeRepo.findAllOrderByCreated_atDesc();
	}
	
	// Method to fetch Employee by ID
    public Employee getEmployeeById(Long id) {
        return employeeRepo.findById(id).orElse(null);
    }
    
 // Method to fetch Employee by Username
    public Employee getEmployeeByUsername(String username) {
        return employeeRepo.findAllByUsername(username);
    }

    // Add a new project and link it to the employee who added it
    public void addProjectToEmployeeByUsername(Project project, String username) {
        Employee employee = getEmployeeByUsername(username);
        if (employee != null) {
            project.setEmployee(employee); // Set the employee in the project
            projectRepository.save(project); // Save the project in the DB
        } else {
            throw new IllegalArgumentException("Employee not found");
        }
    }
    
    public Employee findAllByUsername(String username) {
        return employeeRepo.findAllByUsername(username);
    }
    
  //for fetch Reporting Personnel tabel 
    public List<Employee> getAllManagers() {
        return employeeRepo.findAllEmployeesByManagerRole();
    }
    
    //fetch name search
   
    public List<Map<String, Object>> searchEmployeesByFullName(String search) {
        List<Object[]> results = employeeRepo.searchByFullName(search);
        
        // Transform the results into a list of maps with 'id' and 'full_name'
        List<Map<String, Object>> employees = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> employee = new HashMap<>();
            employee.put("id", result[0]);
            employee.put("full_name", result[1]);
            employees.add(employee);
        }
        
        return employees;
    }


	public Employee findById(Long employeeId) {
		// TODO Auto-generated method stub
		return null;
	}


}
