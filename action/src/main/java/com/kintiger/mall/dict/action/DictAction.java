package com.kintiger.mall.dict.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.dict.IDictService;
import com.kintiger.mall.api.dict.bo.Dict;
import com.kintiger.mall.api.dict.bo.DictType;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.annotation.Decode;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 系统参数.
 * 
 * @author xujiakun
 * 
 */
public class DictAction extends BaseAction {

	private static final long serialVersionUID = 5042752280539471298L;

	private Logger4jExtend logger = Logger4jCollection.getLogger(DictAction.class);

	private List<Dict> dictList = new ArrayList<Dict>();

	private List<DictType> dictTypeList = new ArrayList<DictType>();

	private IDictService dictService;

	private int total;

	@Decode
	private String dictTypeName;

	@Decode
	private String remark;

	@Decode
	private String dictTypeValue;

	private String dictTypeId;

	private String dictId;

	private String dictValue;

	private Dict dict;

	private DictType dictType;

	/**
	 * 查询DictType.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "系统参数查询")
	public String searchDictType() {
		return "searchDictType";
	}

	/**
	 * 查询DictType.
	 * 
	 * @return
	 */
	@JsonResult(field = "dictTypeList", include = { "dictTypeId", "dictTypeName", "dictTypeValue", "remark",
		"modifyDate" }, total = "total")
	public String getDictTypeJsonList() {
		DictType m = new DictType();
		m = getSearchInfo(m);

		if (StringUtils.isNotBlank(dictTypeName)) {
			m.setDictTypeName(dictTypeName.trim());
		}

		if (StringUtils.isNotBlank(dictTypeValue)) {
			m.setDictTypeValue(dictTypeValue.trim());
		}

		if (StringUtils.isNotBlank(remark)) {
			m.setRemark(remark.trim());
		}

		total = dictService.getDictTypeCount(m);

		if (total > 0) {
			dictTypeList = dictService.getDictTypeList(m);
		}

		return JSON_RESULT;
	}

	/**
	 * 
	 * @return
	 */
	@JsonResult(field = "dictList", include = { "dictId", "dictName", "dictValue", "remark", "modifyDate" }, total = "total")
	public String getDictJsonList() {
		Dict m = new Dict();
		m = getSearchInfo(m);

		if (StringUtils.isNotBlank(dictTypeId)) {
			try {
				m.setDictTypeId(dictTypeId.trim());
			} catch (NumberFormatException e) {
				logger.error("dictTypeId:" + dictTypeId, e);

				return JSON_RESULT;
			}
		} else {
			return JSON_RESULT;
		}

		total = dictService.getDictCount(m);
		if (total > 0) {
			dictList = dictService.getDictList(m);
		}

		return JSON_RESULT;
	}

	@JsonResult(field = "dictList", include = { "dictId", "dictName", "dictValue" })
	public String getDictList4ComboBox() {
		Dict m = new Dict();

		if (StringUtils.isNotBlank(dictTypeValue)) {
			m.setDictTypeValue(dictTypeValue.trim());
		} else {
			return JSON_RESULT;
		}

		if (StringUtils.isNotBlank(dictValue)) {
			m.setDictValue(dictValue.trim());
		}

		dictList = dictService.getDicts(m);

		return JSON_RESULT;
	}

	private boolean validateDictType(DictType dictType) {
		if (dictType == null) {
			return false;
		}

		if (StringUtils.isBlank(dictType.getDictTypeName()) || StringUtils.isBlank(dictType.getDictTypeValue())) {
			return false;
		}

		return true;
	}

	public String createDictTypePrepare() {
		return "createDictTypePrepare";
	}

