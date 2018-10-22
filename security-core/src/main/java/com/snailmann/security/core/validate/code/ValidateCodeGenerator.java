package com.snailmann.security.core.validate.code;

import com.snailmann.security.core.validate.code.entity.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeGenerator {


    ValidateCode generate(ServletWebRequest request);

}
