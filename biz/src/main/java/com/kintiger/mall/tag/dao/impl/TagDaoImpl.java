package com.kintiger.mall.tag.dao.impl;

import java.util.List;

import com.kintiger.mall.api.tag.bo.Tag;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.tag.dao.ITagDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class TagDaoImpl extends BaseDaoImpl implements ITagDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> getTagList(Tag tag) {
		return (List<Tag>) getSqlMapClientTemplate().queryForList("tag.getTagList", tag);
	}

}
