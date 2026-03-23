package com.RBAC.CommonController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CommonMethod {


	    public static void main(String[] args) {
	        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        String rawPassword = "admin";
	        String encodedPassword = encoder.encode(rawPassword);
	        System.out.println(encodedPassword);
	    }
	    public static String generateUsername(String fullName, String contactNumber) {
	        if (fullName == null || contactNumber == null || contactNumber.length() < 4) {
	            throw new IllegalArgumentException("Invalid full name or contact number");
	        }

	        // Split the full name into parts
	        String[] nameParts = fullName.split(" ");
	        if (nameParts.length == 0) {
	            throw new IllegalArgumentException("Full name must contain at least one part");
	        }

	        // Take the first part of the full name and convert it to lowercase
	        String firstName = nameParts[0].toLowerCase();

	        // Take the last 4 digits of the contact number
	        String lastFourDigits = contactNumber.substring(contactNumber.length() - 4);

	        // Combine the first name and last four digits with an underscore
	        return firstName + "_" + lastFourDigits;
	    }

}
