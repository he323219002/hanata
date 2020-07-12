package com.jimmy.utils;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.jimmy.config.OSSConfig;
import com.jimmy.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

public class UploadUtil {

    private UploadUtil(){};

    private volatile static OSSClient ossClient = null;



    public static String upload(OSSConfig ossConfig, MultipartFile file,String fileType,String fileId){
        initOSS(ossConfig);

        
        String originalFilename = file.getOriginalFilename();
        String subfix = (originalFilename != null ? originalFilename.split("\\.") : new String[0])[1];


        Calendar now = Calendar.getInstance();
        String year = now.get(Calendar.YEAR) + "";
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);


        MessageFormat messageFormat = new MessageFormat("{0}/{1}/{2}/{3}/{4}.{5}");
        String objectName = messageFormat.format(new Object[]{fileType, year, month, day, fileId, subfix});

        try {
            ossClient.putObject(ossConfig.getBucket(), objectName, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return objectName;
    }



    private static OSSClient initOSS(OSSConfig ossConfig) {
        if (ossClient == null ) {
            synchronized (UploadUtil.class) {
                if (ossClient == null) {
                    ossClient = new OSSClient(ossConfig.getEndpoint(),
                            new DefaultCredentialProvider(ossConfig.getAccessKey(), ossConfig.getSecret()),
                            new ClientConfiguration());
                }
            }
        }
        return ossClient;
    }

}
