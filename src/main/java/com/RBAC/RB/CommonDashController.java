package com.RBAC.RB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.RBAC.DAO.AgencyRepository;
import com.RBAC.DAO.AgencyService;
import com.RBAC.DAO.BillreimbursementRepository;
import com.RBAC.DAO.BillreimbursementService;
import com.RBAC.DAO.ClientVisitService;
import com.RBAC.DAO.DepartmentRepository;
import com.RBAC.DAO.DepartmentService;
import com.RBAC.DAO.EmployeeRepoImpl;
import com.RBAC.DAO.EmployeeRepository;
import com.RBAC.DAO.FileService;
import com.RBAC.DAO.HometownRepository;
import com.RBAC.DAO.HometownService;
import com.RBAC.DAO.JobTitleRepository;
import com.RBAC.DAO.JobTitleService;
import com.RBAC.DAO.LogininformationRepository;
import com.RBAC.DAO.PasswordService;
import com.RBAC.DAO.ProjectAddRepository;
import com.RBAC.DAO.ProjectAddService;
import com.RBAC.DAO.ProjectRepository;
import com.RBAC.DAO.ProjectService;
import com.RBAC.DAO.ReportingPersonnelRepository;
import com.RBAC.DAO.ReportingPersonnelService;
import com.RBAC.Model.Billreimbursement;
import com.RBAC.Model.ChartDataDTO;
import com.RBAC.Model.ClientVisit;
import com.RBAC.Model.Employee;
import com.RBAC.Model.Project;
import com.RBAC.Model.ProjectAdd;

@Controller
@RequestMapping(value = { "/admin", "/user", "/manager" })
public class CommonDashController {

	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private BillreimbursementRepository billreimbursementRepository;

	@Autowired
	private EmployeeRepoImpl empImpl;

	@Autowired
	private LogininformationRepository logininformationRepository;

	@Autowired
	private BillreimbursementService billreimbursementService;

	@Autowired
	private AgencyService agencyService;

	@Autowired
	private HometownService hometownService;

	@Autowired
	private AgencyRepository agencyRepository;

	@Autowired
	private HometownRepository hometownRepository;

	@Autowired
	private PasswordService passwordService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectAddRepository projectAddRepository;

	@Autowired
	private ProjectAddService projectAddService;

	@Autowired
	private ClientVisitService clientVisitService;

	@Value("${file.upload-dir}")
	private String uploadDir;

//////////////////////////////////////////////////////////////////////////////

