package com.kintiger.mall.tag.dao;

import java.util.List;

import com.kintiger.mall.api.tag.bo.Tag;

/**
 * 
 * @author xujiakun
 * 
 */
public interface ITagDao {

	/**
	 * 
	 * @param tag
	 * @return
	 */
	List<Tag> getTagList(Tag tag);

}
