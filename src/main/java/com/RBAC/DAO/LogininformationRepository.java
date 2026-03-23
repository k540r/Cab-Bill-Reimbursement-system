package com.RBAC.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RBAC.Model.Logininformation;

@Repository
public interface LogininformationRepository extends JpaRepository<Logininformation, Long> {

	// Define a method to find a user by their username

	 Logininformation findByUsername(String username);
}
