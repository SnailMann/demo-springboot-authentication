/**
 * 
 */
package com.snailmann.security.core.validate.code;

import java.util.Map;

import com.snailmann.security.core.constant.ValidateCodeType;
import com.snailmann.security.core.validate.code.exception.ValidateCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 系统中的校验码处理器
 */
@Component
public class ValidateCodeProcessorHolder {

	@Autowired
	private Map<String, ValidateCodeProcessor> validateCodeProcessors;   //获得所有注册到Spring容器中的ValidateCodeProcessor实例

	public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
		return findValidateCodeProcessor(type.toString().toLowerCase());
	}

	public ValidateCodeProcessor findValidateCodeProcessor(String type) {
		String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();  //type的小写+ValidateCodeProcessor拼接都是对应的type的Processer，这对类的命名是有要求的
		ValidateCodeProcessor processor = validateCodeProcessors.get(name);  //根据凭借的name获取到对应的ValidateCodeProcessor
		if (processor == null) {
			throw new ValidateCodeException("验证码处理器" + name + "不存在");
		}
		return processor;
	}

}
