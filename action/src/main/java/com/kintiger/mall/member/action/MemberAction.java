package com.kintiger.mall.member.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.member.IMemberLevelService;
import com.kintiger.mall.api.member.IMemberService;
import com.kintiger.mall.api.member.bo.Member;
import com.kintiger.mall.api.member.bo.MemberLevel;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.annotation.Decode;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.ArithUtil;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.util.ExcelUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 会员管理.
 * 
 * @author xujiakun
 * 
 */
public class MemberAction extends BaseAction {

	private static final long serialVersionUID = 6195426642069833833L;

	private Logger4jExtend logger = Logger4jCollection.getLogger(MemberAction.class);

	private IMemberService memberService;

	private IMemberLevelService memberLevelService;

	private int total;

	private List<Member> memberList;

	/**
	 * 查询条件: 会员姓名.
	 */
	@Decode
	private String userName;

	/**
	 * 查询条件：等级名称.
	 */
	@Decode
	private String levelId;

	/**
	 * 会员等级.
	 */
	private List<MemberLevel> memLevelList;

	/**
	 * 会员信息.
	 */
	private Member member;

	private int total0;

	private String total1;

	private String total7;

	private String total30;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 会员管理首页.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "会员管理")
	public String index() {
		String shopId = getUser().getShopId();

		String gmt = DateUtil.getNowDateStr();

		String gmt1 = DateUtil.getDateTime(DateUtil.addDays(new Date(), -1), DateUtil.DEFAULT_DATE_FORMAT);

		String gmt7 = DateUtil.getDateTime(DateUtil.addDays(new Date(), -7), DateUtil.DEFAULT_DATE_FORMAT);

		String gmt30 = DateUtil.getDateTime(DateUtil.addDays(new Date(), -30), DateUtil.DEFAULT_DATE_FORMAT);

		// 累计会员数
		total = memberService.getMemberCountStats(shopId, null, null);

		// 1天前会员数
		int t1 = memberService.getMemberCountStats(shopId, null, gmt1);

		total1 = String.valueOf(ArithUtil.mul(ArithUtil.div(total - t1, total, 4), 100));

		// 7天前会员数
		int t7 = memberService.getMemberCountStats(shopId, null, gmt7);

		total7 = String.valueOf(ArithUtil.mul(ArithUtil.div(total - t7, total, 4), 100));

		// 30天前会员数
		int t30 = memberService.getMemberCountStats(shopId, null, gmt30);

		total30 = String.valueOf(ArithUtil.mul(ArithUtil.div(total - t30, total, 4), 100));

		// 新增会员数
		total0 = memberService.getMemberCountStats(shopId, gmt, gmt);

		return SUCCESS;
	}

	@JsonResult(field = "memberList", include = { "dates", "count" })
	public String getMemberStats() {
		User user = getUser();

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		String gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -30), DateUtil.DEFAULT_DATE_FORMAT);
		String gmtEnd = DateUtil.getNowDateStr();

		memberList = memberService.getMemberStats(shopId, gmtStart, gmtEnd);

		return JSON_RESULT;
	}

	/**
	 * 会员查询.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "会员查询")
	public String searchMember() {
		User u = this.getUser();

		String companyId = u.getCompanyId();

		if (StringUtils.isBlank(companyId)) {
			return LOGIN_TIMEOUT;
		}

		memLevelList = memberLevelService.getMemberLevelList(companyId);

		return "searchMember";
	}

	@JsonResult(field = "memberList", include = { "memId", "userId", "userName", "levelName", "points",
		"surplusPoints", "mobile", "sex", "birthday", "address", "postalCode", "profession", "education",
		"maritalStatus", "weddingDay" }, total = "total")
	public String getMemberJsonList() {
		User u = this.getUser();
		if (u == null) {
			return LOGIN_TIMEOUT;
		}

		String shopId = u.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		Member m = new Member();
		m = getSearchInfo(m);

		if (StringUtils.isNotBlank(userName)) {
			m.setUserName(userName.trim());
		}

		if (StringUtils.isNotBlank(levelId)) {
			m.setLevelId(levelId.trim());
		}

		total = memberService.getMemberCount(shopId, m);

		if (total > 0) {
			memberList = memberService.getMemberList(shopId, m);
		}

		return JSON_RESULT;
	}

	/**
	 * 会员信息导出.
	 */
	public String exportMemberData() {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return RESULT_MESSAGE;
		}

		Member m = new Member();

		if (StringUtils.isNotBlank(userName)) {
			m.setUserName(userName.trim());
		}

		if (StringUtils.isNotBlank(levelId)) {
			m.setLevelId(levelId.trim());
		}

		total = memberService.getMemberCount(shopId, m);

		if (total > 0) {
			m.setStart(0);
			m.setLimit(total);
			memberList = memberService.getMemberList(shopId, m);
		}

		List<String> props = new ArrayList<String>();
		props.add("memId");
		props.add("userName");
		props.add("levelName");
		props.add("points");
		props.add("mobile");
		props.add("sex");
		props.add("birthday");
		props.add("address");
		props.add("postalCode");
		props.add("profession");
		props.add("education");
		props.add("maritalStatus");
		props.add("weddingDay");

		try {
			ExcelUtil eu = new ExcelUtil();

			inputStream =
				Thread.currentThread().getContextClassLoader().getResource("resource/exportMemberData.xls")
					.openStream();

			HttpServletResponse response = this.getServletResponse();
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment; filename=\""
				+ new String("会员信息导出".getBytes("GBK"), ("ISO8859-1")) + ".xls\"");
			outputStream = response.getOutputStream();

			eu.createExcelWithTemplate(inputStream, outputStream, props, memberList);

			outputStream.flush();
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}

		return RESULT_MESSAGE;
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 会员中心.
	 * 
	 * @return
	 */
	public String memberCenter() {
		// 会员信息
		member = memberService.getMember(getShop().getShopId(), getUser().getUserId());

		return SUCCESS;
	}

	/**
	 * 会员信息 积分信息 说明.
	 * 
	 * @return
	 */
	public String member() {
		// 获取会员等级
		memLevelList = memberLevelService.getMemberLevelList(getShop().getShopId());

		return SUCCESS;
	}

	public IMemberService getMemberService() {
		return memberService;
	}

	public void setMemberService(IMemberService memberService) {
		this.memberService = memberService;
	}

	public IMemberLevelService getMemberLevelService() {
		return memberLevelService;
	}

	public void setMemberLevelService(IMemberLevelService memberLevelService) {
		this.memberLevelService = memberLevelService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<Member> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<Member> memberList) {
		this.memberList = memberList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public List<MemberLevel> getMemLevelList() {
		return memLevelList;
	}

	public void setMemLevelList(List<MemberLevel> memLevelList) {
		this.memLevelList = memLevelList;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public int getTotal0() {
		return total0;
	}

	public void setTotal0(int total0) {
		this.total0 = total0;
	}

	public String getTotal1() {
		return total1;
	}

	public void setTotal1(String total1) {
		this.total1 = total1;
	}

	public String getTotal7() {
		return total7;
	}

	public void setTotal7(String total7) {
		this.total7 = total7;
	}

	public String getTotal30() {
		return total30;
	}

	public void setTotal30(String total30) {
		this.total30 = total30;
	}

}
