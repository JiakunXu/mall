package com.kintiger.mall.item.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.file.IFileService;
import com.kintiger.mall.api.file.bo.FileInfo;
import com.kintiger.mall.api.item.IItemService;
import com.kintiger.mall.api.item.bo.Item;
import com.kintiger.mall.api.item.bo.ItemFile;
import com.kintiger.mall.api.item.bo.ItemSku;
import com.kintiger.mall.api.shop.IShopService;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.annotation.Decode;
import com.kintiger.mall.framework.annotation.Pageview;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 商品管理.
 * 
 * @author xujiakun
 * 
 */
public class ItemAction extends BaseAction {

	private static final long serialVersionUID = -8410248557263387344L;

	private static final int PAGESIZE = 16;

	private Logger4jExtend logger = Logger4jCollection.getLogger(ItemAction.class);

	private IItemService itemService;

	private IFileService fileService;

	private IShopService shopService;

	private List<Item> itemStats4Sold;

	private List<Item> itemStats4Search;

	private List<Item> itemStats4Stock;

	private int total;

	private List<Item> itemList;

	/**
	 * 区别 出售中的商品 和 已售罄的商品.
	 */
	private String type;

	/**
	 * 是否上下架 Y or N.
	 */
	private String isDisplay;

	/**
	 * 查询条件: 商品名称.
	 */
	@Decode
	private String itemName;

	/**
	 * 查询条件: 货号.
	 */
	@Decode
	private String itemNo;

	/**
	 * 商品创建.
	 */
	private Item item;

	/**
	 * 商品文件.
	 */
	private String[] fileId;

	/**
	 * 商品创建 规格.
	 */
	private String[] properties;

	/**
	 * 商品创建 规格.
	 */
	private String[] propertiesName;

	/**
	 * 商品创建 价格.
	 */
	private String[] price;

	/**
	 * 商品创建 库存.
	 */
	private String[] stock;

	/**
	 * 商品编码.
	 */
	private String[] codes;

	/**
	 * 商品创建 市场价格.
	 */
	private String[] origin;

	/**
	 * 商品创建 主键.
	 */
	private String itemId;

	/**
	 * 店铺别名.
	 */
	private String alias;

	/**
	 * 首页统计 出售中的商品.
	 */
	private int countOfSold;

	/**
	 * 首页统计 已售罄的商品.
	 */
	private int countOfSoldout;

	/**
	 * 首页统计 仓库中的商品.
	 */
	private int countOfStock;

	/**
	 * 创建商品 修改商品 已上传图片.
	 */
	private List<FileInfo> fileList;

	/***
	 * 第 p 页.
	 */
	private int p;

