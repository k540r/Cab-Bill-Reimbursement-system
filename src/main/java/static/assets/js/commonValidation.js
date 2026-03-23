/*document.addEventListener("DOMContentLoaded", function () {

	function validateEmail(email) {
		const re = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
		return re.test(String(email).toLowerCase());
	}

	function validateContactNumber(number) {
		const re = /^[0-9]{10}$/;
		return re.test(String(number));
	}

	function validateAadharNumber(aadharNumber) {
		const re = /^[0-9]{12}$/;
		return re.test(String(aadharNumber));
	}

	function validatePassword(password) {
		const lengthCheck = password.length >= 8;
		const specialCharCheck = /[!@#$%^&*(),.?":{}|<>]/.test(password);
		const digitCheck = /\d/.test(password);

		return lengthCheck && specialCharCheck && digitCheck;
	}

	function validateForm() {
		let isValid = true;

		const fullName = document.getElementById("full_name").value.trim();
		const email = document.getElementById("email").value.trim();
		const contactNumber = document.getElementById("contact_number").value.trim();
		const aadharNumber = document.getElementById("aadharNumber").value.trim();
		const password = document.getElementById("password").value.trim();
		const confirmPassword = document.getElementById("confirmPassword").value.trim();

		if (!fullName) {
			alert("Full Name is required.");
			isValid = false;
		}

		if (!validateEmail(email)) {
			alert("Please enter a valid email.");
			isValid = false;
		}

		if (!validateContactNumber(contactNumber)) {
			alert("Please enter a valid Contact Number (10 digits).");
			isValid = false;
		}

		if (aadharNumber && !validateAadharNumber(aadharNumber)) {
			alert("Please enter a valid 12-digit Aadhaar Number.");
			isValid = false;
		}

		if (!validatePassword(password)) {
			alert("Password must be at least 8 characters long, include at least one special character, and one digit.");
			isValid = false;
		}

		if (password !== confirmPassword) {
			alert("Passwords do not match!");
			isValid = false;
		}

		// Validations for dropdowns, dates, and other fields
		const jobTitle = document.querySelector("select[name='jobTitle']").value;
		const gender = document.querySelector("select[name='gender']").value;
		const maritalStatus = document.querySelector("select[name='maritalStatus']").value;
		const department = document.querySelector("select[name='department']").value;
		const agency = document.querySelector("select[name='agency']").value;

		if (jobTitle === "Select Job Title") {
			alert("Please select a Job Title.");
			isValid = false;
		}

		if (gender === "Select Gender") {
			alert("Please select a Gender.");
			isValid = false;
		}

		if (maritalStatus === "Select Marital Status") {
			alert("Please select a Marital Status.");
			isValid = false;
		}

		if (department === "Select Department") {
			alert("Please select a Department.");
			isValid = false;
		}

		if (agency === "Select Agency") {
			alert("Please select an Agency.");
			isValid = false;
		}

		return isValid;
	}

	document.querySelector("form").addEventListener("submit", function (event) {
		if (!validateForm()) {
			event.preventDefault();
		}
	});
});
*/




/**
 * 
 */

/*$(document).ready(function() {
	var dateOfBirthInput = $("#dateOfBirth");
	var dateOfJoiningInput = $("#joiningDate");
	var today = new Date().toISOString().split('T')[0];

	dateOfBirthInput.attr("max", today);
	dateOfJoiningInput.attr("max", today);
});*/

/*
$(document).ready(function() {
	var dateOfBirth1 = $("#tdob");
	var today2 = new Date().toISOString().split('T')[0];
	dateOfBirth1.attr("max", today2);
});
*/






