package hqu.edu.reggie.controller;


import hqu.edu.reggie.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${upload.path}")
    private String basePath;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){

        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+suffix;

        // 创建一个目录对象 dir
        File dir = new File(basePath);

        if (!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success(fileName);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));

            ServletOutputStream servletOutputStream = response.getOutputStream();

            response.setContentType("image/jpeg");
            int length = 0;
            byte[] bytes = new byte[1024];
            while ((length = fileInputStream.read(bytes))!=-1){
                servletOutputStream.write(bytes,0,length);
                servletOutputStream.flush();
            }
            fileInputStream.close();
            servletOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
