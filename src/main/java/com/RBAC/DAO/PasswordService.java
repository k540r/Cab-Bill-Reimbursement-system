package com.RBAC.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.RBAC.Model.Logininformation;

@Service
public class PasswordService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LogininformationRepository logininformationRepository;

	public String changePassword(String username, String currentPassword, String newPassword) {
		Logininformation loginfo = logininformationRepository.findByUsername(username);

		if (loginfo == null) {
			return "User not found";
		}

		if (passwordEncoder.matches(currentPassword, loginfo.getPassword())) {
			if (passwordEncoder.matches(newPassword, loginfo.getPassword())) {
				return "New password cannot be the same as the current password.";
			}
			
			
			loginfo.setPassword(new BCryptPasswordEncoder().encode(newPassword));
			
			//second logic to remove the bcrypt prefix from the database
			/*
			 * // Encode new password String encodedPassword =
			 * passwordEncoder.encode(newPassword);
			 * 
			 * // Remove '{bcrypt}' prefix if it exists if
			 * (encodedPassword.startsWith("{bcrypt}")) { encodedPassword =
			 * encodedPassword.substring(8); }
			 * 
			 * loginfo.setPassword(encodedPassword); // Set the password without '{bcrypt}'
			 * prefix
			 */			
			logininformationRepository.save(loginfo);
			return "Your password has been changed successfully.";
		} else {
			return "Current password is incorrect.";
		}
	}
}
