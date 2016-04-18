package com.kintiger.mall.api.ip;

/**
 * ip.
 * 
 * @author xujiakun
 * 
 */
public interface IIPService {

	/**
	 * 验证本地 IP 库中是否已存在信息.
	 * 
	 * @param ip
	 */
	boolean validate(String ip);

}
