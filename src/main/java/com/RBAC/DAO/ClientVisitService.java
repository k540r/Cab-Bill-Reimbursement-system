package com.RBAC.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RBAC.Model.Billreimbursement;
import com.RBAC.Model.ClientVisit;
import com.RBAC.Model.Employee;

@Service
public class ClientVisitService {
	
	@Autowired
	private ClientVisitRepository clientVisitRepository;

 //   private final ClientVisitRepository clientVisitRepository;

    public ClientVisitService(ClientVisitRepository clientVisitRepository) {
        this.clientVisitRepository = clientVisitRepository;
    }

    // Method to save ClientVisit entity
    public void saveClientVisit(ClientVisit clientVisit) {
        clientVisitRepository.save(clientVisit);  // Save clientVisit to the database
    }
    
 // Method to fetch 
    public List<ClientVisit> getDeatailClientVisitById(Long id) {
    	return  clientVisitRepository.findAll();
    }	
    
 // Method to fetch by ID
    public Optional<ClientVisit> findById(Long id) {
        return clientVisitRepository.findById(id);
    }
    

    // Fetch client visits by projectId 
    public List<ClientVisit> getClientVisitsByProjectId(Long projectId) {
        return clientVisitRepository.findByProjectId(projectId);//
    }

}
