package com.kintiger.mall.member.action;

import com.kintiger.mall.api.member.IMemberInfoService;
import com.kintiger.mall.api.member.bo.MemberInfo;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 会员信息.
 * 
 * @author jiakunxu
 * 
 */
public class MemberInfoAction extends BaseAction {

	private static final long serialVersionUID = -7676601341570517172L;

	private IMemberInfoService memberInfoService;

	private MemberInfo memberInfo;

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 查询个人信息.
	 * 
	 * @return
	 */
	public String searchMemberInfo() {

		memberInfo = memberInfoService.getMemberInfo(getUser().getMemId());

		return SUCCESS;
	}

	/**
	 * 人员修改.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "会员信息修改")
	public String updateMemberInfo() {
		Shop shop = this.getShop();
		if (shop == null) {
			this.setFailMessage("店铺信息不存在！");
			return RESULT_MESSAGE;
		}

		User user = getUser();

		BooleanResult result =
			memberInfoService.updateMemberInfo(user.getMemId(), user.getUserId(), shop.getShopId(), memberInfo);

		if (result.getResult()) {
			this.setSuccessMessage("会员信息修改成功！");
		} else {
			this.setFailMessage(result.getCode());
		}

		return RESULT_MESSAGE;
	}

	public IMemberInfoService getMemberInfoService() {
		return memberInfoService;
	}

	public void setMemberInfoService(IMemberInfoService memberInfoService) {
		this.memberInfoService = memberInfoService;
	}

	public MemberInfo getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(MemberInfo memberInfo) {
		this.memberInfo = memberInfo;
	}

}
