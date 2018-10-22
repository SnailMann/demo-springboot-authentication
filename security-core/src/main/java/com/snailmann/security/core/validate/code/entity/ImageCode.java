package com.snailmann.security.core.validate.code.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 图形验证码
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageCode extends ValidateCode {

    //图片
    private BufferedImage image;


    /**
     *
     * @param image
     * @param code
     * @param expireInt 多少秒后过期，Second为单位
     */
    public ImageCode(BufferedImage image, String code, int expireInt) {
        super(code, expireInt);
        this.image = image;
    }


    public ImageCode(String code, LocalDateTime expireTime, BufferedImage image) {
        super(code, expireTime);
        this.image = image;
    }

}
