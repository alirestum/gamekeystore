package hu.restumali.gamekeystore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    @Value("${upload.location}")
    String uploadPath;

    public String uploadFile(MultipartFile file){

        String fileName =  System.currentTimeMillis() + file.getOriginalFilename();
        String path = uploadPath + fileName;
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  fileName;
    }
}
