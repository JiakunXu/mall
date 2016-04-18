package com.kintiger.mall.api.dict;

import java.util.List;
import java.util.Map;

import com.kintiger.mall.api.dict.bo.Dict;
import com.kintiger.mall.api.dict.bo.DictType;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 字典接口.
 * 
 * @author xujiakun
 * 
 */
public interface IDictService {

	String ERROR_MESSAGE = "操作失败！";

	String ERROR_INPUT_MESSAGE = "操作失败，输入有误！";

	String ERROR_NULL_MESSAGE = "操作失败，单据已不存在！";

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
	 * 不存在分页.
	 * 
	 * @param dict
	 * @return
	 */
	List<Dict> getDicts(Dict dict);

	/**
	 * 不存在分页.
	 * 
	 * @param dictTypeValue
	 * @return
	 */
	Map<String, String> getDictMap(String dictTypeValue);

	/**
	 * 
	 * @param dictType
	 * @return
	 */
	BooleanResult createDictType(DictType dictType);

	/**
	 * 
	 * @param dict
	 * @return
	 */
	BooleanResult createDict(Dict dict);

	/**
	 * 
	 * @param dict
	 * @return
	 */
	BooleanResult updateDict(Dict dict);

	/**
	 * 
	 * @param dictType
	 * @return
	 */
	BooleanResult updateDictType(DictType dictType);

	/**
	 * 逻辑删除dictType and dict.
	 * 
	 * @param dictType
	 * @return
	 */
	BooleanResult deleteDictType(DictType dictType);

	/**
	 * 逻辑删除dict.
	 * 
	 * @param dict
	 * @return
	 */
	BooleanResult deleteDict(Dict dict);

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

}
