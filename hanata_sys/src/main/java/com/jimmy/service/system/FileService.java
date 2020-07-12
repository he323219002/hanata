package com.jimmy.service.system;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSUtils;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.jimmy.bean.User;
import com.jimmy.config.OSSConfig;
import com.jimmy.config.PropertyConfig;
import com.jimmy.exception.CustomException;
import com.jimmy.mapper.system.FileMapper;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.CommonUtils;
import com.jimmy.utils.SnowFlake;
import com.jimmy.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

@Service
@Transactional
public class FileService {

    @Autowired
    SnowFlake mySnowFlake;

    @Autowired
    private OSSConfig ossConfig;

    @Autowired
    private PropertyConfig propertyConfig;

    @Autowired
    FileMapper fileMapper;

    public Map<String, Object> uploadFile(MultipartFile file, String fileType) throws CustomException {
//        User user = RequestContext.get().getUser();
//        if(user==null){
//            throw new CustomException("请先登录");
//        }

        if (!CommonUtils.checkFileSize(file.getSize(),50,"M")){
            throw new CustomException("文件大与50M");
        }

        List<String> typeList = propertyConfig.getFileType();
        if (!typeList.contains(fileType)){
            throw new CustomException("类型参数错误");
        }

        String fileId = String.valueOf(mySnowFlake.nextId());
        Date datetime = Calendar.getInstance().getTime();

        // upload
        String filePath = UploadUtil.upload(ossConfig, file, fileType,fileId);

        fileMapper.uploadFile(fileId,datetime,"1",filePath,file.getName(),fileType);

        return new HashMap<String,Object>(){
            {
                put("uid",fileId);
                put("filePath",filePath);
            }
        };

    }
}
