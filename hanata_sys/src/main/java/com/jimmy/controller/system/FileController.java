package com.jimmy.controller.system;

import com.jimmy.exception.CustomException;
import com.jimmy.service.system.FileService;
import com.jimmy.utils.HaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping(value = "/upload/",produces = MediaType.APPLICATION_JSON_VALUE)
    public HaResponse uploadFile(@RequestParam("file") MultipartFile file,String fileType) throws CustomException {
        Map<String, Object> map = fileService.uploadFile(file, fileType);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new HaResponse(){
            {
                setData(map);
            }
        };

    }
}
