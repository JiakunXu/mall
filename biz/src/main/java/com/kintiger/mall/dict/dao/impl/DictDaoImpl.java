package com.kintiger.mall.dict.dao.impl;

import java.util.List;

import com.kintiger.mall.api.dict.bo.Dict;
import com.kintiger.mall.api.dict.bo.DictType;
import com.kintiger.mall.dict.dao.IDictDao;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;

/**
 * 
 * @author xujiakun
 * 
 */
public class DictDaoImpl extends BaseDaoImpl implements IDictDao {

	public int getDictTypeCount(DictType dictType) {
		return (Integer) getSqlMapClientTemplate().queryForObject("dict.getDictTypeCount", dictType);
	}

	@SuppressWarnings("unchecked")
	public List<DictType> getDictTypeList(DictType dictType) {
		return (List<DictType>) getSqlMapClientTemplate().queryForList("dict.getDictTypeList", dictType);
	}

	public int getDictCount(Dict dict) {
		return (Integer) getSqlMapClientTemplate().queryForObject("dict.getDictCount", dict);
	}

	@SuppressWarnings("unchecked")
	public List<Dict> getDictList(Dict dict) {
		return (List<Dict>) getSqlMapClientTemplate().queryForList("dict.getDictList", dict);
	}

	@SuppressWarnings("unchecked")
	public List<Dict> getDicts(Dict dict) {
		return (List<Dict>) getSqlMapClientTemplate().queryForList("dict.getDicts", dict);
	}

	public String createDict(Dict dict) {
		return (String) getSqlMapClientTemplate().insert("dict.createDict", dict);
	}

	public String createDictType(DictType dictType) {
		return (String) getSqlMapClientTemplate().insert("dict.createDictType", dictType);
	}

	public int updateDict(Dict dict) {
		return getSqlMapClientTemplate().update("dict.updateDict", dict);
	}

	public int updateDictType(DictType dictType) {
		return getSqlMapClientTemplate().update("dict.updateDictType", dictType);
	}

	public DictType getDictType(DictType dictType) {
		return (DictType) getSqlMapClientTemplate().queryForObject("dict.getDictType", dictType);
	}

	public Dict getDict(Dict dict) {
		return (Dict) getSqlMapClientTemplate().queryForObject("dict.getDict", dict);
	}

	public int deleteDict(Dict dict) {
		return getSqlMapClientTemplate().delete("dict.deleteDict", dict);
	}

	public int deleteDictType(DictType dictType) {
		return getSqlMapClientTemplate().delete("dict.deleteDictType", dictType);
	}

}
