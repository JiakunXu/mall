package com.kintiger.mall.item.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alibaba.fastjson.JSON;
import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.item.IItemFileService;
import com.kintiger.mall.api.item.IItemService;
import com.kintiger.mall.api.item.IItemSkuService;
import com.kintiger.mall.api.item.IItemTagService;
import com.kintiger.mall.api.item.bo.Item;
import com.kintiger.mall.api.item.bo.ItemFile;
import com.kintiger.mall.api.item.bo.ItemSku;
import com.kintiger.mall.api.pageview.IPageviewService;
import com.kintiger.mall.api.pageview.bo.Pageview;
import com.kintiger.mall.api.spec.ISpecService;
import com.kintiger.mall.api.spec.bo.SpecCat;
import com.kintiger.mall.api.spec.bo.SpecItem;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.item.dao.IItemDao;

/**
 * 商品实现.
 * 
 * @author xujiakun
 * 
 */
public class ItemServiceImpl implements IItemService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(ItemServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IMemcachedCacheService memcachedCacheService;

	private ISpecService specService;

	private IItemFileService itemFileService;

	private IItemSkuService itemSkuService;

	private IItemTagService itemTagService;

	private IPageviewService pageviewService;

	private IItemDao itemDao;

	@Override
	public int getItemCount(String shopId, Item item) {
		// shopId 必填
		if (StringUtils.isBlank(shopId) || item == null) {
			return 0;
		}

		item.setShopId(shopId.trim());

		try {
			return itemDao.getItemCount(item);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(item), e);
		}

		return 0;
	}

	@Override
	public List<Item> getItemList(String shopId, Item item) {
		// shopId 必填
		if (StringUtils.isBlank(shopId) || item == null) {
			return null;
		}

		item.setShopId(shopId.trim());

		List<Item> itemList = getItemList(item);

		if (itemList == null || itemList.size() == 0) {
			return null;
		}

		String[] itemId = new String[itemList.size()];
		int i = 0;
		for (Item it : itemList) {
			itemId[i++] = it.getItemId();
		}

		// 2. 获取商品文件信息
		Map<String, List<ItemFile>> map = itemFileService.getItemFileList(shopId, itemId);

		// 不存在商品文件 直接返回
		if (map == null || map.isEmpty()) {
			return itemList;
		}

		for (Item it : itemList) {
			it.setItemFileList(map.get(it.getItemId()));
		}

		return itemList;
	}

	private List<Item> getItemList(Item item) {
		try {
			return itemDao.getItemList(item);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(item), e);
		}

		return null;
	}

	@Override
	public List<Item> getItemStats(String shopId, String type) {
		// shopId type 必填
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(type)) {
			return null;
		}

		if (IItemService.STATS_TYPE_SOLD.equals(type.trim())) {
			try {
				return itemDao.getItemStats("item.getItemStats4Sold", shopId);
			} catch (Exception e) {
				logger.error("shopId:" + shopId + "type:" + type, e);
			}

			return null;
		}

		if (IItemService.STATS_TYPE_SEARCH.equals(type.trim())) {
			try {
				return itemDao.getItemStats("item.getItemStats4Search", shopId);
			} catch (Exception e) {
				logger.error("shopId:" + shopId + "type:" + type, e);
			}

			return null;
		}

		if (IItemService.STATS_TYPE_STOCK.equals(type.trim())) {
			try {
				return itemDao.getItemStats("item.getItemStats4Stock", shopId);
			} catch (Exception e) {
				logger.error("shopId:" + shopId + "type:" + type, e);
			}

			return null;
		}

		if (IItemService.STATS_TYPE_EXCHANGE.equals(type.trim())) {
			try {
				return itemDao.getItemStats("item.getItemStats4Exchange", shopId);
			} catch (Exception e) {
				logger.error("shopId:" + shopId + "type:" + type, e);
			}

			return null;
		}

		return null;
	}

	@Override
	public List<Item> getItemStats(String shopId) {
		if (StringUtils.isBlank(shopId)) {
			return null;
		}

		String gmtEnd = DateUtil.getNowDateStr();
		String gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -7), DateUtil.DEFAULT_DATE_FORMAT);

		List<Pageview> pageviewList =
			pageviewService.getPageviewStats(shopId, IPageviewService.ACTION_NAME_ITEM, gmtStart, gmtEnd);

		if (pageviewList == null || pageviewList.size() == 0) {
			return null;
		}

		List<Item> itemList = new ArrayList<Item>();
		String[] itemIds = new String[pageviewList.size()];
		int i = 0;

		// 丛 url 中获取 itemId
		for (Pageview pv : pageviewList) {
			String[] parameter = pv.getParameter().split("&");
			for (String par : parameter) {
				String[] pars = par.split("=");
				if ("itemId".equals(pars[0])) {
					itemIds[i++] = pars[1];

					Item item = new Item();
					item.setItemId(pars[1]);
					item.setPv(pv.getPv());
					item.setUv(pv.getUv());
					itemList.add(item);

					break;
				}
			}
		}

		// 根据 itemIds 获取商品名称
		Item item = new Item();
		item.setShopId(shopId.trim());
		item.setCodes(itemIds);
		item.setStart(0);
		item.setLimit(itemIds.length);
		// 上架但是不一定有库存，查询所有
		item.setType("all");

		List<Item> list = getItemList(item);
		if (list == null || list.size() == 0) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();
		for (Item itme : list) {
			map.put(itme.getItemId(), itme.getItemName());
		}

		for (Item itme : itemList) {
			itme.setItemName(map.get(itme.getItemId()));
		}

		return itemList;
	}

	private BooleanResult validate(Item item) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (item == null) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		if (StringUtils.isEmpty(item.getItemName()) || item.getItemName().length() > 50) {
			result.setCode("商品名称不能为空或过长！");
			return result;
		}

		if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) < 0) {
			result.setCode("商品价格不能为空或为负！");
			return result;
		}

		if (item.getOrigin() == null || item.getOrigin().compareTo(BigDecimal.ZERO) < 0) {
			result.setCode("商品原价不能为空或为负！");
			return result;
		}

		if (item.getStock() < 0) {
			result.setCode("商品库存不能为负！");
			return result;
		}

		if (item.getPostage() == null || item.getPostage().compareTo(BigDecimal.ZERO) < 0) {
			result.setCode("商品邮费不能为空或负！");
			return result;
		}

		if (item.getQuota() < 0) {
			result.setCode("商品限额不能为负！");
			return result;
		}

		if (StringUtils.isBlank(item.getItemCid())) {
			result.setCode("商品所属类目不能为空！");
			return result;
		}

		result.setResult(true);
		return result;
	}

	private BooleanResult validate(List<ItemSku> skuList) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (skuList == null || skuList.size() == 0) {
			result.setResult(true);
			return result;
		}

		for (ItemSku sku : skuList) {
			if (sku.getPrice() == null || sku.getPrice().compareTo(BigDecimal.ZERO) < 0) {
				result.setCode("商品规格价格不能为空或负！");
				return result;
			}

			if (sku.getOrigin() == null || sku.getOrigin().compareTo(BigDecimal.ZERO) < 0) {
				result.setCode("商品规格原价不能为空或负！");
				return result;
			}

			if (StringUtils.isEmpty(sku.getProperties()) || sku.getProperties().length() > 50) {
				result.setCode("商品规格组合编号不能为空或编号过长！");
				return result;
			}

			if (StringUtils.isEmpty(sku.getPropertiesName()) || sku.getPropertiesName().length() > 100) {
				result.setCode("商品规格组合名称不能为空或名称过长！");
				return result;
			}

			if (StringUtils.isEmpty(sku.getCodes()) || sku.getCodes().length() > 10) {
				result.setCode("商品规格商品编码不能为空或编码过长！");
				return result;
			}
		}

		result.setResult(true);
		return result;
	}

	@Override
	public BooleanResult createItem(String shopId, final Item item, final String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		if (item == null) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(item.getItemName())) {
			result.setCode("商品名称不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		item.setShopId(shopId.trim());
		item.setModifyUser(modifyUser.trim());

		result = validate(item);
		if (!result.getResult()) {
			return result;
		}

		// if not null then 验证
		result = validate(item.getSkuList());
		if (!result.getResult()) {
			return result;
		}

		result = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);

				String itemId;
				// 1. 保存 商品 信息
				try {
					itemId = itemDao.createItem(item);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(item), e);
					ts.setRollbackOnly();

					result.setCode("创建商品信息失败！");
					return result;
				}

				// 2. 保存 sku 信息
				List<ItemSku> skuList = item.getSkuList();
				if (skuList != null && skuList.size() > 0) {
					result = itemSkuService.createItemSku(itemId, skuList, modifyUser);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				// 3. 保存 文件图片 信息
				List<ItemFile> itemFileList = item.getItemFileList();
				if (itemFileList != null && itemFileList.size() > 0) {
					result = itemFileService.createItemFile(itemId, itemFileList, modifyUser);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				result.setResult(true);
				return result;
			}
		});

		return result;
	}

	@Override
	public Item getItem(String shopId, String itemId) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(itemId)) {
			return null;
		}

		// 1. 获取商品基本信息
		Item item = new Item();
		item.setShopId(shopId.trim());
		item.setItemId(itemId.trim());

		item = getItem(item);

		if (item == null) {
			return null;
		}

		// 2. 获取商品文件信息
		item.setItemFileList(itemFileService.getItemFileList(shopId, itemId));

		// 3. 获取商品 sku 信息
		List<ItemSku> skuList = itemSkuService.getItemSkuList(shopId, itemId);

		// 不存在 sku 信息 直接返回
		if (skuList == null || skuList.size() == 0) {
			// 设置价格区间 = item 总表价格
			item.setOriginRange(item.getOrigin().toString());
			item.setPriceRange(item.getPrice().toString());

			return item;
		}

		// titleList 获取第一个 sku 从中获取 specCat 信息
		ItemSku sku = skuList.get(0);
		String[] properties = sku.getProperties().split(";");

		String[] specCId = new String[properties.length];
		int i = 0;
		for (String id : properties) {
			String[] cid = id.split(":");
			specCId[i++] = cid[0];
		}

		List<SpecCat> specCatList = specService.getSpecCatList(shopId.trim(), specCId);
		// 根据 specCId[] 重新排序
		if (specCatList != null && specCatList.size() > 0) {
			Map<String, SpecCat> map = new HashMap<String, SpecCat>();
			for (SpecCat sc : specCatList) {
				map.put(sc.getSpecCId(), sc);
			}

			specCatList = new ArrayList<SpecCat>();
			for (String cid : specCId) {
				specCatList.add(map.get(cid));
			}
		}

		// 遍历所有的已有 sku 获取 specItem 信息；遍历的同时，统计 金额最大最小
		// 原始价格 max0 min0
		// 会员价格 max1 min1
		BigDecimal max0 = BigDecimal.ZERO;
		BigDecimal min0 = new BigDecimal("100000000");
		BigDecimal max1 = BigDecimal.ZERO;
		BigDecimal min1 = new BigDecimal("100000000");

		String[] specItemId = new String[properties.length * skuList.size()];
		int j = 0;
		// 遍历所有
		for (ItemSku sk : skuList) {
			String[] ps = sk.getProperties().split(";");
			for (String id : ps) {
				String[] cid = id.split(":");
				specItemId[j++] = cid[1];
			}

			// 判断最大最小
			if (sku.getOrigin().compareTo(min0) == -1) {
				min0 = sku.getOrigin();
			}

			if (sku.getOrigin().compareTo(max0) == 1) {
				max0 = sku.getOrigin();
			}

			if (sku.getPrice().compareTo(min1) == -1) {
				min1 = sku.getPrice();
			}

			if (sku.getPrice().compareTo(max1) == 1) {
				max1 = sku.getPrice();
			}
		}

		// 设置价格区间 = item 总表价格
		item.setOriginRange(min0.toString() + " - " + max0.toString());
		item.setPriceRange(min1.toString() + " - " + max1.toString());

		List<SpecItem> specItemList = specService.getSpecItemList(shopId.trim(), specItemId);

		// 规格组合 黑色 大 ／ 红色 大
		if (specItemList != null && specItemList.size() > 0) {
			Map<String, String> map = new HashMap<String, String>();

			for (SpecItem specItem : specItemList) {
				map.put(specItem.getSpecItemId(), specItem.getSpecItemValue());
			}

			for (ItemSku sk : skuList) {
				List<String> specItemValueList = new ArrayList<String>();
				String[] ps = sk.getProperties().split(";");
				for (String id : ps) {
					String[] cid = id.split(":");
					specItemValueList.add(map.get(cid[1]));
				}

				sk.setSpecItemValueList(specItemValueList);
			}
		}

		// 选中的规格信息
		item.setSpecCat(JSON.toJSONString(specCatList).replace("'", "\\'"));
		item.setSpecItem(JSON.toJSONString(specItemList).replace("'", "\\'"));

		// sku 明细
		item.setSkuList(skuList);
		// 规格列表中 规格组合信息
		item.setSpecCatList(specCatList);

		return item;
	}

	@Override
	public Item getItem(String shopId, String itemId, String skuId) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(itemId)) {
			return null;
		}

		// 1. 获取商品基本信息
		Item item = new Item();
		item.setShopId(shopId.trim());
		item.setItemId(itemId.trim());

		item = getItem(item);

		if (item == null) {
			return null;
		}

		// 2. 获取商品文件信息
		item.setItemFileList(itemFileService.getItemFileList(shopId, itemId));

		if (StringUtils.isBlank(skuId)) {
			// 3. 获取商品 sku 信息
			ItemSku sku = itemSkuService.getItemSku(shopId, itemId, skuId);

			// 设置价格区间 = item 总表价格
			item.setOriginRange(sku.getOrigin().toString());
			item.setPriceRange(sku.getPrice().toString());
		} else {
			// 设置价格区间 = item 总表价格
			item.setOriginRange(item.getOrigin().toString());
			item.setPriceRange(item.getPrice().toString());
		}

		return item;
	}

	private Item getItem(Item item) {
		try {
			return itemDao.getItem(item);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(item), e);
		}

		return null;
	}

	@Override
	public BooleanResult updateItem(String shopId, final String itemId, final Item item, final String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(itemId)) {
			result.setCode("商品编号信息不能为空！");
			return result;
		}

		if (item == null) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(item.getItemName())) {
			result.setCode("商品名称不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		item.setShopId(shopId.trim());
		item.setItemId(itemId.trim());
		item.setModifyUser(modifyUser.trim());

		result = validate(item);
		if (!result.getResult()) {
			return result;
		}

		// if not null then 验证
		result = validate(item.getSkuList());
		if (!result.getResult()) {
			return result;
		}

		result = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);

				// 1. 修改 商品 信息
				try {
					int i = itemDao.updateItem(item);
					if (i != 1) {
						logger.error(LogUtil.parserBean(item));
						ts.setRollbackOnly();

						result.setCode("当前编辑商品已被删除！");
						return result;
					}
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(item), e);
					ts.setRollbackOnly();

					result.setCode("编辑商品信息失败！");
					return result;
				}

				// 2. 逻辑删除已有 sku 信息
				result = itemSkuService.deleteItemSku(itemId.trim(), modifyUser);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 3. 保存 sku 信息
				List<ItemSku> skuList = item.getSkuList();
				if (skuList != null && skuList.size() > 0) {
					result = itemSkuService.createItemSku(itemId.trim(), skuList, modifyUser);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				// 4. 逻辑删除已有 文件图片 信息
				result = itemFileService.deleteItemFile(itemId.trim(), modifyUser);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 5. 保存 文件图片 信息
				List<ItemFile> itemFileList = item.getItemFileList();
				if (itemFileList != null && itemFileList.size() > 0) {
					result = itemFileService.createItemFile(itemId.trim(), itemFileList, modifyUser);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				return result;
			}
		});

		return result;
	}

	@Override
	public BooleanResult updateItemStock(String shopId, List<Item> itemList, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		if (itemList == null || itemList.size() == 0) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		try {
			itemDao.updateItem(shopId, itemList, modifyUser);
			result.setResult(true);
		} catch (Exception e) {
			logger.error("shopId:" + shopId + LogUtil.parserBean(itemList) + "modifyUser:" + modifyUser, e);

			result.setCode("更新商品库存信息失败！");
			return result;
		}

		return result;
	}

	@Override
	public BooleanResult updateItemStock(String shopId, String[] itemId, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		if (itemId == null || itemId.length == 0) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		try {
			itemDao.updateItem(shopId, itemId, modifyUser);
			result.setResult(true);
		} catch (Exception e) {
			logger.error("shopId:" + shopId + LogUtil.parserBean(itemId) + "modifyUser:" + modifyUser, e);

			result.setCode("更新商品库存信息失败！");
			return result;
		}

		return result;
	}

	@Override
	public BooleanResult deleteItem(String shopId, final String itemId, final String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		final Item item = new Item();

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		item.setShopId(shopId.trim());

		if (StringUtils.isBlank(itemId)) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		item.setItemId(itemId.trim());

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人不能为空！");
			return result;
		}

		item.setModifyUser(modifyUser);

		// 事物处理 item item_file item_sku item_tag 表
		result = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);

				// 1. 逻辑删除商品信息
				try {
					int i = itemDao.deleteItem(item);
					if (i != 1) {
						logger.error(LogUtil.parserBean(item));
						ts.setRollbackOnly();

						result.setCode("当前删除商品已被删除！");
						return result;
					}
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(item), e);
					ts.setRollbackOnly();

					result.setCode("删除商品信息失败！");
					return result;
				}

				// 2. 逻辑删除商品规格信息
				result = itemSkuService.deleteItemSku(itemId.trim(), modifyUser);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 3. 逻辑删除商品文件信息
				result = itemFileService.deleteItemFile(itemId.trim(), modifyUser);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 4. 逻辑删除商品标签信息
				result = itemTagService.deleteItemTag(itemId.trim(), modifyUser);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				result.setResult(true);
				return result;
			}
		});

		return result;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public ISpecService getSpecService() {
		return specService;
	}

	public void setSpecService(ISpecService specService) {
		this.specService = specService;
	}

	public IItemFileService getItemFileService() {
		return itemFileService;
	}

	public void setItemFileService(IItemFileService itemFileService) {
		this.itemFileService = itemFileService;
	}

	public IItemSkuService getItemSkuService() {
		return itemSkuService;
	}

	public void setItemSkuService(IItemSkuService itemSkuService) {
		this.itemSkuService = itemSkuService;
	}

	public IItemTagService getItemTagService() {
		return itemTagService;
	}

	public void setItemTagService(IItemTagService itemTagService) {
		this.itemTagService = itemTagService;
	}

	public IPageviewService getPageviewService() {
		return pageviewService;
	}

	public void setPageviewService(IPageviewService pageviewService) {
		this.pageviewService = pageviewService;
	}

	public IItemDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(IItemDao itemDao) {
		this.itemDao = itemDao;
	}

}
