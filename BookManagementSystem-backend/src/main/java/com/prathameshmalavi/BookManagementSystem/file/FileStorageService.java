package com.prathameshmalavi.BookManagementSystem.file;

import com.prathameshmalavi.BookManagementSystem.book.Book;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;


    public String saveFile(@NotNull MultipartFile sourceFile,@NotNull Book book,@NotNull Integer userId) {

        final String fileUploadSubPath = "users" + File.separator + userId;

        return uploadFile(sourceFile , fileUploadSubPath);
    }

    private String uploadFile(@NotNull MultipartFile sourceFile,@NotNull String fileUploadSubPath) {

        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;

        File targetFolder = new File(finalUploadPath);

        if(targetFolder.exists()){
            boolean folderCreated = targetFolder.mkdir();
            if(!folderCreated){
                log.warn("Failed to create the target folder");
                return null;
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());

        String targetFilePath  = fileUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);

        try{
            Files.write(targetPath , sourceFile.getBytes());
            log.info("File upload sucess");
            return targetFilePath;
        } catch (IOException e) {
            log.error("FIle was not saved" + e);
            throw null;
        }
    }

    private String getFileExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()){
            return  "";
        }

        int lastDotIndex = originalFilename.lastIndexOf(".");
        if ((lastDotIndex == -1)){
            return "";
        }

        return originalFilename.substring(lastDotIndex+1).toLowerCase();
    }
}