function validateForm() {
	/*const firstNameInput = document.getElementById("first_name");
	const lastNameInput = document.getElementById("last_name");*/
	const fullNameInput = document.getElementById("full_name");
	const fullNameValue = fullNameInput.value.trim();
	const emailInput = document.getElementById("email");
	const contactNumberInput = document.getElementById("contact_number");
	const EmegencyContact = document.getElementById("eme_contact_number");
	//	const dateOfBirthInput = document.getElementById("dob");
	const genderDropdown = document.getElementById("gender");
	const bloodGroup = document.getElementById("bloodgroup");
	const maritalStatus = document.getElementById("maritalstatus");
	const aadhaarInput = document.getElementById("aadharNumber");
	const homeTown = document.getElementById("hometown");
	const addressInput = document.getElementById("address");
	const agencyName = document.getElementById("agency");
	const departmentName = document.getElementById("department");
	const managerName = document.getElementById("manager");/*
	const fullNameInput = document.getElementById("tname");
	const relationInput = document.getElementById("relation");
	const ageInput = document.getElementById("age");*/
	const passwordInput = document.getElementById("password").value.trim();
	const confirmPasswordInput = document.getElementById("confirmPassword").value.trim();

	// Validation for first name: must not be empty and only contain letters
	/*const firstNameRegex = /^[a-zA-Z]+$/;
	if (!firstNameRegex.test(firstNameInput.value) || firstNameInput.value === "") {
		alert("Please enter a valid first name (letters only)");
		firstNameInput.focus();
		return false;
	}

	// Validation for last name: must not be empty and only contain letters
	const lastNameRegex = /^[a-zA-Z]+$/;
	if (!lastNameRegex.test(lastNameInput.value) || lastNameInput.value === "") {
		alert("Please enter a valid last name (letters only)");
		lastNameInput.focus();
		return false;
	}*/

	/*

	// Validation for email: must follow a valid email format
	const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	if (!emailRegex.test(emailInput.value)) {
		alert("Please enter a valid email address");
		emailInput.focus();
		return false;
	}
*/
	/*validation for full Name*/

	function validateFullName(fullName) {
		const fullNameRegex = /^[A-Za-z\s]{3,40}$/; // Allows only letters and spaces, between 3 and 40 characters
		if (!fullNameRegex.test(fullName)) {
			alert("Full name must be between 3 and 40 characters and contain only letters and spaces.");
			return false;
		}
		return true;
	}

	if (!validateFullName(fullNameValue)) {
		fullNameInput.focus();
		return false;
	}

	// Validation for email: must follow a valid email format
	const emailRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
	const allowedDomains = ["gmail.com", "yahoo.com", "outlook.com", "bisag.in"];

	if (!emailRegex.test(emailInput.value)) {
		alert("Please enter a valid email address");
		emailInput.focus();
		return false;
	}

	const domain = emailInput.value.split('@')[1];
	if (!allowedDomains.includes(domain)) {
		alert("Email address must be from gmail.com, yahoo.com, outlook.com or bisag.in");
		return false;
	}

	// Check if a selection has been made in the JOB TITLE dropdown
	if (!hasSelection(document.getElementById("jobTitle"))) {
		alert("Please select a Job Title");
		return false;
	}

	function hasSelection(jobTitle) {
		return jobTitle.selectedIndex > 0;
	}


	// Validation for Contact Number
	const contact_number = contactNumberInput.value;
	function validateContactNumber(contact_number) {
		const NumberRegex = /^\d{10}$/;  // Matches exactly 10 digits
		if (!NumberRegex.test(contact_number) || contact_number.charAt(0) < '6' || contact_number.charAt(0) > '9') {
			alert("Please enter a valid mobile number (10 digits only, starting with 6, 7, 8, or 9)");
			return false;
		}
		return true;
	}

	if (!validateContactNumber(contact_number)) {
		return false;
	}

	// Validation for Emergency Contact Detail
	/*	const eme_contact_number = EmegencyContact.value;
		function validateContactNumber(eme_contact_number) {
			const EmeNumberRegex = /^\d{10}$/;  // Matches exactly 10 digits
			if (!EmeNumberRegex.test(eme_contact_number) || eme_contact_number.charAt(0) < '6' || eme_contact_number.charAt(0) > '9') {
				alert("Please enter a valid mobile number (10 digits only, starting with 6, 7, 8, or 9)");
				return false;
			}
			return true;
		}
	
		if (!validateContactNumber(eme_contact_number)) {
			return false;
		}
	*/

	const eme_contact_number = document.getElementById("EmegencyContact").value;

	// Check if the field is empty, if yes, skip validation
	if (eme_contact_number === "") {
		return true; // No validation needed
	}

	const EmeNumberRegex = /^\d{10}$/; // Matches exactly 10 digits
	if (!EmeNumberRegex.test(eme_contact_number) ||
		eme_contact_number.charAt(0) < '6' || eme_contact_number.charAt(0) > '9') {
		alert("Please enter a valid mobile number (10 digits only, starting with 6, 7, 8, or 9).");
		return false;
	}
	return true;
}

