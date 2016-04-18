package com.kintiger.mall.dict.dao;

import java.util.List;

import com.kintiger.mall.api.dict.bo.Dict;
import com.kintiger.mall.api.dict.bo.DictType;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IDictDao {

	/**
	 * 
	 * @param dictType
	 * @return
	 */
	int getDictTypeCount(DictType dictType);

	/**
	 * 
	 * @param dictType
	 * @return
	 */
	List<DictType> getDictTypeList(DictType dictType);

	/**
	 * 关联 dict type 查询 dict.
	 * 
	 * @param dict
	 * @return
	 */
	int getDictCount(Dict dict);

	/**
	 * 关联 dict type 查询 dict.
	 * 
	 * @param dict
	 * @return
	 */
	List<Dict> getDictList(Dict dict);

	/**
	 * 
	 * @param dict
	 * @return
	 */
	List<Dict> getDicts(Dict dict);

	/**
	 * 
	 * @param dictType
	 * @return
	 */
	String createDictType(DictType dictType);

	/**
	 * 
	 * @param dict
	 * @return
	 */
	String createDict(Dict dict);

	/**
	 * 
	 * @param dict
	 * @return
	 */
	int updateDict(Dict dict);

	/**
	 * 
	 * @param dictType
	 * @return
	 */
	int updateDictType(DictType dictType);

	/**
	 * 
	 * @param dictType
	 * @return
	 */
	DictType getDictType(DictType dictType);

	/**
	 * 
	 * @param dict
	 * @return
	 */
	Dict getDict(Dict dict);

	/**
	 * 
	 * @param dict
	 * @return
	 */
	int deleteDict(Dict dict);

	/**
	 * 
	 * @param dictType
	 * @return
	 */
	int deleteDictType(DictType dictType);

}
