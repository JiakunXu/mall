package com.kintiger.mall.item.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.kintiger.mall.api.item.bo.ItemSku;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.item.dao.IItemSkuDao;

/**
 * sku.
 * 
 * @author xujiakun
 * 
 */
public class ItemSkuDaoImpl extends BaseDaoImpl implements IItemSkuDao {

	@Override
	public int createItemSku(final String itemId, final List<ItemSku> skuList, final String verId,
		final String modifyUser) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (ItemSku sku : skuList) {
					sku.setItemId(itemId);
					sku.setVerId(verId);
					sku.setModifyUser(modifyUser);
					executor.insert("item.sku.createItemSku", sku);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

	@Override
	public ItemSku getItemSku(ItemSku sku) {
		return (ItemSku) getSqlMapClientTemplate().queryForObject("item.sku.getItemSku", sku);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemSku> getItemSkuList(ItemSku sku) {
		return (List<ItemSku>) getSqlMapClientTemplate().queryForList("item.sku.getItemSkuList", sku);
	}

	public int deleteItemSku(ItemSku sku) {
		return getSqlMapClientTemplate().update("item.sku.deleteItemSku", sku);
	}

	public int updateItemSku(final List<ItemSku> skuList, final String modifyUser) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (ItemSku sku : skuList) {
					sku.setModifyUser(modifyUser);
					executor.update("item.sku.updateItemSku", sku);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

}
