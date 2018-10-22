package com.snailmann.security.core.validate.code.image;

import com.snailmann.security.core.config.properties.SecurityProperties;
import com.snailmann.security.core.validate.code.ValidateCodeGenerator;
import com.snailmann.security.core.validate.code.entity.ImageCode;
import com.snailmann.security.core.validate.code.entity.ValidateCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


public class ImageCodeGenerator implements ValidateCodeGenerator {

    @Getter
    @Setter
    SecurityProperties securityProperties;

    @Override
    public ValidateCode generate(ServletWebRequest request) {

        int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width",
                securityProperties.getValidate().getImage().getWidth());

        int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height",
                securityProperties.getValidate().getImage().getHeight());

        // 在内存中创建图象
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(getRandColor(230, 255));
        g.fillRect(0, 0, 100, 25);
        // 设定字体
        g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 18));
        // 产生0条干扰线，
        g.drawLine(0, 0, 0, 0);
        // 取随机产生的认证码(4位数字)
        String sRand = "";
        for (int i = 0; i < securityProperties.getValidate().getImage().getLength(); i++) {  //lenght是图形验证码的长度,即几位验证码
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            // 将认证码显示到图象中
            g.setColor(getRandColor(100, 150));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.drawString(rand, 15 * i + 6, 16);
        }
        for (int i = 0; i < (random.nextInt(5) + 5); i++) {
            g.setColor(new Color(random.nextInt(255) + 1, random.nextInt(255) + 1, random.nextInt(255) + 1));
            g.drawLine(random.nextInt(100), random.nextInt(30), random.nextInt(100), random.nextInt(30));
            // 图象生效
            g.dispose();
        }

        //image是图片，sRand是数字
        return new ImageCode(image, sRand, 60);
    }


    /**
     * 给定范围获得随机颜色
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
