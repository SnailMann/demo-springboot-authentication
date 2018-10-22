package com.snailmann.security.core.validate.code.controller;

import com.snailmann.security.core.config.properties.SecurityProperties;
import com.snailmann.security.core.validate.code.Service.ValidateCodeGenerate;
import com.snailmann.security.core.validate.code.entity.ImageCode;
import com.snailmann.security.core.validate.code.util.ImageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class ValidateCodeController {

    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";
    //Session操作工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private ImageCodeUtil imageCodeUtil;

    /**
     * 图形验证码验证
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = imageCodeUtil.createImageCode(new ServletWebRequest(request));
        //将SESSION_KEY与imageCode放进Session
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
        //然后将图片放入response
        ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());

    }


    /**
     * 短信验证
     * @param request
     * @param response
     */
    @GetMapping("/code/sms")
    public void createSmsCode(HttpServletRequest request,HttpServletResponse response){

    }


}
