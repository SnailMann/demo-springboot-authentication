package com.snailmann.security.core.validate.code.processor;

import com.snailmann.security.core.constant.ValidateCodeType;
import com.snailmann.security.core.validate.code.generator.ValidateCodeGenerator;
import com.snailmann.security.core.validate.code.entity.ValidateCode;
import com.snailmann.security.core.validate.code.exception.ValidateCodeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * 可以说这是一个模板方法设计模式的体现
 *
 *
 * AbstractValidateCodeProcessor 是一个通用的抽象类Processor
 * 相同的行为都定义在此处，不同行为都定义在子类
 *
 * 所以我们可以看到create,save,validate等行为都定义在了AbstractValidateCodeProcessor
 * 而不相同的send，generator方法则分别交给对应的子类去处理
 * @param <C>
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();



    /**
     * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
     * 把所有的ValidateCodeGenerator装进map中
     */
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;


    /**
     * 生成code需要经历的三个模板步骤
     *
     * 模板设计模式，定义算法的模板
     * 相同方法在父类实现，例如create,save
     * 不同方法在子类实现，例如send
     * @param request
     * @throws Exception
     */
    @Override
    public void create(ServletWebRequest request) throws Exception {
        C validateCode = generate(request);   //生成验证码
        save(request, validateCode);          //保存验证码到session中
        send(request, validateCode);          //发送到客户端,呈现给用户
    }

    /**
     * 生成校验码
     *
     * 算是策略设计模式的体现，根据传入的算法的不同，而执行不同的行为
     * 这里是拿到不同的generator，而执行不同的行为
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private C generate(ServletWebRequest request) {
        String type = getValidateCodeType(request).toString().toLowerCase();
        String generatorName = type + "CodeGenerator";   //拼接具体类型的Generator的名字
        //
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
        if (validateCodeGenerator == null) {
            throw new ValidateCodeException("验证码生成器" + generatorName + "不存在");
        }
        return (C) validateCodeGenerator.generate(request);
    }

    /**
     * 保存校验码到Session中，在验证码生成的时候
     *
     * @param request
     * @param validateCode
     */
    private void save(ServletWebRequest request, C validateCode) {
        sessionStrategy.setAttribute(request, getSessionKey(request), validateCode);
    }



    /**
     * 发送校验码，由子类实现
     *
     * @param request
     * @param validateCode
     * @throws Exception
     */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;


    /**
     * 根据请求的url获取校验码的类型
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        //基本上实际调用该方法的是子类，所以获得的type是image或则sms
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");

        //然后通过判断是image还是sms获取对应的常量
        return ValidateCodeType.valueOf(type.toUpperCase());
    }


    /**
     * 构建验证码放入session时的key
     *
     * @param request
     * @return
     */
    private String getSessionKey(ServletWebRequest request) {
        return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
    }


    /**
     * 验证码的校验逻辑
     * 因为Image类型和SMS类型的验证码校验逻辑几乎相同，所以抽象到AbstractValidateCodeProcessor去实现
     * @param request
     */
    @SuppressWarnings("unchecked")
    @Override
    public void validate(ServletWebRequest request) {

        ValidateCodeType processorType = getValidateCodeType(request);  //获得验证码类型 image or sms
        String sessionKey = getSessionKey(request);                    //获得对应的Key

        C codeInSession = (C) sessionStrategy.getAttribute(request, sessionKey); //从Session中得到所存储的ImageCode实体类或者ValidateCode实体类（即生成验证码时存放到了session中）
        String codeInRequest;

        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),   //我们在从用户从request传递过来的验证码
                    processorType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException(processorType + "验证码不存在");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException(processorType + "验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码不匹配");
        }

        //如果都通过了，则说明验证码正常，删除session中存储的验证码实体类。作用是防止该验证码一直在session中生效
        // pass
        sessionStrategy.removeAttribute(request, sessionKey);
    }

}
