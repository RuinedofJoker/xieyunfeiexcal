package org.lin.changeexcal.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelTable {
    String[] name();
    char[] title() default {};
}
