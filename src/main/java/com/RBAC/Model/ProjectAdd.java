package com.RBAC.Model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "projectAddEmployee")
public class ProjectAdd {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//
//	@Column(name = "employee_name", nullable = false)
//	private String employeeName;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private Project projectId;

	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employeeId;

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public String getEmployeeName() {
//		return employeeName;
//	}
//
//	public void setEmployeeName(String employeeName) {
//		this.employeeName = employeeName;
//	}

	public Project getProjectId() {
		return projectId;
	}

	public void setProjectId(Project projectId) {
		this.projectId = projectId;
	}

	public Employee getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Employee employeeId) {
		this.employeeId = employeeId;
	}
}