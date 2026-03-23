package com.RBAC.DAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.RBAC.Model.Billreimbursement;
import com.RBAC.Model.ChartDataDTO;
import com.RBAC.Model.Employee;

@Repository
public interface BillreimbursementRepository
		extends JpaRepository<Billreimbursement, Long>, BillreimbursementCustomRepository {

	List<Billreimbursement> findByEmployee(Employee employee);

	List<Billreimbursement> findAll();

	List<Billreimbursement> findByEmployeeId(Long employeeId);

	@Query("SELECT COUNT(b) FROM Billreimbursement b")
	long countAllBills();

	@Query("SELECT "
			+ "(SELECT COUNT(DISTINCT b.employee) FROM Billreimbursement b WHERE b.status = 'Approved') AS approvedEmployeeCount, "
			+ "(SELECT COUNT(DISTINCT b.employee) FROM Billreimbursement b WHERE b.status = 'Pending') AS pendingEmployeeCount, "
			+ "(SELECT SUM(b.billAmount) FROM Billreimbursement b WHERE b.status = 'Pending') AS totalPendingAmount, "
			+ "(SELECT SUM(b.billAmount) FROM Billreimbursement b WHERE b.status = 'Approved') AS totalApprovedAmount, "
			+ "(SELECT COUNT(b) FROM Billreimbursement b) AS totalBillsCount " + "FROM Billreimbursement b")
	List<Object[]> getBillReimbursementStats();

	@Query("SELECT DISTINCT EXTRACT(YEAR FROM TO_DATE(b.fromDate, 'YYYY-MM-DD')) FROM Billreimbursement b ORDER BY EXTRACT(YEAR FROM TO_DATE(b.fromDate, 'YYYY-MM-DD'))DESC")
	List<Integer> findDistinctYears();

	@Query("SELECT new com.RBAC.Model.ChartDataDTO(TO_CHAR(TO_DATE(b.fromDate, 'YYYY-MM-DD'), 'YYYY-MM') AS month, "
			+ "SUM(CASE WHEN b.status = 'Pending' THEN b.billAmount ELSE 0 END) AS pendingAmount, "
			+ "SUM(CASE WHEN b.status = 'Approved' THEN b.billAmount ELSE 0 END) AS approvedAmount) "
			+ "FROM Billreimbursement b " + "WHERE EXTRACT(YEAR FROM TO_DATE(b.fromDate, 'YYYY-MM-DD')) = :year "
			+ "GROUP BY TO_CHAR(TO_DATE(b.fromDate, 'YYYY-MM-DD'), 'YYYY-MM') "
			+ "ORDER BY TO_CHAR(TO_DATE(b.fromDate, 'YYYY-MM-DD'), 'YYYY-MM')")
	List<ChartDataDTO> findTotalBillAmountByMonth(@Param("year") int year);

	
	@Query("SELECT b FROM Billreimbursement b WHERE b.employee.id = :employeeId AND EXTRACT(MONTH FROM CAST(b.fromDate AS date)) = :month AND EXTRACT(YEAR FROM CAST(b.fromDate AS date)) = :year")
	List<Billreimbursement> findByEmployeeIdAndMonthAndYear(
	        @Param("employeeId") Long employeeId,
	        @Param("month") int month,
	        @Param("year") int year);
	 
	
	
	@Query("SELECT b.employee.id FROM Billreimbursement b WHERE b.id = :billId")
	Long findEmployeeIdByBillId(@Param("billId") Long billId);

}
