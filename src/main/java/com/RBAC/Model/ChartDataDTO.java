package com.RBAC.Model;

public class ChartDataDTO {
	
	private String month;
	private Double pendingAmount;
	private Double approvedAmount;

	// Constructor
	public ChartDataDTO(String month, Double pendingAmount, Double approvedAmount) {
		this.month = month;
		this.pendingAmount = pendingAmount;
		this.approvedAmount = approvedAmount;
	}

	// Getters and setters
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Double getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(Double pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

}
