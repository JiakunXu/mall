package com.kintiger.mall.api.tag;

import java.util.List;

import com.kintiger.mall.api.tag.bo.Tag;

/**
 * 标签.
 * 
 * @author xujiakun
 * 
 */
public interface ITagService {

	/**
	 * 查询店铺 tag.
	 * 
	 * @param shopId
	 * @return
	 */
	List<Tag> getTagList(String shopId);

}