// Validation for Gender
function validateGender(genderDropdown) {
	// Checks selection
	if (genderDropdown.selectedIndex === 0) {
		alert("Please select your gender");
		return false;
	}
	return true;
}

if (!validateGender(genderDropdown)) {
	return false;
}


// Validation for Blood group
function validateBloodGroup(bloodGroup) {
	// Checks selection 
	if (bloodGroup.selectedIndex === 0) {
		alert("Please select your blood group");
		return false;
	}
	return true;
}

if (!validateBloodGroup(bloodGroup)) {
	return false;
}



// Validation for Marital Status

function validateMaritalStatus(maritalStatus) {
	// Checks selection 
	if (maritalStatus.selectedIndex === 0) {
		alert("Please select your Marital status");
		return false;
	}
	return true;
}

if (!validateMaritalStatus(maritalStatus)) {
	return false;
}


// Validation for Aadhaar Card Number
const aadhaarNumber = aadhaarInput.value;
function validateAadhaarNumber(aadhaarNumber) {

	const aadhaarRegex = /^\d{12}$/;

	if (!aadhaarRegex.test(aadhaarNumber)) {
		alert("Please enter a valid Aadhaar number (12 digits only)");
		return false;
	}

	return true;
}

if (!validateAadhaarNumber(aadhaarNumber)) {
	return false;
}


// Validation for Hometown
function validateHometown(homeTown) {
	// Checks selection 
	if (homeTown.selectedIndex === 0) {
		alert("Please select your Hometown");
		return false;
	}
	return true;
}

if (!validateHometown(homeTown)) {
	return false;
}

// Validation for Current Address
const address = addressInput.value;
function validateIndianAddress(address) {
	const addressRegex = /^[\w\s\,\-\/]+\s*(?:[A-Za-z0-9]+)?(?:,\s*[A-Za-z0-9]+)*\s*(?:[0-9]+\s*)?(?:[A-Z]{2}\s*)?$/;

	if (!addressRegex.test(address)) {
		alert("Please enter a valid Address.");
		return false;
	}

	return true;
}

if (!validateIndianAddress(address)) {
	return false;
}







// Validation for Agency
function validateAgency(agencyName) {
	// Checks selection 
	if (agencyName.selectedIndex === 0) {
		alert("Please select Agency");
		return false;
	}
	return true;
}

if (!validateAgency(agencyName)) {
	return false;
}

// Validation for Department
function validateDepartment(departmentName) {
	// Checks selection 
	if (departmentName.selectedIndex === 0) {
		alert("Please select your Department");
		return false;
	}
	return true;
}

if (!validateDepartment(departmentName)) {
	return false;
}

// Validation for Manager

function validateManager(managerName) {
	// Checks selection 
	if (managerName.selectedIndex === 0) {
		alert("Please select your Manager");
		return false;
	}
	return true;
}

if (!validateManager(managerName)) {
	return false;
}

// Password validation function
function validatePassword(password) {
	const passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/;

	if (!passwordRegex.test(password)) {
		alert("Password must be at least 8 characters long, contain at least one digit and one special character.");
		return false;
	}

	return true;
}

// Check if passwords match
function passwordsMatch(password, confirmPassword) {
	if (password !== confirmPassword) {
		alert("Passwords do not match. Please ensure both fields have the same password.");
		return false;
	}

	return true;
}

// Validate password and confirm password fields
if (!validatePassword(passwordInput) || !passwordsMatch(passwordInput, confirmPasswordInput)) {
	return false;
}

return true;
   



// Function to validate first name
/* function validateFirstName(firstName) {
	   var regex = /^[a-zA-Z]+$/;
	   return regex.test(firstName);
}

// Function to validate last name
function validateLastName(lastName) {
	   var regex = /^[a-zA-Z]+$/;
	   return regex.test(lastName);
}

// Function to validate email
function validateEmail(email) {
	   var regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	   return regex.test(email);
}
*/