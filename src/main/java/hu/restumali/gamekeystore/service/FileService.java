package hu.restumali.gamekeystore.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.restumali.gamekeystore.model.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    @Autowired
    ObjectMapper objectMapper;

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

    public List<ProductEntity> importFile(MultipartFile file){
        File importedFile = new File(file.getOriginalFilename());
        try {
            importedFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(importedFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ProductEntity> importedProducts= null;
        try {
            importedProducts = objectMapper.readValue(importedFile, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return importedProducts;
    }

    public void exportProducts(List<ProductEntity> products){
        try {
            objectMapper.writeValue(new File(uploadPath + "export.json"), products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
