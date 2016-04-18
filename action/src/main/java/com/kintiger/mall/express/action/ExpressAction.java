package com.kintiger.mall.express.action;

import java.util.List;

import com.kintiger.mall.api.express.IExpressService;
import com.kintiger.mall.api.express.bo.Express;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 物流.
 * 
 * @author xujiakun
 * 
 */
public class ExpressAction extends BaseAction {

	private static final long serialVersionUID = -2614085648961749452L;

	private IExpressService expressService;

	private List<Express> expressList;

	@JsonResult(field = "expressList", include = { "epsId", "epsName" }, total = "total")
	public String getExpressJsonList() {
		User user = this.getUser();
		if (user == null) {
			return LOGIN_TIMEOUT;
		}

		expressList = expressService.getExpressList();

		return JSON_RESULT;
	}

	public IExpressService getExpressService() {
		return expressService;
	}

	public void setExpressService(IExpressService expressService) {
		this.expressService = expressService;
	}

	public List<Express> getExpressList() {
		return expressList;
	}

	public void setExpressList(List<Express> expressList) {
		this.expressList = expressList;
	}

}
