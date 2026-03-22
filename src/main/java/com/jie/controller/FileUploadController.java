package com.jie.controller;

import com.jie.pojo.Result;
import com.jie.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile  file) throws IOException {
        //把文件存到本地磁盘
        String originalFilename = file.getOriginalFilename();
        //保证文件名唯一,从而防止文件覆盖
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //file.transferTo(new File("D:\\IDEproject\\" + fileName));
        String url = AliOssUtil.uploadFile(fileName,file.getInputStream());
        return Result.success(url);
    }
}
