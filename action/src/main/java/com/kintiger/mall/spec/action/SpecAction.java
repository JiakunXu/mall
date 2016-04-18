package com.kintiger.mall.spec.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.select.bo.Select;
import com.kintiger.mall.api.spec.ISpecService;
import com.kintiger.mall.api.spec.bo.SpecCat;
import com.kintiger.mall.api.spec.bo.SpecItem;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 商品规格.
 * 
 * @author xujiakun
 * 
 */
public class SpecAction extends BaseAction {

	private static final long serialVersionUID = -1034364952231311631L;

	private ISpecService specService;

	private List<Select> selectList;

	/**
	 * 查询规格明细.
	 */
	private String specCId;

	/**
	 * 创建规格明细.
	 */
	private String specItemId;

	/**
	 * 创建规格明细.
	 */
	private String specItemValue;

	@JsonResult(field = "selectList", include = { "id", "text" })
	public String getSpecCatJsonList() {
		User user = this.getUser();
		if (user == null) {
			return JSON_RESULT;
		}

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		List<SpecCat> specCatList = specService.getSpecCatList(shopId);

		if (specCatList == null || specCatList.size() == 0) {
			return JSON_RESULT;
		}

		selectList = new ArrayList<Select>();

		for (SpecCat sc : specCatList) {
			Select s = new Select();
			s.setId(sc.getSpecCId());
			s.setText(sc.getSpecCName());

			selectList.add(s);
		}

		return JSON_RESULT;
	}

	@JsonResult(field = "selectList", include = { "id", "text" })
	public String getSpecItemJsonList() {
		User user = this.getUser();
		if (user == null) {
			return JSON_RESULT;
		}

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		List<SpecItem> specItemList = specService.getSpecItemList(shopId, specCId);

		if (specItemList == null || specItemList.size() == 0) {
			return JSON_RESULT;
		}

		selectList = new ArrayList<Select>();

		for (SpecItem si : specItemList) {
			Select s = new Select();
			s.setId(si.getSpecItemId());
			s.setText(si.getSpecItemValue());

			selectList.add(s);
		}

		return JSON_RESULT;
	}

	/**
	 * 创建规格明细.
	 * 
	 * @return
	 */
	@JsonResult(field = "specItemId")
	public String createSpecItem() {
		HttpServletResponse response = this.getServletResponse();
		// 初始化 返回失败
		response.setStatus(500);
		specItemId = "规格信息创建失败，请稍后再试！";

		User user = this.getUser();
		if (user == null) {
			return JSON_RESULT;
		}

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		BooleanResult result = specService.createSpecItem(shopId, specCId, specItemValue, user.getUserId());
		specItemId = result.getCode();

		if (result.getResult()) {
			response.setStatus(200);
		}

		return JSON_RESULT;
	}

	public ISpecService getSpecService() {
		return specService;
	}

	public void setSpecService(ISpecService specService) {
		this.specService = specService;
	}

	public List<Select> getSelectList() {
		return selectList;
	}

	public void setSelectList(List<Select> selectList) {
		this.selectList = selectList;
	}

	public String getSpecCId() {
		return specCId;
	}

	public void setSpecCId(String specCId) {
		this.specCId = specCId;
	}

	public String getSpecItemId() {
		return specItemId;
	}

	public void setSpecItemId(String specItemId) {
		this.specItemId = specItemId;
	}

	public String getSpecItemValue() {
		return specItemValue;
	}

	public void setSpecItemValue(String specItemValue) {
		this.specItemValue = specItemValue;
	}

}
