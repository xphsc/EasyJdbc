package com.xphsc.easyjdbc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: 用于标记实体类中表示修改时间的字段默认填充时间值
 * For reference, the example
 * public class User {
 *     @ModifiedDate
 *     private LocalDateTime updateTime;
 * }
 * @since 2.0.5
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ModifiedDate {
}
