package pw.hais.utils.sqlite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 設置該字段為表的 主鍵
 * @author 韦海生
 * @date 2014年7月15日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

	boolean auto() default true;		//默认为 自增


}