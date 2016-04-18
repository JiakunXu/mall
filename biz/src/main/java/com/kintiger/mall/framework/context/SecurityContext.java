package com.kintiger.mall.framework.context;

import java.util.HashMap;
import java.util.Map;

import com.kintiger.mall.api.user.bo.User;

/**
 * 
 * @author
 * 
 */
public final class SecurityContext {

	private static final String USER = "USER";

	private static final ThreadLocal<Object> CONTEXT = new ThreadLocal<Object>() {
		protected Object initialValue() {
			return new HashMap<Object, Object>();
		}
	};

	private SecurityContext() {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, User> getContext() {
		return (Map) CONTEXT.get();
	}

	public static void clear() {
		getContext().clear();
	}

	public static User getUser() {
		return (User) getContext().get(USER);
	}

	public static void setUser(User user) {
		getContext().put(USER, user);
	}

}
