package com.RBAC.CommonController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.RBAC.DAO.AgencyRepository;
import com.RBAC.DAO.AgencyService;
import com.RBAC.DAO.DepartmentRepository;
import com.RBAC.DAO.DepartmentService;
import com.RBAC.DAO.EmployeeRepository;
import com.RBAC.DAO.HometownRepository;
import com.RBAC.DAO.HometownService;
import com.RBAC.DAO.JobTitleRepository;
import com.RBAC.DAO.JobTitleService;
import com.RBAC.DAO.LogininformationRepository;
import com.RBAC.DAO.PasswordService;
import com.RBAC.DAO.ReportingPersonnelRepository;
import com.RBAC.DAO.ReportingPersonnelService;
import com.RBAC.Model.Agency;
import com.RBAC.Model.Department;
import com.RBAC.Model.Employee;
import com.RBAC.Model.Hometown;
import com.RBAC.Model.JobTitle;
import com.RBAC.Model.Logininformation;
import com.RBAC.Model.ReportingPersonnel;
import com.RBAC.RB.MasterDropDown;

@Controller

public class LoginController {
	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private PasswordService passwordService;

	@Autowired
	private LogininformationRepository logininformationRepository;

	@Autowired
	private AgencyService agencyService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private JobTitleService jobTitleService;

	@Autowired
	private ReportingPersonnelService reportingPersonnelService;

	@Autowired
	private HometownService hometownService;

	@Autowired
	private AgencyRepository agencyRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private JobTitleRepository jobTitleRepository;
	@Autowired
	private ReportingPersonnelRepository reportingPersonnelRepository;
	@Autowired
	private HometownRepository hometownRepository;

