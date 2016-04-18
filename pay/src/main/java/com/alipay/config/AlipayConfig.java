package com.alipay.config;

/* *
 * 类名：AlipayConfig
 * 功能：基础配置类
 * 详细：设置帐户有关信息及返回路径
 * 版本：3.3
 * 日期：2012-08-10
 * 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 * 
 * 提示：如何获取安全校验码和合作身份者ID
 * 1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 * 2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 * 3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”
 * 
 * 安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 * 解决方法：
 * 1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 * 2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static final String partner = "2088601231846618";

	// 交易安全检验码，由数字和字母组成的32位字符串
	// 如果签名方式设置为“MD5”时，请设置该参数
	public static final String key = "wa7mo084caguugx0565m0xokxdbiifec";

	// 商户的私钥
	// 如果签名方式设置为“0001”时，请设置该参数
	public static final String private_key =
		"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMbG29Hlwbd+rM3m5aMXwaK4cNxqJPMKyFYgOhhhdjDLtGf3WsVe81hhW9fId1S27neuibl4HTNr1XRbtItm2N5ZBvciL6sqE6WHQNlJ1wW+duNHrW43WF5d/2s+KboA0U8fzlmL5tf/nleRtRwq1kUSl9Qj7ME+0RfW7AlcbpRHAgMBAAECgYEAjgT2eXkWduviuxoFaAlJJ+FvgYu06pEBocdUuqansyVXuBBaojm806B75igsWjJ5HrltXT4cXujkkUYUMKVCvBXIJsUMq952AycQt6r+V7EOHmUTnc3t9/Bxw7W+C+Fz6RU2zQ2dyIh1cVktt5NMsvX44ef1yv9IO4it3PKnvIECQQD7rd0JB/CsDSejPxcEEJ5fTm5CqeS6l7qbj9w59Dt4oori2FQ7F8StHNUDEeMw/lghS/sj48qE2GU4zGlnjiYZAkEAyjB86xR6h6MydUjAw+Iegc+KTXojsy+dg3yzfzD6+FQmHkHk7Nn59rXa1Bla/65XCXZn2DwfEbFYYXNwt2YZXwJBAJjdi4m2tunpu4+d/F7NsUP2IWaARptHGWLvSDYzMAQfxffuqYqS7zSudHs+3B2f/uuKeOndq95wiVqsXcE2R9ECQEFl+aHYF+HziYiiJae2ZgO665Mv5Ig66In1NeH4J9VW8pVq3u81ekKwg72DDgoRjBmFcQViPPJuGJY8zsKjhaUCQDvlg7sx66zHqTDB/Z5L5GqS/sJFMDT2B8KS6YGydBs6IaTNUrDmrNZ40/xOfMTfNgHRPEAUQRB62N2w/wGtUZw=";

	// 支付宝的公钥
	// 如果签名方式设置为“0001”时，请设置该参数
	public static final String ali_public_key =
		"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdxt7Ld2UfbmxK1meO+JJ9h8x65vYv920AIFIK +SwoaOGJR+vbalalH3tIXLCHbvqbkW4REFKhX57xl5rr1wPOwT7PK2UbVpVKD/pbJJFRRbnPBRZI 8yh0tFFBAOnaSecsMgN9bS9UMHnbRqjUHTbc0kUvi+eLNlZpzYgayIu1awIDAQAB";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 调试用，创建TXT日志文件夹路径
	public static final String log_path = "/tmp/alipay/";

	// 字符编码格式 目前支持 utf-8
	public static final String input_charset = "utf-8";

	// 签名方式，选择项：0001(RSA)、MD5
	public static final String sign_type = "0001";
	// 无线的产品中，签名方式为rsa时，sign_type需赋值为0001而不是RSA

}
