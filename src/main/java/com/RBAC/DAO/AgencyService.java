package com.RBAC.DAO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.RBAC.Model.Agency;


@Service
public class AgencyService{
	
	@Autowired
	private AgencyRepository agencyRepository;
	
	
	
	public List<Agency> getAllAgency() {
		return agencyRepository
				.findAll(Sort.by(Sort.Direction.DESC, "updatedAt").and(Sort.by(Sort.Direction.ASC, "createdAt")));
	}

	public void saveOrUpdateAgency(Agency agency) {
		agencyRepository.save(agency);
	}

	public void deleteAgency(Long id) {
		agencyRepository.deleteById(id);
	}

	public List<Agency> getAgencysByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return agencyRepository.findByCreatedAtBetweenOrUpdatedAtBetween(startDate, endDate, startDate, endDate);
    }

}
