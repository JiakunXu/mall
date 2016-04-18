package com.kintiger.mall.item.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.kintiger.mall.api.item.bo.ItemFile;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.item.dao.IItemFileDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class ItemFileDaoImpl extends BaseDaoImpl implements IItemFileDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemFile> getItemFileList(ItemFile itemFile) {
		return (List<ItemFile>) getSqlMapClientTemplate().queryForList("item.file.getItemFileList", itemFile);
	}

	@Override
	public int deleteItemFile(ItemFile itemFile) {
		return getSqlMapClientTemplate().update("item.file.deleteItemFile", itemFile);
	}

	@Override
	public int createItemFile(final String itemId, final List<ItemFile> itemFileList, final String modifyUser) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (ItemFile file : itemFileList) {
					file.setItemId(itemId);

					if (StringUtils.isBlank(file.getFileId())) {
						continue;
					}

					file.setModifyUser(modifyUser);
					executor.insert("item.file.createItemFile", file);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

}
