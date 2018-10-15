package com.snailmann.security.demo.controller;


import com.snailmann.security.demo.entity.FileInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/file")
public class FileController {
    String path = "D:\\IdeaProject\\demo-springboot-security\\security-demo\\src\\main\\resources\\file";
    @PostMapping
    public FileInfo upload(MultipartFile file) {
        System.out.println(file.getName()); //上传的参数名
        System.out.println(file.getOriginalFilename());  //文件的原始名称
        System.out.println(file.getSize()); //文件的大小


        File localFile = new File(path, "file.txt");
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FileInfo(localFile.getAbsolutePath());
    }

    @GetMapping("/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {

        try (InputStream inputStream = new FileInputStream(new File(path, id + ".txt"));
             OutputStream outputStream = response.getOutputStream()
        ) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=test.txt"); //下载出去时的名字为test.txt

            IOUtils.copy(inputStream,outputStream); //commons-io的工具类，将inputstream转换为outputstream，本质就是封装好的read和write
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