	private Shop shop;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 商品管理首页.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "商品管理")
	public String index() {
		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return LOGIN_TIMEOUT;
		}

		// 关键指标
		Item ite = new Item();

		// 出售中的商品
		countOfSold = itemService.getItemCount(shopId, ite);

		// 已售罄的商品
		ite.setIsDisplay("N");
		countOfStock = itemService.getItemCount(shopId, ite);

		// 仓库中的商品
		ite.setType("soldout");
		ite.setIsDisplay("Y");
		countOfSoldout = itemService.getItemCount(shopId, ite);

		// 七天售出商品排名
		itemStats4Sold = itemService.getItemStats(shopId.trim(), IItemService.STATS_TYPE_SOLD);

		// 七天搜索商品排名
		itemStats4Search = itemService.getItemStats(shopId.trim());

		// 商品库存排名
		itemStats4Stock = itemService.getItemStats(shopId.trim(), IItemService.STATS_TYPE_STOCK);

		return SUCCESS;
	}

	/**
	 * 商品查询.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "商品查询")
	public String searchItem() {
		gmtStart = DateUtil.getNowDateStr();

		String shopId = getUser().getShopId();

		if (StringUtils.isNotEmpty(shopId)) {
			shop = shopService.getShop(shopId);
		}

		return "searchItem";
	}

	@JsonResult(field = "itemList", include = { "itemId", "itemNo", "itemName", "stock", "quota", "createDate" }, total = "total")
	public String getItemJsonList() {
		User user = this.getUser();

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		Item ite = new Item();
		ite = getSearchInfo(ite);

		if (StringUtils.isNotBlank(type)) {
			ite.setType(type.trim());
		}

		if (StringUtils.isNotBlank(isDisplay)) {
			ite.setIsDisplay(isDisplay.trim());
		}

		if (StringUtils.isNotBlank(itemName)) {
			ite.setItemName(itemName.trim());
		}

		if (StringUtils.isNotBlank(itemNo)) {
			ite.setItemNo(itemNo.trim());
		}

		total = itemService.getItemCount(shopId, ite);

		if (total > 0) {
			itemList = itemService.getItemList(shopId, ite);
		}

		return JSON_RESULT;
	}

	/**
	 * 获取 第 1 页 文件.
	 */
	private void initFile() {
		String shopId = getUser().getShopId();
		FileInfo fileInfo = new FileInfo();

		total = fileService.getFileCount(shopId, fileInfo);

		if (total > 0) {
			p = 1;
			total = total / PAGESIZE + 1;
			fileInfo.setStart(0);
			fileInfo.setLimit(PAGESIZE);
			fileInfo.setSort("MODIFY_DATE");
			fileInfo.setDir("DESC");
			fileList = fileService.getFileList(shopId, fileInfo);
		}
	}

	/**
	 * 获取 第 p 页 文件.
	 */
	@JsonResult(field = "fileList", include = { "fileId", "filePath", "code" })
	public String getFileJsonList() {
		String shopId = getUser().getShopId();
		FileInfo fileInfo = new FileInfo();

		int t = fileService.getFileCount(shopId, fileInfo);

		if (p < 1 || (p - 1) * PAGESIZE > t) {
			return JSON_RESULT;
		}

		if (t > 0) {
			// 转化成页码
			t = t / PAGESIZE + 1;
			fileInfo.setStart((p - 1) * PAGESIZE);
			fileInfo.setLimit(PAGESIZE);
			fileInfo.setSort("MODIFY_DATE");
			fileInfo.setDir("DESC");
			fileList = fileService.getFileList(shopId, fileInfo);

			if (fileList != null && fileList.size() > 0) {
				fileList.get(0).setCode(String.valueOf(t));
			}
		}

		return JSON_RESULT;
	}

	/**
	 * 组装商品信息.
	 */
	private void handle() {
		if (item != null) {
			// 处理 sku 信息
			if (properties != null && properties.length > 0) {
				List<ItemSku> skuList = new ArrayList<ItemSku>();
				for (int i = 0; i < properties.length; i++) {
					ItemSku s = new ItemSku();
					s.setProperties(properties[i]);
					s.setPropertiesName(propertiesName[i]);
					s.setPrice(new BigDecimal(price[i]));
					s.setStock(Integer.parseInt(stock[i]));
					s.setCodes(codes[i]);
					// 原始价格 if null then the same as price
					if (StringUtils.isNotBlank(origin[i])) {
						s.setOrigin(new BigDecimal(origin[i]));
					} else {
						s.setOrigin(new BigDecimal(price[i]));
					}

					skuList.add(s);
				}

				item.setSkuList(skuList);
			}

			// 处理商品文件图片
			if (fileId != null && fileId.length > 0) {
				List<ItemFile> itemFileList = new ArrayList<ItemFile>();
				for (int i = 0; i < fileId.length; i++) {
					ItemFile f = new ItemFile();
					f.setItemId(itemId);
					f.setFileId(fileId[i]);

					itemFileList.add(f);
				}

				item.setItemFileList(itemFileList);
			}
		}
	}

	/**
	 * 商品创建页面.
	 * 
	 * @return
	 */
	public String createItemPrepare() {
		// 初始化已上传文件
		initFile();

		return CREATE_PREPARE;
	}

	/**
	 * 商品创建.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "商品创建")
	public String createItem() {
		User user = this.getUser();

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return LOGIN_TIMEOUT;
		}

		try {
			handle();
		} catch (NumberFormatException e) {
			logger.error("price:" + LogUtil.parserBean(price) + "stock:" + LogUtil.parserBean(stock) + "origin:"
				+ LogUtil.parserBean(origin), e);

			this.setFailMessage("金额或数字不正确，请重新输入！");
			return RESULT_MESSAGE;
		}

		BooleanResult result = itemService.createItem(shopId, item, user.getUserId());

		if (result.getResult()) {
			this.setSuccessMessage("商品创建成功！");
		} else {
			this.setFailMessage(result.getCode());
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 商品详细信息查看.
	 * 
	 * @return
	 */
	public String searchItemDetail() {
		User user = this.getUser();

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return LOGIN_TIMEOUT;
		}

		item = itemService.getItem(shopId, itemId);

		if (item == null) {
			return NONE;
		}

		// 初始化已上传文件
		initFile();

		return UPDATE_PREPARE;
	}

	/**
	 * 商品修改.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "商品修改")
	public String updateItem() {
		User user = this.getUser();

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return LOGIN_TIMEOUT;
		}

		try {
			handle();
		} catch (NumberFormatException e) {
			logger.error("price:" + LogUtil.parserBean(price) + "stock:" + LogUtil.parserBean(stock) + "origin:"
				+ LogUtil.parserBean(origin), e);

			this.setFailMessage("金额或数字不正确，请重新输入！");
			return RESULT_MESSAGE;
		}

		BooleanResult result = itemService.updateItem(shopId, itemId, item, user.getUserId());

		if (result.getResult()) {
			this.setSuccessMessage("商品编辑成功！");
		} else {
			this.setFailMessage(result.getCode());
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 商品删除
	 * 
	 * @return
	 */
	public String deleteItem() {
		User user = getUser();

		BooleanResult result = itemService.deleteItem(user.getShopId(), itemId, user.getUserId());

		if (result.getResult()) {
			this.setSuccessMessage("商品删除成功！");
		} else {
			this.setFailMessage(result.getCode());
		}

		return RESULT_MESSAGE;
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 客户端查询商品详细信息.
	 * 
	 * @return
	 */
	@Pageview()
	public String item() {
		// 根据 itemId 获取商品详细信息
		item = itemService.getItem(super.getShop().getShopId(), itemId);

		if (item == null) {
			// TODO
		}

		return SUCCESS;
	}

	public IItemService getItemService() {
		return itemService;
	}

	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

	public IFileService getFileService() {
		return fileService;
	}

	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	public IShopService getShopService() {
		return shopService;
	}

	public void setShopService(IShopService shopService) {
		this.shopService = shopService;
	}

	public List<Item> getItemStats4Sold() {
		return itemStats4Sold;
	}

	public void setItemStats4Sold(List<Item> itemStats4Sold) {
		this.itemStats4Sold = itemStats4Sold;
	}

	public List<Item> getItemStats4Search() {
		return itemStats4Search;
	}

	public void setItemStats4Search(List<Item> itemStats4Search) {
		this.itemStats4Search = itemStats4Search;
	}

	public List<Item> getItemStats4Stock() {
		return itemStats4Stock;
	}

	public void setItemStats4Stock(List<Item> itemStats4Stock) {
		this.itemStats4Stock = itemStats4Stock;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String[] getFileId() {
		return fileId != null ? Arrays.copyOf(fileId, fileId.length) : null;
	}

	public void setFileId(String[] fileId) {
		if (fileId != null) {
			this.fileId = Arrays.copyOf(fileId, fileId.length);
		}
	}

	public String[] getProperties() {
		return properties != null ? Arrays.copyOf(properties, properties.length) : null;
	}

	public void setProperties(String[] properties) {
		if (properties != null) {
			this.properties = Arrays.copyOf(properties, properties.length);
		}
	}

	public String[] getPropertiesName() {
		return propertiesName != null ? Arrays.copyOf(propertiesName, propertiesName.length) : null;
	}

	public void setPropertiesName(String[] propertiesName) {
		if (propertiesName != null) {
			this.propertiesName = Arrays.copyOf(propertiesName, propertiesName.length);
		}
	}

	public String[] getPrice() {
		return price != null ? Arrays.copyOf(price, price.length) : null;
	}

	public void setPrice(String[] price) {
		if (price != null) {
			this.price = Arrays.copyOf(price, price.length);
		}
	}

	public String[] getStock() {
		return stock != null ? Arrays.copyOf(stock, stock.length) : null;
	}

	public void setStock(String[] stock) {
		if (stock != null) {
			this.stock = Arrays.copyOf(stock, stock.length);
		}
	}

	public String[] getCodes() {
		return codes != null ? Arrays.copyOf(codes, codes.length) : null;
	}

	public void setCodes(String[] codes) {
		if (codes != null) {
			this.codes = Arrays.copyOf(codes, codes.length);
		}
	}

	public String[] getOrigin() {
		return origin != null ? Arrays.copyOf(origin, origin.length) : null;
	}

	public void setOrigin(String[] origin) {
		if (origin != null) {
			this.origin = Arrays.copyOf(origin, origin.length);
		}
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getCountOfSold() {
		return countOfSold;
	}

	public void setCountOfSold(int countOfSold) {
		this.countOfSold = countOfSold;
	}

	public int getCountOfSoldout() {
		return countOfSoldout;
	}

	public void setCountOfSoldout(int countOfSoldout) {
		this.countOfSoldout = countOfSoldout;
	}

	public int getCountOfStock() {
		return countOfStock;
	}

	public void setCountOfStock(int countOfStock) {
		this.countOfStock = countOfStock;
	}

	public List<FileInfo> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileInfo> fileList) {
		this.fileList = fileList;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

}
