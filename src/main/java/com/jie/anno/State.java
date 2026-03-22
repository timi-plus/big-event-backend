package com.jie.anno;


import com.jie.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented// 注解文档
@Target({ ElementType.FIELD})// 注解作用目标
@Retention(RetentionPolicy.RUNTIME)// 注解保留时间
@Constraint(validatedBy = {StateValidation.class})// 指定校验器

public @interface State {

    // 校验提示信息
    String message() default "state参数的值只能是已发布或者草稿";

    // 分组
    Class<?>[] groups() default {};
    // 负载
    Class<? extends Payload>[] payload() default {};
}
