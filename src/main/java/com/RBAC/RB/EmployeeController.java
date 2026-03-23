package com.RBAC.RB;

import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.RBAC.DAO.EmployeeRepoImpl;
import com.RBAC.DAO.EmployeeRepository;
import com.RBAC.DAO.HexatoAsciiDAO;
import com.RBAC.DAO.HexatoAsciiDAOImpl;
import com.RBAC.DAO.LogininformationRepository;
import com.RBAC.Model.Employee;
import com.RBAC.Model.Logininformation;

@Controller
@RequestMapping("/admin")
public class EmployeeController {
	@Autowired
	private EmployeeRepoImpl empImpl;

	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private LogininformationRepository logininformationRepository;

	HexatoAsciiDAO hex_2_ascii = new HexatoAsciiDAOImpl();

	@GetMapping("/reg_employee")
	public String reg_employee(@AuthenticationPrincipal User p, Model model) {

		Map<String, Integer> employeeStats = empImpl.getEmployeeStatistics();
		model.addAttribute("totalEmp", employeeStats.get("total_employees"));
		model.addAttribute("approvedEmp", employeeStats.get("approved_employees"));
		model.addAttribute("pendingEmp", employeeStats.get("pending_employees"));
		model.addAttribute("rejectedEmp", employeeStats.get("rejected_employees"));
		//
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
                                    
		
		return "employee";
	}

	@RequestMapping(value = "/new_empl_list", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray getAllEmployees(HttpSession session)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Employee> emplist = empRepo.findAll();
		JSONArray ja = new JSONArray();
		String pass = "BISAGBISAG";
		Cipher c = hex_2_ascii.EncryptionSHA256Algo(pass);

		for (Employee emp : emplist) {
			JSONObject jo = new JSONObject(); // Create a new JSONObject instance for each employee
			jo.put("id", new String(Base64.encodeBase64(c.doFinal(emp.getId().toString().getBytes()))));
			jo.put("first_name", emp.getFull_name());
			jo.put("contact_number", emp.getContact_number());
			jo.put("jobTitle", emp.getJobTitle());
			jo.put("department", emp.getDepartment());
			jo.put("joiningDate", emp.getJoiningDate());
			jo.put("reporting_per", emp.getReporting_per());
			jo.put("agency", emp.getAgency());
			jo.put("status", emp.getStatus());
			ja.add(jo);
		}
		return ja;
	}

	@GetMapping("/employee_details")
	public String getEmployeeDetails(@AuthenticationPrincipal User user, HttpServletRequest req, Model m) {
		//System.out.println("iddddddddddddddddddd"+req);
		String pass = "BISAGBISAG";
		String id=req.getParameter("newid");
		String DcryptedPk = hex_2_ascii.decrypt(id, pass);
		Long eid = Long.parseLong(DcryptedPk);
		System.out.println(eid + " : " + id);
		Employee emp = empRepo.getEmployeeById(eid);
		Logininformation loginInfo = emp.getLogininformation();

		// Add employee details to the model
		m.addAttribute("emp", emp);
		
		// Fetch the admin's profile picture based on the username
        String adminUsername = user.getUsername();
        Employee admin = empRepo.findAllByUsername(adminUsername);
        if (admin != null) {
            String adminProfilePicturePath = admin.getProfilePicturePath();
            String adminpic = adminProfilePicturePath != null ? new File(adminProfilePicturePath).getName() : "";
            m.addAttribute("adminProfilePicturePath", "/uploads/" + adminpic);
        } 
		
        // Extract and add the employee's profile picture path to the model
        String employeeProfilePicturePath = emp.getProfilePicturePath();
        String employeeFileName = employeeProfilePicturePath != null ? new File(employeeProfilePicturePath).getName() : "";
        m.addAttribute("employeeProfilePicturePath", "/uploads/" + employeeFileName);

		return "view_profile_detail";
	}


	/////// change password

