package com.RBAC.Model;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	 private Long id;
	@Column(name = "full_name", nullable = false)
	private String full_name;
	@Column(length = 255)
	private String profilePicturePath;
	@Column(name = "email", unique = true, nullable = false)
    private String email;
	@Column(name = "job_title", nullable = false)
    private String jobTitle;
	@Column(name = "contact_number", nullable = false)
    private String contact_number;
	@Column(name = "emergency_contact_number")
    private String eme_contact_number;
	@Column(name = "date_of_birth", nullable = false)
    private String dateOfBirth;
	@Column(name = "gender", nullable = false)
    private String gender;
	@Column(name = "blood_group")
    private String bloodGroup;
	@Column(name = "marital_status")
    private String maritalStatus;
	@Column(name = "aadhar_number")
    private String aadharNumber;
	@Column(name = "current_address")
    private String currentAddress;
    @Column(name = "joining_date", nullable = false)
    private String joiningDate;
    @Column(name = "agency")
    private String agency;
    @Column(name = "department")
    private String department;
    @Column(name = "reporting_per")
    private String reporting_per;
    @Column(name = "hometown")
    private String hometown;
    @Column(name = "status")
    private String status="0";
    
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private Logininformation logininformation;
    
    
    @CreationTimestamp 
	  private LocalDateTime created_at;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getContact_number() {
		return contact_number;
	}
	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}
	public String getEme_contact_number() {
		return eme_contact_number;
	}
	public void setEme_contact_number(String eme_contact_number) {
		this.eme_contact_number = eme_contact_number;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBloodGroup() {
		return bloodGroup;
	}
	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getAadharNumber() {
		return aadharNumber;
	}
	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getCurrentAddress() {
		return currentAddress;
	}
	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}
	public String getJoiningDate() {
		return joiningDate;
	}
	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getReporting_per() {
		return reporting_per;
	}
	public void setReporting_per(String reporting_per) {
		this.reporting_per = reporting_per;
	}
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Logininformation getLogininformation() {
		return logininformation;
	}
	public void setLogininformation(Logininformation logininformation) {
		this.logininformation = logininformation;
	}
	public String getProfilePicturePath() {
		return profilePicturePath;
	}
	public void setProfilePicturePath(String profilePicturePath) {
		this.profilePicturePath = profilePicturePath;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", full_name=" + full_name + ", profilePicturePath=" + profilePicturePath
				+ ", email=" + email + ", jobTitle=" + jobTitle + ", contact_number=" + contact_number
				+ ", eme_contact_number=" + eme_contact_number + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender
				+ ", bloodGroup=" + bloodGroup + ", maritalStatus=" + maritalStatus + ", aadharNumber=" + aadharNumber
				+ ", currentAddress=" + currentAddress + ", joiningDate=" + joiningDate + ", agency=" + agency
				+ ", department=" + department + ", reporting_per=" + reporting_per + ", hometown=" + hometown
				+ ", status=" + status + ", logininformation=" + logininformation + "]";
	}
	
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}


}
