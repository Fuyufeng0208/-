package com.changgou.file.controller;


import com.changgou.file.FastDFSFile;
import com.changgou.util.com.changgou.util.FastDFSClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by fyf on 2019/8/20
 */
@RestController
@CrossOrigin
public class FileController {


    @PostMapping("/upload")
    public String upload(MultipartFile file){
        try {
            FastDFSFile fastdfsfile = new FastDFSFile(file.getOriginalFilename(),
                    file.getBytes(),
                    StringUtils.getFilenameExtension(file.getOriginalFilename())
                    );
            String[] upload = FastDFSClient.upload(fastdfsfile);
            return FastDFSClient.getTrackerUrl()+"/"+upload[0]+"/"+upload[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
