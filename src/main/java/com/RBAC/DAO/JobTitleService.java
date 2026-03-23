package com.RBAC.DAO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.RBAC.Model.Agency;
import com.RBAC.Model.JobTitle;


@Service
public class JobTitleService {
	
	
	@Autowired
	private JobTitleRepository jobTitleRepository;
	
	
	public List<JobTitle> getAllJobTitle() {
		return jobTitleRepository
				.findAll(Sort.by(Sort.Direction.DESC, "updatedAt").and(Sort.by(Sort.Direction.ASC, "createdAt")));
	}

	public void saveOrUpdateJobTitle(JobTitle jobTitle) {
		jobTitleRepository.save(jobTitle);
	}

	public void deleteJobTitle(Long id) {
		jobTitleRepository.deleteById(id);
	}

	public List<JobTitle> getJobTitleByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return jobTitleRepository.findByCreatedAtBetweenOrUpdatedAtBetween(startDate, endDate, startDate, endDate);
    }
	
	
}
