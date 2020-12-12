package io.github.karlatemp.mxlib.annotations.injector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    static final String NAME_UNSET = "#$#$NAME+UNSET#$$#";

    Class<?> value() default Void.class;

    String name() default NAME_UNSET;

    boolean nullable() default false;
}
