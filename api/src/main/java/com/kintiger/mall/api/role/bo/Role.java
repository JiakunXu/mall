package com.kintiger.mall.api.role.bo;

import java.util.Date;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 角色.
 * 
 * @author xujiakun
 * 
 */
public class Role extends SearchInfo {

	private static final long serialVersionUID = -5100230087589323253L;

	private String roleId;

	private String roleName;

	private String remark;

	private String state;

	/**
	 * 字典id item value.
	 */
	private String type;

	private Date createDate;

	/**
	 * 修改人id.
	 */
	private String modifyUser;

	/**
	 * 是否默认拥有　(Y是/N否).
	 */
	private String defaults;

	private Date modifyDate;

	/**
	 * 过期日期.
	 */
	private String expireDate;

	/**
	 * 过期天数.
	 */
	private String expireDays;

	/**
	 * station_role.id or position_role.id or user_role.id.
	 */
	private String id;

	/**
	 * 岗位.
	 */
	private String staId;

	/**
	 * 编制.
	 */
	private String posId;

	/**
	 * 菜单id.
	 */
	private String menuId;

	private String userId;

	private String passport;

	private String userName;

	private int menuCount;

	private int stationCount;

	private int userCount;

	private int positionCount;

	/**
	 * dict.
	 */
	private String dictName;

	private String staState;

	private String userState;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateDate() {
		return createDate != null ? (Date) createDate.clone() : null;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			this.createDate = (Date) createDate.clone();
		}
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getDefaults() {
		return defaults;
	}

	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}

	public Date getModifyDate() {
		return modifyDate != null ? (Date) modifyDate.clone() : null;
	}

	public void setModifyDate(Date modifyDate) {
		if (modifyDate != null) {
			this.modifyDate = (Date) modifyDate.clone();
		}
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getExpireDays() {
		return expireDays;
	}

	public void setExpireDays(String expireDays) {
		this.expireDays = expireDays;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStaId() {
		return staId;
	}

	public void setStaId(String staId) {
		this.staId = staId;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getMenuCount() {
		return menuCount;
	}

	public void setMenuCount(int menuCount) {
		this.menuCount = menuCount;
	}

	public int getStationCount() {
		return stationCount;
	}

	public void setStationCount(int stationCount) {
		this.stationCount = stationCount;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public int getPositionCount() {
		return positionCount;
	}

	public void setPositionCount(int positionCount) {
		this.positionCount = positionCount;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getStaState() {
		return staState;
	}

	public void setStaState(String staState) {
		this.staState = staState;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

}
