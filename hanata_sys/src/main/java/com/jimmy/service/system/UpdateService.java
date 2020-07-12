package com.jimmy.service.system;

import com.github.pagehelper.Page;
import com.jimmy.bean.UpdateRecord;
import com.jimmy.bean.User;
import com.jimmy.exception.CustomException;
import com.jimmy.mapper.system.UpdateMapper;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Jimmy He
 * @date 2020-06-20
 */

@Service
public class UpdateService {

    @Autowired
    SnowFlake mySnowFlake;

    @Autowired
    UpdateMapper updateMapper;

    public void newUpdateRecord(String version,String content) throws CustomException {
        if (version.equals("")||content.equals("")){
            throw new CustomException("参数必填");
        }
        User user = RequestContext.get().getUser();
        String uid = String.valueOf(mySnowFlake.nextId());
        Date datetime = Calendar.getInstance().getTime();
        updateMapper.newUpdate(uid,datetime,user.getUid(),version,content);
    }

    public void delUpdateRecord(String uid) throws CustomException {
        UpdateRecord recordById = updateMapper.getRecordById(uid);
        if (recordById==null){
            throw new CustomException("参数错误");
        }

        User user = RequestContext.get().getUser();
        Date datetime = Calendar.getInstance().getTime();
        updateMapper.delUpdate(uid,datetime,user.getUid());
    }

    public void updateUpdateRecord(String uid,String version,String content) throws CustomException {
        UpdateRecord recordById = updateMapper.getRecordById(uid);
        if (recordById==null){
            throw new CustomException("参数错误");
        }
        User user = RequestContext.get().getUser();
        Date datetime = Calendar.getInstance().getTime();

        updateMapper.updateRecord(uid,datetime,user.getUid(),version,content);

    }

    public Page<UpdateRecord> listUpdate(){
        return updateMapper.listUpdate();
    }

}
