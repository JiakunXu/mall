package com.kintiger.mall.item.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.kintiger.mall.api.item.bo.Item;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.item.dao.IItemDao;

/**
 * 商品 dao 现实.
 * 
 * @author xujiakun
 * 
 */
public class ItemDaoImpl extends BaseDaoImpl implements IItemDao {

	@Override
	public int getItemCount(Item item) {
		return (Integer) getSqlMapClientTemplate().queryForObject("item.getItemCount", item);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Item> getItemList(Item item) {
		return (List<Item>) getSqlMapClientTemplate().queryForList("item.getItemList", item);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Item> getItemStats(String statementName, String shopId) {
		return (List<Item>) getSqlMapClientTemplate().queryForList(statementName, shopId);
	}

	@Override
	public String createItem(Item item) {
		return (String) getSqlMapClientTemplate().insert("item.createItem", item);
	}

	@Override
	public Item getItem(Item item) {
		return (Item) getSqlMapClientTemplate().queryForObject("item.getItem", item);
	}

	@Override
	public int updateItem(Item item) {
		return getSqlMapClientTemplate().update("item.updateItem1", item);
	}

	@Override
	public int updateItem(final String shopId, final List<Item> itemList, final String modifyUser) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (Item item : itemList) {
					item.setShopId(shopId.trim());
					item.setModifyUser(modifyUser);
					executor.insert("item.updateItem2", item);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

	@Override
	public int updateItem(String shopId, String[] itemId, String modifyUser) {
		Item item = new Item();
		item.setShopId(shopId.trim());
		item.setCodes(itemId);
		item.setModifyUser(modifyUser);

		return getSqlMapClientTemplate().update("item.updateItem3", item);
	}

	@Override
	public int deleteItem(Item item) {
		return getSqlMapClientTemplate().update("item.updateItem4", item);
	}

}
