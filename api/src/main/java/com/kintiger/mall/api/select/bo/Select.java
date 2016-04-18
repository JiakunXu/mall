package com.kintiger.mall.api.select.bo;

import java.io.Serializable;

/**
 * Select2 控件.
 * 
 * @author xujiakun
 * 
 */
public class Select implements Serializable {

	private static final long serialVersionUID = -2390635998653315606L;

	private String id;

	private String text;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
