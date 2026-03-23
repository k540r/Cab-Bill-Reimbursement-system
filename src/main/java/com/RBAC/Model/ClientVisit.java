package com.RBAC.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "client_visits")
public class ClientVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(name="visit_memeber_name", nullable = false)
//    private String visitMemberName;
    
    
    @Column(name = "visitor_members_name", nullable = false)
    private String  VisitorMembersName;

	@Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "purpose_of_visit", nullable = false)
    private String purposeOfVisit;
    
    @CreationTimestamp 
	  private LocalDateTime created_at;
  
	public String getVisitorMembersName() {
		return VisitorMembersName;
	}

	public void setVisitorMembersName(String visitorMembersName) {
		VisitorMembersName = visitorMembersName;
	}

	@Column(name = "date_of_visit", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfVisit;

    @Column(name = "mom_file_path")
    private String momFilePath;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // Getters and setters
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getPurposeOfVisit() {
		return purposeOfVisit;
	}

	public void setPurposeOfVisit(String purposeOfVisit) {
		this.purposeOfVisit = purposeOfVisit;
	}

	public LocalDate getDateOfVisit() {
		return dateOfVisit;
	}

	public void setDateOfVisit(LocalDate dateOfVisit) {
		this.dateOfVisit = dateOfVisit;
	}

	public String getMomFilePath() {
		return momFilePath;
	}

	public void setMomFilePath(String momFilePath) {
		this.momFilePath = momFilePath;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	
	

	

	



    
    

}

