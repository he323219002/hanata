package com.jimmy.controller.captcha;

/**
 * @author Jimmy He
 * @date 2020-06-19
 */

import com.jimmy.servlet.PassToken;
import com.jimmy.utils.Captcha;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CaptchaController {
    /**
     * 用于生成带四位数字验证码的图片
     */
    @RequestMapping(value = "/captcha")
    @PassToken
    @ResponseBody
    public String imagecode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        OutputStream os = response.getOutputStream();

        String formId = request.getParameter("formId");
        //返回验证码和图片的map
        Map<String,Object> map = Captcha.getImageCode(86, 37, os,formId);
        String simpleCaptcha = "simpleCaptcha";
        request.getSession().setAttribute(simpleCaptcha, map.get("strEnsure").toString().toLowerCase());
        request.getSession().setAttribute("codeTime",new Date().getTime());
        try {
            ImageIO.write((BufferedImage) map.get("image"), "jpg", os);
        } catch (IOException e) {
            return "";
        }   finally {
            if (os != null) {
                os.flush();
                os.close();
            }
        }
        return null;
    }
}