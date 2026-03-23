package com.RBAC.DAO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.RBAC.Model.Hometown;

@Service
public class HometownService {

	@Autowired
	private HometownRepository hometownRepository;

	public List<Hometown> getAllHometown() {
		return hometownRepository
				.findAll(Sort.by(Sort.Direction.DESC, "updatedAt").and(Sort.by(Sort.Direction.ASC, "createdAt")));
	}

	public void saveOrUpdateHometown(Hometown hometown) {
	    hometownRepository.save(hometown);
	}

	public void deleteHometown(Long id) {
		hometownRepository.deleteById(id);
	}

	public List<Hometown> getHometownsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
		return hometownRepository.findByCreatedAtBetweenOrUpdatedAtBetween(startDate, endDate, startDate, endDate);
	}



}
