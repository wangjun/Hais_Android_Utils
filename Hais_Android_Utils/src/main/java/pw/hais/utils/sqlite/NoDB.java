package pw.hais.utils.sqlite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该字段 不参与数据库操作
 * @author Hello_海生
 * @date 2014年7月25日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDB {

	boolean auto() default true;

}