	@ActionMonitor(actionName = "系统参数类型创建")
	public String createDictType() {
		if (!validateDictType(dictType)) {
			this.setFailMessage(IDictService.ERROR_INPUT_MESSAGE);
			return RESULT_MESSAGE;
		}

		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(IDictService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		dictType.setModifyUser(user.getUserId());

		BooleanResult booleanResult = dictService.createDictType(dictType);

		if (!booleanResult.getResult()) {
			this.setFailMessage(booleanResult.getCode());
		} else {
			this.setSuccessMessage("系统参数类型创建成功！");
		}

		return RESULT_MESSAGE;
	}

	private boolean validateDict(Dict dict) {
		if (dict == null) {
			return false;
		}

		if (dict.getDictTypeId() == null || StringUtils.isBlank(dict.getDictName())
			|| StringUtils.isBlank(dict.getDictValue())) {
			return false;
		}

		return true;
	}

	public String createDictPrepare() {
		if (StringUtils.isNotBlank(dictTypeName)) {
			try {
				dictTypeName = new String(dictTypeName.trim().getBytes("ISO8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error(dictTypeName, e);
			}
		}

		return "createDictPrepare";
	}

	@ActionMonitor(actionName = "系统参数明细创建")
	public String createDict() {
		if (!validateDict(dict)) {
			this.setFailMessage(IDictService.ERROR_INPUT_MESSAGE);
			return RESULT_MESSAGE;
		}

		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(IDictService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		dict.setModifyUser(user.getUserId());

		BooleanResult booleanResult = dictService.createDict(dict);
		if (!booleanResult.getResult()) {
			this.setFailMessage(booleanResult.getCode());
		} else {
			this.setSuccessMessage("系统参数明细创建成功！");
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 系统参数类型查看修改.
	 * 
	 * @return
	 */
	public String updateDictTypePrepare() {
		if (StringUtils.isNotBlank(dictTypeId)) {
			DictType type = new DictType();
			type.setDictTypeId(dictTypeId.trim());
			dictType = dictService.getDictType(type);
		}

		return "updateDictTypePrepare";
	}

	@ActionMonitor(actionName = "系统参数类型修改")
	public String updateDictType() {
		if (!validateDictType(dictType)) {
			this.setFailMessage(IDictService.ERROR_INPUT_MESSAGE);
			return RESULT_MESSAGE;
		}

		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(IDictService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		dictType.setModifyUser(user.getUserId());

		BooleanResult booleanResult = dictService.updateDictType(dictType);
		if (!booleanResult.getResult()) {
			this.setFailMessage(booleanResult.getCode());
		} else {
			this.setSuccessMessage("系统参数类型修改成功！");
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 系统参数明细查看修改.
	 * 
	 * @return
	 */
	public String updateDictPrepare() {
		if (StringUtils.isNotBlank(dictId)) {
			Dict d = new Dict();
			d.setDictId(dictId.trim());
			dict = dictService.getDict(d);
		}

		return "updateDictPrepare";
	}

	@ActionMonitor(actionName = "系统参数明细修改")
	public String updateDict() {
		if (!validateDict(dict)) {
			this.setFailMessage(IDictService.ERROR_INPUT_MESSAGE);
			return RESULT_MESSAGE;
		}

		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(IDictService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		dict.setModifyUser(user.getUserId());

		BooleanResult booleanResult = dictService.updateDict(dict);
		if (!booleanResult.getResult()) {
			this.setFailMessage(booleanResult.getCode());
		} else {
			this.setSuccessMessage("系统参数明细修改成功！");
		}

		return RESULT_MESSAGE;
	}

	@ActionMonitor(actionName = "系统参数类型删除")
	public String deleteDictType() {
		if (StringUtils.isBlank(dictTypeId)) {
			this.setFailMessage("被删除系统参数类型不能为空！");
			return RESULT_MESSAGE;
		}

		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(IDictService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		DictType d = new DictType();
		d.setDictTypeId(dictTypeId.trim());
		d.setModifyUser(user.getUserId());

		BooleanResult booleanResult = dictService.deleteDictType(d);
		if (!booleanResult.getResult()) {
			this.setFailMessage(booleanResult.getCode());
		} else {
			this.setSuccessMessage("系统参数类型删除成功！");
		}

		return RESULT_MESSAGE;

	}

	@ActionMonitor(actionName = "系统参数明细删除")
	public String deleteDict() {
		if (StringUtils.isBlank(dictId)) {
			this.setFailMessage("被删除系统参数明细不能为空！");
			return RESULT_MESSAGE;
		}

		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(IDictService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		Dict d = new Dict();
		d.setDictId(dictId.trim());
		d.setModifyUser(user.getUserId());

		BooleanResult booleanResult = dictService.deleteDict(d);

		if (!booleanResult.getResult()) {
			this.setFailMessage(booleanResult.getCode());
		} else {
			this.setSuccessMessage("系统参数明细删除成功！");
		}

		return RESULT_MESSAGE;
	}

	public List<Dict> getDictList() {
		return dictList;
	}

	public void setDictList(List<Dict> dictList) {
		this.dictList = dictList;
	}

	public List<DictType> getDictTypeList() {
		return dictTypeList;
	}

	public void setDictTypeList(List<DictType> dictTypeList) {
		this.dictTypeList = dictTypeList;
	}

	public IDictService getDictService() {
		return dictService;
	}

	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getDictTypeName() {
		return dictTypeName;
	}

	public void setDictTypeName(String dictTypeName) {
		this.dictTypeName = dictTypeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDictTypeValue() {
		return dictTypeValue;
	}

	public void setDictTypeValue(String dictTypeValue) {
		this.dictTypeValue = dictTypeValue;
	}

	public String getDictTypeId() {
		return dictTypeId;
	}

	public void setDictTypeId(String dictTypeId) {
		this.dictTypeId = dictTypeId;
	}

	public Dict getDict() {
		return dict;
	}

	public void setDict(Dict dict) {
		this.dict = dict;
	}

	public DictType getDictType() {
		return dictType;
	}

	public void setDictType(DictType dictType) {
		this.dictType = dictType;
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

}
