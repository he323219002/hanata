package com.jimmy.service;

import com.github.pagehelper.Page;
import com.jimmy.bean.User;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.mapper.UserMapper;
import com.jimmy.service.system.MailSendService;
import com.jimmy.servlet.PassToken;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.RedisUtil;
import com.jimmy.utils.SnowFlake;
import com.jimmy.utils.ThrottleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author Jimmy He
 * @date 2020-06-05
 */

@Service
@Transactional
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    SnowFlake mySnowFlake;

    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    BCryptPasswordEncoder encoder;

    @Autowired
    RedisUtil redisUtil;

    @Value("${hanata.redirectAddr}")
    private String serverAddr;

    @Autowired
    ThrottleUtil throttleUtil;

    @Autowired
    MailSendService mailSendService;


    public User getUser(String uid) throws CustomException {
        if(uid.equals("")){
            throw new CustomException("参数必填");
        }
        return userMapper.getUserById(uid);
    }

    public User getUserbyUsername(String username){
        return userMapper.getUserByUsername(username);
    }

    public boolean checkUserCredential(User user,String password){
        String encode_pwd = user.getPassword();
        Date time = Calendar.getInstance().getTime();
        userMapper.updateLoginTime(user.getUid(),time);
        return encoder.matches(password, encode_pwd);

    }

    public Integer createAdmin(String username,String password,String phone,String mail) throws AlreadyExistsException, CustomException {
        if (username.equals("")|| password.equals("") || phone.equals("") || mail.equals("")) {
            throw new CustomException("参数必填");
        }



        User userByUsername = userMapper.getUserByUsername(username);
        if(userByUsername != null){
            throw new AlreadyExistsException("用户名已存在");
        }

        User userByMail = userMapper.getUserByMail(mail);
        if(userByMail != null){
            throw new AlreadyExistsException("邮箱已注册");
        }

        User userByPhone = userMapper.getUserByPhone(phone);
        if(userByPhone != null){
            throw new AlreadyExistsException("号码已注册");
        }

        String new_password = encoder.encode(password);

        String uid = String.valueOf(mySnowFlake.nextId());

        return userMapper.createAdmin(uid,username,new_password,phone,mail);
    }

    public Integer register(String username,String password,String nickname,String avatar,String mail,String gender) throws AlreadyExistsException, CustomException {
        if(username.equals("") || password.equals("") || nickname.equals("")|| mail.equals("") || gender.equals("")){
            throw new CustomException("参数必填");
        }


        User userByUsername = userMapper.getUserByUsername(username);
        if(userByUsername != null){
            throw new AlreadyExistsException("用户名已存在");
        }

        User userByMail = userMapper.getUserByMail(mail);
        if(userByMail != null){
            throw new AlreadyExistsException("邮箱已注册");
        }

        List<User> userByNickname = userMapper.getUserByNickname(nickname);

        if(userByNickname.size() != 0){
            throw new AlreadyExistsException("昵称已被使用了哟");
        }


        String new_password = encoder.encode(password);

        String uid = String.valueOf(mySnowFlake.nextId());
        Date time = Calendar.getInstance().getTime();


        return userMapper.registUser(uid,username,new_password,nickname,avatar,mail,gender,time);
    }


    public void resetPwd(String userId,String password,String token) throws CustomException {
        String realToken = (String) redisUtil.get(userId);
        // throttle
//        ThrottleUtil.getSingleton().record(userId);
        throttleUtil.record(userId);

        if (!realToken.equals(token)){
            throw new CustomException("安全校验错误");
        }

        User userById = userMapper.getUserById(userId);
        if (userById==null){
            throw new CustomException("参数错误");
        }

        String new_password = encoder.encode(password);
        Date time = Calendar.getInstance().getTime();
        userMapper.updatePassword(userId,time,new_password);
        redisUtil.del(userId);

    }

    public void forgetPwd(String mail) throws CustomException, MessagingException {
        if(mail.equals("")){
            throw new CustomException("参数必填");
        }


        String userId = userMapper.getUserIdByMail(mail);
        if(userId==null){
            throw new CustomException("邮箱不正确，请重新输入");
        }


        // generate uuid token concat userId to url
        String uuid = UUID.randomUUID().toString();
        redisUtil.set(userId,uuid,15*60);
        MessageFormat messageFormat = new MessageFormat("{0}/redirect/resetPwd?userId={1}&token={2}");
        String url = messageFormat.format(new Object[]{serverAddr, userId, uuid});
        mailSendService.resetPassword(url,mail);

    }

    public Page<User> listUser(String q) throws CustomException {
        RequestContext requestContext = RequestContext.get();
        if (requestContext==null){
            throw new CustomException("权限不足");
        }
        User user = requestContext.getUser();
        if (!user.getRole().equals("0")){
            throw new CustomException("权限不足");
        }

        return userMapper.listUser(q);
    }

    public void activeUser(String userId,String active) throws CustomException {
        RequestContext requestContext = RequestContext.get();
        if (requestContext==null){
            throw new CustomException("权限不足");
        }
        User user = requestContext.getUser();
        if (!user.getRole().equals("0")){
            throw new CustomException("权限不足");
        }


        User userById = userMapper.getUserById(userId);
        if (userById==null){
            throw new CustomException("参数错误");
        }
        if (!active.equals("1")&&!active.equals("0")){
            throw new CustomException("参数错误");
        }

        userMapper.updateUserActive(userId,active);
    }

    public Integer checkUsername(String username){
        User userByUsername = userMapper.getUserByUsername(username);
        if (userByUsername!=null){
            return 1;
        }
        return 0;
    }

    public Integer checkMail(String mail){
        User userByMail = userMapper.getUserByMail(mail);
        if (userByMail!=null){
            return 1;
        }
        return 0;
    }

    public Integer checkNickname(String nickname){
        List<User> userByNickname = userMapper.getUserByNickname(nickname);
        if (userByNickname.size()!=0){
            return 1;
        }
        return 0;
    }


    public void updateUser(String nickname,String mail,String avatar) throws CustomException {
        RequestContext requestContext = RequestContext.get();
        if (requestContext==null){
            throw new CustomException("请先登录");
        }



        User user = requestContext.getUser();
        String userUid = user.getUid();
        List<User> userByNickname = userMapper.updateUserByNickname(userUid,nickname);
        if (userByNickname.size()!=0){
            throw new CustomException("用户名已被使用");
        }

        List<User> users = userMapper.updateUserByMail(userUid, mail);
        if (userByNickname.size()!=0){
            throw new CustomException("邮箱已被使用");
        }

        userMapper.updateUserById(userUid,nickname,mail,avatar);
    }
}
