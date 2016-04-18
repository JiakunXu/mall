package com.kintiger.mall.api.member.bo;

import com.kintiger.mall.api.user.bo.User;

/**
 * 会员补充信息.
 * 
 * @author xujiakun
 * 
 */
public class MemberInfo extends User {

	private static final long serialVersionUID = 371579041912475217L;

	private String id;

	/**
	 * sex.
	 */
	private String sex;

	/**
	 * 生日 yyyy-mm-dd.
	 */
	private String birthday;

	/**
	 * 地址.
	 */
	private String address;

	/**
	 * 邮政编码.
	 */
	private String postalCode;

	/**
	 * 行业 职业.
	 */
	private String profession;

	/**
	 * 学历.
	 */
	private String education;

	/**
	 * 婚姻状况 Y or N.
	 */
	private String maritalStatus;

	/**
	 * 结婚纪念日.
	 */
	private String weddingDay;

	/**
	 * 关联赠送积分表.
	 */
	private String memPointsId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getWeddingDay() {
		return weddingDay;
	}

	public void setWeddingDay(String weddingDay) {
		this.weddingDay = weddingDay;
	}

	public String getMemPointsId() {
		return memPointsId;
	}

	public void setMemPointsId(String memPointsId) {
		this.memPointsId = memPointsId;
	}

}
