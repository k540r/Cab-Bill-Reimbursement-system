package com.RBAC.DAO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.RBAC.Model.ReportingPersonnel;


@Service
public class ReportingPersonnelService {
	
	@Autowired
	private ReportingPersonnelRepository reportingPersonnelRepository;
	
	
	public List<ReportingPersonnel> getAllReportingPersonnel() {
		return reportingPersonnelRepository
				.findAll(Sort.by(Sort.Direction.DESC, "updatedAt").and(Sort.by(Sort.Direction.ASC, "createdAt")));
	}

	public void saveOrUpdateReportingPersonnel(ReportingPersonnel reportingPersonnel) {
		reportingPersonnelRepository.save(reportingPersonnel);
	}

	public void deleteReportingPersonnel(Long id) {
		reportingPersonnelRepository.deleteById(id);
	}

	public List<ReportingPersonnel> getReportingPersonnelByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return reportingPersonnelRepository.findByCreatedAtBetweenOrUpdatedAtBetween(startDate, endDate, startDate, endDate);
    }

}