	@GetMapping("/employee_detail")
	@ResponseBody
	public Map<String, Object> getEmployeeDetail(@RequestParam("id") String id) {
		String pass = "BISAGBISAG";
		String DcryptedPk = hex_2_ascii.decrypt(id, pass);
		Long eid = Long.parseLong(DcryptedPk);

		Employee emp = empRepo.getEmployeeById(eid);
		Logininformation loginInfo = emp.getLogininformation();

		Map<String, Object> response = new HashMap<>();
		response.put("full_name", emp.getFull_name());
		response.put("status", emp.getStatus());
		response.put("enable", loginInfo.isEnabled());
		response.put("role", loginInfo.getRole());
		return response;
	}
//	@GetMapping("/profile")
//
//	public String showProfile(@AuthenticationPrincipal User p, Model m) {
//		String username = p.getUsername();
//		Employee employee = empRepo.findAllByUsername(username);
//		if (employee != null) {
//			m.addAttribute("emp", employee);
//			// Extract only the file name from the absolute path
//			String profilePicturePath = employee.getProfilePicturePath();
//			String fileName = profilePicturePath != null ? new File(profilePicturePath).getName() : "";
//			m.addAttribute("profilePicturePath", "/uploads/" + fileName);
//			System.out.println("p.getUsername():" + employee.getCurrentAddress());
//		} else {
//			m.addAttribute("error", "Employee not found");
//		}
//		return "profile"; // resolves to /WEB-INF/views/profile.html
//	}

//	@PostMapping("/profileupdated")
//	public String updateProfile(@ModelAttribute("emp") Employee formEmp,
//	                            RedirectAttributes redirectAttributes) {
//
//	   
//	    Employee updateEmp = empRepo.findById(formEmp.getId())
//	            .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//	   
//	    updateEmp.setFull_name(formEmp.getFull_name());
//	    updateEmp.setContact_number(formEmp.getContact_number());
//	    updateEmp.setEmail(formEmp.getEmail());
//	    updateEmp.setJoiningDate(formEmp.getJoiningDate());
//	    updateEmp.setDateOfBirth(formEmp.getDateOfBirth());
//	    updateEmp.setAgency(formEmp.getAgency());
//	    updateEmp.setHometown(formEmp.getHometown());
//	    updateEmp.setCurrentAddress(formEmp.getCurrentAddress());
//
//	   	    empRepo.save(updateEmp);
//
//	    redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
//	    return "redirect:/admin/profile";
//	}

	@PostMapping("/updateStatusRole")
	public String updateStatusRole(@RequestParam("id") String id, @RequestParam("logininformation.role") String role,
			@RequestParam("logininformation.enabled") boolean enabled, @RequestParam("employee.status") String status) {
		String pass = "BISAGBISAG";
		String decryptedPk = hex_2_ascii.decrypt(id, pass);
		Long eid = Long.parseLong(decryptedPk);

		Employee existingEmployee = empRepo.findById(eid).orElse(null);
		if (existingEmployee == null) {
			return "error"; // Handle case where employee with given ID is not found
		}

		// Update status and role
		existingEmployee.getLogininformation().setEnabled(enabled);

		existingEmployee.getLogininformation().setRole(role);
		existingEmployee.setStatus(status);

		// Save updated employee
		empRepo.save(existingEmployee);

		// Redirect to the employee details page
		return "redirect:/admin/reg_employee";
	}

	@PostMapping("/delete")
	@ResponseBody
	public Map<String, String> deleteItems(HttpServletRequest req) {
		Map<String, String> response = new HashMap<>();
		req.getParameter("newid");
		String pass = "BISAGBISAG";
		String id=req.getParameter("newid");
		String DcryptedPk = hex_2_ascii.decrypt(req.getParameter("newid"), pass);
		Long eid = Long.parseLong(DcryptedPk);
		Employee emp = empRepo.getEmployeeById(eid);
		Logininformation loginInfo = emp.getLogininformation();

		try {
			if (!loginInfo.getRole().equals("ADMIN")) {
				empRepo.deleteById(eid);
				response.put("status", "success");
				response.put("message", "Deleted Successfully");
			} else {
				response.put("message", "Administrator Cannot Be Removed.");
			}
		} catch (Exception e2) {
			response.put("status", "catches");
			response.put("message", "Something Wrong");
		}

		return response;
	}
}
