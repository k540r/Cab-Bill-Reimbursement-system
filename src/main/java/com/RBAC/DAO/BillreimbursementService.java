package com.RBAC.DAO;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.RBAC.Model.Billreimbursement;
import com.RBAC.Model.ChartDataDTO;
import com.RBAC.Model.Employee;

@Service
public class BillreimbursementService {

    @Autowired
    private BillreimbursementRepository billreimbursementRepository;
    
    public long countAllBills() {
        return billreimbursementRepository.countAllBills();
    }
  
    public List<Object[]> getBillReimbursementStats() {
        return billreimbursementRepository.getBillReimbursementStats();
    }
    

    @Autowired
    private EmployeeRepository employeeRepository;

    public Billreimbursement submitBillReimbursement(Billreimbursement billReimbursement) {
        Employee currentEmployee = getCurrentLoggedInEmployee();
        if (currentEmployee != null) {
            billReimbursement.setEmployee(currentEmployee);
            return billreimbursementRepository.save(billReimbursement);
        } else {
            throw new RuntimeException("No logged-in employee found.");
        }
    }

    private Employee getCurrentLoggedInEmployee() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return employeeRepository.findByEmail(email).orElse(null);
    }

	public static long getTotalBillsByEmployee(Long employeeId) {
		return 0;
	}


	
	public List<Billreimbursement> getAllBillsByEmployee(Employee employee) {
		return billreimbursementRepository.findByEmployee(employee);
	}

	public void saveBillReimbursement(Billreimbursement billReimbursement) {
		billreimbursementRepository.save(billReimbursement);
	}

	public void updateBillReimbursement(Billreimbursement billReimbursement) {
		billreimbursementRepository.save(billReimbursement);
	}

	
	public Optional<Billreimbursement> findById(Long id) {
        return billreimbursementRepository.findById(id);
    }

    
    
    public void deleteBillReimbursement(Long id) {
        billreimbursementRepository.deleteById(id);
    }
    
	/////
	public List<Billreimbursement> getBillsByEmployeeId(Long employeeId) {
		return billreimbursementRepository.findByEmployeeId(employeeId);
	}

	public List<Billreimbursement> getAllBills() {
		return billreimbursementRepository.findAll();
	}

	public Map<Employee, Long> getEmployeeBillCounts() {
		List<Billreimbursement> allBills = getAllBills();
		return allBills.stream().collect(Collectors.groupingBy(Billreimbursement::getEmployee, Collectors.counting()));
	}

	public Map<Employee, Long> getPendingBillCounts() {
		List<Billreimbursement> allBills = getAllBills();
		return allBills.stream().filter(bill -> "pending".equalsIgnoreCase(bill.getStatus()))
				.collect(Collectors.groupingBy(Billreimbursement::getEmployee, Collectors.counting()));
	}

	public void updateBillStatus(Long billId, String status) {
		Billreimbursement bill = billreimbursementRepository.findById(billId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid bill ID"));
		bill.setStatus(status);
		billreimbursementRepository.save(bill);
	}

	public Long findEmployeeIdByBillId(Long billId) {
		Billreimbursement bill = billreimbursementRepository.findById(billId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid bill ID"));
		return bill.getEmployee().getId();
	}
	
	public Long getEmployeeIdForCurrentBill(Long billId) {
	    return billreimbursementRepository.findEmployeeIdByBillId(billId);
	}

	
	public void updateBillStatusWithRemarks(Long billId, String status, String remarks) {
	    Billreimbursement bill = billreimbursementRepository.findById(billId)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid bill ID"));
	    bill.setStatus(status);
	    bill.setRemarks(remarks);
	    billreimbursementRepository.save(bill);
	}
	
	
	public List<Integer> getDistinctYears() {
        return billreimbursementRepository.findDistinctYears();
    }

    public List<ChartDataDTO> getTotalBillAmountByMonth(int year) {
        return billreimbursementRepository.findTotalBillAmountByMonth(year);
    }
    
    
     
    public List<Billreimbursement> getBillsForMonthAndYearByEmployee(Long employeeId, String monthYear) {
        return billreimbursementRepository.findByEmployeeIdAndMonthAndYear(
                employeeId,
                Integer.parseInt(monthYear.split("-")[1]),
                Integer.parseInt(monthYear.split("-")[0])
        );
    }

    public File mergePdfsAndCreateZip(List<Billreimbursement> bills) throws IOException {
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        File mergedPdf = new File("merged.pdf");

        for (Billreimbursement bill : bills) {
            File pdfFile = new File(bill.getFilePath());
            if (pdfFile.exists()) {
                pdfMerger.addSource(pdfFile);
            }
        }
        pdfMerger.setDestinationFileName(mergedPdf.getAbsolutePath());
        pdfMerger.mergeDocuments(null);

        File zipFile = new File("bills.zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            ZipEntry zipEntry = new ZipEntry(mergedPdf.getName());
            zipOut.putNextEntry(zipEntry);
            zipOut.write(Files.readAllBytes(mergedPdf.toPath()));
            zipOut.closeEntry();
        }

        if (mergedPdf.exists()) {
            mergedPdf.delete();
        }

        return zipFile;
    }
    
    public String getRemarksByBillId(Long billId) {
        Billreimbursement bill = billreimbursementRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        
        return bill.getRemarks(); // Return the remarks field
    }


    	

}