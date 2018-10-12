package com.snailmann.security.demo.validator;

import com.snailmann.security.demo.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MyConstraintValidator implements ConstraintValidator<MyConstraint,Object> {

    @Autowired
    private HelloService helloService;
    @Override
    public void initialize(MyConstraint constraintAnnotation) {
        System.out.println("My validator initialize");
    }

    /**
     * 真正的校验逻辑
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        helloService.greeting("jerry"); //vaildator里面可以注入我们想要的bean
        System.out.println(value);
        return false;
    }
}
