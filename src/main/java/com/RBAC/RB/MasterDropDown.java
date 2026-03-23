package com.RBAC.RB;

import java.io.File;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.RBAC.DAO.AgencyService;
import com.RBAC.DAO.DepartmentService;
import com.RBAC.DAO.EmployeeRepoImpl;
import com.RBAC.DAO.EmployeeRepository;
import com.RBAC.DAO.HometownService;
import com.RBAC.DAO.JobTitleService;
import com.RBAC.DAO.LogininformationRepository;
import com.RBAC.DAO.ReportingPersonnelService;
import com.RBAC.Model.Agency;
import com.RBAC.Model.Department;
import com.RBAC.Model.Employee;
import com.RBAC.Model.Hometown;
import com.RBAC.Model.JobTitle;
import com.RBAC.Model.ReportingPersonnel;

@Controller
@RequestMapping("/admin")
public class MasterDropDown {

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
	private EmployeeRepoImpl empImpl;

	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private LogininformationRepository logininformationRepository;

	@GetMapping("/Dept")
	public String adminPageDept(Model model,@AuthenticationPrincipal User p) {
		model.addAttribute("dropdownItems", departmentService.getAllDepartment());
		model.addAttribute("newItem1", new Department());
		model.addAttribute("item", new Department());
		
		
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			model.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}
		return "DropDownDepartment";
		
	}

	@PostMapping("/adminDept/addItem")
	public String addItemDept(@ModelAttribute("newItem1") Department newItem) {
		departmentService.saveOrUpdateDepartment(newItem);
		return "redirect:/admin/Dept";
	}

	@PostMapping("/adminDept/updateItemDept")
	public String updateItemDept(@ModelAttribute("item") Department item) {
		departmentService.saveOrUpdateDepartment(item);
		return "redirect:/admin/Dept";
	}

	@GetMapping("/adminDept/deleteItem/{id}")
	public String deleteItemDept(@PathVariable Long id) {
		departmentService.deleteDepartment(id);
		return "redirect:/admin/Dept";
	}

	@GetMapping("/filterDept")
	public String filterByDateDepartment(@RequestParam("startDate") String startDateStr,
			@RequestParam("endDate") String endDateStr, Model model,@AuthenticationPrincipal User p) {
		LocalDateTime startDate = LocalDateTime.parse(startDateStr);
		LocalDateTime endDate = LocalDateTime.parse(endDateStr);
		model.addAttribute("dropdownItems", departmentService.getDepartmentsByDateRange(startDate, endDate));
		model.addAttribute("newItem1", new Department());
		model.addAttribute("item", new Department());
			
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			model.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}
		return "DropDownDepartment";
	}

	@GetMapping("/Agency")
	public String adminPageAgency(Model model,@AuthenticationPrincipal User p) {
		model.addAttribute("dropdownItems", agencyService.getAllAgency());
		model.addAttribute("newItem1", new Agency());
		model.addAttribute("item", new Agency());
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			model.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}
		return "DropDownAgency";
	}

	@PostMapping("/adminAgency/addItem")
	public String addItemAgency(@ModelAttribute("newItem1") Agency newItem) {
		agencyService.saveOrUpdateAgency(newItem);
		return "redirect:/admin/Agency";
	}

	@PostMapping("/adminAgency/updateItemAgency")
	public String updateItemAgency(@ModelAttribute("item") Agency item) {
		agencyService.saveOrUpdateAgency(item);
		return "redirect:/admin/Agency";
	}

	@GetMapping("/adminAgency/deleteItem/{id}")
	public String deleteItemAgency(@PathVariable Long id) {
		agencyService.deleteAgency(id);
		return "redirect:/admin/Agency";
	}

	@GetMapping("/filterAgency")
	public String filterByDateAgency(@RequestParam("startDate") String startDateStr,
			@RequestParam("endDate") String endDateStr, Model model,@AuthenticationPrincipal User p) {
		LocalDateTime startDate = LocalDateTime.parse(startDateStr);
		LocalDateTime endDate = LocalDateTime.parse(endDateStr);
		model.addAttribute("dropdownItems", agencyService.getAgencysByDateRange(startDate, endDate));
		model.addAttribute("newItem1", new Agency());
		model.addAttribute("item", new Agency());
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			model.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}
		return "DropDownAgency";
	}

	@GetMapping("/hometown")
	public String adminPageHometown(Model model,@AuthenticationPrincipal User p) {
		model.addAttribute("dropdownItems", hometownService.getAllHometown());
		model.addAttribute("newItem1", new Hometown());
		model.addAttribute("item", new Hometown());
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			model.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}
		return "DropDownHometown";
	}

	@PostMapping("/adminHometown/addItem")
	public String addItemHometown(@ModelAttribute("newItem1") Hometown newItem) {
		hometownService.saveOrUpdateHometown(newItem);
		return "redirect:/admin/hometown";
	}

	@PostMapping("/adminHometown/updateItemHometown")
	public String updateItemHometown(@ModelAttribute("item") Hometown item) {
		hometownService.saveOrUpdateHometown(item);
		return "redirect:/admin/hometown";
	}

	@GetMapping("/adminHometown/deleteItem/{id}")
	public String deleteItemHometown(@PathVariable Long id) {
		hometownService.deleteHometown(id);
		return "redirect:/admin/hometown";
	}

	@GetMapping("/filterHometown")
	public String filterByDateHometown(@RequestParam("startDate") String startDateStr,
			@RequestParam("endDate") String endDateStr, Model model,@AuthenticationPrincipal User p) {
		LocalDateTime startDate = LocalDateTime.parse(startDateStr);
		LocalDateTime endDate = LocalDateTime.parse(endDateStr);
		model.addAttribute("dropdownItems", hometownService.getHometownsByDateRange(startDate, endDate));
		model.addAttribute("newItem1", new Hometown());
		model.addAttribute("item", new Hometown());
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			model.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}
		return "DropDownHometown";
	}

	@GetMapping("/jobTitle")
	public String adminPageJobTitle(Model model,@AuthenticationPrincipal User p) {
		model.addAttribute("dropdownItems", jobTitleService.getAllJobTitle());
		model.addAttribute("newItem1", new JobTitle());
		model.addAttribute("item", new JobTitle());
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			model.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}
		return "DropDownJobTitle";
	}

	@PostMapping("/adminJobTitle/addItem")
	public String addItemJobTitle(@ModelAttribute("newItem1") JobTitle newItem) {
		jobTitleService.saveOrUpdateJobTitle(newItem);
		return "redirect:/admin/jobTitle";
	}

	@PostMapping("/adminJobTitle/updateItemJobTitle")
	public String updateItemJobTitle(@ModelAttribute("item") JobTitle item) {
		jobTitleService.saveOrUpdateJobTitle(item);
		return "redirect:/admin/jobTitle";
	}

	@GetMapping("/adminJobTitle/deleteItem/{id}")
	public String deleteItemJobTitle(@PathVariable Long id) {
		jobTitleService.deleteJobTitle(id);
		return "redirect:/admin/jobTitle";
	}

	@GetMapping("/filterJobTitle")
	public String filterByDateJobTitle(@RequestParam("startDate") String startDateStr,
			@RequestParam("endDate") String endDateStr, Model model,@AuthenticationPrincipal User p) {
		LocalDateTime startDate = LocalDateTime.parse(startDateStr);
		LocalDateTime endDate = LocalDateTime.parse(endDateStr);
		model.addAttribute("dropdownItems", jobTitleService.getJobTitleByDateRange(startDate, endDate));
		model.addAttribute("newItem1", new JobTitle());
		model.addAttribute("item", new JobTitle());
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			model.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}
		return "DropDownJobTitle";
	}

	@GetMapping("/reportingPersonnel")
	public String adminPageReportingPersonnel(Model model,@AuthenticationPrincipal User p) {
		model.addAttribute("dropdownItems", reportingPersonnelService.getAllReportingPersonnel());
		model.addAttribute("newItem1", new ReportingPersonnel());
		model.addAttribute("item", new ReportingPersonnel());
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			model.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}
		return "DropDownReportingPersonnel";
	}

	@PostMapping("/adminReportingPersonnel/addItem")
	public String addItemReportingPersonnel(@ModelAttribute("newItem1") ReportingPersonnel newItem) {
		reportingPersonnelService.saveOrUpdateReportingPersonnel(newItem);
		return "redirect:/admin/reportingPersonnel";
	}

	@PostMapping("/adminReportingPersonnel/updateItemReportingPersonnel")
	public String updateItemReportingPersonnel(@ModelAttribute("item") ReportingPersonnel item) {
		reportingPersonnelService.saveOrUpdateReportingPersonnel(item);
		return "redirect:/admin/reportingPersonnel";
	}

	@GetMapping("/adminReportingPersonnel/deleteItem/{id}")
	public String deleteItemReportingPersonnel(@PathVariable Long id) {
		reportingPersonnelService.deleteReportingPersonnel(id);
		return "redirect:/admin/reportingPersonnel";
	}

	@GetMapping("/filterReportingPersonnel")
	public String filterByDateReportingPersonnel(@RequestParam("startDate") String startDateStr,
			@RequestParam("endDate") String endDateStr, Model model,@AuthenticationPrincipal User p) {
		LocalDateTime startDate = LocalDateTime.parse(startDateStr);
		LocalDateTime endDate = LocalDateTime.parse(endDateStr);
		model.addAttribute("dropdownItems",
				reportingPersonnelService.getReportingPersonnelByDateRange(startDate, endDate));
		model.addAttribute("newItem1", new ReportingPersonnel());
		model.addAttribute("item", new ReportingPersonnel());
		String username = p.getUsername();
		Employee employee = empRepo.findAllByUsername(username);
		if (employee != null) {
			model.addAttribute("emp", employee);
			// Extract only the file name from the absolute path
			String profilePicturePath = employee.getProfilePicturePath();
			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
			model.addAttribute("profilePicturePath", "/uploads/" + fileName);
			System.out.println("p.getUsername():" + employee.getCurrentAddress());
		} else {
			model.addAttribute("error", "Employee not found");
		}
		return "DropDownReportingPersonnel";
	}

}
