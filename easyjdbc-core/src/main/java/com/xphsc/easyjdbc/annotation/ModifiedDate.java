package com.xphsc.easyjdbc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:
 * @date:
 * @since 2.0.5
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ModifiedDate {
}
