package com.kintiger.mall.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于获取当前用户权限点；if alias is empty then 获取当前function actions else 根据actionName +
 * alias验证当前用户是否有权限.
 * 
 * example:
 * 
 * @ActionLog(alias = "")
 * 
 * @author xujiakun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FunAction {

	/**
	 * action操作名称.
	 */
	String alias() default "";

}
