package com.snailmann.security.core.validate.code.Service;

import com.snailmann.security.core.validate.code.entity.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeGenerate {

    ValidateCode generate(ServletWebRequest request);

}