	/*
	 * @GetMapping("/login") public String redirectF() { return "redirect:/"; }
	 */

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "success", required = false) String success, Model model) {

		if (error != null) {
			model.addAttribute("message", "Invalid username or password.");
//			return "login?logout";
		} else if (success != null) {
			model.addAttribute("message", "Login successful!");

		}
		return "login";
	}

	@GetMapping("/")
	public String home(HttpServletRequest request) {
		System.out.println(getClientIp1(request));
		return "login"; // Assuming layout/login.html is the correct path
	}

	public static String getClientIp1(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("X-Real-IP");
		}
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}

		// Handle IPv6 Addresses
		if (ipAddress != null && ipAddress.contains(":")) {
			// IPv4-mapped IPv6 address (e.g., ::ffff:192.168.1.1)
			if (ipAddress.startsWith("::ffff:")) {
				ipAddress = ipAddress.substring(7);
			}
			// Convert to more readable form if needed, or handle specifically
		}

		return ipAddress;
	}

	@GetMapping("/employee_registration")
	public String employee_registration(Model model, HttpServletRequest request) {
//		 m.addAttribute("viewMode", false);

		List<Department> dropdownItemsDept = departmentService.getAllDepartment();
		model.addAttribute("dropdownItemsDept", dropdownItemsDept);

		List<Hometown> dropdownItemsHometown = hometownService.getAllHometown();
		model.addAttribute("dropdownItemsHometown", dropdownItemsHometown);

		List<Agency> dropdownItemsAgency = agencyService.getAllAgency();
		model.addAttribute("dropdownItemsAgency", dropdownItemsAgency);

		List<JobTitle> dropdownItemsJob = jobTitleService.getAllJobTitle();
		model.addAttribute("dropdownItemsJob", dropdownItemsJob);

		List<ReportingPersonnel> dropdownItemsReportingPersonnel = reportingPersonnelService.getAllReportingPersonnel();
		model.addAttribute("dropdownItemsReportingPersonnel", dropdownItemsReportingPersonnel);
		System.out.println("IP IS :: " + getClientIp(request));
		return "register";

	}

	public static String getClientIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("X-Real-IP");
		}
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	@GetMapping("/forgot_password")
	public String forgot_password() {

		return "forgot-password";

	}

	@GetMapping("/reset_password")
	public String reset_password() {

		return "reset-password";

	}

	@Value("${file.upload-dir}")
	private String uploadDir;

	@PostMapping("/emp_reg")
	@ResponseBody
	public ResponseEntity<Map<String, String>> registerEmployee(
			@Validated @ModelAttribute("employee") Employee employee,
			@RequestParam("confirmPassword") String verifyPassword,
			@ModelAttribute("logininformation") Logininformation log, BindingResult result,
			@RequestParam("profilePicture") MultipartFile profilePicture, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		// Generate username and encode password

		Map<String, String> response = new HashMap<>();

		// Validate Full Name
		if (employee.getFull_name() == null || !employee.getFull_name().matches("^[A-Za-z\\s]{3,40}$")) {
			response.put("full_name",
					"Full name must be between 3 and 40 characters and contain only letters and spaces.");
		}

		else {
			// Get the full name from the form
			String fullName = employee.getFull_name();
			String[] nameParts = fullName.trim().split("\\s+");
			StringBuilder capitalizedFullName = new StringBuilder();

			// Capitalize each part of the name
			for (String part : nameParts) {
				if (part.length() > 0) {
					capitalizedFullName.append(Character.toUpperCase(part.charAt(0)))
							.append(part.substring(1).toLowerCase()).append(" ");
				}
			}

			// Set the capitalized name back to the employee object
			employee.setFull_name(capitalizedFullName.toString().trim());

		}

		// Validate and save the profile picture to the file system
		if (!profilePicture.isEmpty()) {

			long maxSize = 100 * 1024; // 100 KB
			//int maxWidth = 500; // Maximum allowed width
			//int maxHeight = 500; // Maximum allowed height

			// Validate file size
			if (profilePicture.getSize() > maxSize) {
				response.put("profilePicture", "Profile picture size should not exceed 100 KB.");
				return ResponseEntity.badRequest().body(response);
			}

			try {
				// Validate image dimensions
				BufferedImage bufferedImage = ImageIO.read(profilePicture.getInputStream());
				if (bufferedImage != null) {
					int width = bufferedImage.getWidth();
					int height = bufferedImage.getHeight();

					// Check if the image dimensions exceed the maximum allowed size
//					if (width > maxWidth || height > maxHeight) {
//						response.put("profilePicture", "Profile picture dimensions should not exceed " + maxWidth + "x"
//								+ maxHeight + " pixels.");
//						return ResponseEntity.badRequest().body(response);
//					}
				} else {
					response.put("profilePicture", "Invalid image format.");
					return ResponseEntity.badRequest().body(response);
				}

				 // Use the original filename
				//String fileName = profilePicture.getOriginalFilename();

				// Generate a filename using the employee's username
				                      String username = employee.getFull_name(); // Assuming employee has a username field
				                      
				                      // Generate a sanitized filename based on the username and the original file extension
				                      String originalFilename = profilePicture.getOriginalFilename();
				                      String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
				                      String fileName = username + fileExtension;

				                // Remove any special characters and spaces from the file name (if necessary)
				                      fileName = fileName.replaceAll("[^a-zA-Z0-9.]", ""); // Keeping only letters, numbers, and dots for the extension
				                      
				// Create the file path
				Path filePath = Paths.get(uploadDir, fileName).toAbsolutePath().normalize();

				// Save the file to the directory
				Files.write(filePath, profilePicture.getBytes());

				// Set the profile picture path in the employee entity // Store the full path in the database
				employee.setProfilePicturePath(filePath.toString()); // Store the absolute file path

				} catch (IOException e) {
				e.printStackTrace();
				response.put("profilePicture", "Profile picture upload failed.");
				return ResponseEntity.badRequest().body(response);
				}
				} else {
				response.put("profilePicture", "Please upload Profile picture");
				}


		// Validate Email Format and Domain
		String email = employee.getEmail();
		String emailRegex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
		String[] allowedDomains = { "gmail.com", "yahoo.com", "outlook.com", "bisag.in" };
		String domain = email != null ? email.substring(email.indexOf("@") + 1) : "";

		if (!email.matches(emailRegex) || !Arrays.asList(allowedDomains).contains(domain)) {
			response.put("email",
					"Please enter a valid email address from gmail.com, yahoo.com, outlook.com, or bisag.in.");
		}

		if (employee.getJobTitle() == null || employee.getJobTitle().isEmpty()) {
			response.put("jobTitle", "Please select an JobTitle");
		} else {
			// Assuming agency field in employee is now the ID from the dropdown
			Long jobTitleId = Long.valueOf(employee.getJobTitle());
			Optional<JobTitle> existingJobTitle = jobTitleRepository.findById(jobTitleId);

			if (existingJobTitle.isPresent()) {
				// Agency exists, proceed with saving

				// // Assuming JobTitle has a getName() method
				employee.setJobTitle(existingJobTitle.get().getName()); // Assuming JobTitle has a getName() method
			} else {
				response.put("jobTitle", "Selected JobTitle does not exist.");
			}
		}

		// Validate Contact Number
		String contactNumber = employee.getContact_number();
		if (contactNumber == null || !contactNumber.matches("^[6-9]\\d{9}$")) {
			response.put("contact_number",
					"Please enter a valid mobile number (10 digits only, starting with 6, 7, 8, or 9).");
		}

		// Validate Emergency Contact Number
		String emeContactNumber = employee.getEme_contact_number();
		if (emeContactNumber != null && !emeContactNumber.isEmpty()) {
			if (!emeContactNumber.matches("^[6-9]\\d{9}$")) {
				response.put("eme_contact_number",
						"Please enter a valid emergency contact number (10 digits only, starting with 6, 7, 8, or 9).");
			}
		}

		// Validate Date of Birth
		String dateOfBirthString = employee.getDateOfBirth();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try {
			LocalDate dateOfBirth = LocalDate.parse(dateOfBirthString, formatter);
			LocalDate startDate = LocalDate.of(1950, 1, 1);
			LocalDate endDate = LocalDate.of(2005, 12, 31);

			if (dateOfBirth.isBefore(startDate) || dateOfBirth.isAfter(endDate)) {
				response.put("dateOfBirth", "Date of Birth must be between 1950 and 2005.");
			}
		} catch (DateTimeParseException e) {
			response.put("dateOfBirth", "Invalid Date of Birth format.");
		}
		
		//validation for joining dates

		String joiningDate = employee.getJoiningDate();
		DateTimeFormatter formatterJoiningDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try {
			LocalDate dateOfBirth = LocalDate.parse(dateOfBirthString, formatter); // Re-parse DOB for later use
			LocalDate joiningDates = LocalDate.parse(joiningDate, formatter);
			LocalDate today = LocalDate.now();

			// Check if the joining date is a future date
			if (joiningDates.isAfter(today)) {
				response.put("joiningDate", "Date of Joining cannot be a future date.");
			}

			// Check if the joining date is at least 18 years after the date of birth
			LocalDate dateOfBirthPlus18Years = dateOfBirth.plusYears(18);
			if (joiningDates.isBefore(dateOfBirthPlus18Years)) {
				response.put("joiningDate", "Date of Joining must be at least 18 years after Date of Birth.");
			}

		} catch (DateTimeParseException e) {
			response.put("joiningDate", "Invalid Joining Date format.");
		}

		// Validate Aadhaar Number
		String aadhaarNumber = employee.getAadharNumber();
		if (aadhaarNumber == null || !aadhaarNumber.matches("^\\d{12}$")) {
			response.put("aadharNumber", "Please enter a valid Aadhaar number (12 digits only).");
		}

		// Validate Gender
		if (employee.getGender() == null || (!employee.getGender().equalsIgnoreCase("Male")
				&& !employee.getGender().equalsIgnoreCase("Female"))) {
			response.put("gender", "Please select a valid gender (Male or Female).");
		}

		// Validate Blood Group
		if (employee.getBloodGroup() == null || employee.getBloodGroup().isEmpty()) {
			response.put("bloodGroup", "Please select your blood group.");
		}

		// Validate Marital Status
		if (employee.getMaritalStatus() == null
				|| (!employee.getMaritalStatus().equals("Single") && !employee.getMaritalStatus().equals("Married"))) {
			response.put("maritalStatus", "Please select a valid marital status (Single or Married).");
		}

		// Validate Hometown
		if (employee.getHometown() == null || employee.getHometown().isEmpty()) {
			response.put("hometown", "Please select your hometown.");
		} else {
			// Assuming agency field in employee is now the ID from the dropdown
			Long homeTownId = Long.valueOf(employee.getHometown());
			Optional<Hometown> existingHomeTown = hometownRepository.findById(homeTownId);

			if (existingHomeTown.isPresent()) {
				// HomeTown exists, proceed with saving
				employee.setHometown(existingHomeTown.get().getName());// .getId().toString());
			} else {
				response.put("hometown", "Selected HomeTown does not exist.");
			}
		}

		// Validate Agency
		if (employee.getAgency() == null || employee.getAgency().isEmpty()) {
			response.put("agency", "Please select an agency.");
		} else {
			// Assuming agency field in employee is now the ID from the dropdown
			Long agencyId = Long.valueOf(employee.getAgency());
			Optional<Agency> existingAgency = agencyRepository.findById(agencyId);

			if (existingAgency.isPresent()) {
				// Agency exists, proceed with saving
				employee.setAgency(existingAgency.get().getName());// .get().getId().toString());
			} else {
				response.put("agency", "Selected agency does not exist.");
			}
		}

		// Validate Department
		if (employee.getDepartment() == null || employee.getDepartment().isEmpty()) {
			response.put("department", "Please select your department.");
		} else {
			// Assuming Department field in employee is now the ID from the dropdown
			Long departmentId = Long.valueOf(employee.getDepartment());
			Optional<Department> existingDepartment = departmentRepository.findById(departmentId);

			if (existingDepartment.isPresent()) {
				// HomeTown exists, proceed with saving
				employee.setDepartment(existingDepartment.get().getName());// .get().getId().toString());
			} else {
				response.put("department", "Selected Department does not exist.");
			}
		}

		// Validate Reporting Personnel
		if (employee.getReporting_per() == null || employee.getReporting_per().isEmpty()) {
			response.put("reporting_per", "Please select your manager.");
		} else {
			// Assuming Reporting personnel field in employee is now the ID from the
			// dropdown
			Long reportingPersonnelId = Long.valueOf(employee.getReporting_per());
			Optional<ReportingPersonnel> existingReportingPersonnel = reportingPersonnelRepository
					.findById(reportingPersonnelId);

			if (existingReportingPersonnel.isPresent()) {
				// Reporting Personnel exists, proceed with saving
				employee.setReporting_per(existingReportingPersonnel.get().getName());// .get().getId().toString());
			} else {
				response.put("reporting_per", "Selected RepotingPersonnel does not exist.");
			}
		}

		// Validate Address
		String address = employee.getCurrentAddress();
		String addressRegex = "^[\\w\\s,\\-\\/]+\\s*(?:[A-Za-z0-9]+)?(?:,\\s*[A-Za-z0-9]+)*\\s*(?:[0-9]+\\s*)?(?:[A-Z]{2}\\s*)?$";
		if (address == null || !address.matches(addressRegex)) {
			response.put("currentAddress", "Please enter a valid address.");
		}

		// Validate Password and Confirm Password
		String password = log.getPassword();
		String confirmPassword = verifyPassword;
		String passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$";

		if (!password.equals(confirmPassword)) {
			response.put("password", "Passwords do not match. Please ensure both fields have the same password.");
		} else if (!password.matches(passwordRegex)) {
			response.put("password",
					"Password must be at least 8 characters long, contain at least one digit and one special character.");
		}

		// Check for any validation errors collected in BindingResult
		if (result.hasErrors() || !response.isEmpty()) {
			if (result.hasErrors()) {
				result.getFieldErrors().forEach(error -> response.put(error.getField(), error.getDefaultMessage()));
			}
			return ResponseEntity.badRequest().body(response);
		}

		String username = CommonMethod.generateUsername(employee.getFull_name().toLowerCase(),
				employee.getContact_number());
		log.setUsername(username);
		log.setPassword(new BCryptPasswordEncoder().encode(log.getPassword()));
		log.setIp(getClientIp(request));
		// Save employee and login information to the database if no errors
		empRepo.save(employee);
		log.setEmployee(employee);
		logininformationRepository.save(log);

		// Add success message and redirect to success page

		// Prepare the success response
		Map<String, String> successResponse = new HashMap<>();
		successResponse.put("message", "Employee registered successfully");
		successResponse.put("username", username);
		successResponse.put("redirectUrl", "reg_success");

		return ResponseEntity.ok(successResponse);

	}

	private boolean isValidEmail(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@GetMapping("/reg_success")
	public String regSuccess(@RequestParam(value = "message", required = false) String message,
			@RequestParam(value = "username", required = false) String username, Model model) {
		// Add the success message and username to the model
		model.addAttribute("success", message);
		model.addAttribute("username", username);

		return "registration_success"; // Thymeleaf template for success page
	}

	
}