	@GetMapping("/managerDash")
	public String managerDashboard(@AuthenticationPrincipal User user, Model model) {
		// Get the logged-in user's email
		String employeeEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		// Fetch employee by username using the method findAllByUsername
		Employee employee = empImpl.findAllByUsername(employeeEmail);

		// Get all the projects added by this employee
		/*
		 * List<Project> projects =
		 * projectRepository.findAllByEmployeeId(employee.getId());
		 */

		List<Project> projects = projectService.getProjectsByEmployeeOrderByIdDesc(employee.getId());

		// Add the form object and project list to the model
		model.addAttribute("project", new Project());
		model.addAttribute("projects", projects);

		String username = user.getUsername();
		Employee emp = empRepo.findAllByUsername(username);
		if (emp != null) {
			// Extract only the file name from the absolute path
			String profilePicturePath = emp.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			// System.out.println("p.getUsername():" + emp.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}

		return "Manager_dashboard";
	}

	//////////////////////////

	@GetMapping("/viewprojects")
	public String viewProjects(@AuthenticationPrincipal User user, Model model) {
		// Get the logged-in user's email
		String employeeEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		// Fetch employee by username using the method findAllByUsername
		Employee employee = empImpl.findAllByUsername(employeeEmail);

		// Fetch only the projects added by this employee
		List<Project> projects = projectRepository.findAllByEmployeeId(employee.getId());

		// Add the filtered project list to the model
		model.addAttribute("projects", projects);

		String userPhoto = user.getUsername();
		Employee emp = empRepo.findAllByUsername(userPhoto);
		if (emp != null) {
			// Extract only the file name from the absolute path
			String profilePicturePath = emp.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			// System.out.println("p.getUsername():" + emp.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}

		return "view_projects"; // This must match the template filename without the .html extension

	}

	@PostMapping("/update")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateProject(@RequestBody Project project) {
		try {
			// Find the existing project by ID
			Optional<Project> existingProjectOpt = projectRepository.findById(project.getId());

			if (existingProjectOpt.isPresent()) {
				Project existingProject = existingProjectOpt.get();
				// Update the project details
				existingProject.setProjectName(project.getProjectName());
				existingProject.setProjectDesc(project.getProjectDesc());
				existingProject.setClientName(project.getClientName());
				existingProject.setStartDate(project.getStartDate());

				// Save the updated project
				projectRepository.save(existingProject);

				// Return success response
				Map<String, Object> response = new HashMap<>();
				response.put("success", true);
				return ResponseEntity.ok(response);
			} else {
				// If the project is not found, return error response
				Map<String, Object> response = new HashMap<>();
				response.put("success", false);
				response.put("message", "Project not found");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Error updating project");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/addProject")
	public String addProject(@ModelAttribute Project project) {
		// Get the logged-in user's username
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		// Add the project and link it to the employee
		empImpl.addProjectToEmployeeByUsername(project, username);

		return "redirect:managerDash";
	}

	///////////////////////////////////////////////////////////////////////
	/* this handler for user side view project */

	@GetMapping("/viewProjectUser")
	public String viewProjectUser(@AuthenticationPrincipal User user, Model model) {
		String username = user.getUsername();
		Employee emp = empRepo.findAllByUsername(username);

		if (emp != null) {
			// Retrieve projects assigned to the employee using ProjectAdd table
			List<Project> projects = projectAddService.getProjectsByEmployeeId(emp.getId());
			model.addAttribute("projects", projects);
			model.addAttribute("empId", emp.getId());

			// For profile picture display
			String profilePicturePath = emp.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
		} else {
			model.addAttribute("error", "Employee not found");
		}

		return "view_project_user";
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * this handler for user side add client visit it is working to get the data in
	 * the table and grt the data in the modal
	 */

	@GetMapping("/addClientVisit/{projectId}/{employeeId}")
	public String showAddClientVisitPage(@PathVariable("projectId") Long projectId,
			@PathVariable("employeeId") Long employeeId, Model model) {

		// Fetch the employee based on the provided employeeId
		System.out.println("projectId " + projectId + "Employee Id" + employeeId);
		Employee emp = empRepo.findById(employeeId).orElse(null);
		List<ClientVisit> clientVisits = clientVisitService.getClientVisitsByProjectId(projectId);
		if (emp != null) {
			// Fetch the project based on the provided projectId
			Project project = projectService.findById(projectId);

			if (project != null) {
				// Add the project, employee, and a new ClientVisit object to the model
				model.addAttribute("project", project);
				model.addAttribute("employee", emp);
				model.addAttribute("empId", employeeId);
				model.addAttribute("clientVisit", new ClientVisit()); // New ClientVisit instance for the form
				model.addAttribute("clientData", clientVisits);

				// For profile picture display
				String profilePicturePath = emp.getProfilePicturePath();
				String fileName = (profilePicturePath != null && !profilePicturePath.isEmpty())
						? new File(profilePicturePath).getName()
						: "";
				model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			} else {
				// Handle case where the project is not found
				model.addAttribute("error", "Project not found");
			}
		} else {
			// Handle case where the employee is not found
			model.addAttribute("error", "Employee not found");
		}

		// Return the view name for the client visit form
		return "AddClientVisit";
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/* this Handler is used to show the mom of particular visitor Members */
	@GetMapping("/clientVisitManager/{projectId}")
	public String showAddClientVisitPageManager(@PathVariable("projectId") Long projectId, Model model,
			@AuthenticationPrincipal User user) {
		// Fetch the employee based on the provided employeeId
		System.out.println("projectId " + projectId + "Employee Id");
//		Employee emp = empRepo.findById(employeeId).orElse(null);
		List<ClientVisit> clientVisits = clientVisitService.getClientVisitsByProjectId(projectId);
		model.addAttribute("clientVisit", new ClientVisit()); // New ClientVisit instance for the form
		model.addAttribute("clientData", clientVisits);

		String username = user.getUsername();
		Employee emp = empRepo.findAllByUsername(username);
		if (emp != null) {
			// Extract only the file name from the absolute path
			String profilePicturePath = emp.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			// System.out.println("p.getUsername():" + emp.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}

		// Return the view name for the client visit form
		return "AddClientVisitManager";//
	}

	////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * this handler for user add client visit and it is working to to post the from
	 * the form
	 */

	@PostMapping("/addClientVisit")
	public String addClientVisit(@ModelAttribute("clientVisit") ClientVisit clientVisit,
			@RequestParam("momFile") MultipartFile momFile, @RequestParam("projectId") Long projectId,
			@RequestParam("employeeId") Long employeeId, RedirectAttributes redirectAttributes) {
		try {
			// Handle file upload if the file is not empty
			if (!momFile.isEmpty()) {
				String momFilePath = fileService.saveFile(momFile); // Save the file and get the file path
				clientVisit.setMomFilePath(momFilePath); // Set the file path in the clientVisit entity
			}

			// Retrieve Project and Employee entities from their IDs
			Project project = projectService.findById(projectId);
			Employee employee = empRepo.findById(employeeId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + employeeId));

			// Set the Project and Employee entities in the ClientVisit
			clientVisit.setProject(project);
			clientVisit.setEmployee(employee);

			// Save the clientVisit to the database
			clientVisitService.saveClientVisit(clientVisit);

			// Add success message to RedirectAttributes
			redirectAttributes.addFlashAttribute("message", "Client visit added successfully!");

			// Redirect back to the form with both projectId and employeeId
			return "redirect:/user/addClientVisit/" + projectId + "/" + employeeId;
		} catch (Exception e) {
			// Handle errors and add an error message
			redirectAttributes.addFlashAttribute("error", "Error saving client visit: " + e.getMessage());
			return "redirect:/user/addClientVisit/" + projectId + "/" + employeeId;
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * this handler for user side add client visit this handler is used to post the
	 * data from the modal after updating
	 */

	@PostMapping("/updateClientVisit")
	public String updateClientVisit(@ModelAttribute ClientVisit clientVisit,
			@RequestParam("momFile") MultipartFile momFile, @RequestParam("pId") Long projectId,
			@RequestParam("eId") Long employeeId, BindingResult result, RedirectAttributes redirectAttributes) {

		// Validate and process the form submission
		System.out.println("Eid : " + employeeId + " PId : " + projectId + "momFilePathOld ");

		// Check for validation errors
		if (result.hasErrors()) {
			// If validation fails, redirect back with client visit data
			redirectAttributes.addFlashAttribute("error", clientVisit);
			return "redirect:/user/addClientVisit/" + projectId + "/" + employeeId;
		}
		Long id = clientVisit.getId();
		// Retrieve the existing ClientVisit from the database
		Optional<ClientVisit> existingClientVisit = clientVisitService.findById(id);
		System.out.println("?????????" + existingClientVisit);
		// Retrieve the Project and Employee entities
		Project project = projectService.findById(projectId);
		Employee employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + employeeId));

		// Set the Project and Employee in the ClientVisit entity
		clientVisit.setProject(project);
		clientVisit.setEmployee(employee);

		// Handle file upload if a new file is provided
		if (!momFile.isEmpty()) {
			String momFilePaths = fileService.saveFile(momFile); // Save the file and get the file path
			clientVisit.setMomFilePath(momFilePaths); // Set the file path in the clientVisit entity
		} else {
			// If no new file is uploaded, retain the existing file path
//	        clientVisit.setMomFilePath(existingClientVisit.getMomFilePath());
		}
//		if (!momFilePath.isEmpty()) {
//			String momFilePaths = fileService.saveFile(momFilePath); // Save the file and get the file path
//			clientVisit.setMomFilePath(momFilePaths); // Set the file path in the clientVisit entity
//		}

		// Save the updated client visit
		clientVisitService.saveClientVisit(clientVisit);

		// Redirect to the client visit list with success message
		redirectAttributes.addFlashAttribute("message", "Client Visit updated successfully.");
		return "redirect:/user/addClientVisit/" + projectId + "/" + employeeId;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* this handler used view to pdf user side */

	@GetMapping("/pdfView/{id}")
	public ResponseEntity<Resource> getSubmittedPdfPageView(@PathVariable Long id, Principal principal)
			throws IOException {
		Optional<ClientVisit> optionalClientVisit = clientVisitService.findById(id);
		if (optionalClientVisit.isPresent()) {
			ClientVisit clientVisit = optionalClientVisit.get();
			String pdfPath = clientVisit.getMomFilePath();
			if (pdfPath != null) {
				File pdfFile = new File(pdfPath);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDisposition(ContentDisposition.inline().filename(pdfFile.getName()).build());
				Resource resource = new FileSystemResource(pdfFile);
				return new ResponseEntity<>(resource, headers, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/////////////////////////////////////////////////////////////////////////////////
	/* this handler for Admin dashboard */
	@GetMapping("/commonDash")
	public String commonDashboard(@AuthenticationPrincipal User user, Model m) {
		String username = user.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			m.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			m.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			m.addAttribute("error", "Employee not found");
		}

		List<Employee> employees = empImpl.findAllEmployeesOrderByCreated_atDesc();
		m.addAttribute("allEmployee", employees);

		Map<String, Integer> employeeStats = empImpl.getEmployeeStatistics();
		m.addAttribute("totalEmp", employeeStats.get("total_employees"));
		m.addAttribute("approvedEmp", employeeStats.get("approved_employees"));
		m.addAttribute("pendingEmp", employeeStats.get("pending_employees"));

		long totalBillCount = billreimbursementService.countAllBills();
		m.addAttribute("totalBillCount", totalBillCount);

		// Fetch distinct years for dropdown
		List<Integer> years = billreimbursementService.getDistinctYears();
		m.addAttribute("years", years);

		return "admin_dashboard";
	}
	////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * this handler used to view detail of registered employee inside the form in
	 * admin dashboard
	 */
	@GetMapping("registeredemployee")
	public String getRegisteredEmployee(@RequestParam("id") Long id, Model model) {
		// Fetch the employee by ID

		Employee employee = empRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));

		// Add the employee to the model
		model.addAttribute("employee", employee);

		// Return the view name
		return "registeredemployee";
	}
	////////////////////////////////////////////////////////////////////////////////////////////

	@GetMapping("/emp_Dash")
	public String emp_Dashboard(@AuthenticationPrincipal User p, Model m) {
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			m.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			m.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			m.addAttribute("error", "Employee not found");
		}
		return "emp_dashboard";
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////

	@GetMapping("/reportingManager")
	public String reportingManager(@AuthenticationPrincipal User user, Model m) {
		String username = user.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			m.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			m.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			m.addAttribute("error", "Employee not found");
		}

		List<Employee> manager = empImpl.getAllManagers();
		m.addAttribute("manager", manager);

		return "ReportingManager";
	}

	@GetMapping("/employeeData")
	@ResponseBody
	public List<Map<String, Object>> searchEmployees(@RequestParam String search) {
		return empImpl.searchEmployeesByFullName(search);
	}

	@PostMapping("/addEmployeeToProjectX")
	public ResponseEntity<String> addEmployeeToProjectX(@RequestBody Map<String, String> requestData) {
		try {
			Long projectId = Long.parseLong(requestData.get("projectId"));
			String employeeName = requestData.get("employeeName");

			Project project = projectRepository.findById(projectId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));

			ProjectAdd projectAdd = new ProjectAdd();
			projectAdd.setProjectId(project);
//			projectAdd.setEmployeeName(employeeName);

			projectAddService.addEmployeeToProject(projectAdd);

			return ResponseEntity.ok("Employee added successfully");
		} catch (Exception e) {
			// Handle the exception and return an appropriate error response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add employee");
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@PostMapping("/addEmployeeToProject")
	public ResponseEntity<Map<String, String>> addEmployeeToProject(@RequestBody Map<String, String> requestData) {
		Map<String, String> response = new HashMap<>();
		try {
			Long projectId = Long.parseLong(requestData.get("projectId"));
//			String employeeName = requestData.get("employeeName");
			Long employeeId = Long.parseLong(requestData.get("employeeId"));

			// Retrieve project and employee entities
			Project project = projectRepository.findById(projectId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));

			Employee employee = empRepo.findById(employeeId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));

			// Check if the employee is already added to the project
			if (projectAddService.isEmployeeAlreadyAddedToProject(projectId, employeeId)) {
				response.put("message", "Employee already added to this project");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			// Create and set up the ProjectAdd entity
			ProjectAdd projectAdd = new ProjectAdd();
			projectAdd.setProjectId(project);
//			projectAdd.setEmployeeName(employeeName); // Use the name provided
			projectAdd.setEmployeeId(employee); // Make sure this is the correct field in your entity

			// Save the ProjectAdd entity
			projectAddService.addEmployeeToProject(projectAdd);

			response.put("message", "Employee added successfully");
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			response.put("message", "Invalid input: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} catch (Exception e) {
			e.printStackTrace(); // Log the error
			response.put("message", "Failed to add employee: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Endpoint to fetch employee details by project
	////////////////////////////////////////////////////////////////////////////////////////////////////////// IDredirect:/user/addClientVisit/"
	////////////////////////////////////////////////////////////////////////////////////////////////////////// +
	////////////////////////////////////////////////////////////////////////////////////////////////////////// projectId
	////////////////////////////////////////////////////////////////////////////////////////////////////////// +
	////////////////////////////////////////////////////////////////////////////////////////////////////////// "/"
	////////////////////////////////////////////////////////////////////////////////////////////////////////// +
	////////////////////////////////////////////////////////////////////////////////////////////////////////// employeeId
	@GetMapping("/employeesByProject/{projectId}")
	public ResponseEntity<List<Map<String, Object>>> getEmployeesByProject(@PathVariable Long projectId) {
		List<Map<String, Object>> employees = projectAddService.getEmployeeIdsAndNamesByProjectId(projectId);
		return ResponseEntity.ok(employees);
	}

	// Remove an employee from a project
	@DeleteMapping("/removeEmployeeFromProject")
	public ResponseEntity<String> removeEmployeeFromProject(@RequestParam Long projectId,
			@RequestParam Long employeeId) {
		try {
			projectAddService.removeEmployeeFromProject(projectId, employeeId);
			return ResponseEntity.ok("Employee removed successfully");
		} catch (Exception e) {
			e.printStackTrace(); // Log the error
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to remove employee: " + e.getMessage());
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////////////
	/* This handler for user side new cab bill */
	@GetMapping("/new_cabBill")
	public String showNewCabBillForm(@AuthenticationPrincipal User p, Model model) {
		// Add the empty Billreimbursement object for the form
		model.addAttribute("billReimbursement", new Billreimbursement());

		// Get the current employee's applied bills
		String username = p.getUsername();
		Employee currentEmployee = empRepo.findAllByUsername(username);

		if (currentEmployee != null) {
			model.addAttribute("emp", currentEmployee);
			// Extract only the file name from the absolute path
			String profilePicturePath = currentEmployee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + currentEmployee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}

		if (currentEmployee != null) {
			List<Billreimbursement> employeeBills = billreimbursementRepository.findByEmployee(currentEmployee);
			model.addAttribute("employeeBills", employeeBills);

			Map<String, Object> summary = getSummary(currentEmployee);

			model.addAttribute("approvedCount", summary.get("approvedCount"));
			model.addAttribute("pendingCount", summary.get("pendingCount"));
			model.addAttribute("totalPendingAmount", summary.get("totalPendingAmount"));
			model.addAttribute("totalBillAmount", summary.get("totalBillAmount"));
		} else {
			model.addAttribute("employeeBills", new ArrayList<>());
			model.addAttribute("approvedCount", 0);
			model.addAttribute("pendingCount", 0);
			model.addAttribute("totalPendingAmount", "0.00");
			model.addAttribute("totalBillAmount", "0.00");
		}

		return "new_cab_bill";
	}

	public Map<String, Object> getSummary(Employee employee) {
		Object[] summary = billreimbursementRepository.findSummary(employee);

		int approvedCount = summary[0] != null ? ((Long) summary[0]).intValue() : 0;
		int pendingCount = summary[1] != null ? ((Long) summary[1]).intValue() : 0;
		double totalPendingAmount = summary[2] != null ? ((Double) summary[2]).doubleValue() : 0.0;
		double totalBillAmount = summary[3] != null ? ((Double) summary[3]).doubleValue() : 0.0;

		DecimalFormat df = new DecimalFormat("#0.00");

		Map<String, Object> formattedSummary = new HashMap<>();
		formattedSummary.put("approvedCount", approvedCount);
		formattedSummary.put("pendingCount", pendingCount);
		formattedSummary.put("totalPendingAmount", df.format(totalPendingAmount));
		formattedSummary.put("totalBillAmount", df.format(totalBillAmount));

		return formattedSummary;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* This handler for user side new cab bill this is post the form data */
	@PostMapping("/submit_billreimbursement")
	public ResponseEntity<Map<String, String>> submitBillReimbursement(Billreimbursement billReimbursement,
			@Validated @AuthenticationPrincipal User p, @RequestParam("billFile") MultipartFile billFile,
			RedirectAttributes redirectAttributes, BindingResult result) {

		Map<String, String> response = new HashMap<>();

		// Fetching the logged-in employee
		String username = p.getUsername();
		Employee currentEmployee = empRepo.findAllByUsername(username);

		if (currentEmployee != null) {

			// Backend validation for Traveling From
			if (billReimbursement.getTravelingFrom() == null || billReimbursement.getTravelingFrom().isEmpty()) {
				response.put("travelingFrom", "Traveling From field is required.");
			}

			// Backend validation for Traveling To
			if (billReimbursement.getTravelingTo() == null || billReimbursement.getTravelingTo().isEmpty()) {
				response.put("travelingTo", "Traveling To field is required.");
			}

			// Backend validation for Project name (should contain only letters and spaces)
			if (billReimbursement.getProject() == null || !billReimbursement.getProject().matches("^[A-Za-z\\s]+$")) {
				response.put("project", "Please enter a valid project name (letters and spaces only).");
			}

			// Backend validation for Date of Journey (should be within 30 days from today)
			LocalDate currentDate = LocalDate.now();
			LocalDate minDate = currentDate.minusDays(90);
			LocalDate fromDate;
			try {
				fromDate = LocalDate.parse(billReimbursement.getFromDate());
				if (fromDate.isBefore(minDate) || fromDate.isAfter(currentDate)) {
					response.put("fromDate", "Date of Journey should be within 30 days from today.");
				}
			} catch (DateTimeParseException e) {
				response.put("fromDate", "Invalid Date of Journey format.");
			}

			// Backend validation for Whom to Visit (only letters and spaces)
			if (billReimbursement.getWhomToVisit() == null
					|| !billReimbursement.getWhomToVisit().matches("^[A-Za-z\\s]+$")) {
				response.put("whomToVisit", "Please enter a valid name (letters and spaces only).");
			}

			// Backend validation for Bill Amount (only numbers and decimals)
			String billAmountStr = String.valueOf(billReimbursement.getBillAmount());
			Double billAmount = null;

			try {
				// Try to parse the billAmount string to a Double
				billAmount = Double.parseDouble(billAmountStr);

				// Check if it's positive
				if (billAmount <= 0) {
					response.put("billAmount", "Please enter a valid bill amount (positive numbers only).");
				} else {
					// Check if it is a valid number (integer or decimal with up to 2 decimals)
					if (!billAmountStr.matches("\\d+(\\.\\d{1,2})?")) {
						response.put("billAmount",
								"Bill amount must be a valid number (integer or decimal, up to 2 decimal places).");
					}
				}

			} catch (NumberFormatException e) {
				// Handle invalid number format (e.g., strings, special characters)
				response.put("billAmount", "Bill amount must be a valid number (no letters or special characters).");
			}

			// Backend validation for Bill File (PDF only and size limit)
			if (!billFile.isEmpty()) {
				String fileName = billFile.getOriginalFilename();
				if (fileName != null && !fileName.endsWith(".pdf")) {
					response.put("billFile", "Only PDF files are allowed.");
				}
				if (billFile.getSize() > 200 * 1024) { // 200 KB limit
					response.put("billFile", "File size must be less than 200 KB.");
				}
			} else {
				response.put("billFile", "Please upload a bill in PDF format.");
			}

			// Check for any validation errors collected in BindingResult
			if (result.hasErrors() || !response.isEmpty()) {
				if (result.hasErrors()) {
					result.getFieldErrors().forEach(error -> response.put(error.getField(), error.getDefaultMessage()));
				}
				return ResponseEntity.badRequest().body(response);
			}

			// Save the file and generate a file path
			String filePath = fileService.saveFile(billFile);
			billReimbursement.setFilePath(filePath);

			// Set employee and status
			billReimbursement.setEmployee(currentEmployee);
			billReimbursement.setStatus("Pending");

			// Save the bill reimbursement object
			billreimbursementRepository.save(billReimbursement);

			// Prepare the success response
			Map<String, String> successResponse = new HashMap<>();
			successResponse.put("redirectUrl", "new_cabBill");

			return ResponseEntity.ok(successResponse);
		}

		// If employee is not found
		response.put("employee", "Logged-in employee not found.");
		return ResponseEntity.badRequest().body(response);
	}

	/////////////////////////

	@PostMapping("/update_bill")
	public String updateBillReimbursement(@ModelAttribute("billReimbursement") Billreimbursement billReimbursement,
			@RequestParam(value = "billFile", required = false) MultipartFile billFile, Model model) {
		Billreimbursement existingBill = billreimbursementService.findById(billReimbursement.getId()).orElse(null);

		if (existingBill != null) {
			existingBill.setTravelingFrom(billReimbursement.getTravelingFrom());
			existingBill.setTravelingTo(billReimbursement.getTravelingTo());
			existingBill.setProject(billReimbursement.getProject());
			existingBill.setFromDate(billReimbursement.getFromDate());
			existingBill.setWhomToVisit(billReimbursement.getWhomToVisit());
			existingBill.setBillAmount(billReimbursement.getBillAmount());
			existingBill.setStatus(billReimbursement.getStatus());

			if (billFile != null && !billFile.isEmpty()) {
				try {
					String filePath = fileService.saveFile(billFile);
					existingBill.setFilePath(filePath);
				} catch (RuntimeException e) {
					e.printStackTrace();
					model.addAttribute("error", "Failed to upload file. Please try again.");
					return "redirect:new_cabBill";
				}
			}

			billreimbursementService.saveBillReimbursement(existingBill);
			model.addAttribute("message", "Bill reimbursement updated successfully.");
		}

		return "redirect:new_cabBill";
	}

	@GetMapping("/delete_bill/{id}")
	public String deleteBillReimbursement(@PathVariable("id") Long id, Model model) {
		try {
			billreimbursementService.deleteBillReimbursement(id);
			model.addAttribute("message", "Bill reimbursement deleted successfully.");
			return "redirect:/user/new_cabBill";
		} catch (Exception e) {
			model.addAttribute("error", "An error occurred while deleting the bill reimbursement.");
			return "redirect:new_cabBill";
		}
	}

	@GetMapping("/billSubmitted")
	public String showSubmittedBills(Model model, @AuthenticationPrincipal User p) {

		List<Billreimbursement> allBills = billreimbursementService.getAllBills();
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		String profilePicturePaths = "";
		if (employee != null) {
			model.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			profilePicturePaths = "/uploads/" + fileName;
			model.addAttribute("profilePicturePath", profilePicturePaths);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}

		if (allBills == null || allBills.isEmpty()) {
			/// Set all attributes to null or default values
			model.addAttribute("empDetails", null);
			model.addAttribute("pendingBillCounts", 0);
			model.addAttribute("approvedEmployeeCount", 0);
			model.addAttribute("pendingEmployeeCount", 0);
			model.addAttribute("totalPendingAmount", 0);
			model.addAttribute("totalApprovedAmount", 0);
			model.addAttribute("emp", null);
			// model.addAttribute("error", "No bills found."); // Added error message

			return "BillSubmitted";
		}

		// Group by employee details and count the number of bills
		Map<Employee, Long> employeeBillCounts = billreimbursementService.getEmployeeBillCounts();
		Map<Employee, Long> pendingBillCounts = billreimbursementService.getPendingBillCounts();

		List<Object[]> stats = billreimbursementService.getBillReimbursementStats();
		Object[] stat = stats.get(0);

		long approvedEmployeeCount = ((Number) stat[0]).longValue();
		long pendingEmployeeCount = ((Number) stat[1]).longValue();
		Double totalPendingAmount = (Double) stat[2];
		Double totalApprovedAmount = (Double) stat[3];

		// Handle null cases for sums
		totalPendingAmount = (totalPendingAmount != null) ? totalPendingAmount : 0.0;
		totalApprovedAmount = (totalApprovedAmount != null) ? totalApprovedAmount : 0.0;

		model.addAttribute("empDetails", employeeBillCounts);
		model.addAttribute("pendingBillCounts", pendingBillCounts);
		model.addAttribute("approvedEmployeeCount", approvedEmployeeCount);
		model.addAttribute("pendingEmployeeCount", pendingEmployeeCount);
		model.addAttribute("totalPendingAmount", totalPendingAmount);
		model.addAttribute("totalApprovedAmount", totalApprovedAmount);

		return "BillSubmitted"; // Thymeleaf template name
	}

//////change password handler///////////////////////
	@PostMapping("changePassword")
	public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
			RedirectAttributes redirectAttributes) {

		// Get the logged-in user's username
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		// Change the password for the authenticated user
		String result = passwordService.changePassword(username, currentPassword, newPassword);

		// Determine the user's role for redirection purposes
		String redirectUrl = "redirect:/";
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MANAGER"))) {
			redirectUrl = "redirect:/manager/managerDash";
		} else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			redirectUrl = "redirect:/admin/commonDash";
		} else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
			redirectUrl = "redirect:/user/emp_Dash";
		}

		// Store appropriate messages in the redirect attributes based on the result
		if (result.equals("Your password has been changed successfully.")) {
			redirectAttributes.addFlashAttribute("message", result);
		} else if (result.equals("New password cannot be the same as the current password.")
				|| result.equals("Current password is incorrect.") || result.equals("User not found")) {
			redirectAttributes.addFlashAttribute("error", result);
			return redirectUrl; // Redirect to the user's specific dashboard with error
		} else {
			redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
			return redirectUrl; // Redirect to the user's specific dashboard with error
		}

		return redirectUrl; // Redirect to the user's specific dashboard on success
	}

	// for pdf
	@GetMapping("/pdfsubmitted/{id}")
	public ResponseEntity<Resource> getSubmittedPdfPage(@PathVariable Long id, Principal principal) throws IOException {
		Optional<Billreimbursement> optionalBillReimbursement = billreimbursementService.findById(id);
		if (optionalBillReimbursement.isPresent()) {
			Billreimbursement billReimbursement = optionalBillReimbursement.get();
			String pdfPath = billReimbursement.getFilePath();
			if (pdfPath != null) {
				File pdfFile = new File(pdfPath);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDisposition(ContentDisposition.inline().filename(pdfFile.getName()).build());
				Resource resource = new FileSystemResource(pdfFile);
				return new ResponseEntity<>(resource, headers, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////
	@GetMapping("/submittedbillstatus/{employeeId}")
	public String getSubmittedBillStatus(@PathVariable("employeeId") Long employeeId, Model model,
			@AuthenticationPrincipal User p) {

		// Fetch the employee details using the employeeId
		Employee employeeById = empImpl.getEmployeeById(employeeId);

		// Fetch bills using the employee ID
		List<Billreimbursement> bills = billreimbursementService.getBillsByEmployeeId(employeeId);

		if (bills.isEmpty()) {
			model.addAttribute("message", "No bills found for the specified employee ID.");
		} else {
			model.addAttribute("bills", bills);
		}

		String username = p.getUsername();
		Employee employeeByUsername = empRepo.findAllByUsername(username);

		if (employeeByUsername != null) {
			model.addAttribute("emp", employeeByUsername);
			// Extract only the file name from the absolute path
			String profilePicturePath = employeeByUsername.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employeeByUsername.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}

		// Adding the employee retrieved by ID to the model
		model.addAttribute("employee", employeeById);

		return "submittedbillstatus";
	}

	@PostMapping("/bill/approve/{id}")
	public String approveBill(@PathVariable("id") Long billId) {
		billreimbursementService.updateBillStatus(billId, "Approved");
		return "redirect:/admin/submittedbillstatus/" + billreimbursementService.findEmployeeIdByBillId(billId);
	}

//	@PostMapping("/submittedbillstatus/bill/reject/{id}")
//	public ResponseEntity<?> rejectBill(@PathVariable("id") Long billId, @RequestParam("remarks") String remarks) {
//		System.out.println("Rejected");
//		try {
//			billreimbursementService.updateBillStatusWithRemarks(billId, "Rejected", remarks);
//			return ResponseEntity.ok().build();
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error rejecting bill");
//		}
//	}

	@PostMapping("/updateBillStatusApproved")
	@ResponseBody
	public ResponseEntity<String> updateBillStatus(@RequestParam Long billId, @RequestParam String remarks,
			@RequestParam String status) {
		Optional<Billreimbursement> billOpt = billreimbursementRepository.findById(billId);

		if (billOpt.isPresent()) {
			Billreimbursement bill = billOpt.get();
			bill.setRemarks(remarks);
			bill.setStatus(status);
			billreimbursementRepository.save(bill);
			return ResponseEntity.ok("Bill updated successfully");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bill not found");
		}
	}

	@GetMapping("/getBillRemarks/{billId}")
	public ResponseEntity<String> getBillRemarks(@PathVariable Long billId) {
		try {
			String remarks = billreimbursementService.getRemarksByBillId(billId);
			return ResponseEntity.ok(remarks);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error fetching remarks: " + e.getMessage());
		}
	}

	@GetMapping("/chart/data")
	@ResponseBody
	public List<ChartDataDTO> getChartData(@RequestParam("year") int year) {
		System.out.println("Year received: " + year); // Debugging line
		return billreimbursementService.getTotalBillAmountByMonth(year);
	}

	@GetMapping("/downloadZip")
	public ResponseEntity<Resource> downloadZip(@RequestParam("monthYear") String monthYear,
			@RequestParam("employeeId") Long employeeId) {
		try {
			List<Billreimbursement> bills = billreimbursementService.getBillsForMonthAndYearByEmployee(employeeId,
					monthYear);
			File zipFile = billreimbursementService.mergePdfsAndCreateZip(bills);

			InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + zipFile.getName())
					.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(zipFile.length()).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	////////////////////////////////////

//	 @GetMapping("/profile")
//	    public String showProfile(
//	            @AuthenticationPrincipal User user,
//	            Model model,
//	            HttpServletRequest request) {
//
//	        Employee emp = empRepo.findAllByUsername(user.getUsername());
//	        model.addAttribute("emp", emp);
//	     // Extract only the file name from the absolute path
//	     			String profilePicturePath = emp.getProfilePicturePath();
//	     			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
//	     			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
//
//	        String rolePrefix = request.getRequestURI().split("/")[1];
//	        model.addAttribute("role", rolePrefix);
//
//	        return "profile";
//	    }

	@GetMapping("/profile")
	public String showProfile(@AuthenticationPrincipal User user, Model model, HttpServletRequest request) {

		Employee emp = empRepo.findAllByUsername(user.getUsername());
		model.addAttribute("emp", emp);

		model.addAttribute("dropdownItemsAgency", agencyService.getAllAgency());
		model.addAttribute("dropdownItemsHometown", hometownService.getAllHometown());

		// Profile image
		String profilePicturePath = emp.getProfilePicturePath();
		String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
		model.addAttribute("profilePicturePath", "/uploads/" + fileName);

		String rolePrefix = request.getRequestURI().split("/")[1];
		model.addAttribute("role", rolePrefix);

		return "profile";
	}

	@PostMapping("/profileupdated")
	public String updateProfile(@ModelAttribute("emp") Employee formEmp, RedirectAttributes ra) {

		Employee emp = empRepo.findById(formEmp.getId()).orElseThrow(() -> new RuntimeException("Employee not found"));

		emp.setFull_name(formEmp.getFull_name());
		emp.setContact_number(formEmp.getContact_number());
		emp.setEmail(formEmp.getEmail());
		emp.setDateOfBirth(formEmp.getDateOfBirth());
		emp.setJoiningDate(formEmp.getJoiningDate());
		emp.setCurrentAddress(formEmp.getCurrentAddress());
		emp.setAgency(formEmp.getAgency());
		emp.setHometown(formEmp.getHometown());

		empRepo.save(emp);

		ra.addFlashAttribute("success", "Profile updated successfully");
		return "redirect:profile";
	}

}

/*
 * @GetMapping("/viewprojects") public String viewProjects(Model model) {
 * List<Project> projects = projectService.getAllProjects();
 * model.addAttribute("projects", projects); return "view_projects"; // This
 * must match the template filename without the .html extension }
 */

/////////////////////////
/*
 * @PostMapping("/updateClientVisit") public String
 * updateClientVisit(@ModelAttribute ClientVisit clientVisit,
 *
 * @RequestParam("momFile") MultipartFile momFile, @RequestParam("eId") Long
 * employeeId,
 *
 * @RequestParam("pId") Long projectId,// Add this // employeeId BindingResult
 * result, RedirectAttributes redirectAttributes ) { // Validate and process the
 * form submission System.out.println("Eid : "+employeeId +"PId : "+projectId);
 * if (result.hasErrors()) { // If validation fails, stay on the same page and
 * show errors redirectAttributes.addFlashAttribute("clientVisit", clientVisit);
 * return "redirect:/user/addClientVisit/" + projectId + "/" + employeeId;//
 * Ensure this is the correct redirect after errors }
 *
 * // Retrieve the Employee entity using the employeeId Employee employee =
 * empRepo.findById(employeeId) .orElseThrow(() -> new
 * IllegalArgumentException("Invalid employee ID: " + employeeId));
 * clientVisit.setEmployee(employee); // Set the employee in the clientVisit
 *
 * // Handle the file upload if a new file is provided if (!momFile.isEmpty()) {
 * String fileName = momFile.getOriginalFilename(); try {
 *
 * if (!momFile.isEmpty()) { String momFilePath = fileService.saveFile(momFile);
 * // Save the file and get the file path
 * clientVisit.setMomFilePath(momFilePath); // Set the file path in the
 * clientVisit entity } } return "redirect:/user/addClientVisit/" + projectId +
 * "/" + employeeId;
 *
 * catch (IOException e) { e.printStackTrace();
 * redirectAttributes.addFlashAttribute("error",
 * "Error while uploading the file."); return "redirect:/user/addClientVisit/" +
 * projectId + "/" + employeeId; // Show the same page with an error message } }
 *
 * // Save the updated client visit entity to the database
 * clientVisitService.saveClientVisit(clientVisit);
 *
 * // Redirect to the client visit list page after successful save
 * redirectAttributes.addFlashAttribute("successMessage",
 * "Client Visit updated successfully."); return
 * "redirect:/user/addClientVisit/" + projectId + "/" + employeeId; }
 */

////////////////////////
