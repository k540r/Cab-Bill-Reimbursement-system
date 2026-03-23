package com.RBAC.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "billreimbursement")
public class Billreimbursement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	private String project;
	private String travelingFrom;
	private String travelingTo;
	private String fromDate;
	private String whomToVisit;
	private Double billAmount;
	


	 @Column(nullable=false)
	    private String status= "Pending";

	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	private String filePath;
	
	private String remarks;


	// Getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getTravelingFrom() {
		return travelingFrom;
	}

	public void setTravelingFrom(String travelingFrom) {
		this.travelingFrom = travelingFrom;
	}


	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}



	public String getWhomToVisit() {
		return whomToVisit;
	}

	public void setWhomToVisit(String whomToVisit) {
		this.whomToVisit = whomToVisit;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTravelingTo() {
		return travelingTo;
	}

	public void setTravelingTo(String travelingTo) {
		this.travelingTo = travelingTo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



}