package com.github.amalykhin.paint.service;

import com.github.amalykhin.paint.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PictureService {
    private Path uploadDir;

    @Autowired
    public PictureService(FileStorageProperties fileStorageProperties) {
        //uploadDir = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        uploadDir = Paths.get(fileStorageProperties.getUploadDir()).normalize();

        try {
            Files.createDirectories(uploadDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Resource storePicture(MultipartFile file) throws Exception {
        String fileName = String.valueOf(file.getOriginalFilename()) + ".png";
        try {
            Path targetLocation = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return new FileSystemResource(targetLocation.toFile());
        } catch (IOException ex) {
            throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource getPictureResource(String pictureName) throws IOException {
        Path filePath = uploadDir.resolve(pictureName + ".png").normalize();
        System.out.println(filePath);
        return new FileSystemResource(filePath.toFile());
    }

    public List<String> getPicturesList() throws IOException {
        return Files.walk(uploadDir)
            .filter(Files::isRegularFile)
            .map(Path::getFileName)
            .map(Path::toString)
            .collect(Collectors.toList());
    }
}
