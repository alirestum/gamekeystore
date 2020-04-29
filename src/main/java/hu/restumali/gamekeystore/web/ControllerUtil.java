package hu.restumali.gamekeystore.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ControllerUtil {

    public ControllerUtil(){}

    @Value("${gamekeystore.external}")
    String uploadPath = "/home/restumali/Documents/projects/gk_files/";

    public String uploadFile(MultipartFile file){

        String fileName =  file.getOriginalFilename() + System.currentTimeMillis();
        String path = uploadPath + fileName;
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  fileName;
    }
}
