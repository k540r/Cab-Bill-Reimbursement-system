package com.RBAC.DAO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private final Path fileStorageLocation;
    

    private final String uploadDir = "/path/to/save/mom/files/";


    public FileService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String saveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    
    
    
//    public String saveFiles(MultipartFile file) throws IOException {
//        if (file.isEmpty()) {
//            throw new IOException("Empty file cannot be saved.");
//        }
//
//        // Save file
//        String fileName = file.getOriginalFilename();
//        Path filePath = Paths.get(uploadDir + fileName);
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//        return filePath.toString();
//    }
//    
//    
//    public FileService(@Value("${file.upload-dir}") String uploadDir) {
//        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (Exception ex) {
//            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
//        }
//    }
//
//    public String saveFile(MultipartFile file) throws IOException {
//        if (file.isEmpty()) {
//            throw new IOException("Empty file cannot be saved.");
//        }
//
//        String fileName = file.getOriginalFilename();
//        Path targetLocation = this.fileStorageLocation.resolve(fileName);
//        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//        return targetLocation.toString();
//    }
    

}
