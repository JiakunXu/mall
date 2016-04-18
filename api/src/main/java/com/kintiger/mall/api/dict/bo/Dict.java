package com.kintiger.mall.api.dict.bo;

import java.util.Date;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 字典對象.
 * 
 * @author xujiakun
 * 
 */
public class Dict extends SearchInfo {

	private static final long serialVersionUID = -31231206571094321L;

	/**
	 * 字典项ID.
	 */
	private String dictId;

	/**
	 * 字典类型ID.
	 */
	private String dictTypeId;

	/**
	 * 字典项名称.
	 */
	private String dictName;

	/**
	 * 值.
	 */
	private String dictValue;

	private String state;

	private String remark;

	private String modifyUser;

	private Date modifyDate;

	private String dictTypeValue;

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getDictTypeId() {
		return dictTypeId;
	}

	public void setDictTypeId(String dictTypeId) {
		this.dictTypeId = dictTypeId;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyDate() {
		return modifyDate != null ? (Date) modifyDate.clone() : null;
	}

	public void setModifyDate(Date modifyDate) {
		if (modifyDate != null) {
			this.modifyDate = (Date) modifyDate.clone();
		}
	}

	public String getDictTypeValue() {
		return dictTypeValue;
	}

	public void setDictTypeValue(String dictTypeValue) {
		this.dictTypeValue = dictTypeValue;
	}

}
