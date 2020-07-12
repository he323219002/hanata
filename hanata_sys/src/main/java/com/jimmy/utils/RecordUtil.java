package com.jimmy.utils;

import com.jimmy.bean.es.EsRecord;
import com.jimmy.exception.CustomException;
import com.jimmy.servlet.AuthenticationInterceptor;
import com.jimmy.servlet.RequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author Jimmy He
 * @date 2020-06-19
 */
public class RecordUtil {



    public static EsRecord recordRequest(HttpServletRequest httpServletRequest) throws CustomException {

        String uid = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        String IP = httpServletRequest.getRemoteAddr();
        Date time = Calendar.getInstance().getTime();


        try {
//            RequestWrapper requestWrapper = new RequestWrapper(httpServletRequest);
//            String params = requestWrapper.getBody();
            EsRecord esRecord = new EsRecord(uid, time, IP, url);
            return esRecord;
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }
}
